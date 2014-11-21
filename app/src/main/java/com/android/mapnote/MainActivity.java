//Android Studio sucks!

package com.android.mapnote;

import com.android.mapnote.R;

import com.android.mapnote.adapter.DBAdapter;
import com.android.mapnote.adapter.TabsPagerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.app.ListActivity;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {

    private ListActivity la;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Reminders", "Map" };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reminders);

        final DBAdapter db = new DBAdapter(this);

        db.open();
        Cursor c = db.getAllReminders();

        ListView lstView = la.getListView();

        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // one choice

        lstView.setTextFilterEnabled(true); // filter the children according to user input

        la.setListAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_checked, rems));

        }

    /* add actions items to the action bar
     * - inflate /res/menu/simple_action_bar.xml
     */
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
            case R.id.menu_settings:
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_help:
                DialogFragment dialog = new HelpDialogFragment();
                dialog.show(getSupportFragmentManager(), "HelpDialogFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayCursor( Cursor c ){

        if (c.moveToFirst())
        {
            do {
                c.getString(cursor.getColumnIndex("location"));
            } while (c.moveToNext());
        }
    }


}
