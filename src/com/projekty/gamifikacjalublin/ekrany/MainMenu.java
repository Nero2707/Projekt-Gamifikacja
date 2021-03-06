package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.R.id;
import com.projekty.gamifikacjalublin.R.layout;
import com.projekty.gamifikacjalublin.core.JSONParser;
import com.projekty.gamifikacjalublin.ekrany.IdeaMenu.ModifyVotes;
import com.projekty.gamifikacjalublin.ekrany.IdeasListMenu.LoadIdeas;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainMenu extends ListActivity implements OnClickListener{
	private Intent i;
	private static final String READ_QUEST_URL = "http://lublinquest.ugu.pl/webservice/getactivequest.php";
	private static final String NEW_QUEST_URL = "http://lublinquest.ugu.pl/webservice/getnewquest.php";
	private static final String GET_POINTS_URL = "http://www.lublinquest.ugu.pl/webservice/getpoints.php";
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_TITLE = "title";
    private static final String TAG_QUEST = "quest";
    private static final String TAG_QUEST_ID = "id";
    private static final String TAG_POINTS = "points";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_OBJECTIVES = "objectives";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    private JSONArray mQuests = null;
    private ArrayList<HashMap<String, String>> mQuestsList;
    View przyciskListaPomyslow; 
    View przyciskOsiagniecia;
    View przyciskNajlepszeWyniki;
    private TextView punktyTextView;
    private View przyciskPobierzZadanie;
    private JSONParser jParser = new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		przyciskOsiagniecia = findViewById(R.id.przycisk_wyswietl_osiagniecia);
		przyciskOsiagniecia.setOnClickListener(this);
		przyciskListaPomyslow = findViewById(R.id.przycisk_zadania_innych);
		przyciskListaPomyslow.setOnClickListener(this);
		przyciskPobierzZadanie = findViewById(R.id.przycisk_pobierz_zadanie);
		przyciskPobierzZadanie.setOnClickListener(this);
		przyciskNajlepszeWyniki = findViewById(R.id.przycisk_najlepsze_wyniki);
		przyciskNajlepszeWyniki.setOnClickListener(this);
		

	}
	 @Override
	    protected void onResume() {
	    	
	    	super.onResume();
	    	
	    	new GetPoints().execute();
	    	new LoadActiveQuest().execute();
	    	
	    }
	 
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_wyswietl_osiagniecia:
			i = new Intent(this, AchievmentsMenu.class);
			startActivity(i);
			break;
		case R.id.przycisk_pobierz_zadanie:
				new NewQuest().execute();
			break;
		case R.id.przycisk_najlepsze_wyniki:
			i = new Intent(this, HighScoresMenu.class);
			startActivity(i);
		break;
		case R.id.przycisk_zadania_innych:
			i = new Intent(this, IdeasListMenu.class);
			startActivity(i);
			break;
		}
	
		
		
	}
	
	
	
	
	public void updateJSONdata(){

      
        mQuestsList = new ArrayList<HashMap<String, String>>();
        
       
        try {
        	 List<NameValuePair> params = new ArrayList<NameValuePair>();
             SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);
             params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
             
             JSONObject json = jParser.makeHttpRequest(
            		 READ_QUEST_URL, "POST", params);

            mQuests = json.getJSONArray(TAG_QUEST);
          
           
            for (int i = 0; i < mQuests.length(); i++) {
                JSONObject c = mQuests.getJSONObject(i);

              
                String title = c.getString(TAG_TITLE);
                String description = c.getString(TAG_DESCRIPTION);
                String objectives = c.getString(TAG_OBJECTIVES);
                String id = c.getString(TAG_QUEST_ID);

              
                HashMap<String, String> map = new HashMap<String, String>();
              
                map.put(TAG_TITLE, title);
                map.put(TAG_DESCRIPTION, description);
                map.put(TAG_OBJECTIVES, objectives);
                map.put(TAG_QUEST_ID, id);
            
                mQuestsList.add(map);
                
               
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
	
	
	
	private void updateList() {
	
		
				if(mQuestsList.size()>0){
					przyciskPobierzZadanie.setVisibility(View.INVISIBLE);
				}else{
					przyciskPobierzZadanie.setVisibility(View.VISIBLE);
				}
				ListAdapter adapter = new SimpleAdapter(this, mQuestsList,
						R.layout.single_quest, new String[] { TAG_TITLE, TAG_DESCRIPTION }, new int[] { R.id.quest_title, R.id.quest_description});

				
				setListAdapter(adapter);
				
			
				ListView lv = getListView();	
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						HashMap activeQuest=(HashMap) parent.getAdapter().getItem(position);
						i = new Intent(MainMenu.this, QuestMenu.class);
						i.putExtra("tytul", activeQuest.get("title").toString());
						i.putExtra("opis", activeQuest.get("objectives").toString());
						i.putExtra("idZadania", activeQuest.get("id").toString());
		
						startActivity(i);	
						
						

					}
				});
    }
	
	
	
	public class LoadActiveQuest extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainMenu.this);
			pDialog.setMessage("Wczytywanie aktywnych zada�...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			updateJSONdata();
			return null;
		}
		
		 @Override
	        protected void onPostExecute(Boolean result) {
	            super.onPostExecute(result);
	            pDialog.dismiss();
	            updateList();
	        }
		
	}
	
	
	
	
	
	class NewQuest extends AsyncTask<String, String, String> {
		boolean failure = false;
		
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(MainMenu.this);
	            pDialog.setMessage("Pobieranie nowego zadania...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
		 
		@Override
		protected String doInBackground(String... args) {
			int success;
			
             
            try {
               
            	 List<NameValuePair> params = new ArrayList<NameValuePair>();
                 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);
                 params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
                Log.d("request!", "starting");

               
                JSONObject json = jParser.makeHttpRequest(
                		NEW_QUEST_URL, "POST", params);

               
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Zadanie pobrane!", json.toString());
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Zadanie nie pobrane!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
			return null;
		}
		
		 protected void onPostExecute(String file_url) {
	      
	            pDialog.dismiss();
	            if (file_url != null){
	            	Toast.makeText(MainMenu.this, file_url, Toast.LENGTH_LONG).show();
	            	new LoadActiveQuest().execute();
	            }

	        }
		
	}
	
	
	
	
	class GetPoints extends AsyncTask<String, String, String> {
		
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(MainMenu.this);
	            pDialog.setMessage("Aktualizowanie ilo�ci punkt�w...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	        

	        }
		 
		@Override
		protected String doInBackground(String... args) {
			
			int success;
			try {
              
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);
               params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
               Log.d("request!", "starting");

             
               JSONObject json = jParser.makeHttpRequest(
            		   GET_POINTS_URL, "POST", params);

              
               Log.d("Odpowied� json", json.toString());

            
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               	Log.d("Zaktualizowano ilo�� g�os�w!", json.toString());
               	
              
               	return json.getString(TAG_POINTS);
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
	    	 punktyTextView = (TextView) findViewById(R.id.punkty_tytul);
          	 punktyTextView.setText("Punkty: "+file_url);
	    }
		
	}
}
