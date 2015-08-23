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
	  //JSON IDS:
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
	    	//loading the comments via AsyncTask
	    	new LoadIdeas().execute();
	    }
	 
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_dodaj_pomysl:
			i = new Intent(this, NewIdeaMenu.class);
			startActivity(i);
			break;
	//	case R.id.pomysl_test_tytul:
		//	i = new Intent(this, IdeaMenu.class);
			// i.putExtra("tytul", "Temat pierwszego pomys³u");
			// i.putExtra("opis", "Opis pomys³u testtesttesttesttesttesttesttesttesttesttesttest");
		//	startActivity(i);
	//		break;
		}
		
	}
	
	public void updateJSONdata(){

        // Instantiate the arraylist to contain all the JSON data.
    	// we are going to use a bunch of key-value pairs, referring
    	// to the json element name, and the content, for example,
    	// message it the tag, and "I'm awesome" as the content..
    	
        mIdeasList = new ArrayList<HashMap<String, String>>();
        
        // Bro, it's time to power up the J parser 
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(READ_IDEAS_URL);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {
            
        	//I know I said we would check if "Posts were Avail." (success==1)
        	//before we tried to read the individual posts, but I lied...
        	//mComments will tell us how many "posts" or comments are
        	//available
            mIdeas = json.getJSONArray(TAG_IDEAS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mIdeas.length(); i++) {
                JSONObject c = mIdeas.getJSONObject(i);

                //gets the content of each tag
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_MESSAGE);
                String username = c.getString(TAG_USERNAME);
                String idea_id= c.getString(TAG_IDEA_ID);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
              
                map.put(TAG_TITLE, title);
                map.put(TAG_MESSAGE, content);
                map.put(TAG_USERNAME, username);
                map.put(TAG_IDEA_ID, idea_id);
                // adding HashList to ArrayList
                mIdeasList.add(map);
                
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
				ListAdapter adapter = new SimpleAdapter(this, mIdeasList,
						R.layout.single_idea, new String[] { TAG_TITLE, TAG_MESSAGE,
								TAG_USERNAME }, new int[] { R.id.title, R.id.message,
								R.id.username });

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
						HashMap chosenIdea=(HashMap) parent.getAdapter().getItem(position);
						i = new Intent(IdeasListMenu.this, IdeaMenu.class);
						i.putExtra("tytul",chosenIdea.get("title").toString());
						i.putExtra("opis", chosenIdea.get("message").toString());
						i.putExtra("idea_id",chosenIdea.get("idea_id").toString());
						startActivity(i);
						// This method is triggered if an item is click within our
						// list. For our example we won't be using this, but
						// it is useful to know in real life applications.

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
