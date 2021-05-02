package com.ayansh.spellathon.android.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.widget.TextView;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;
import com.ayansh.spellathon.android.commonui.CommonExample;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Example extends CommonExample {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example);
		setTitle("Example - How to play");

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

		Application.get_instance();

		// Show Ad.
		if(!Application.constants.isPremiumVersion()){

			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice("8C44DABF2A81BBE341ECAFD882A49F09").build();

			AdView adView = (AdView) findViewById(R.id.adView);

			// Start loading the ad in the background.
			adView.loadAd(adRequest);
		}

		TV = (TextView) findViewById(R.id.text);
		TV.setTextColor(getResources().getColor(R.color.black));
		initializeText();
		Spannable t = (Spannable) Html.fromHtml(text);
		TV.setText(t);

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);
		
	}
}