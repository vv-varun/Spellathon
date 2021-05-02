package org.varunverma.spellathon.commonui;

/*
 * This is List View. We don't want to use it becuase I can't show Ads here!
 * May be we will use it in a Paid Version.
 */

import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;

public class CommonGameSolution extends FragmentActivity  {

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
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance().activityStop(this);
	}

}