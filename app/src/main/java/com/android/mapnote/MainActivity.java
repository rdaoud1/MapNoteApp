package com.android.mapnote;

import com.android.mapnote.adapter.DBAdapter;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.ListActivity;
import android.annotation.SuppressLint;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class MainActivity extends ListActivity {

    private static final String TAG = "MainActivity";
    public final static String EXTRA_MESSAGE = "com.android.mapnote.MESSAGE";
    private Intent serviceIntent;

    // decelerations for DB
    public final DBAdapter db = new DBAdapter(this);
    private ArrayList<String> rems = new ArrayList<String>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations);

        //explicit intent
        serviceIntent = new Intent( getBaseContext(), LocationService.class);
        startService( serviceIntent );

        // open DB connection
        db.open();

        // get all locations from the DB
        Cursor c = db.getLocations();

        TextView eText = (TextView) findViewById(R.id.location);

        // title for the layout
        eText.setText("Reminder Locations");

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            // The Cursor is now set to the right position
            rems.add(c.getString(0));
        }

        // make a new list view for the layout
        ListView lstView = getListView();

        // one choice allowed for selection of the list view
        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // filter the children according to user input
        lstView.setTextFilterEnabled(true);

        // populate values of the list view
        setListAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.item_list, rems));

        // close DB connection
        db.close();
        }

    // event handler for clicking a list item
    // direct the user to the corresponding location clicked
    public void onListItemClick(ListView parent, View v, int position, long id )
    {
        Intent intent = new Intent(this, RemindersActivity.class);
        String message = rems.get(position);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // inflate the menu: add action items to the action bar if it is visible
        getMenuInflater().inflate(R.menu.simple_action_bar, menu);
        return true;
    } // end onCreateOptionsMenu

    // event handling of the selection of an action item (menu item)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ) {

            case R.id.menu_add:
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } // end onOptionsItemSelected

} // end MainActivity
