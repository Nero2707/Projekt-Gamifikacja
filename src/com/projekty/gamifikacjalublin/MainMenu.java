package com.projekty.gamifikacjalublin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenu extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		View przyciskOsiagniecia = findViewById(R.id.przycisk_wyswietl_osiagniecia);
		przyciskOsiagniecia.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_wyswietl_osiagniecia:
			Intent i = new Intent(this, AchievmentsMenu.class);
			startActivity(i);
		}
		
	}
}
