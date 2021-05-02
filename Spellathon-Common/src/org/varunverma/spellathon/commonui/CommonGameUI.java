package org.varunverma.spellathon.commonui;

import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;

public abstract class CommonGameUI extends FragmentActivity implements CommonGameUIFragment.Callbacks {
	
	protected boolean multiPane;
	
	@Override
	public void addWord(String word) {
		// Nothing
	}

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