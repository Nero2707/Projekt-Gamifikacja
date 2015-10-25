package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.R.id;
import com.projekty.gamifikacjalublin.R.layout;
import com.projekty.gamifikacjalublin.core.JSONParser;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class IdeasListMenu extends ListActivity implements OnClickListener{
	private Intent i;
	private Button przyciskDodajPomysl;
	private ProgressDialog pDialog;
	private static final String READ_IDEAS_URL = "http://lublinquest.ugu.pl/webservice/ideas.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TITLE = "title";
    private static final String TAG_IDEAS = "ideas";
    private static final String TAG_IDEA_ID = "idea_id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_MESSAGE = "message";
    
    private JSONArray mIdeas = null;
    private ArrayList<HashMap<String, String>> mIdeasList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_ideas);
		
		Button przyciskDodajPomysl = (Button)findViewById(R.id.przycisk_dodaj_pomysl);
		przyciskDodajPomysl.setOnClickListener(this);
	}
	
	 @Override
	    protected void onResume() {
	    	// TODO Auto-generated method stub
	    	super.onResume();
	  
	    	new LoadIdeas().execute();
	    }
	 
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_dodaj_pomysl:
			i = new Intent(this, NewIdeaMenu.class);
			startActivity(i);
			break;
	
		}
		
	}
	
	public void updateJSONdata(){


    	
        mIdeasList = new ArrayList<HashMap<String, String>>();
        
     
        JSONParser jParser = new JSONParser();
       
        JSONObject json = jParser.getJSONFromUrl(READ_IDEAS_URL);

      
        try {
            
        
            mIdeas = json.getJSONArray(TAG_IDEAS);

          
            for (int i = 0; i < mIdeas.length(); i++) {
                JSONObject c = mIdeas.getJSONObject(i);

               
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_MESSAGE);
                String username = c.getString(TAG_USERNAME);
                String idea_id= c.getString(TAG_IDEA_ID);

             
                HashMap<String, String> map = new HashMap<String, String>();
              
                map.put(TAG_TITLE, title);
                map.put(TAG_MESSAGE, content);
                map.put(TAG_USERNAME, username);
                map.put(TAG_IDEA_ID, idea_id);
               
                mIdeasList.add(map);
                
               
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
	
	private void updateList() {
		
				ListAdapter adapter = new SimpleAdapter(this, mIdeasList,
						R.layout.single_idea, new String[] { TAG_TITLE, TAG_MESSAGE,
								TAG_USERNAME }, new int[] { R.id.title, R.id.message,
								R.id.username });

				
				setListAdapter(adapter);
				
			
				ListView lv = getListView();	
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					
						HashMap chosenIdea=(HashMap) parent.getAdapter().getItem(position);
						i = new Intent(IdeasListMenu.this, IdeaMenu.class);
						i.putExtra("tytul",chosenIdea.get("title").toString());
						i.putExtra("opis", chosenIdea.get("message").toString());
						i.putExtra("idea_id",chosenIdea.get("idea_id").toString());
						i.putExtra("autor",chosenIdea.get("username").toString());
						startActivity(i);
					

					}
				});
    }
	
	
	public class LoadIdeas extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(IdeasListMenu.this);
			pDialog.setMessage("Wczytywanie pomys³ów...");
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

}
