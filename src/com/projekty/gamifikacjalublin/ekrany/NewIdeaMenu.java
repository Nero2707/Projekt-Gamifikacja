package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.core.CompleteAchievment;
import com.projekty.gamifikacjalublin.core.JSONParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewIdeaMenu extends Activity implements OnClickListener{
	private Intent i;
	private EditText title, idea;
	private Button  przyciskDodajPomysl;
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private static final String POST_IDEA_URL = "http://lublinquest.ugu.pl/webservice/addidea.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_idea_menu);
		
		title = (EditText)findViewById(R.id.tytul_pomyslu);
		idea = (EditText)findViewById(R.id.opis_pomyslu);
		
		Button przyciskDodajPomysl = (Button)findViewById(R.id.przycisk_dodaj_pomysl);
		przyciskDodajPomysl.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_dodaj_pomysl:
			new PostIdea().execute();
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(NewIdeaMenu.this);
			String achievmentUser=sp.getString("username", "anonymous");
			new CompleteAchievment("2",achievmentUser).execute(); // achievment - umieszczenie pomyslu
			break;	
		}
		
	}
	
	class PostIdea extends AsyncTask<String, String, String> {

	 @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewIdeaMenu.this);
            pDialog.setMessage("Publikacja pomys³u...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		@Override
		protected String doInBackground(String... args) {
			 int success;
	            String post_title = title.getText().toString();
	            String post_idea = idea.getText().toString();
	            
	            SharedPreferences sp =PreferenceManager.getDefaultSharedPreferences(NewIdeaMenu.this);
	            String post_username = sp.getString("username", "anonymous");
	            
	            try {
	                List<NameValuePair> params = new ArrayList<NameValuePair>();
	                params.add(new BasicNameValuePair("username", post_username));
	                params.add(new BasicNameValuePair("title", post_title));
	                params.add(new BasicNameValuePair("idea", post_idea));
	 
	                Log.d("request!", "starting");
	                
	                JSONObject json = jsonParser.makeHttpRequest(
	                		POST_IDEA_URL, "POST", params);
	 
	                Log.d("Post Comment attempt", json.toString());
	 
	                success = json.getInt(TAG_SUCCESS);
	                if (success == 1) {
	                	Log.d("Comment Added!", json.toString());    
	                	finish();
	                	return json.getString(TAG_MESSAGE);
	                }else{
	                	Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
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
	            	Toast.makeText(NewIdeaMenu.this, file_url, Toast.LENGTH_LONG).show();
	            }
	 
	        }
		
	}

}
