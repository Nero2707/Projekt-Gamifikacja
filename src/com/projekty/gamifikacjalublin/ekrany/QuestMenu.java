package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.R.id;
import com.projekty.gamifikacjalublin.R.layout;
import com.projekty.gamifikacjalublin.core.JSONParser;
import com.projekty.gamifikacjalublin.ekrany.IdeaMenu.GetVotes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class QuestMenu extends Activity implements OnClickListener{
	private Intent i;
	private static final String GET_OBJECTIVES_URL = "http://www.lublinquest.ugu.pl/webservice/getcompletedobjectives.php";
	private static final String SET_OBJECTIVES_URL = "http://www.lublinquest.ugu.pl/webservice/setcompletedobjectives.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_COMPLETED_OBJ = "completed_objectives";
	private String tytul;
	private String opis;
	private String idZadania;
	private ProgressDialog pDialog;
	private TextView tytulTextView;
	private TextView opisTextView;
	private View przyciskZglosWykonanie;
	private View przyciskZobaczMape;
	//private String activeObjectives;
	private List<String> activeObjectives;
	JSONParser jsonParser = new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quest_menu);
		
		i = getIntent();
		tytul =  i.getStringExtra("tytul");
		opis =  i.getStringExtra("opis");
		idZadania=i.getStringExtra("idZadania");
		
		tytulTextView = (TextView) findViewById(R.id.zadanie_tytul);
		tytulTextView.setText(tytul);
		opisTextView = (TextView) findViewById(R.id.zadanie_opis);
		opisTextView.setText(opis);
		przyciskZglosWykonanie = findViewById(R.id.przycisk_zglos_wykonanie);
		przyciskZglosWykonanie.setOnClickListener(this);
		przyciskZobaczMape = findViewById(R.id.przycisk_zobacz_mape);
		przyciskZobaczMape.setOnClickListener(this);
		
		new GetActiveObjectives().execute();
		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_zobacz_mape:
			i = new Intent(this, MapMenu.class);
			i.putExtra("idZadania", idZadania);
			
			ArrayList<String> stuff = new ArrayList<String>();
			stuff.addAll(activeObjectives);
			Log.d("MMtest Opis zadania2",""+stuff.getClass().getName());
			i.putStringArrayListExtra("activeObjectives",stuff);

			startActivity(i);
			break;
		
		}

	}
	
	
	
	
	class GetActiveObjectives extends AsyncTask<String, String, String> {
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(QuestMenu.this);
	            pDialog.setMessage("Pobieranie aktualnych celów zadania");
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
               SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QuestMenu.this);
               params.add(new BasicNameValuePair("username", sp.getString("username", "anonymous")));
               Log.d("request!", "starting");

               //Posting user data to script
               JSONObject json = jsonParser.makeHttpRequest(
            		   GET_OBJECTIVES_URL, "POST", params);

               // full json response
               Log.d("OdpowiedŸ json", json.toString());

               // json success element
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               	//Log.d("Testowy log json", json.getString(TAG_COMPLETED_OBJ));
               	//activeObjectives=json.getString(TAG_COMPLETED_OBJ);
               	//Log.d("Testowy log json", activeObjectives);
               	//finish();
               	return json.getString(TAG_COMPLETED_OBJ);
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
	    		 activeObjectives = Arrays.asList(file_url.split("\\s*,\\s*"));
		    	 Log.d("MMtest tablica aktualnych zadan",""+activeObjectives);
		    	 modifyObjectives(activeObjectives);
		    	
		    	
	            }
	    	
	    	 
	}
	
	
	}	
	
	
	
	
	
	private void modifyObjectives(List<String> activeObjectives){
		String[] text=opis.split("\\r?\\n");
		
		 Log.d("MMtest Opis zadania",""+opis.split("\\r?\\n").length);
		 
		 if(Integer.parseInt(idZadania)==2){
			 String newText="";
			 for(int i=0;i<text.length;i++){
				 if(activeObjectives.contains(Integer.toString(i))){	
					 newText=newText+text[i]+" \u2714 \n";
				 }else{
					 newText=newText+text[i]+"\n";
				 }
			 }
			 opisTextView.setText(newText);
			 
		 }
	}
}	
