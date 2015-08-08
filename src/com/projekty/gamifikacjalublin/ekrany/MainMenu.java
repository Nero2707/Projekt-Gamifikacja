package com.projekty.gamifikacjalublin.ekrany;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.R.id;
import com.projekty.gamifikacjalublin.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenu extends Activity implements OnClickListener{
	private Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		View przyciskOsiagniecia = findViewById(R.id.przycisk_wyswietl_osiagniecia);
		przyciskOsiagniecia.setOnClickListener(this);
		View przyciskListaPomyslow = findViewById(R.id.przycisk_zadania_innych);
		przyciskListaPomyslow.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_wyswietl_osiagniecia:
			i = new Intent(this, AchievmentsMenu.class);
			startActivity(i);
			break;
		case R.id.zadanie_testowe_viewText:
			i = new Intent(this, QuestMenu.class);
			 i.putExtra("tytul", "Tytul pierwszego zadania testowego");
			 i.putExtra("opis", "Opis pierwszego zadania testowego");
			startActivity(i);	
			break;
		case R.id.przycisk_zadania_innych:
			i = new Intent(this, IdeasListMenu.class);
			startActivity(i);
			break;
		}
	
		
		
	}
}
