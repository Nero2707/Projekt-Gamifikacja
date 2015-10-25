package com.projekty.gamifikacjalublin.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.projekty.gamifikacjalublin.ekrany.AchievmentsMenu;

public class CompleteAchievment extends AsyncTask<String, String, String> {
	private static final String SET_ACHIEVMENTS_URL = "http://www.lublinquest.ugu.pl/webservice/setachievments.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private String username;
	JSONParser jsonParser = new JSONParser();
	
	String numer_achievmentu;
	
	public CompleteAchievment(String numer_achievmentu,String username){
		this.numer_achievmentu=numer_achievmentu;
		this.username=username;
	}
	
	
	
	 @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
	 
	@Override
	protected String doInBackground(String... args) {
		
		int success;
		try {

         List<NameValuePair> params = new ArrayList<NameValuePair>();
         params.add(new BasicNameValuePair("username", username));
         params.add(new BasicNameValuePair("completed_achievments", numer_achievmentu));
         Log.d("request!", "starting");


         JSONObject json = jsonParser.makeHttpRequest(
        		 SET_ACHIEVMENTS_URL, "POST", params);


         Log.d("OdpowiedŸ json", json.toString());


         success = json.getInt(TAG_SUCCESS);
         if (success == 1) {
         
         	return json.getString(TAG_MESSAGE);
         }else{
         	Log.d("Nie zaktualizowano celów", json.getString(TAG_MESSAGE));
         	return json.getString(TAG_MESSAGE);

         }
     } catch (JSONException e) {
         e.printStackTrace();
     }
		return null;
	}
	
	 protected void onPostExecute(String file_url) {

    	 
}

}
