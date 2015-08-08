package com.projekty.gamifikacjalublin.ekrany;

import com.projekty.gamifikacjalublin.R;
import com.projekty.gamifikacjalublin.R.id;
import com.projekty.gamifikacjalublin.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class IdeasListMenu extends Activity implements OnClickListener{
	private Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideas_list_menu);
		
		View przyciskZaloguj = findViewById(R.id.przycisk_dodaj_pomysl);
		przyciskZaloguj.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_dodaj_pomysl:
			i = new Intent(this, NewIdeaMenu.class);
			startActivity(i);
			break;
		case R.id.pomysl_test_tytul:
			i = new Intent(this, IdeaMenu.class);
			 i.putExtra("tytul", "Temat pierwszego pomys³u");
			 i.putExtra("opis", "Opis pomys³u testtesttesttesttesttesttesttesttesttesttesttest");
			startActivity(i);
			break;
		}
		
			
		
	}

}
