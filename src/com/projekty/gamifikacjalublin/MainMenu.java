package com.projekty.gamifikacjalublin;

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
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_wyswietl_osiagniecia:
			i = new Intent(this, AchievmentsMenu.class);
			startActivity(i);
			System.out.println("tst");
			break;
		case R.id.zadanie_testowe_viewText:
			i = new Intent(this, QuestMenu.class);
			 i.putExtra("tytul", "Tytul pierwszego zadania testowego");
			 i.putExtra("opis", "Opis pierwszego zadania testowego");
			startActivity(i);	
			break;
		}
	
		
		
	}
}
