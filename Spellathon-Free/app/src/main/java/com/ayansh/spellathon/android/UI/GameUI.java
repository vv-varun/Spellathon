package com.ayansh.spellathon.android.UI;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;
import com.ayansh.spellathon.android.commonui.CommonGameUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class GameUI extends CommonGameUI {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_layout);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

		setTitle("Spellathon");
		
		// Show Ad.
		if(!Application.constants.isPremiumVersion()){

			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice("8C44DABF2A81BBE341ECAFD882A49F09").build();

			AdView adView = (AdView) findViewById(R.id.adView);

			// Start loading the ad in the background.
			adView.loadAd(adRequest);
		}

		if(multiPane){
			// This activity should not start in multi pane
			finish();
		}

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);
        
		int gameId = getIntent().getIntExtra("GameId", 1);
		
		// Start the Game UI Fragment.
		// Create the Fragment.
		FragmentManager fm = this.getSupportFragmentManager();
		Fragment fragment;

		// Show the Game Grid.
		fragment = new GameUIFragment();
		Bundle arguments = new Bundle();
		arguments.putInt("GameId", gameId);
		fragment.setArguments(arguments);
		fm.beginTransaction().replace(R.id.main_area, fragment)
				.commitAllowingStateLoss();
		
	}
	
	@Override
	public void showSolution() {
		// Start Game Solution Activity
		Intent gameSolution = new Intent(GameUI.this, GameSolution.class);
		GameUI.this.startActivityForResult(gameSolution,GAME_SOLUTION);
	}

}