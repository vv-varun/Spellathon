package com.ayansh.spellathon.android.commonui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class CommonGameUI extends AppCompatActivity implements CommonGameUIFragment.Callbacks {
	
	protected boolean multiPane = false;
	protected static final int GAME_SOLUTION = 11;
	
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == GAME_SOLUTION){

			// Finish this activity.
			finish();

		}

	}
}