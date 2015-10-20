package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.R.id;
import com.projekty.gamifikacjalublin.R.layout;
import com.projekty.gamifikacjalublin.core.CompleteAchievment;
import com.projekty.gamifikacjalublin.core.JSONParser;
import com.projekty.gamifikacjalublin.ekrany.MainActivity.AttemptLogin;
import com.projekty.gamifikacjalublin.ekrany.RegisterMenu.CreateUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class IdeaMenu extends Activity implements OnClickListener{
	private Intent i;
	private int voted=0;
	private String tytul;
	private String opis;
	private String autor;
	private ProgressDialog pDialog;
	private TextView tytulTextView,opisTextView,glosyTextView;
	private View przyciskPopieram,przyciskNiePopieram;
	JSONParser jsonParser = new JSONParser();
	private int votes=0;
	private static final String GET_VOTES_URL = "http://lublinquest.ugu.pl/webservice/getvotes.php";
	private static final String MODIFY_VOTES_URL = "http://lublinquest.ugu.pl/webservice/modifyvotes.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_VOTES = "votes";
	private static final String TAG_VOTED = "voted";
	private String iloscGlosow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idea_menu);
		
		i = getIntent();
		tytul =  i.getStringExtra("tytul");
		opis =  i.getStringExtra("opis");
		autor= i.getStringExtra("autor");
		tytulTextView = (TextView) findViewById(R.id.pomysl_tytul);
		tytulTextView.setText(tytul);
		opisTextView = (TextView) findViewById(R.id.pomysl_opis);
		opisTextView.setText(opis);
		przyciskPopieram = findViewById(R.id.przycisk_popieram);
		przyciskNiePopieram = findViewById(R.id.przycisk_nie_popieram);
		przyciskPopieram.setVisibility(View.INVISIBLE);
		przyciskNiePopieram.setVisibility(View.INVISIBLE);
		new GetVotes().execute();
		Log.d("pobrano dwa",""+voted);
		
	 

		
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.przycisk_popieram) {
			votes=votes+1;
			new ModifyVotes(votes).execute();
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(IdeaMenu.this);
			String achievmentUser=sp.getString("username", "anonymous");
			new CompleteAchievment("1",achievmentUser).execute(); // achievment - zaglosowanie na pomysl
			finish();
		} else if (id == R.id.przycisk_nie_popieram) {
			votes=votes-1;
			new ModifyVotes(votes).execute();
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(IdeaMenu.this);
			String achievmentUser=sp.getString("username", "anonymous");
			new CompleteAchievment("1",achievmentUser).execute(); // achievment - zaglosowanie na pomysl
			finish();
		}
	}
	
	
	
	
	class GetVotes extends AsyncTask<String, String, String> {
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(IdeaMenu.this);
	            pDialog.setMessage("Pobieranie iloœci g³osów...");
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
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(IdeaMenu.this);
            	
                params.add(new BasicNameValuePair("idea_id", i.getStringExtra("idea_id")));
                params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                		GET_VOTES_URL, "POST", params);

                // full json response
                Log.d("OdpowiedŸ json", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Pobrano iloœæ g³osów!", json.toString());
                	voted=Integer.parseInt(json.getString(TAG_VOTED));
                	//finish();
                	return json.getString(TAG_VOTES);
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
	    		glosyTextView = (TextView) findViewById(R.id.pomysl_glosy);
             	glosyTextView.setText("Iloœæ g³osów : "+file_url);
             	votes=Integer.parseInt(file_url);
	            }
	    	if(voted==0){
	 	 		przyciskPopieram.setVisibility(View.VISIBLE);
	 			przyciskNiePopieram.setVisibility(View.VISIBLE);
	 			przyciskPopieram.setOnClickListener(IdeaMenu.this);
	 	 		przyciskNiePopieram.setOnClickListener(IdeaMenu.this);
	    }
		
	}
	
	
	}	
	class ModifyVotes extends AsyncTask<String, String, String> {
		private String votesToSave;
		public ModifyVotes(int votes) {
	        super();
	        votesToSave=Integer.toString(votes);
	        
	    }
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(IdeaMenu.this);
	            pDialog.setMessage("Aktualizowanie iloœci g³osów...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	            
	            if(votes>=10){
	    			new CompleteAchievment("3",autor).execute(); // achievment - za zdobycie 10 glosow
	            }

	        }
		 
		@Override
		protected String doInBackground(String... args) {
			
			int success;
			try {
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(IdeaMenu.this);
               params.add(new BasicNameValuePair("idea_id", i.getStringExtra("idea_id")));
               params.add(new BasicNameValuePair("votes", votesToSave));
               params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
               Log.d("request!", "starting");

               //Posting user data to script
               JSONObject json = jsonParser.makeHttpRequest(
            		   MODIFY_VOTES_URL, "POST", params);

               // full json response
               Log.d("OdpowiedŸ json", json.toString());

               // json success element
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               	Log.d("Zaktualizowano iloœæ g³osów!", json.toString());
               	
               	//finish();
               	return json.getString(TAG_VOTES);
               }else{
               	Log.d("Nie zaktualizowano iloœci g³osów", json.getString(TAG_MESSAGE));
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
