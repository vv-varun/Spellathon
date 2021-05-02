package org.varunverma.spellathon.ui;

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.commonui.CommonExample;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class Example extends CommonExample {
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example);
		setTitle("Example - How to play");

		// Tracking.
		EasyTracker.getInstance().activityStart(this);
		
		TV = (TextView) findViewById(R.id.text);
		TV.setTextColor(getResources().getColor(R.color.black));
		initializeText();
		Spannable t = (Spannable) Html.fromHtml(text);
		TV.setText(t);
		
		// Enable app icon as back button
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
        	ActionBar actionBar = getActionBar();
        	actionBar.setDisplayHomeAsUpEnabled(true);
        }
		
	}
}