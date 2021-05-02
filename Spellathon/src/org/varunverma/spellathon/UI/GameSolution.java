package org.varunverma.spellathon.ui;

/*
 * This is List View. We don't want to use it becuase I can't show Ads here!
 * May be we will use it in a Paid Version.
 */

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.commonui.CommonGameSolution;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.analytics.tracking.android.EasyTracker;

public class GameSolution extends CommonGameSolution  {

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Tracking.
		EasyTracker.getInstance().activityStart(this);

		boolean multiPane;
		
		if (findViewById(R.id.game_list) != null) {
			// Multi Pane
			multiPane = true;
		} else {
			multiPane = false;
		}

		// Enable app icon as back button
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
        	ActionBar actionBar = getActionBar();
        	actionBar.setDisplayHomeAsUpEnabled(true);
        }

		if (multiPane) {
			// This activity should not start in multi pane
			finish();
		}

		int gameId = getIntent().getIntExtra("GameId", 1);

		// Start the Game UI Fragment.
		// Create the Fragment.
		FragmentManager fm = this.getSupportFragmentManager();
		Fragment fragment;

		// Show the Game Grid.
		fragment = new GameSolutionFragment();
		Bundle arguments = new Bundle();
		arguments.putInt("GameId", gameId);
		fragment.setArguments(arguments);
		fm.beginTransaction().replace(R.id.main_area, fragment)
				.commitAllowingStateLoss();

	}

}