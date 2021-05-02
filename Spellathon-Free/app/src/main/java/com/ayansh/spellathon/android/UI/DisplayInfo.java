package com.ayansh.spellathon.android.UI;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;
import com.ayansh.spellathon.android.commonui.CommonDisplayInfo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

public class DisplayInfo extends CommonDisplayInfo {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        Application app = Application.get_instance();
        app.set_context(getApplicationContext());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        my_web_view = (WebView) findViewById(R.id.info);       
        my_web_view.getSettings().setJavaScriptEnabled(true);

        my_web_view.setBackgroundColor(Color.TRANSPARENT);
        my_web_view.setBackgroundResource(R.drawable.bg_nowords);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        
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

            case 3:
                String subject = getIntent().getStringExtra("Subject");
                String content = getIntent().getStringExtra("Content");
                html_text = "<html><body>" +
                        "<h3>" + subject + "</h3>" +
                        "<p>" + content + "</p>" +
                        "</body></html>";
                show_from_direct_source();
                break;
        }
		
	}

}