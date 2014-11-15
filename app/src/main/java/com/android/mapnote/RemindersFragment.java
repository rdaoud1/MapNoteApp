package com.android.mapnote;

import com.android.mapnote.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RemindersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reminders, container, false);
//        Intent intent = getIntent();
//        ArrayList<String> rems = intent.getStringArrayListExtra(AddActivity.EXTRA_MESSAGE);
//
//        ListView lstView = getListView();
//
//        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // one choice
//
//        lstView.setTextFilterEnabled(true); // filter the children according to user input
//
//        setListAdapter( new ArrayAdapter<String>( context, android.R.layout.simple_list_item_checked, rems ));

        return rootView;
    }

//    public void onListItemClick(
//            ListView parent, View v, int position, long id )
//    {
//        Toast.makeText(this,
//                (CharSequence) rems.get(position),
//                Toast.LENGTH_LONG).show();
//    }

}
