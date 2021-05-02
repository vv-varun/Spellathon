package org.varunverma.spellathon.ui;

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.commonui.CommonDownloadGame;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class DownloadGame extends CommonDownloadGame {
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamedownload);
		setTitle("Download more Games");

		// Tracking.
		EasyTracker.getInstance().activityStart(this);
		
		application = Application.get_instance();
		
		// Enable app icon as back button
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
        	ActionBar actionBar = getActionBar();
        	actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
		TV = (TextView) findViewById(R.id.text);
		TV.setTextColor(getResources().getColor(R.color.black));
		
		TV.setText("Downloading new Puzzles... Please be patient... \n");
		downloadPuzzles();
		
	}
}