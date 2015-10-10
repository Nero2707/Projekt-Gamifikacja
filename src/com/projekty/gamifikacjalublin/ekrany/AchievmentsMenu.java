package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.R.layout;
import com.projekty.gamifikacjalublin.core.JSONParser;
import com.projekty.gamifikacjalublin.ekrany.QuestMenu.GetActiveObjectives;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievmentsMenu extends Activity{
	private static final String GET_ACHIEVMENTS_URL = "http://www.lublinquest.ugu.pl/webservice/getachievments.php";
	private static final String SET_ACHIEVMENTS_URL = "http://www.lublinquest.ugu.pl/webservice/setachievments.php";
	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_COMPLETED_ACHIEVMENTS = "completed_achievments";
	private static final String TAG_MESSAGE = "message";
	private ArrayList<String> completedAchievments;
	private  HashMap<String, ImageView> images_fields=new HashMap<String, ImageView>();
	
	JSONParser jsonParser = new JSONParser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.achievments_menu);
		
		
		images_fields.put("imageView_0",(ImageView) findViewById(R.id.imageView1));
		images_fields.put("imageView_1",(ImageView) findViewById(R.id.imageView2));
		images_fields.put("imageView_2",(ImageView) findViewById(R.id.imageView3));
		images_fields.put("imageView_3",(ImageView) findViewById(R.id.imageView4));
		images_fields.put("imageView_4",(ImageView) findViewById(R.id.imageView5));
		
		new GetAchievments().execute();
	}
	
	
	
	
	
	
	
	class GetAchievments extends AsyncTask<String, String, String> {
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(AchievmentsMenu.this);
	            pDialog.setMessage("Pobieranie osi¹gniêæ");
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
              SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AchievmentsMenu.this);
              params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
              Log.d("request!", "starting");

              //Posting user data to script
              JSONObject json = jsonParser.makeHttpRequest(
           		   GET_ACHIEVMENTS_URL, "POST", params);

              // full json response
              Log.d("OdpowiedŸ json", json.toString());

              // json success element
              success = json.getInt(TAG_SUCCESS);
              if (success == 1) {
              	//Log.d("Testowy log json", json.getString(TAG_COMPLETED_OBJ));
              	//activeObjectives=json.getString(TAG_COMPLETED_OBJ);
              	//Log.d("Testowy log json", activeObjectives);
              	//finish();
              	return json.getString(TAG_COMPLETED_ACHIEVMENTS);
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
	    	 pDialog.dismiss();
	    	 if (file_url != null){
	    		 completedAchievments = new ArrayList<String>(Arrays.asList(file_url.split("\\s*,\\s*")));
		    	 modifyAchievments(completedAchievments);
	            }
	    	
	    	 
	}
	
	
	}
	
	
	
	private void modifyAchievments(List<String> completedAchievments){
	
		for(int i=0;i<completedAchievments.size();i++){
		    if(completedAchievments.get(i)!=""){
		    	images_fields.get("imageView_"+Integer.parseInt(completedAchievments.get(i))).setImageResource(R.drawable.aktywny_achievment); 	
		    }
		}

	}
}
