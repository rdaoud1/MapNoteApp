package com.android.mapnote.adapter;

import com.android.mapnote.adapter.TabsPagerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Artemy on 13/11/2014.
 */
public class AddActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);
        final EditText eText = (EditText) findViewById(R.id.reminder);
        final Button btn = (Button) findViewById(R.id.addReminder);

        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String str = eText.getText().toString();
                ArrayList<String> rems = new ArrayList<String>();
                int start = 0;
                int end = str.indexOf(",");
                rems.add(str.substring(start, end));
                while (start != -1) {
                    start = end;
                    end = str.indexOf(",", start);
                    if(end == -1)
                        break;
                    rems.add(str.substring(start, end));
                }
                String items = rems.toString();
                String location = str.substring(str.indexOf("@"));

                Toast.makeText(this, "\n Location: " + location + "Items: " + items, Toast.LENGTH_LONG).show();
            }
        });
    }
}
