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
import com.projekty.gamifikacjalublin.R.menu;
import com.projekty.gamifikacjalublin.core.JSONParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	private Intent i;
	private EditText user, pass;
	private Button przyciskZaloguj, przyciskRejestruj;
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	
	 private static final String LOGIN_URL = "http://lublinquest.ugu.pl/webservice/login.php";
	 private static final String TAG_SUCCESS = "success";
	 private static final String TAG_MESSAGE = "message";
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		user = (EditText)findViewById(R.id.pole_login);
		pass = (EditText)findViewById(R.id.pole_haslo);
		
		przyciskZaloguj = (Button)findViewById(R.id.przycisk_zaloguj);
		przyciskZaloguj.setOnClickListener(this);
		
		przyciskRejestruj = (Button)findViewById(R.id.przycisk_zarejestruj);
		przyciskRejestruj.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_zaloguj:
			//i = new Intent(this, MainMenu.class);
			//startActivity(i);
			new AttemptLogin().execute();
			break;
		case R.id.przycisk_zarejestruj:
			i = new Intent(this, RegisterMenu.class);
			startActivity(i);
			break;
		}
		
		
	}
	
	
	
	
	class AttemptLogin extends AsyncTask<String,String,String>{
		
		private boolean failure = false;
		 
		 
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(MainActivity.this);
	        pDialog.setMessage("Logowanie...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

		@Override
		protected String doInBackground(String... args) {
			int success;
			String username= user.getText().toString();
			String password = pass.getText().toString();
			try{
				 // Parametryzowanie
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // pobieranie danych uæytkownika przez httpRequest
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);

                
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Logowanie udane!", json.toString());
                	//pDialog.dismiss();
                	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                	Editor edit = sp.edit();
                	edit.putString("username", username);
                	edit.commit();
                	i = new Intent(MainActivity.this, MainMenu.class);
    				startActivity(i);
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Logowanie nieudane!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);

                }
			}catch (JSONException e){
				e.printStackTrace();
			}
	        return null;

		}

	    protected void onPostExecute(String file_url) {
	    	 pDialog.dismiss();
	    	 if (file_url != null){
	            	Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
	            }
	    }

	}
}
