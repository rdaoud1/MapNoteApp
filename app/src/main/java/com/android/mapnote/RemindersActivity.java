package com.android.mapnote;

import com.android.mapnote.adapter.DBAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.app.ListActivity;
import android.annotation.SuppressLint;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class RemindersActivity extends ListActivity {

    private static final String TAG = "MainActivity";

    public final DBAdapter db = new DBAdapter(this);

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminders);

        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // open connection to DB
        db.open();

        // grab all reminders from DB
        Cursor c = db.getReminder(message);

        TextView eText = (TextView) findViewById(R.id.location);
        eText.append(message);

        ArrayList<String> rems = new ArrayList<String>();


        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            // The Cursor is now set to the right position
            rems.add(c.getString(2));
        }

        // create a list view to be displayed
        ListView lstView = getListView();

        // no choice to be picked
        lstView.setChoiceMode(ListView.CHOICE_MODE_NONE);

        // filter the children according to user input
        lstView.setTextFilterEnabled(true);

        // populate the list view with some values
        setListAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.item_list, rems));

        // delete reminder button
        Button button = (Button) findViewById(R.id.delete);
        button.setOnClickListener(new View.OnClickListener() {
            // event handler for the button
            @Override
            public void onClick(View view) {
                // open connection to DB
                db.open();

                // delete a specified reminder
                db.deleteReminders(message);

                // close connection to DB
                db.close();

                // redirect to main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } // end event handler
        });

        // close connection to DB
        db.close();
    } // end onCreate

    // add actions items to the action bar
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

} // end reminder activity