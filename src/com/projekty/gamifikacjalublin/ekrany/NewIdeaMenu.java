package com.projekty.gamifikacjalublin.ekrany;

import com.projekty.gamifikacjalublin.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class NewIdeaMenu extends Activity implements OnClickListener{
	private Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_idea_menu);
		
		View przyciskDodajPomysl = findViewById(R.id.przycisk_dodaj_pomysl);
		przyciskDodajPomysl.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_dodaj_pomysl:
			break;	
		}
		
	}

}
