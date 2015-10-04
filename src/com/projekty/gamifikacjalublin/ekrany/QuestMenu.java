package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.R.id;
import com.projekty.gamifikacjalublin.R.layout;
import com.projekty.gamifikacjalublin.core.GPSTracker;
import com.projekty.gamifikacjalublin.core.JSONParser;
import com.projekty.gamifikacjalublin.ekrany.IdeaMenu.GetVotes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class QuestMenu extends Activity implements OnClickListener{
	private Intent i;
	private static final String GET_OBJECTIVES_URL = "http://www.lublinquest.ugu.pl/webservice/getcompletedobjectives.php";
	private static final String SET_OBJECTIVES_URL = "http://www.lublinquest.ugu.pl/webservice/setcompletedobjectives.php";
	private static final String COMPLETE_QUEST_URL = "http://www.lublinquest.ugu.pl/webservice/completequest.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_COMPLETED_OBJ = "completed_objectives";
	private String tytul;
	private String opis;
	private String idZadania;
	private ProgressDialog pDialog;
	private TextView tytulTextView;
	private TextView opisTextView;
	private View przyciskZglosWykonanie;
	private View przyciskZobaczMape;
	//private String activeObjectives;
	private ArrayList<String> activeObjectives;
	
	
	JSONParser jsonParser = new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quest_menu);
		
		i = getIntent();
		tytul =  i.getStringExtra("tytul");
		opis =  i.getStringExtra("opis");
		idZadania=i.getStringExtra("idZadania");
		
		tytulTextView = (TextView) findViewById(R.id.zadanie_tytul);
		tytulTextView.setText(tytul);
		opisTextView = (TextView) findViewById(R.id.zadanie_opis);
		opisTextView.setText(opis);
		przyciskZglosWykonanie = findViewById(R.id.przycisk_zglos_wykonanie);
		przyciskZglosWykonanie.setOnClickListener(this);
		przyciskZobaczMape = findViewById(R.id.przycisk_zobacz_mape);
		przyciskZobaczMape.setOnClickListener(this);
		
		new GetActiveObjectives().execute();
		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_zobacz_mape:
			i = new Intent(this, MapMenu.class);
			i.putExtra("idZadania", idZadania);
			
			ArrayList<String> stuff = new ArrayList<String>();
			stuff.addAll(activeObjectives);
			Log.d("MMtest Opis zadania2",""+stuff.getClass().getName());
			i.putStringArrayListExtra("activeObjectives",stuff);

			startActivity(i);
			break;
			
		case R.id.przycisk_zglos_wykonanie:
			checkCurrentPosition();
			break;
		}

	}
	
	
	
	
	class GetActiveObjectives extends AsyncTask<String, String, String> {
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(QuestMenu.this);
	            pDialog.setMessage("Pobieranie aktualnych celów zadania");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();

	        }
		 
		@Override
		protected String doInBackground(String... args) {
			
			int success;
			try {
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QuestMenu.this);
               params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
               Log.d("request!", "starting");

               //Posting user data to script
               JSONObject json = jsonParser.makeHttpRequest(
            		   GET_OBJECTIVES_URL, "POST", params);

               // full json response
               Log.d("OdpowiedŸ json", json.toString());

               // json success element
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               	//Log.d("Testowy log json", json.getString(TAG_COMPLETED_OBJ));
               	//activeObjectives=json.getString(TAG_COMPLETED_OBJ);
               	//Log.d("Testowy log json", activeObjectives);
               	//finish();
               	return json.getString(TAG_COMPLETED_OBJ);
               }else{
               	Log.d("Nie pobrano iloœci g³osów", json.getString(TAG_MESSAGE));
               	return json.getString(TAG_MESSAGE);

               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
			return null;
		}
		
		 protected void onPostExecute(String file_url) {
			 i = getIntent();
	    	 pDialog.dismiss();
	    	 if (file_url != null){
	    		 activeObjectives = new ArrayList<String>(Arrays.asList(file_url.split("\\s*,\\s*")));
		    	 Log.d("MMtest tablica aktualnych zadan",""+activeObjectives);
		    	 modifyObjectives(activeObjectives);
		    	
		    	
	            }
	    	
	    	 
	}
	
	
	}	
	
	
	class SetActiveObjectives extends AsyncTask<String, String, String> {
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(QuestMenu.this);
	            pDialog.setMessage("Aktualizowanie aktualnych celów zadania");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();

	        }
		 
		@Override
		protected String doInBackground(String... args) {
			
			int success;
			try {
              // Building Parameters
              List<NameValuePair> params = new ArrayList<NameValuePair>();
              SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QuestMenu.this);
              params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
              String newObjectives="";
              for(int i=0;i<activeObjectives.size();i++){
            	  newObjectives=newObjectives+activeObjectives.get(i)+",";
              }
              params.add(new BasicNameValuePair("completed_objectives", newObjectives));
              Log.d("request!", "starting");

              //Posting user data to script
              JSONObject json = jsonParser.makeHttpRequest(
            		  SET_OBJECTIVES_URL, "POST", params);

              // full json response
              Log.d("OdpowiedŸ json", json.toString());

              // json success element
              success = json.getInt(TAG_SUCCESS);
              if (success == 1) {
              	//Log.d("Testowy log json", json.getString(TAG_COMPLETED_OBJ));
              	//activeObjectives=json.getString(TAG_COMPLETED_OBJ);
              	//Log.d("Testowy log json", activeObjectives);
              	//finish();
              	return json.getString(TAG_COMPLETED_OBJ);
              }else{
              	Log.d("Nie zaktualizowano celów", json.getString(TAG_MESSAGE));
              	return json.getString(TAG_MESSAGE);

              }
          } catch (JSONException e) {
              e.printStackTrace();
          }
			return null;
		}
		
		 protected void onPostExecute(String file_url) {
			 i = getIntent();
	    	 pDialog.dismiss();
	    	 new GetActiveObjectives().execute();
	    	 
	}
	
	
	}
	
	class CompleteQuest extends AsyncTask<String, String, String> {
		String id_wykonanego_zadania;
		
		
		public CompleteQuest(String id_wykonanego_zadania){
			this.id_wykonanego_zadania=id_wykonanego_zadania;
		}
		
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(QuestMenu.this);
	            pDialog.setMessage("Wykonanie zadania");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();

	        }
		 
		@Override
		protected String doInBackground(String... args) {
			
			int success;
			try {
             // Building Parameters
             List<NameValuePair> params = new ArrayList<NameValuePair>();
             SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QuestMenu.this);
             params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
             params.add(new BasicNameValuePair("quest_id", id_wykonanego_zadania));
             Log.d("request!", "starting");

             //Posting user data to script
             JSONObject json = jsonParser.makeHttpRequest(
            		 COMPLETE_QUEST_URL, "POST", params);

             // full json response
             Log.d("OdpowiedŸ json", json.toString());

             // json success element
             success = json.getInt(TAG_SUCCESS);
             if (success == 1) {
             	//Log.d("Testowy log json", json.getString(TAG_COMPLETED_OBJ));
             	//activeObjectives=json.getString(TAG_COMPLETED_OBJ);
             	//Log.d("Testowy log json", activeObjectives);
             	//finish();
             	return json.getString(TAG_MESSAGE);
             }else{
             	Log.d("Nie zaktualizowano celów", json.getString(TAG_MESSAGE));
             	return json.getString(TAG_MESSAGE);

             }
         } catch (JSONException e) {
             e.printStackTrace();
         }
			return null;
		}
		
		 protected void onPostExecute(String file_url) {
			 i = getIntent();
	    	 pDialog.dismiss();
	    	 finish();
	    	 //new GetActiveObjectives().execute();
	    	 
	}
	
	
	}
	
	
	private void modifyObjectives(List<String> activeObjectives){
		String[] text=opis.split("\\r?\\n");
		
		 Log.d("MMtest Opis zadania",""+opis.split("\\r?\\n").length);
		 
		 if(Integer.parseInt(idZadania)==2){
			 String newText="";
			 for(int i=0;i<text.length;i++){
				 if(activeObjectives.contains(Integer.toString(i))){	
					 newText=newText+text[i]+" \u2714 \n";
				 }else{
					 newText=newText+text[i]+"\n";
				 }
			 }
			 opisTextView.setText(newText);
			 
		 }
	}
	
	
	private void checkCurrentPosition(){
		if(Integer.parseInt(idZadania)==2){
			GPSTracker gps = new GPSTracker(this);
			if(gps.canGetLocation()){			
			Location currentLocation=gps.getLocation();
			Location objectiveLocation=new Location("");
			
			Double[] LatitudeQuestMarkers = {51.248156,51.250581,51.25238,51.257839,51.248109,51.243247,51.24606800000001,51.247921,51.247814};
			Double[] LongitudeQuestMarkers = {22.559524,22.571443,22.572956,22.572699,22.544975,22.552099,22.565789,22.566133,22.569695000000003};
			
			for(int i=0;i<LatitudeQuestMarkers.length;i++){
				objectiveLocation.setLatitude(LatitudeQuestMarkers[i]);
				objectiveLocation.setLongitude(LongitudeQuestMarkers[i]);
				if(!activeObjectives.contains(Integer.toString(i)) && currentLocation.distanceTo(objectiveLocation)<500){
					activeObjectives.add(Integer.toString(i));
					new SetActiveObjectives().execute();
					
					if(activeObjectives.size()==9){
						activeObjectives.clear();
						new CompleteQuest("2").execute();
					}
					
					break;
				}
			}
			Log.d("MMtest AKTUALNA LOKALIZACJA"," "+currentLocation);
			
			}else{
				gps.showSettingsAlert();
			}
		}
	}
	
}	
