package com.android.mapnote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import android.support.v4.app.Fragment;


public class MapFragment extends Fragment{

    //private MapActivity ma;
    //private MapView mapView;
    //private GoogleMap map;


    //private static GoogleMap googleMap;
    //private static Double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // works
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        return rootView;
        //end works




        //googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.location_map)).getMap();

        //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.location_map)).getMap();

        //return this.getView();
		
		
		
		
		
		
		/*
		// Gets the MapView from the XML layout and creates it
		
		View v = inflater.inflate(R.layout.fragment_map, container, false);
		
		mapView = (MapView) v.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
 
		// Gets to GoogleMap from the MapView and does initialization stuff
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.setMyLocationEnabled(true);
 
		// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
		//try {
			MapsInitializer.initialize(this.getActivity());
		//} catch (GooglePlayServicesNotAvailableException e) {
		//e.printStackTrace();
		//}
		
		// Updates the location and zoom of the MapView
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
		map.animateCamera(cameraUpdate);
 
		return v;
		*/
    } // end onCreate
	/*
	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}
 
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
	*/








}
