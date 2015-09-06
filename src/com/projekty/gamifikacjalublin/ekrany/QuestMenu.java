package com.projekty.gamifikacjalublin.ekrany;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.R.id;
import com.projekty.gamifikacjalublin.R.layout;

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
		View przyciskZobaczMape = findViewById(R.id.przycisk_zobacz_mape);
		przyciskZobaczMape.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_zobacz_mape:
			i = new Intent(this, MapMenu.class);
			startActivity(i);
			break;
		
		}

	}
}	
