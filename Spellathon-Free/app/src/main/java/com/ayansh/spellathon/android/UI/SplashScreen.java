package com.ayansh.spellathon.android.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ayansh.CommandExecuter.Command;
import com.ayansh.CommandExecuter.CommandExecuter;
import com.ayansh.CommandExecuter.Invoker;
import com.ayansh.CommandExecuter.MultiCommand;
import com.ayansh.CommandExecuter.ProgressInfo;
import com.ayansh.CommandExecuter.ResultObject;
import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.Application.Game.AchievementsResultCallBack;
import com.ayansh.spellathon.android.Application.SavePlayerDetailsCommand;
import com.ayansh.spellathon.android.Application.SaveRegIdCommand;
import com.ayansh.spellathon.android.R;
import com.ayansh.spellathon.android.billingutil.IabHelper;
import com.ayansh.spellathon.android.billingutil.IabResult;
import com.ayansh.spellathon.android.billingutil.Inventory;
import com.ayansh.spellathon.android.billingutil.Purchase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

public class SplashScreen extends Activity implements Invoker,
		IabHelper.OnIabSetupFinishedListener, IabHelper.QueryInventoryFinishedListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

	private Application app;
	private ProgressDialog pd;
	private TextView statusView;
	private boolean appStarted = false;
	private boolean firstUse = false;
	private boolean showNewFeatures = false;

	boolean mExplicitSignOut = false;
	boolean mInSignInFlow = false; // set to true when you're in the middle of the
	// sign in flow, to know you should not attempt
	// to connect in onStart()
	private static int RC_SIGN_IN = 9001;

	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInflow = true;
	private boolean mSignInClicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		statusView = (TextView) findViewById(R.id.status);

        // Get Application Instance.
		// Initialize Application
		app = Application.get_instance();

        // Set the context of the application
        app.set_context(getApplicationContext());

		findViewById(R.id.sign_in_button).setOnClickListener(this);

		// Accept my Terms
		// Check EULA.
		if (app.isEULAAccepted()) {

			setupApp();

		} else {
			// Show EULA.
			Intent eula = new Intent(SplashScreen.this, Eula.class);
			SplashScreen.this.startActivityForResult(eula, 1);
		}
	}

	private void setupApp() {

		// Create the Google Api Client with access to the Play Games services
		app.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Games.API).addScope(Games.SCOPE_GAMES)
				// add other APIs and scopes here as needed
				.build();

		// Create billing helper
		if (app.billingHelper == null) {

			// Billing Helper
			app.billingHelper = new IabHelper(this, Application.constants.getPublicKey());

			// Start the set up
			app.billingHelper.startSetup(this);

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {

    		return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

	private void startMainActivity() {

		// Load Achievements
		Games.Achievements.load(app.mGoogleApiClient,false)
				.setResultCallback(new AchievementsResultCallBack());

		// Register application.
		CommandExecuter ce = new CommandExecuter();
		MultiCommand command = new MultiCommand(Command.DUMMY_CALLER);

		String regStatus = app.readStringPreference("RegistrationStatus");
		String regId = app.readStringPreference("RegistrationId");

		if(regId != null && !regId.contentEquals("")) {

			if(regStatus == null || regStatus.contentEquals("")) {

				SaveRegIdCommand command1 = new SaveRegIdCommand(Command.DUMMY_CALLER, regId);
				command.addCommand(command1);
			}
		}

		String player_details_status = app.readStringPreference("PlayerDetailsStatus");
		if(player_details_status.contentEquals("")){

			SavePlayerDetailsCommand command2 = new SavePlayerDetailsCommand(Command.DUMMY_CALLER);
			command.addCommand(command2);

		}

		ce.execute(command);

		if(app.isThisFirstUse()){

			firstUse = true;

			statusView.setText("Initializing app for first use.\nPlease wait, this may take a while");
			app.initializeAppForFirstUse(this);

		}
		else{

			statusView.setText("Initializing application...");
			app.initializeAppForNormalUse();

			int curr_version = app.getCurrentAppVersionCode();
			int old_version = app.getOldVersion();

			if (curr_version > old_version) {
				app.setNewVersion(curr_version);
				showNewFeatures = true;
			}

			startApp();
		}
	}

	@Override
	protected void onStart() {

		super.onStart();

		if (!mInSignInFlow && !mExplicitSignOut && app.isEULAAccepted()) {
			// auto sign in
			app.mGoogleApiClient.connect();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (!app.isEULAAccepted()) {
				finish();
			} else {
				// Setup the app
				setupApp();
			}

		} else if (requestCode == 900) {
			firstUse = false;
			startApp();

		} else if (requestCode == 901) {
			showNewFeatures = false;
			startApp();

		} else if (requestCode == RC_SIGN_IN) {
			mSignInClicked = false;
			mResolvingConnectionFailure = false;
			if (resultCode == RESULT_OK) {
				app.mGoogleApiClient.connect();
			} else {
				// Bring up an error dialog to alert the user that sign-in
				// failed. The R.string.signin_failure should reference an error
				// string in your strings.xml file that tells the user they
				// could not be signed in, such as "Unable to sign in."
				BaseGameUtils.showActivityResultError(this,
						requestCode, resultCode, R.string.sign_in_failed);
			}
		}
	}

	@Override
	public void NotifyCommandExecuted(ResultObject result) {

		Log.i("Spellathon", "Command Execution completed");

		if (!result.isCommandExecutionSuccess()) {

			// Application is not registered.
			String message = "Error occurred while initializing, Please start app again.";

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			alertDialogBuilder
				.setTitle("Error during Initialization !")
				.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SplashScreen.this.finish();
					}
				}).
				create().
				show();

		}
		else{
			// Start the app
			app.savePreference("FirstUse","No");
			startApp();
		}

	}

	@Override
	public void ProgressUpdate(ProgressInfo progress) {

		String message = progress.getProgressMessage();
		if(message != null && !message.contentEquals("")){

			if (message.contentEquals("Show UI")) {
				startApp();
			}

		}
	}

	private void showWhatsNew() {

		Intent newFeatures = new Intent(SplashScreen.this, DisplayInfo.class);
		newFeatures.putExtra("Type", 1);
		newFeatures.putExtra("File", "new_features.html");
		newFeatures.putExtra("Title", "New Features: ");
		SplashScreen.this.startActivityForResult(newFeatures, 901);

	}

	private void startApp() {

		if(appStarted){
			return;
		}

		if (showNewFeatures) {
			showWhatsNew();
			return;
		}

		appStarted = true;

		app.loadGameData();

		// Start the Quiz List
		Log.i("Spellathon", "Start the main screen");
		Intent start = new Intent(SplashScreen.this, Main.class);
		SplashScreen.this.startActivity(start);

		// Kill this activity.
		Log.i("Spellathon", "Kill splash Activity");
		SplashScreen.this.finish();
	}

	@Override
	public void onIabSetupFinished(IabResult result) {

		if (!result.isSuccess()) {
			app.billingHelper = null;
			Log.w("Spellathon", result.getMessage());
		} else {
			// Check if the user has purchased premium service
			app.billingHelper.queryInventoryAsync(this);
		}
	}

	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inv) {

		if (result.isFailure()) {
			Log.w("Billing", result.getMessage());
		} else {
			Purchase item = inv.getPurchase(Application.constants.getProductKey("ShowMeanings"));
			if(item != null){

				boolean has_purchase = inv.hasPurchase(Application.constants.getProductKey("ShowMeanings"));
				Application.constants.setPremiumVersion(has_purchase);
				// Use this ONLY for FCM Messaging Scenario -
				// because we don't want to initialize the IAB there.
				app.savePreference("IsPremiumUser",has_purchase);
			}
		}
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {

		startMainActivity();
	}

	@Override
	public void onConnectionSuspended(int i) {
		// Attempt to reconnect
		app.mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

		if (mResolvingConnectionFailure) {
			// already resolving
			return;
		}

		// if the sign-in button was clicked or if auto sign-in is enabled,
		// launch the sign-in flow
		if (mSignInClicked || mAutoStartSignInflow) {
			mAutoStartSignInflow = false;
			mSignInClicked = false;
			mResolvingConnectionFailure = true;

			// Attempt to resolve the connection failure using BaseGameUtils.
			// The R.string.signin_other_error value should reference a generic
			// error string in your strings.xml file, such as "There was
			// an issue with sign-in, please try again later."
			if (!BaseGameUtils.resolveConnectionFailure(this,
					app.mGoogleApiClient, connectionResult,
					RC_SIGN_IN, "Sing In Failed !")) {
				mResolvingConnectionFailure = false;
			}
		}

		// Put code here to display the sign-in button
		findViewById(R.id.sign_in_bar).setVisibility(View.VISIBLE);
	}

	// Call when the sign-in button is clicked
	private void signInClicked() {
		mSignInClicked = true;
		app.mGoogleApiClient.connect();
	}

	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.sign_in_button){
			signInClicked();
		}

	}
}