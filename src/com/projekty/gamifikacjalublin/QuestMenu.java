package com.projekty.gamifikacjalublin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class QuestMenu extends Activity implements OnClickListener{
	private Intent i;
	private String tytul;
	private String opis;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quest_menu);
		
		i = getIntent();
		tytul =  i.getStringExtra("tytul");
		opis =  i.getStringExtra("opis");
		
		TextView tytulTextView = (TextView) findViewById(R.id.zadanie_tytul);
		tytulTextView.setText(tytul);
		TextView opisTextView = (TextView) findViewById(R.id.zadanie_opis);
		opisTextView.setText(opis);
		View przyciskZglosWykonanie = findViewById(R.id.przycisk_zglos_wykonanie);
		przyciskZglosWykonanie.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		/*switch(v.getId()){
		case R.id.przycisk_wyswietl_osiagniecia:
			i = new Intent(this, AchievmentsMenu.class);
			
			startActivity(i);
			System.out.println("tst");
			break;
		
		}*/

	}
}	
