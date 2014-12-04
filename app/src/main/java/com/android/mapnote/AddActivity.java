package com.android.mapnote;

import com.android.mapnote.adapter.DBAdapter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AddActivity extends FragmentActivity {
    public final static String EXTRA_MESSAGE = "com.android.mapnote.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        // declarations of variables for the database and UI elements
        final EditText eText = (EditText) findViewById(R.id.reminder);
        final Button btn = (Button) findViewById(R.id.addReminder);
        final DBAdapter db = new DBAdapter(this);

        // a listener that will do event handling for the button
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // grab user intput in the text field
                String str = eText.getText().toString();

                String geodata ="";
                ArrayList<String> rems = new ArrayList<String>();
                int start = 0, end = 0;
                long id;

                // open connection to DB
                db.open();

                // get location. will always be the first item in the array.
                rems.add(str.substring(str.indexOf("@")));

                // a loop to extract the items part out of the user entry
                // based on the `@` symbol
                while (start != -1) {
                    end = str.indexOf(",", start);
                    if(end == -1) {
                        end = str.indexOf("@", start);
                        // last item into the array
                        rems.add(str.substring(start, end).trim());
                        break;
                    }
                    // build the items array while adding it to the array
                    rems.add(str.substring(start, end).trim());
                    start = end+2;
                }

                String items = rems.toString();

                // extracted location
                final String location = str.substring(str.indexOf("@"));

                // get the geo coordinates for the location
                geodata = run(location);

                // loop to insert the location, geo code and items into the DB
                for(int i = 1; i < rems.size(); i++)
                    id = db.insertReminders(rems.get(0), rems.get(i), geodata);


                db.close(); // close DB connection

                // redirect to the main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    } // end onCreate



    // a method to populate items onto the action bar
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        // inflate the menu: add action items to the action bar if it is visible
        getMenuInflater().inflate(R.menu.add_action_bar, menu);
        return true;
    }

     // event handling of the selection of an action item (menu item)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ) {
            case R.id.menu_help:
                DialogFragment dialog = new HelpDialogFragment();
                // display help dialog
                dialog.show(getSupportFragmentManager(), "HelpDialogFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // a custom method to take in an address then convert it to coordinates
    public String run(String location)
    {
        String geodata="";

        // check if the Geocoder functionality is available
        Geocoder geocoder = new Geocoder( getApplicationContext(), Locale.getDefault());
        if(!Geocoder.isPresent())
        {
            Toast.makeText(getApplicationContext(), "Geocoder Not Present!", Toast.LENGTH_LONG).show();
        }
        try
        {
            List<Address> list = geocoder.getFromLocationName(location, 1);
            Address address = list.get(0);

            // grab coordinates
            double lat = address.getLatitude();
            double lng = address.getLongitude();

            // build a string to contain the coordinates
            geodata = String.valueOf(lat) + "," + String.valueOf(lng);

        }catch (IOException e)
        {
            Log.e("IOException", e.getMessage());
        }

        return geodata;

    } //end run
} // end activity
