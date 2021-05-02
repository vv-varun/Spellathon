package org.varunverma.spellathon.ui;

/*
 * by Varun Verma
 */

import org.varunverma.CommandExecuter.CommandExecuter;
import org.varunverma.CommandExecuter.Invoker;
import org.varunverma.CommandExecuter.ProgressInfo;
import org.varunverma.CommandExecuter.ResultObject;
import org.varunverma.spellathon.R;
import org.varunverma.spellathon.SpellathonConstants;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameDownloadCommand;
import org.varunverma.spellathon.commonui.UIFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;

public class Main extends FragmentActivity implements
		GameGridFragment.Callbacks, GameUIFragment.Callbacks, Invoker {

	private boolean multiPane;
	private Application application;
	private LicenseCheckerCallback licenseCheckerCallback;
	private LicenseChecker licenseChecker;
	private ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("Spellathon");

		// Tracking.
        EasyTracker.getInstance().activityStart(this);
        
		// Initialize Application
        Application.constants = new SpellathonConstants();
		application = Application.get_instance();
		application.set_context(getApplicationContext());

		// First Check License.
		checkLicense();

	}

	private void checkLicense() {
		// Check license
		String deviceId = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);

		// Library calls this when it's done.
		licenseCheckerCallback = new myLicenseCheckerCallback();

		// Construct the LicenseChecker with a policy.
		licenseChecker = new LicenseChecker(this,
				new ServerManagedPolicy(this, new AESObfuscator(
						Application.constants.getSalt(), getPackageName(), deviceId)),
				Application.constants.getPublicKey());

		// Check Access.
		licenseChecker.checkAccess(licenseCheckerCallback);

	}

	private void startMainActivity() {

		if (application.ScreenSize < 1) {
			// Determine Screen Size & Save it.
			WindowManager w = getWindowManager();
			DisplayMetrics dm = new DisplayMetrics();
			w.getDefaultDisplay().getMetrics(dm);
			application.setScreenSize(dm.densityDpi);
		}
		
		// Download Games in a new thread.
		if (application.lastGameDownloaded < 1) {
			
			pd = ProgressDialog.show(this, "", "Downloading new Puzzles. Please wait...", true);
			
			int from = application.lastGameDownloaded + 1;
			int to;
			if(from < 30){
				to = from + 20;	// 10 games for the first time.
			}
			else{
				to = from + 10;	// 5 games maximum.
			}

			CommandExecuter commander = new CommandExecuter();
			GameDownloadCommand command = new GameDownloadCommand(this, from, to);
			commander.execute(command);
		}
		else{
			this.runOnUiThread(new Runnable() {
			    public void run(){
			    	showMainScreen();
			    }
			});
		}

	}
	
	private void showMainScreen(){

		// Show Example.
		showExample();

		// Show what's new in this version
		show_whats_new();

		if (findViewById(R.id.game_list) != null) {
			// Multi Pane
			multiPane = true;
		} else {
			multiPane = false;
		}

		/*
		 * Start Fragments
		 */
		application.initializeGameView();
		
		// Create the Fragment.
		FragmentManager fm = this.getSupportFragmentManager();
		Fragment fragment;

		if (multiPane) {
			// Show the Game List
			fragment = new GameListFragment();
			fm.beginTransaction().replace(R.id.game_list, fragment)
					.commitAllowingStateLoss();

		} else {
			// Show the Game Grid.
			fragment = new GameGridFragment();
			fm.beginTransaction().replace(R.id.main_area, fragment)
					.commitAllowingStateLoss();
		}
	}

	private void show_whats_new() {
		// Show What's new !
		int curr_version;
		try {
			curr_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			curr_version = 1;
		}
		int old_version = application.oldVersion;

		if (curr_version > old_version) {
			application.setNewVersion(curr_version);
			Intent info = new Intent(Main.this, DisplayInfo.class);
			info.putExtra("Type", 1);
			info.putExtra("File", "new_features.html");
			info.putExtra("Title", "What's New?");
			Main.this.startActivity(info);
		}
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
		
		case R.id.Settings:
			Intent settings = new Intent(Main.this, Settings.class);
			Main.this.startActivity(settings);
			return true;
		
		case R.id.help:
			Intent help = new Intent(Main.this, DisplayInfo.class);
			help.putExtra("Type", 1);
			help.putExtra("Title", "Help:");
			help.putExtra("File", "help.html");
			Main.this.startActivity(help);
			return true;
			
		case R.id.About:
			Intent about = new Intent(Main.this, DisplayInfo.class);
			about.putExtra("Type", 1);
			about.putExtra("Title", "About:");
			about.putExtra("File", "about.html");
			Main.this.startActivity(about);
			return true;
			
		case R.id.Download:
			Intent game_download = new Intent(Main.this, DownloadGame.class);
			Main.this.startActivity(game_download);
			break;
			
		}
		
		return false;
	}

	private void showExample() {
		// Show example.
		if (application.showExampleonStart) {
			// Show Instructions with Example.
			Intent i = new Intent(Main.this, Example.class);
			Main.this.startActivity(i);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Handle the result of an Activity.
		if(requestCode == Application.constants.getActivityCodes("EULA")){
			if(application.isEULAAccepted()){
				// If EULA was accepted, Start Main Activity.
				startMainActivity();
			}
			else{
				// EULA was rejected. So Finish Activity.
				finish();
			}
			return;
		}
		
		if(requestCode == Application.constants.getActivityCodes("GAME_UI")){
			FragmentManager fm = getSupportFragmentManager();
			UIFragment gameListFragment = (UIFragment) fm.findFragmentById(R.id.main_area);
			gameListFragment.reloadUI();
			return;
		}

	}

	@Override
	public void onItemSelected(int id) {
		// Click on a Game
		if (multiPane) {
			// Load Game UI Fragment
			Bundle arguments = new Bundle();
			arguments.putInt("GameId", id);
			GameUIFragment uiFragment = new GameUIFragment();
			uiFragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.game_ui, uiFragment).commit();

			// Load the Game Solution Fragment also.
			GameSolutionFragment solFragment = new GameSolutionFragment();
			solFragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.game_solution, solFragment).commit();

		} else {

			// Start Game UI Activity
			Intent gameUI = new Intent(Main.this, GameUI.class);
			gameUI.putExtra("GameId", id);
			Main.this.startActivityForResult(gameUI,Application.constants.getActivityCodes("GAME_UI"));
		}
	}

	// Private Class to check License Check Response
	private class myLicenseCheckerCallback implements LicenseCheckerCallback {

		public void allow(int reason) {
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
			}
			// Should allow user access.
			// Initialize at start of App.
			application.initialize();

			// Check EULA.
			if (application.isEULAAccepted()) {
				// Start Main Activity.
				startMainActivity();
			} else {
				// Show EULA.
				Intent eula = new Intent(Main.this, Eula.class);
				Main.this.startActivityForResult(eula, Application.constants.getActivityCodes("EULA"));
			}

		}

		public void dontAllow(int reason) {
			
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
			}
			// Should not allow access. Take the user to Market.
			String url = "http://market.android.com/details?id="
					+ getPackageName();
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(marketIntent);
			finish();
		}

		public void applicationError(int errorCode) {
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
			}
			// This is a polite way of saying the developer made a mistake
			// while setting up or calling the license checker library.
			// Please examine the error code and fix the error.
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	public void NotifyCommandExecuted(ResultObject result) {
		// Show main screen.
		pd.dismiss();
		
		String message;
		if(result.getResultCode() < 0){
			message = result.getErrorMessage() + "\n Please try downloading again.";
		}
		else{
			message = "New Puzzles downloaded successfully.\nHappy Gaming.";
		}
		
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		
		showMainScreen();
		
	}

	@Override
	public void ProgressUpdate(ProgressInfo progress) {
		// Nothing to do
	}

	@Override
	public void showSolution() {
		// The Game UI Fragment requested to refresh the solution fragment.
		FragmentManager fm = getSupportFragmentManager();
		
		UIFragment gameListFragment;
		if (multiPane) {
			// Show the Game List
			gameListFragment = (UIFragment) fm.findFragmentById(R.id.game_list);

		} else {
			// Show the Game Grid.
			gameListFragment = (UIFragment) fm.findFragmentById(R.id.main_area);
		}
		gameListFragment.reloadUI();
		
		GameSolutionFragment fragment = (GameSolutionFragment) fm.findFragmentById(R.id.game_solution);
		fragment.showSolution();
	}

	@Override
	public void addWord(String word) {
		FragmentManager fm = getSupportFragmentManager();
		GameSolutionFragment fragment = (GameSolutionFragment) fm.findFragmentById(R.id.game_solution);
		
		if(fragment != null){
			fragment.addWord(word);
		}
		
	}
}