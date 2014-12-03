//Android Studio sucks!

package com.android.mapnote;

import com.android.mapnote.adapter.DBAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.ListActivity;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class MainActivity extends ListActivity {

    private FragmentActivity fa = new FragmentActivity();
    private ActionBar actionBar;
    private static final String TAG = "MainActivity";
    public final static String EXTRA_MESSAGE = "com.android.mapnote.MESSAGE";
    private Intent serviceIntent;
    // Tab titles
    public final DBAdapter db = new DBAdapter(this);
    private ArrayList<String> rems = new ArrayList<String>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations);

        //explicit intent
        serviceIntent = new Intent( getBaseContext(),
                LocationService.class);


        db.open();
        Log.d(TAG, "getting locations");
        Cursor c = db.getLocations();
        TextView eText = (TextView) findViewById(R.id.location);
        eText.setText("Reminder Locations");
        Log.d(TAG, "going into loop");
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            // The Cursor is now set to the right position
            rems.add(c.getString(0));
            Log.d(TAG, "id: ");
            Log.d(TAG, c.getString(0));
        }
        Log.d(TAG, "exit loop");
        ListView lstView = getListView();

        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // one choice

        lstView.setTextFilterEnabled(true); // filter the children according to user input

        setListAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.item_list, rems));
        db.close();
        }

    /* add actions items to the action bar
     * - inflate /res/menu/simple_action_bar.xml
     */

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
    }

    /* event handling of the selection of an action item (menu item)
     * - only 3 out 4 action items are handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ) {

            case R.id.menu_add:
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_map:
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_LONG).show();
                return true;
//            case R.id.menu_help:
//                DialogFragment dialog = new HelpDialogFragment();
//                dialog.show(fa.getSupportFragmentManager(), "HelpDialogFragment");
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
