package com.ayansh.spellathon.android.commonui;

/*
 * This is List View. We don't want to use it becuase I can't show Ads here!
 * May be we will use it in a Paid Version.
 */

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.ads.InterstitialAd;

public class CommonGameSolution extends AppCompatActivity {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
			case android.R.id.home:
				finish();
				return true;
				
			default:
	            return false;
		}
		
	}

	@Override
	public void onDestroy() {

		showInterstitialAd();
		super.onDestroy();
	}

	protected void showInterstitialAd(){

		InterstitialAd iad = MyInterstitialAd.getInterstitialAd(this);
		if(iad.isLoaded()){
			iad.show();
		}
	}
}