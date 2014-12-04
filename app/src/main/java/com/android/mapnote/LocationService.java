package com.android.mapnote;


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

import com.android.mapnote.adapter.DBAdapter;

import java.util.Locale;


/**
 * Created by Rafid Daoud on 12/3/2014.
 */
public class LocationService extends Service
{
    public final static String EXTRA_MESSAGE = "com.android.mapnote.MESSAGE";
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    Intent intent;
    int counter = 0;

    @Override
    public void onCreate()
    {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

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
        } else if (isSignificantlyOlder) {
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
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



    /** Checks whether two providers are the same */
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

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }




    public class MyLocationListener implements LocationListener
    {
        final DBAdapter db = new DBAdapter(getApplicationContext());
        public void onLocationChanged(final Location loc)
        {
            Log.i("**************************************", "Location changed");
            //Toast methods to display database contents
            db.open();

            if(isBetterLocation(loc, previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);



                float[] result = new float[1];
                double lat, lon;
                int counter = 0;
                Cursor c = db.getLocationsAndGeo();
                //displayCursor(c);



                if (c.moveToFirst())
                {
                    do {

                        String locationName = c.getString(0); // location
                        String[] geocode = c.getString(1).split(","); // geo code

                        Log.d("#######################", c.getString(0));
                        Log.d("#######################", c.getString(1));

                        lat = Double.parseDouble(geocode[0]);
                        lon = Double.parseDouble(geocode[1]);

                        // start lat, start long, end lat, end long
                        Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), lat, lon, result);

                        //startActivity(intent);//
                        Log.d("dddddddddddddddddddddddd", Float.toString(result[0]));
                        if(result[0] <= 5001.00)
                        {
                            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<lat>,<long>?q=<lat>,<long>(Label+Name)"));
                            String uri = "geo:" + lat + "," + lon + "?q=" + lat + "," + lon + "(" + locationName + ")";
                            Intent intentMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            Intent intentApp = new Intent(getApplicationContext(), RemindersActivity.class);
                            intentApp.putExtra(EXTRA_MESSAGE, c.getString(0));
                            //getApplicationContext().startActivity(intent);

                            // build a pending intent
                            PendingIntent pIntentMap = PendingIntent.getActivity( getApplicationContext(),     // the context for starting the activity
                                                                                0,        // private request code for the SENDER
                                                                                intentMap,   // intent of the activity to be LAUNCED
                                                                                PendingIntent.FLAG_UPDATE_CURRENT ); // flag

                            PendingIntent pIntentApp = PendingIntent.getActivity( getApplicationContext(),     // the context for starting the activity
                                                                                0,        // private request code for the SENDER
                                                                                intentApp,   // intent of the activity to be LAUNCED
                                                                                PendingIntent.FLAG_UPDATE_CURRENT ); // flag

                            // build a notification (use of Notification.Builder)
                            Notification notf = new Notification.Builder(getApplicationContext())

                                    .setSmallIcon(R.drawable.globe)   // 1. icon at the status bar
                                    .setTicker( "MapNote" )                 // 2. ticker text at the status bar
                                    .setContentTitle("MapNote")           // 3. notification title text
                                    .setContentText(locationName) // 4. notification body/detail text
                                    .setContentIntent(pIntentApp)            // 5. primary action via a pending intent
                                    .setAutoCancel(true)
                                    .addAction(android.R.drawable.ic_menu_mapmode, "Map", pIntentMap)
                                    .build();         // 6. automatic removal of a notification!!!



                            // system service/platform service: NOTIFICATION_SERVICE
                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService( NOTIFICATION_SERVICE );

                            notificationManager.notify( counter, notf );  // notification ID: 0
                            counter++;


                        }
                        else
                        {
                            //Toast.makeText(getApplicationContext(), "NO Notification", Toast.LENGTH_LONG).show();
                        }



                    } while (c.moveToNext());
                }




            }
            db.close();
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText(getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }


        private void displayCursor(Cursor c ){

            if (c.moveToFirst())
            {
                do {
                    displayContact(c);
                } while (c.moveToNext());
            }
        }
        private void displayContact( Cursor c )
        {
            Toast.makeText( getApplicationContext(),
                "Location: " + c.getString(0) + "\n" +
                "Code: " + c.getString(1),
                Toast.LENGTH_LONG).show();
        }

    }



}
