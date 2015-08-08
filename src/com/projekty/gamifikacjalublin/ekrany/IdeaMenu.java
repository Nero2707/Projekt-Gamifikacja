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

public class IdeaMenu extends Activity implements OnClickListener{
	private Intent i;
	private String tytul;
	private String opis;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idea_menu);
		
		i = getIntent();
		tytul =  i.getStringExtra("tytul");
		opis =  i.getStringExtra("opis");
		
		TextView tytulTextView = (TextView) findViewById(R.id.pomysl_tytul);
		tytulTextView.setText(tytul);
		TextView opisTextView = (TextView) findViewById(R.id.pomysl_opis);
		opisTextView.setText(opis);
		View przyciskPopieram = findViewById(R.id.przycisk_popieram);
		przyciskPopieram.setOnClickListener(this);
		View przyciskNiePopieram = findViewById(R.id.przycisk_nie_popieram);
		przyciskNiePopieram.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.przycisk_popieram:
			break;
		case R.id.przycisk_nie_popieram:
			break;
		}
	}

}
