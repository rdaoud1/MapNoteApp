package com.android.mapnote;

import com.android.mapnote.adapter.TabsPagerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

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

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = eText.getText().toString();
                ArrayList<String> rems = new ArrayList<String>();
                int start = 0, end = 0;
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
                //String location = str.substring(str.indexOf("@"));
                //Toast.makeText(getApplicationContext(), "Location: " + location + "\n Items: " + items, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), RemindersFragment.class);
                intent.putExtra(EXTRA_MESSAGE, rems);
                startActivity(intent);


            }
        });
    }
}
