package com.android.mapnote;

import com.android.mapnote.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;


public class MapsFragment extends Fragment{

    private static View view;
    private static GoogleMap googleMap; /* Google Map */
    private static Double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (RelativeLayout)inflater.inflate(R.layout.fragment_map, container, false);
//        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        latitude = 26.78;
        longitude = 72.56;

        setUpMapIfNeeded();
//        try {
//            initializeMap();  /* load a Google map */
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//        googleMap.setMapType( GoogleMap.MAP_TYPE_NORMAL);
        //googleMap.setTrafficEnabled(true);


        return view;
        //return rootView;
    }




    /***** Sets up the map if it is possible to do so *****/
    public static void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) MainActivity.fragmentManager
                    .findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    private static void setUpMap() {
        // For showing a move to my loction button
        googleMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
        // For zooming automatically to the Dropped PIN Location
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                longitude), 12.0f));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (googleMap != null)
            setUpMap();

        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) MainActivity.fragmentManager
                    .findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    /**** The mapfragment's id must be removed from the FragmentManager
     **** or else if the same it is passed on the next time then
     **** app will crash ****/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (googleMap != null) {
            MainActivity.fragmentManager.beginTransaction()
                    .remove(MainActivity.fragmentManager.findFragmentById(R.id.map)).commit();
            googleMap = null;
        }
    }




    /* private method to load a Google map
     *     - If the map does not exist,  it will be created for you.
     */
    private void initializeMap() {

        if (googleMap == null) {

            googleMap = ((SupportMapFragment)
                    getFragmentManager().findFragmentById(R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {

                Toast.makeText( getActivity(), "Sorry! unable to create maps", Toast.LENGTH_LONG ).show();

            }
        }
    } // end initializeMap

}
