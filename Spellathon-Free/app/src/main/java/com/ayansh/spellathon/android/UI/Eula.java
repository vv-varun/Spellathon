package com.ayansh.spellathon.android.UI;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;
import com.ayansh.spellathon.android.commonui.CommonEula;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class Eula extends CommonEula {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eula);
		setTitle("End-User License Agreement");

		EULA = (WebView) findViewById(R.id.EULA);
		EULA.setBackgroundColor(Color.TRANSPARENT);
		EULA.setBackgroundResource(R.drawable.bg_nowords);
		
		accept = (Button) findViewById(R.id.Accept);
		accept.setOnClickListener(this);
		
		decline = (Button) findViewById(R.id.Reject);
		decline.setOnClickListener(this);
			
		application = Application.get_instance();
		
		// Read EULA text from the file.
		readEULA(getResources());
		
		// Now display this EULA.
		EULA.loadDataWithBaseURL("fake://not/needed", EULA_Text, "text/html", "UTF-8", "");
		
	}

	public void onClick(View v) {
		// Handle Button click
		switch(v.getId()){
		
		case R.id.Accept:
			EULAAccepted();
			break;
			
		case R.id.Reject:
			EULADeclined();
			break;
				
		}		
	}
}