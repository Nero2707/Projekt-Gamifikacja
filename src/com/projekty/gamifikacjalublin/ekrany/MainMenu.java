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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainMenu extends ListActivity implements OnClickListener{
	private Intent i;
	private static final String READ_QUEST_URL = "http://lublinquest.ugu.pl/webservice/getactivequest.php";
	private static final String NEW_QUEST_URL = "http://lublinquest.ugu.pl/webservice/getnewquest.php";
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_TITLE = "title";
    private static final String TAG_QUEST = "quest";
    private static final String TAG_QUEST_ID = "id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_OBJECTIVES = "objectives";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    private JSONArray mQuests = null;
    private ArrayList<HashMap<String, String>> mQuestsList;
    View przyciskListaPomyslow; 
    View przyciskOsiagniecia;
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
	}
	 @Override
	    protected void onResume() {
	    	// TODO Auto-generated method stub
	    	super.onResume();
	    	//loading the comments via AsyncTask
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
		/*case R.id.zadanie_testowe_viewText:
			i = new Intent(this, QuestMenu.class);
			 i.putExtra("tytul", "Tytul pierwszego zadania testowego");
			 i.putExtra("opis", "Opis pierwszego zadania testowego");
			startActivity(i);	
			break;*/
		case R.id.przycisk_zadania_innych:
			i = new Intent(this, IdeasListMenu.class);
			startActivity(i);
			break;
		}
	
		
		
	}
	
	
	
	
	public void updateJSONdata(){

        // Instantiate the arraylist to contain all the JSON data.
    	// we are going to use a bunch of key-value pairs, referring
    	// to the json element name, and the content, for example,
    	// message it the tag, and "I'm awesome" as the content..
    	
        mQuestsList = new ArrayList<HashMap<String, String>>();
        
        //JSONParser jParser = new JSONParser();
        try {
        	 List<NameValuePair> params = new ArrayList<NameValuePair>();
             SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);
             params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
             
             JSONObject json = jParser.makeHttpRequest(
            		 READ_QUEST_URL, "POST", params);

            mQuests = json.getJSONArray(TAG_QUEST);
          
            // looping through all posts according to the json object returned
            for (int i = 0; i < mQuests.length(); i++) {
                JSONObject c = mQuests.getJSONObject(i);

                //gets the content of each tag
                String title = c.getString(TAG_TITLE);
                String description = c.getString(TAG_DESCRIPTION);
                String objectives = c.getString(TAG_OBJECTIVES);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
              
                map.put(TAG_TITLE, title);
                map.put(TAG_DESCRIPTION, description);
                map.put(TAG_OBJECTIVES, objectives);
                // adding HashList to ArrayList
                mQuestsList.add(map);
                
                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
	
	
	
	private void updateList() {
		// For a ListActivity we need to set the List Adapter, and in order to do
				//that, we need to create a ListAdapter.  This SimpleAdapter,
				//will utilize our updated Hashmapped ArrayList, 
				//use our single_post xml template for each item in our list,
				//and place the appropriate info from the list to the
				//correct GUI id.  Order is important here.
		
				if(mQuestsList.size()>0){
					przyciskPobierzZadanie.setVisibility(View.INVISIBLE);
				}else{
					przyciskPobierzZadanie.setVisibility(View.VISIBLE);
				}
				ListAdapter adapter = new SimpleAdapter(this, mQuestsList,
						R.layout.single_quest, new String[] { TAG_TITLE, TAG_DESCRIPTION }, new int[] { R.id.quest_title, R.id.quest_description});

				// I shouldn't have to comment on this one:
				setListAdapter(adapter);
				
				// Optional: when the user clicks a list item we 
				//could do something.  However, we will choose
				//to do nothing...
				ListView lv = getListView();	
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//Log.d("klik","po klikniêciu "+parent.getAdapter().getItem(position).getClass());
						
						
						/*HashMap chosenIdea=(HashMap) parent.getAdapter().getItem(position);
						i = new Intent(IdeasListMenu.this, IdeaMenu.class);
						i.putExtra("tytul",chosenIdea.get("title").toString());
						i.putExtra("opis", chosenIdea.get("message").toString());
						i.putExtra("idea_id",chosenIdea.get("idea_id").toString());
						startActivity(i);*/
						HashMap activeQuest=(HashMap) parent.getAdapter().getItem(position);
						i = new Intent(MainMenu.this, QuestMenu.class);
						i.putExtra("tytul", activeQuest.get("title").toString());
						i.putExtra("opis", activeQuest.get("objectives").toString());
						//i.putExtra("idea_id",activeQuest.get("objectives").toString());
						startActivity(i);	
						
						// This method is triggered if an item is click within our
						// list. For our example we won't be using this, but
						// it is useful to know in real life applications.

					}
				});
    }
	
	
	
	public class LoadActiveQuest extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainMenu.this);
			pDialog.setMessage("Wczytywanie aktywnych zadañ...");
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
                // Building Parameters
            	 List<NameValuePair> params = new ArrayList<NameValuePair>();
                 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);
                 params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jParser.makeHttpRequest(
                		NEW_QUEST_URL, "POST", params);

                // json success element
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
	            // dismiss the dialog once product deleted
	            pDialog.dismiss();
	            if (file_url != null){
	            	Toast.makeText(MainMenu.this, file_url, Toast.LENGTH_LONG).show();
	            	new LoadActiveQuest().execute();
	            }

	        }
		
	}
}
