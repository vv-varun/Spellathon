package org.varunverma.spellathon.ui;

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.commonui.CommonDisplayInfo;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.analytics.tracking.android.EasyTracker;

@SuppressLint("SetJavaScriptEnabled")
public class DisplayInfo extends CommonDisplayInfo {
	
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        
        // Tracking.
        EasyTracker.getInstance().activityStart(this);
        
        my_web_view = (WebView) findViewById(R.id.info);       
        my_web_view.getSettings().setJavaScriptEnabled(true);
        
        // Enable app icon as back button
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
        	ActionBar actionBar = getActionBar();
        	actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        int type = this.getIntent().getIntExtra("Type", -1);
        String title = this.getIntent().getStringExtra("Title");
        if(title == null || title.contentEquals("")){
        	title = "Spellathon by Varun";
        }
        
        this.setTitle(title);
        
        switch (type){
        
        case 1:
        	String fileName = getIntent().getStringExtra("File");
        	show_from_raw_source(fileName);
        	break;
        
        case 2:
        	html_text = this.getIntent().getStringExtra("HTML");
        	show_from_direct_source();
        	break;
        }
		
	}

}