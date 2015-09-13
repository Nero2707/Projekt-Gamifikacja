package com.projekty.gamifikacjalublin.ekrany;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projekty.gamifikacjalublin.R;


public class MapMenu extends FragmentActivity implements OnMapReadyCallback{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_menu);
		
		MapFragment mapFragment = (MapFragment) getFragmentManager()
			    .findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);

		
	}


	@Override
	public void onMapReady(GoogleMap map) {
		map.addMarker(new MarkerOptions()
        .position(new LatLng(0, 0))
        .title("Marker"));

		
	}

}
