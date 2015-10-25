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

     
        mScoresList = new ArrayList<HashMap<String, String>>();
       
        JSONParser jParser = new JSONParser();
       
        JSONObject json = jParser.getJSONFromUrl(READ_SCORES_URL);

      
        try {
            
        
            mScores = json.getJSONArray(TAG_SCORES);

          
            for (int i = 0; i < mScores.length(); i++) {
                JSONObject c = mScores.getJSONObject(i);

                String points = c.getString(TAG_USERNAME);
                String username = c.getString(TAG_POINTS) + " pkt";
              
                HashMap<String, String> map = new HashMap<String, String>();
              
                map.put(TAG_POINTS, points);
                map.put(TAG_USERNAME, username);
            
                mScoresList.add(map);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
	
	private void updateList() {
	
				ListAdapter adapter = new SimpleAdapter(this, mScoresList,
						R.layout.single_score, new String[] { TAG_POINTS, TAG_USERNAME}, new int[] { R.id.score_points, R.id.score_user});

			
				setListAdapter(adapter);
				

				ListView lv = getListView();	
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						HashMap chosenScore=(HashMap) parent.getAdapter().getItem(position);
						i = new Intent(HighScoresMenu.this, AchievmentsMenu.class);
						i.putExtra("user",chosenScore.get("points").toString());
						i.putExtra("isCurrentUser", false);
						startActivity(i);
					}
				});
    }
}
