package com.android.mapnote;

import com.android.mapnote.adapter.DBAdapter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service
{
    public final static String EXTRA_MESSAGE = "com.android.mapnote.MESSAGE";
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    // create instances of MyLocationListener and LocationManager
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        //intent = new Intent(BROADCAST_ACTION);
    }

    // initial start up of the service
    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        // request location update
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    // a method to find a new location
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        }
        else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        }
        else if (isNewer && !isLessAccurate) {
            return true;
        }
        else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    } // end isBetterLocation

    // checks whether two providers are the same
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    // a class to keep track of the user's location and handle notifications
    public class MyLocationListener implements LocationListener
    {
        final DBAdapter db = new DBAdapter(getApplicationContext());
        public void onLocationChanged(final Location loc)
        {
            // location changed

            float[] result = new float[1];
            double lat, lon;
            int counter = 0;

            // open connection to DB
            db.open();

            // new location acquired
            // check all the reminders
            if(isBetterLocation(loc, previousBestLocation)) {

                // new location
                loc.getLatitude();
                loc.getLongitude();

                // get locations and their respective geo location
                Cursor c = db.getLocationsAndGeo();

                // if there are entries in the DB run the loop
                if (c.moveToFirst())
                {
                    // a loop to go through all the locations stored then
                    // compare it to the current location based on a 5 KM radius
                    // if within 5KM then a notification is sent
                    do {

                        String locationName = c.getString(0); // location
                        String[] geocode = c.getString(1).split(","); // geo code

                        lat = Double.parseDouble(geocode[0]);
                        lon = Double.parseDouble(geocode[1]);

                        // calculate the difference between two geo points
                        // start lat, start long, end lat, end long
                        Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), lat, lon, result);

                        // less than 5 KM radius
                        if(result[0] <= 5001.00)
                        {

                            // create a uri to be used to open up Google Maps App
                            String uri = "geo:" + lat + "," + lon + "?q=" + lat + "," + lon + "(" + locationName + ")";

                            // intent to open Google Maps Application
                            Intent intentMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                            // intent to open the specified location in the app
                            Intent intentApp = new Intent(getApplicationContext(), RemindersActivity.class);

                            // select which location will open up
                            intentApp.putExtra(EXTRA_MESSAGE, c.getString(0));

                            // build a pending intent for the notification that will open Google Maps App
                            PendingIntent pIntentMap =
                                PendingIntent.getActivity( getApplicationContext(), 0, intentMap, PendingIntent.FLAG_UPDATE_CURRENT );

                            // build a pending intent for the notification that will open the app
                            PendingIntent pIntentApp =
                                PendingIntent.getActivity( getApplicationContext(), 0, intentApp, PendingIntent.FLAG_UPDATE_CURRENT );

                            // build a notification (use of Notification.Builder)
                            Notification notf = new Notification.Builder(getApplicationContext())

                                    .setSmallIcon(R.drawable.globe)                 // 1. icon at the status bar
                                    .setTicker( "MapNote" )                         // 2. ticker text at the status bar
                                    .setContentTitle("Reminder Location Nearby")    // 3. notification title text
                                    .setContentText(locationName)                   // 4. notification body/detail text
                                    .setContentIntent(pIntentApp)                   // 5. primary action via a pending intent
                                    .setAutoCancel(true)                            // 6. automatic removal of a notification!!!
                                                                                    // 7. action button to open google maps
                                    .addAction(android.R.drawable.ic_menu_mapmode, "Map", pIntentMap)
                                    .build();

                            // system service/platform service: NOTIFICATION_SERVICE
                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService( NOTIFICATION_SERVICE );

                            // notification id and the notification content
                            notificationManager.notify( counter, notf );

                            // calculate the id for notifications
                            counter++;

                        }
                        else {
                            // no notifications to make
                        }

                    } while (c.moveToNext());

                } // end checking entries in the DB

            } // end isBetterLocation

            // close DB connection
            db.close();
        }

        // check if GPS is disabled
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_LONG).show();
        }

        // check if GPS is enabled
        public void onProviderEnabled(String provider) {
            Toast.makeText( getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) { }

    } // end MyLocationListener

} // end LocationServices
