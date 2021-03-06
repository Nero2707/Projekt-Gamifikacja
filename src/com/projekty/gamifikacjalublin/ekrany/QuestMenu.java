package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.core.CompleteAchievment;
import com.projekty.gamifikacjalublin.core.GPSTracker;
import com.projekty.gamifikacjalublin.core.JSONParser;

public class QuestMenu extends Activity implements OnClickListener{
	private Intent i;
	private static final String GET_OBJECTIVES_URL = "http://www.lublinquest.ugu.pl/webservice/getcompletedobjectives.php";
	private static final String SET_OBJECTIVES_URL = "http://www.lublinquest.ugu.pl/webservice/setcompletedobjectives.php";
	private static final String COMPLETE_QUEST_URL = "http://www.lublinquest.ugu.pl/webservice/completequest.php";
	private static final String MODIFY_POINTS_URL = "http://www.lublinquest.ugu.pl/webservice/modifypoints.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_COMPLETED_OBJ = "completed_objectives";
	private static final String QUEST_PASSWORD="LUBLIN";
	private  HashMap<String, TextView> letters_fields=new HashMap<String, TextView>();
	private  HashMap<String, String> images_names=new HashMap<String, String>();
	private int punktyZaZadanie;
	private String tytul;
	private String opis;
	private String idZadania;
	private ProgressDialog pDialog;
	private TextView tytulTextView;
	private TextView opisTextView;
	private ImageView objectiveImageView;
	private Button przyciskZglosWykonanie;
	private Button przyciskZobaczMape;
	private LinearLayout lettersLayout;
	private EditText hasloZadania;
	
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
		przyciskZglosWykonanie = (Button) findViewById(R.id.przycisk_zglos_wykonanie);
		przyciskZglosWykonanie.setOnClickListener(this);
		przyciskZobaczMape = (Button) findViewById(R.id.przycisk_zobacz_mape);
		przyciskZobaczMape.setOnClickListener(this);
		objectiveImageView=(ImageView) findViewById(R.id.objectiveImageView);
		objectiveImageView.setVisibility(View.GONE);
		hasloZadania = (EditText)findViewById(R.id.pole_zadanie1_haslo);
		hasloZadania.setVisibility(View.INVISIBLE);
		lettersLayout=(LinearLayout) findViewById(R.id.letters_layout);
		lettersLayout.setVisibility(View.INVISIBLE);
		new GetActiveObjectives().execute();
		
