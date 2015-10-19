package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.core.JSONParser;
import com.projekty.gamifikacjalublin.ekrany.IdeasListMenu.LoadIdeas;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class HighScoresMenu  extends ListActivity{
	private Intent i;
	private static final String READ_SCORES_URL = "http://lublinquest.ugu.pl/webservice/scores.php";
	private ProgressDialog pDialog;
	private JSONArray mScores = null;
	 private static final String TAG_POINTS = "points";
	 private static final String TAG_SCORES = "scores";
	 private static final String TAG_USERNAME = "username";
	private ArrayList<HashMap<String, String>> mScoresList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_scores);
		
	}
	
	 @Override
	    protected void onResume() {
	    	// TODO Auto-generated method stub
	    	super.onResume();
	    	//loading the comments via AsyncTask
	    	new LoadHighScores().execute();
	    }
	 
	
	
	
	public class LoadHighScores extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(HighScoresMenu.this);
			pDialog.setMessage("Wczytywanie najlepszych wyników....");
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
	
	public void updateJSONdata(){

        // Instantiate the arraylist to contain all the JSON data.
    	// we are going to use a bunch of key-value pairs, referring
    	// to the json element name, and the content, for example,
    	// message it the tag, and "I'm awesome" as the content..
    	
        mScoresList = new ArrayList<HashMap<String, String>>();
        
        // Bro, it's time to power up the J parser 
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(READ_SCORES_URL);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {
            
        	//I know I said we would check if "Posts were Avail." (success==1)
        	//before we tried to read the individual posts, but I lied...
        	//mComments will tell us how many "posts" or comments are
        	//available
            mScores = json.getJSONArray(TAG_SCORES);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mScores.length(); i++) {
                JSONObject c = mScores.getJSONObject(i);

                //gets the content of each tag
//                String points = c.getString(TAG_POINTS) + " pkt";
//                String username = c.getString(TAG_USERNAME);

                String points = c.getString(TAG_USERNAME);
                String username = c.getString(TAG_POINTS) + " pkt";
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
              
                map.put(TAG_POINTS, points);
                map.put(TAG_USERNAME, username);
                // adding HashList to ArrayList
                mScoresList.add(map);
                
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
				ListAdapter adapter = new SimpleAdapter(this, mScoresList,
						R.layout.single_score, new String[] { TAG_POINTS, TAG_USERNAME}, new int[] { R.id.score_points, R.id.score_user});

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
						
					}
				});
    }
}
