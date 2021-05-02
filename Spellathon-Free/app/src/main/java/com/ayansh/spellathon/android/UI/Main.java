package com.ayansh.spellathon.android.UI;

/*
 * by Varun Verma
 */

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ayansh.spellathon.android.commonui.MyInterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;

public class Main extends AppCompatActivity implements View.OnClickListener{

	private Application application;
	boolean mExplicitSignOut = false;
	private static int REQUEST_ACHIEVEMENTS = 501;
	private static int REQUEST_LEADERBOARD = 502;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("Spellathon");

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

		application = Application.get_instance();

		// Set the context of the application
		application.set_context(getApplicationContext());

		// Show Ad.
		if(!Application.constants.isPremiumVersion()){

			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice("8C44DABF2A81BBE341ECAFD882A49F09").build();

			AdView adView = (AdView) findViewById(R.id.adView);

			// Start loading the ad in the background.
			adView.loadAd(adRequest);

			// Request InterstitialAd
			MyInterstitialAd.getInterstitialAd(this);
			MyInterstitialAd.requestNewInterstitial();
		}

		String greeting;
		Player p;

		try{

			p = Games.Players.getCurrentPlayer(application.mGoogleApiClient);

			if(application.readStringPreference("PlayerID").contentEquals("")){
				application.savePreference("PlayerID",p.getPlayerId());
			}

			if(application.readStringPreference("PlayerName").contentEquals("")){
				application.savePreference("PlayerName",p.getDisplayName());
			}

		}catch(Exception e){
			p = null;
		}

		if (p == null) {
			greeting = "Welcome back!";
		} else {
			greeting = "Welcome back " + p.getDisplayName() + " !";
		}

		updateButton();

		((TextView) findViewById(R.id.greeting_message)).setText(greeting);

		findViewById(R.id.random_game).setOnClickListener(this);
		findViewById(R.id.example).setOnClickListener(this);
		findViewById(R.id.app_purchase).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);
		findViewById(R.id.achievements).setOnClickListener(this);
		findViewById(R.id.leaderboard).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){

			case R.id.About:
				Intent about = new Intent(Main.this, DisplayInfo.class);
				about.putExtra("Type", 1);
				about.putExtra("Title", "About:");
				about.putExtra("File", "about.html");
				Main.this.startActivity(about);
				break;

			case R.id.ShowEula:
				Intent eula = new Intent(Main.this, DisplayInfo.class);
				eula.putExtra("Type", 1);
				eula.putExtra("File", "eula.html");
				eula.putExtra("Title", "Terms and Conditions: ");
				Main.this.startActivity(eula);
				break;

			case R.id.NewFeatures:
				Intent new_features = new Intent(Main.this, DisplayInfo.class);
				new_features.putExtra("Type", 1);
				new_features.putExtra("File", "new_features.html");
				new_features.putExtra("Title", "New features: ");
				Main.this.startActivity(new_features);
				break;

			case R.id.TryPremium:
				Intent try_premium = new Intent(Main.this, TryPremiumFeatures.class);
				Main.this.startActivity(try_premium);
				break;

			case R.id.Settings:
				Intent settings = new Intent(Main.this, SettingsActivity.class);
				Main.this.startActivity(settings);
				break;
		}

		return true;
	}

	@Override
	public void onDestroy() {
		
		if (application.billingHelper != null){
		   application.billingHelper.dispose();
		   application.billingHelper = null;
		}

		if(application.mGoogleApiClient.isConnected()){
			application.mGoogleApiClient.disconnect();
		}

		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

		int i = v.getId();

		if (i == R.id.random_game) {
			int id = application.getRandomGame();
			Intent gameUI = new Intent(Main.this, GameUI.class);
			gameUI.putExtra("GameId", id);
			Main.this.startActivityForResult(gameUI,410);

		} else if (i == R.id.example) {
			Intent help = new Intent(Main.this, Example.class);
			Main.this.startActivity(help);

		} else if (v.getId() == R.id.sign_out_button) {
			// user explicitly signed out, so turn off auto sign in
			mExplicitSignOut = true;
			if (application.mGoogleApiClient != null && application.mGoogleApiClient.isConnected()) {
				Games.signOut(application.mGoogleApiClient);
				application.mGoogleApiClient.disconnect();
				finish();
			}

		} else if(i == R.id.achievements){

			startActivityForResult(Games.Achievements.getAchievementsIntent(application.mGoogleApiClient),
					REQUEST_ACHIEVEMENTS);

		} else if(i == R.id.leaderboard){

			startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(application.mGoogleApiClient),
					REQUEST_LEADERBOARD);

		} else if (v.getId() == R.id.app_purchase){
			// Show App Purchase Screen
			Intent intent = new Intent(Main.this, ActivatePremiumFeatures.class);
			Main.this.startActivity(intent);
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 410){

			updateButton();

		}

	}

	private void updateButton() {

		Button game_button = ((Button) findViewById(R.id.random_game));

		if(application.checkPausedGameAvailable()){
			game_button.setText("Continue Last game");
			game_button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.pause,0,0,0);
		}
		else{
			game_button.setText("New game");
			game_button.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add,0,0,0);
		}
	}

}