		if(Integer.parseInt(idZadania)==1){
			hasloZadania.setVisibility(View.VISIBLE);
			lettersLayout.setVisibility(View.VISIBLE);
			przyciskZobaczMape.setText("Zg�o� wykonanie zadania");
			letters_fields.put("letter_0",(TextView) findViewById(R.id.letter_1));
			letters_fields.put("letter_1",(TextView) findViewById(R.id.letter_2));
			letters_fields.put("letter_2",(TextView) findViewById(R.id.letter_3));
			letters_fields.put("letter_3",(TextView) findViewById(R.id.letter_4));
			letters_fields.put("letter_4",(TextView) findViewById(R.id.letter_5));
			letters_fields.put("letter_5",(TextView) findViewById(R.id.letter_6));
		}else if(Integer.parseInt(idZadania)==3){
			images_names.put("objective_1","objective_1");
			images_names.put("objective_2","objective_2");
			przyciskZobaczMape.setVisibility(View.INVISIBLE);
			opisTextView.setVisibility(View.INVISIBLE);
			objectiveImageView=(ImageView) findViewById(R.id.objectiveImageView);
			objectiveImageView.setVisibility(View.VISIBLE);
			
		}

		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_zobacz_mape:
			if(Integer.parseInt(idZadania)==1){
				checkPassword();
			}else if(Integer.parseInt(idZadania)==2){
				i = new Intent(this, MapMenu.class);
				i.putExtra("idZadania", idZadania);		
				ArrayList<String> stuff = new ArrayList<String>();
				stuff.addAll(activeObjectives);
				i.putStringArrayListExtra("activeObjectives",stuff);
				startActivity(i);
			}
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
	            pDialog.setMessage("Pobieranie aktualnych cel�w zadania");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();

	        }
		 
		@Override
		protected String doInBackground(String... args) {
			
			int success;
			try {
            
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QuestMenu.this);
               params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
               Log.d("request!", "starting");

               
               JSONObject json = jsonParser.makeHttpRequest(
            		   GET_OBJECTIVES_URL, "POST", params);

             
               Log.d("Odpowied� json", json.toString());

              
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               
               	return json.getString(TAG_COMPLETED_OBJ);
               }else{
               	Log.d("Nie pobrano ilo�ci g�os�w", json.getString(TAG_MESSAGE));
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
		    	 modifyObjectives(activeObjectives);
		    	 if(Integer.parseInt(idZadania)==1){
		    		 showLetters(activeObjectives);
		    	 }else if(Integer.parseInt(idZadania)==3){
		    		 showObjectiveImage(activeObjectives);
		    	 }
		    	
	            }
	    	
	    	 
	}
	
	
	}	
	
	
	class SetActiveObjectives extends AsyncTask<String, String, String> {
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(QuestMenu.this);
	            pDialog.setMessage("Aktualizowanie aktualnych cel�w zadania");
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
            	  if(activeObjectives.get(i)!=""){
            		  newObjectives=newObjectives+activeObjectives.get(i)+","; 
            	  }
            	  
              }
              params.add(new BasicNameValuePair("completed_objectives", newObjectives));
              Log.d("request!", "starting");

              
              JSONObject json = jsonParser.makeHttpRequest(
            		  SET_OBJECTIVES_URL, "POST", params);

            
              Log.d("Odpowied� json", json.toString());

             
              success = json.getInt(TAG_SUCCESS);
              if (success == 1) {
              
              	return json.getString(TAG_COMPLETED_OBJ);
              }else{
              	Log.d("Nie zaktualizowano cel�w", json.getString(TAG_MESSAGE));
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
         
             List<NameValuePair> params = new ArrayList<NameValuePair>();
             SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QuestMenu.this);
             params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
             params.add(new BasicNameValuePair("quest_id", id_wykonanego_zadania));
             Log.d("request!", "starting");

           
             JSONObject json = jsonParser.makeHttpRequest(
            		 COMPLETE_QUEST_URL, "POST", params);

        
             Log.d("Odpowied� json", json.toString());

            
             success = json.getInt(TAG_SUCCESS);
             if (success == 1) {
             	return json.getString(TAG_MESSAGE);
             }else{
             	Log.d("Nie zaktualizowano cel�w", json.getString(TAG_MESSAGE));
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
	    	 new ModifyPoints(punktyZaZadanie).execute(); // 50 pkt za zadanie
	    	 finish();
	    	 
	}
	
	
	}
	
	
	private void modifyObjectives(List<String> activeObjectives){
		String[] text=opis.split("\\r?\\n");
		
		 
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
	
	private void showLetters(ArrayList<String> activeObjectives){
		for(int i=0;i<activeObjectives.size();i++){
			    if(activeObjectives.get(i)!=""){
			    	
			        Character litera_do_wstawienia = QUEST_PASSWORD.charAt(Character.getNumericValue(activeObjectives.get(i).charAt(0)));
			        
			        
			    	letters_fields.get("letter_"+Integer.parseInt(activeObjectives.get(i))).setText(String.valueOf(litera_do_wstawienia));
			    	
			    }
			}
	}
	
	private void checkCurrentPosition(){
		if(Integer.parseInt(idZadania)==1){
			GPSTracker gps = new GPSTracker(this);
			if(gps.canGetLocation()){
				Location currentLocation=gps.getLocation();
				Location objectiveLocation=new Location("");
				
				Double[] LatitudeQuestMarkers = {51.253470,51.249676,51.250191,51.248125,51.250941,51.247508};
				Double[] LongitudeQuestMarkers = {22.572297,22.570110,22.575093,22.559492,22.556014,22.566254};
				
				
				for(int i=0;i<LatitudeQuestMarkers.length;i++){
					objectiveLocation.setLatitude(LatitudeQuestMarkers[i]);
					objectiveLocation.setLongitude(LongitudeQuestMarkers[i]);
					if(!activeObjectives.contains(Integer.toString(i)) && currentLocation.distanceTo(objectiveLocation)<500){
						activeObjectives.add(Integer.toString(i));
						new SetActiveObjectives().execute();
						showLetters(activeObjectives);
						if(activeObjectives.size()==5){
							activeObjectives.clear();
							
							new CompleteQuest("1").execute();
						}
					}
				}
			}
		}else if(Integer.parseInt(idZadania)==2){
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
						punktyZaZadanie=50;
						new CompleteQuest("2").execute();
					}
					
					break;
				}
			}
			Log.d("MMtest AKTUALNA LOKALIZACJA"," "+currentLocation);
			
			}else{
				gps.showSettingsAlert();
				
			}
			
		}else if(Integer.parseInt(idZadania)==3){
			GPSTracker gps = new GPSTracker(this);
			if(gps.canGetLocation()){	
				Location currentLocation=gps.getLocation();
				Location objectiveLocation=new Location("");
				
				if(activeObjectives.contains("1")){
					objectiveLocation.setLatitude(51.225477); // majdanek 
					objectiveLocation.setLongitude(22.604204);
					
					if(currentLocation.distanceTo(objectiveLocation)<500){
						activeObjectives.add("1");
						new SetActiveObjectives().execute();
					}
					
				}else{
					objectiveLocation.setLatitude(51.247055); // wie�a trynitarska
					objectiveLocation.setLongitude(22.568230);
					if(currentLocation.distanceTo(objectiveLocation)<500){
						activeObjectives.add("2");
						new SetActiveObjectives().execute();
					}
					
				}
				if(activeObjectives.size()==2){
					activeObjectives.clear();
					punktyZaZadanie=100;
					new CompleteQuest("3").execute();
				}
				
		
				Log.d("MMtest AKTUALNA LOKALIZACJA"," "+currentLocation);
			}else{
				gps.showSettingsAlert();
			}
		}
	}
	public void checkPassword(){
		int punkty_do_przyznania=20;
		 if(hasloZadania.getText().toString().toLowerCase().equals(QUEST_PASSWORD.toString().toLowerCase())){
			 for(int i=0;i<QUEST_PASSWORD.length();i++){
				if(letters_fields.get("letter_"+i).getText().equals("_")){
					punkty_do_przyznania=punkty_do_przyznania+20;
				}
			}
			if(punkty_do_przyznania>=100){
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QuestMenu.this);
				String achievmentUser=sp.getString("username", "anonymous");
				new CompleteAchievment("4",achievmentUser).execute(); // achievment - za rozwiazanie hasla przy odkrytych maksymalnie 2 literach
			}
			activeObjectives.clear();
			punktyZaZadanie=punkty_do_przyznania;
			new CompleteQuest("1").execute();
			
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QuestMenu.this);
			String achievmentUser=sp.getString("username", "anonymous");
			new CompleteAchievment("0",achievmentUser).execute(); // achievment - wykonanie pierwszego zadania
			 
		 }else{
			 Toast.makeText(QuestMenu.this, "Niepoprawne has�o", Toast.LENGTH_LONG).show();
		 }
			
	}
	
	
	public void showObjectiveImage(ArrayList<String> activeObjectives){
		
		if(activeObjectives!=null && activeObjectives.size()>0 && !activeObjectives.isEmpty()){
			
			if(activeObjectives.get(0)!="" && activeObjectives.get(0)!=" " && !activeObjectives.get(0).equals(" ")){

				objectiveImageView.setImageResource(QuestMenu.this.getResources().getIdentifier("objective_"+(Integer.parseInt(activeObjectives.get(activeObjectives.size()-1))+1), "drawable",QuestMenu.this.getPackageName())); 	
			}

		}
		
	}
	
	class ModifyPoints extends AsyncTask<String, String, String> {
		private String pointsToSave;
		public ModifyPoints(int points) {
	        super();
	        pointsToSave=Integer.toString(points);
	        
	    }
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(QuestMenu.this);
	            pDialog.setMessage("Trwa modyfikacja punkt�w...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	           // pDialog.show();

	        }
		 
		@Override
		protected String doInBackground(String... args) {
			
			int success;
			try {
             
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QuestMenu.this);
               params.add(new BasicNameValuePair("points", pointsToSave));
               params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
               Log.d("request!", "starting");

             
               JSONObject json = jsonParser.makeHttpRequest(
            		   MODIFY_POINTS_URL, "POST", params);

             
               Log.d("Odpowied� json", json.toString());

           
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               	Log.d("Zaktualizowano ilo�� punkt�w!", json.toString());
               	
               	
               	return json.getString(TAG_MESSAGE);
               }else{
               	Log.d("Nie zaktualizowano ilo�ci g�os�w", json.getString(TAG_MESSAGE));
               	return json.getString(TAG_MESSAGE);

               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
			return null;
		}
		
		 protected void onPostExecute(String file_url) {
	    	 pDialog.dismiss();
	    }
		
	}
	
}	
