package org.varunverma.spellathon.ui;

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.commonui.CommonEula;

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Eula extends CommonEula {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eula);
		setTitle("End-User License Agreement");

		EULA = (TextView) findViewById(R.id.EULA);
		EULA.setTextColor(getResources().getColor(R.color.black));
		
		accept = (Button) findViewById(R.id.Accept);
		accept.setOnClickListener(this);
		
		decline = (Button) findViewById(R.id.Reject);
		decline.setOnClickListener(this);
			
		application = Application.get_instance();
		
		// Read EULA text from the file.
		readEULA(getResources());
		
		// Now display this EULA.
		Spannable eula = (Spannable) Html.fromHtml(EULA_Text);
		EULA.setText(eula);
		
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