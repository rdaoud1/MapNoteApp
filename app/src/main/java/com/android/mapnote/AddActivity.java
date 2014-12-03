package com.android.mapnote;

import com.android.mapnote.adapter.DBAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Artemy on 13/11/2014.
 * Add Validation later
 */
public class AddActivity extends FragmentActivity {
    public final static String EXTRA_MESSAGE = "com.android.mapnote.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);
        final EditText eText = (EditText) findViewById(R.id.reminder);
        final Button btn = (Button) findViewById(R.id.addReminder);
        final DBAdapter db = new DBAdapter(this);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = eText.getText().toString();
                ArrayList<String> rems = new ArrayList<String>();
                int start = 0, end = 0;
                db.open();
                long id;
                rems.add(str.substring(str.indexOf("@"))); //get location. will always be the first item in the array.

                while (start != -1) {
                    end = str.indexOf(",", start);
                    if(end == -1) {
                        end = str.indexOf("@", start);
                        rems.add(str.substring(start, end).trim());
                        break;
                    }
                    rems.add(str.substring(start, end).trim());
                    start = end+2;
                }
                String items = rems.toString();
                String location = str.substring(str.indexOf("@"));
                for(int i = 1; i < rems.size(); i++)
                    id = db.insertReminders(rems.get(0), rems.get(i));
                Cursor c = db.getAllReminders();
                //displayCursor( c );
                db.close();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Location: " + location + "\n Items: " + items, Toast.LENGTH_LONG).show();
//                DetailsFragment details = new DetailsFragment();
//                details.setArguments(getIntent().getExtras());
//                getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();



            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        // inflate the menu: add action items to the action bar if it is visible
        getMenuInflater().inflate(R.menu.add_action_bar, menu);
        return true;
    }

    /* event handling of the selection of an action item (menu item)
     * - only 3 out 4 action items are handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ) {
            case R.id.menu_help:
                DialogFragment dialog = new HelpDialogFragment();
                dialog.show(getSupportFragmentManager(), "HelpDialogFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Toast methods to display database contents
    private void displayCursor( Cursor c ){

        if (c.moveToFirst())
        {
            do {
                displayContact(c);
            } while (c.moveToNext());
        }
    }
    private void displayContact( Cursor c )
    {
        Toast.makeText( this,
                "id: " + c.getString(0) + "\n" +
                        "Location: " + c.getString(1) + "\n" +
                        "Item:  " + c.getString(2),
                Toast.LENGTH_LONG).show();
    }
}
