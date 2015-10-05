package com.projekty.gamifikacjalublin.ekrany;


import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projekty.gamifikacjalublin.R;


public class MapMenu extends FragmentActivity implements OnMapReadyCallback{
	private String idZadania;
	private Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_menu);
		
		i = getIntent();
		idZadania =  i.getStringExtra("idZadania");
		
		MapFragment mapFragment = (MapFragment) getFragmentManager()
			    .findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);

		
	}


	@Override
	public void onMapReady(GoogleMap map) {
		generateQuestMap(map);
	}

	
	
	
	
	private void generateQuestMap(GoogleMap map){
		
		if(Integer.parseInt(idZadania)==2){
			
			ArrayList<String> activeObjectives= new ArrayList<String>();
					
			activeObjectives=i.getStringArrayListExtra("activeObjectives");
			Log.d("MMtest Opis zadania3",""+activeObjectives);
			if(!activeObjectives.contains("0")){
				map.addMarker(new MarkerOptions()
		        .position(new LatLng(51.248156, 22.559524))
		        .snippet("Plac, kt�rego obecna nazwa sta�a si� popularna dopiero w latach 20. XIX wieku, jest wa�nym miejscem nie tylko w historii Lublina, ale te� ca�ego kraju. W 1569 roku Zygmunt August odebra� tu od ksi�cia pruskiego Albrechta Fryderyka ho�d i przysi�g� lenn�, za� podczas podpisywania w tym samym roku Unii Lubelskiej, by� on miejscem obozowania pos��w litewskich.")
		        .title("Przystanek 1"))
		        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.one));	
			}
			
		    if(!activeObjectives.contains("1")){
		    	map.addMarker(new MarkerOptions()
		        .position(new LatLng(51.250581, 22.571443))
		        .snippet("W XII wieku na wzg�rzu zamkowym znajdowa� si� pierwszy zesp� osadniczy, za� ju� za czas�w Jagiellon�w, w budynku zamku mie�ci�a si� kwatera kr�lewska. W 1569 roku zamek by�  miejscem obrad, a nast�pnie zaprzysi�enia aktu Unii Lubelskiej.")
		        .title("Przystanek 2"))
		        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.two));	
		    }
			
			if(!activeObjectives.contains("2")){
				map.addMarker(new MarkerOptions()
		        .position(new LatLng(51.25238, 22.572956))
		        .snippet("Cerkiew prawos�awna pod wezwaniem Przemienienia Pa�skiego jest budynkiem zwi�zanym z dziejami spo�eczno�ci prawos�awnej i unickiej w Lublinie.")
		        .title("Przystanek 3"))
		        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.three));
			}
		
			if(!activeObjectives.contains("3")){
				map.addMarker(new MarkerOptions()
		        .position(new LatLng(51.257839, 22.572699))
		        .snippet("Historia Jesziwas Chachmej Lublin � wy�szej szko�y talmudycznej, tzw. rabinackiej, przy ulicy Lubartowskiej, wi��e si� �ci�le z dziejami lubelskiej spo�eczno�ci �ydowskiej.")
		        .title("Przystanek 4"))
		        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.four));
			}
			
			if(!activeObjectives.contains("4")){
				map.addMarker(new MarkerOptions()
		        .position(new LatLng(51.248109,22.544975))
		        .snippet("Z inicjatywy ks. Idziego Radziszewskiego, przy wsparciu finansowym polskich przemys�owc�w � Karola Jaroszy�skiego i Franciszka Sk�pskiego, w 1918 roku powsta� Uniwersytet Lubelski, w 1944 roku przemianowany na Katolicki Uniwersytet Lubelski.")
		        .title("Przystanek 5"))
		        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.five));
			}
			
			if(!activeObjectives.contains("5")){
				map.addMarker(new MarkerOptions()
		        .position(new LatLng(51.243247, 22.552099))
				//.position(new LatLng(51.156640, 22.920312))
		        .snippet("Cmentarz przy ulicy Lipowej to jeden z najstarszych i najpi�kniejszych cmentarzy w kraju. Za�o�ony w 1794 roku jako cmentarz katolicki, pod koniec wieku XIX sta� si� nekropoli� wielowyznaniow�.")
		        .title("Przystanek 6"))
		        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.six));
			}
			
			if(!activeObjectives.contains("6")){
				map.addMarker(new MarkerOptions()
		        .position(new LatLng(51.24606800000001, 22.565789))
		        .snippet("Usytuowana w samym �rodku miasta ulica Bernardy�ska jest silnie zwi�zana z dziejami lubelskiego przemys�u oraz szkolnictwa, nawi�zuj�c jednocze�nie do wielokulturowych tradycji miasta.")
		        .title("Przystanek 7"))
		        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.seven));	
			}
			
			if(!activeObjectives.contains("7")){
				map.addMarker(new MarkerOptions()
		        .position(new LatLng(51.247921, 22.566133))
		        .snippet("Ulica Kr�lewska, zwana niegdy� Lwowskim Przedmie�ciem, przebiega wzd�u� dawnych mur�w miejskich. Dzi�ki przywilejom otrzymanym od Stefana Batorego i Zygmunta III Wazy, pierwsze kamienice zbudowano tu ju� na pocz�tku XVII wieku.")
		        .title("Przystanek 8"))
		        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.eight));	
			}
			
			if(!activeObjectives.contains("8")){
				map.addMarker(new MarkerOptions()
		        .position(new LatLng(51.247814, 22.569695000000003))
		       // .position(new LatLng(51.220052, 22.578464))
		        .snippet("Historia ko�cio�a i klasztoru Dominikan�w  si�ga XIII wieku. Obecna bry�a powsta�a na prze�omie XVI i XVII stulecia, kiedy odbudowano gotycki budynek zniszczony w czasie po�aru w 1575 roku.")
		        .title("Przystanek 9"))
		        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.nine));	
			}
			
			pointToPosition(map,new LatLng(51.248156, 22.559524));
			
			map.setInfoWindowAdapter(new InfoWindowAdapter(){

			@Override
			public View getInfoContents(Marker arg0) {
	
				return null;
			}

			@Override
			public View getInfoWindow(Marker arg0) {
	            View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
	            TextView tvtitle = (TextView) v.findViewById(R.id.info_title);
	            TextView tvdescription = (TextView) v.findViewById(R.id.info_description);
	            tvtitle.setText(arg0.getTitle());
	            tvdescription.setText(arg0.getSnippet());
				return v;
			}});
		
		}
		

		
	}
	private void pointToPosition(GoogleMap map,LatLng position) {
	    //Build camera position
	    CameraPosition cameraPosition = new CameraPosition.Builder()
	            .target(position)
	            .zoom(14).build();
	    //Zoom in and animate the camera.
	    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	
}
