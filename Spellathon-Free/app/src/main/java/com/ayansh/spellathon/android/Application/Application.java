package com.ayansh.spellathon.android.Application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ayansh.CommandExecuter.CommandExecuter;
import com.ayansh.CommandExecuter.Invoker;
import com.ayansh.CommandExecuter.MultiCommand;
import com.ayansh.spellathon.android.Application.Game.GameDownloadCommand;
import com.ayansh.spellathon.android.Application.Game.GameForPlay;
import com.ayansh.spellathon.android.Application.Game.LoadGamesFromFileCommand;
import com.ayansh.spellathon.android.SpellathonConstants;
import com.ayansh.spellathon.android.billingutil.IabHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Application {
	
	// Application Constants
	public static Constants constants;
	
	private static Application application;
	
	private Context context;
	private ApplicationDB data_base;
	public GameForPlay game;
	public IabHelper billingHelper;
	//private CommandExecuter ce;
	private FirebaseAnalytics mFirebaseAnalytics;
	public static final String TAG = "Spellathon";

	// Keep it as map so that it is easy to remove.
	public HashMap<Integer,Integer> new_game_list, paused_game_list;

	public HashMap<String,Integer> achievement_status;

	public GoogleApiClient mGoogleApiClient;
	
	public static Application get_instance() {
		// Create a singleton instance.
		if (application == null) {
			application = new Application();
		}
		return application;
	}

	private Application(){

		if(constants == null){
			constants = new SpellathonConstants();
		}

	}

	private void initialize() {

		achievement_status = new HashMap<String,Integer>();

		// Initialize DataBase.
		data_base = ApplicationDB.get_instance(context);
		data_base.open_db_for_writing();

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

		new_game_list = new HashMap<Integer,Integer>();
		paused_game_list = new HashMap<Integer,Integer>();

	}

	public void stop(){
		// Stop Application.
		if(data_base != null){
			data_base.close();
		}
		
	}

	public void set_context(Context c) {

		if(constants == null){
			constants = new SpellathonConstants();
		}

		if(context == null){

			context = c;
			initialize();
		}

	}

	public Context get_context(){
		return context;
	}

	public ApplicationDB getDataBase() {
		return data_base;
	}

	public void setEULAResult(boolean accepted) {
		// Set EULA Result
		savePreference("EULA",String.valueOf(accepted));
	}

	public FirebaseAnalytics getFirebaseAnalytics() {
		return this.mFirebaseAnalytics;
	}

	public boolean isEULAAccepted() {

		String eula = readStringPreference("EULA");
		if(eula.contentEquals("")){
			return false;
		}
		else{
			return Boolean.valueOf(eula);
		}
	}

	public void setNewVersion(int newVersion) {
		// Set New Version.
		savePreference("OldVersion",newVersion);
	}

	public int getOldVersion(){
		// Return Old Version
		return readIntegerPreference("OldVersion");
	}

	public int getCurrentAppVersionCode(){

		int version;
		try {
			version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			version = 0;
			Log.e(TAG, e.getMessage(), e);
		}
		return version;
	}

	public String readStringPreference(String paramname){

		SharedPreferences preferences = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		return preferences.getString(paramname,"");

	}

	public boolean readBooleanPreference(String paramname){

		SharedPreferences preferences = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		return preferences.getBoolean(paramname,false);

	}

	public int readIntegerPreference(String paramname){

		SharedPreferences preferences = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		return preferences.getInt(paramname,0);

	}

	public void savePreference(String paramname, boolean paramvalue){

		SharedPreferences settings = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putBoolean(paramname, paramvalue);
		editor.commit();

	}

	public void savePreference(String paramname, String paramvalue) {

		SharedPreferences settings = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putString(paramname, paramvalue);
		editor.commit();
	}

	public void savePreference(String paramname, int paramvalue) {

		SharedPreferences settings = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putInt(paramname, paramvalue);
		editor.commit();
	}

	public void initializeAppForFirstUse(Invoker caller) {

		Log.v(TAG, "Initializing Application for first use");

		CommandExecuter ce = new CommandExecuter();
		MultiCommand command = new MultiCommand(caller);

		LoadGamesFromFileCommand loadFromFile = new LoadGamesFromFileCommand(caller);
		command.addCommand(loadFromFile);

		ce.execute(command);
	}

	public boolean isThisFirstUse() {

		String first_use = readStringPreference("FirstUse");
		if(first_use.contentEquals("No")){
			return false;
		}
		else{
			return true;
		}
	}

	public void initializeAppForNormalUse() {

		// If premium Trial is ON - Deactivate it.
		String trail_status = Application.get_instance().readStringPreference("TrialStatus");
		if(trail_status.contentEquals("TrialON")){

			String tx = readStringPreference("TrialExpiry");
			long trial_expiry = Long.parseLong(tx);
			long now = (new Date()).getTime();

			if(now >= trial_expiry){

				savePreference("TrialStatus","TrialCompleted");
			}

		}

	}

	public int getRandomGame(){

		ArrayList<Integer> game_list;

		if(paused_game_list.isEmpty()){
			game_list = new ArrayList<>(new_game_list.keySet());
		}
		else{
			game_list = new ArrayList<>(paused_game_list.keySet());
		}

		int index = (new Random()).nextInt(game_list.size());
		return game_list.get(index);

	}

	public void loadGameData() {

		// First get Paused Games
		paused_game_list = data_base.query_game_table(2);
		new_game_list = data_base.query_game_table(1);
	}

	public boolean checkPausedGameAvailable(){

		if(paused_game_list.isEmpty()){
			return false;
		}
		else{
			return true;
		}

	}

	public void updateHealthStatus() {

		/*
		1. Total Games
		2. Games Completed
		3. Games Paused
		 */
		HashMap<String,Integer> healthStatus = data_base.getHealthStatus();
		CommandExecuter ce = new CommandExecuter();

		UpdateHealthStatusCommand command = new UpdateHealthStatusCommand(healthStatus);

		ce.execute(command);


	}

	public void downloadNewGames() {

		/*
		1. Get the latest Game and download after that.
		 */
		int latest_game_id = data_base.getLatestGameID();

		CommandExecuter ce = new CommandExecuter();

		GameDownloadCommand command = new GameDownloadCommand(latest_game_id);

		ce.execute(command);

	}
}