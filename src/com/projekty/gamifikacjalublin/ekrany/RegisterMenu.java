package com.projekty.gamifikacjalublin.ekrany;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.core.JSONParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterMenu extends Activity implements OnClickListener{
	private Intent i;
	private EditText user,pass;
	private Button przyciskRejestruj;
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private static final String LOGIN_URL = "http://lublinquest.ugu.pl/webservice/register.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_menu);
		
		user = (EditText)findViewById(R.id.pole_login_register);
		pass = (EditText)findViewById(R.id.pole_haslo_register);
		
		View przyciskRejestruj = findViewById(R.id.przycisk_zarejestruj_register);
		przyciskRejestruj.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_zarejestruj_register:		
			new CreateUser().execute();
			break;
		}
	}
	
	
	class CreateUser extends AsyncTask<String, String, String> {
		boolean failure = false;
		 @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(RegisterMenu.this);
	            pDialog.setMessage("Tworzenie u¿ytkownika...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
		 
		@Override
		protected String doInBackground(String... args) {
			int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("U¿ytkownik utworzony!", json.toString());
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("U¿ytkownik nie utworzony!", json.getString(TAG_MESSAGE));
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
	            	Toast.makeText(RegisterMenu.this, file_url, Toast.LENGTH_LONG).show();
	            }

	        }
		
	}

}
