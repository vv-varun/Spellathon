package org.varunverma.spellathon.Application;

import java.util.GregorianCalendar;
import java.util.TreeMap;

import org.varunverma.CommandExecuter.Command;
import org.varunverma.spellathon.Application.Game.GameForPlay;
import org.varunverma.spellathon.Application.Game.GameViewAttributes;
import org.varunverma.spellathon.billingutil.IabHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.DisplayMetrics;

public class Application {
	
	// Application Constants
	public static Constants constants;
	
	private static Application application;
	
	Context context;
	ApplicationDB data_base;
	public TreeMap<Integer,GameViewAttributes> map;
	public int ScreenSize;
	public GameForPlay game;
	boolean EULAAccepted; 
	public int oldVersion;
	public int lastGameDownloaded;
	public long lastDateDownloaded;
	public boolean showExampleonStart;
	public boolean downloading;
	public IabHelper billingHelper;
	public Command command;
	
	public static Application get_instance() {
		// Create a singleton instance.
		if (application == null) {
			application = new Application();
		}
		return application;
	}

	public void initialize() {
		
		// Initialize DataBase.
		data_base = ApplicationDB.get_instance(context);
		data_base.open_db_for_writing();
		
		// Initialize Preferneces.
		initializePreferences();
		
	}
	
	private void initializePreferences() {
		// Initialize Preferences.
		SharedPreferences preferences = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		ScreenSize = preferences.getInt("ScreenSize", 0);
		
		// Version.
		oldVersion = preferences.getInt("OldVersion", 1);
		
		// EULA.
		EULAAccepted = preferences.getBoolean("EULA",false);
		
		// Last Game Downloaded.
		lastGameDownloaded = preferences.getInt("lastGameDownloaded", 0);
		
		// Last Date Downloaded.
		lastDateDownloaded = preferences.getLong("lastDownloadDate", 0);
		
		// Show example on Game Start.
		showExampleonStart = preferences.getBoolean("ShowExampleOnStart", true);
	}	

	public void stop(){
		// Stop Application.
		if(data_base != null){
			data_base.close();
		}
		
	}

	public void set_context(Context context) {
		this.context = context;
	}

	public void setScreenSize(int densityDpi) {
		// set Screen Size.
		SharedPreferences settings = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();
		
		switch (densityDpi){
		
		case DisplayMetrics.DENSITY_LOW :
			ScreenSize = 1;
			break;
		
		case DisplayMetrics.DENSITY_MEDIUM:
			ScreenSize = 2;
			break;
			
		case DisplayMetrics.DENSITY_HIGH:
			ScreenSize = 3;
			break;
		
		case DisplayMetrics.DENSITY_XHIGH:
			ScreenSize = 3;
			break;
			
		default:
			ScreenSize = 2;
			break;
		}
		
		editor.putInt("ScreenSize", ScreenSize);
		editor.commit();
		
	}

	public ApplicationDB getDataBase() {
		return data_base;
	}

	public void setEULAResult(boolean accepted) {
		// Set EULA Result
		SharedPreferences settings = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putBoolean("EULA", accepted);		
		editor.commit();
		EULAAccepted = accepted;
		
	}

	public boolean isEULAAccepted() {
		return EULAAccepted;
	}

	public void setNewVersion(int newVersion) {
		// Set New Version.
		SharedPreferences settings = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putInt("OldVersion", newVersion);		
		editor.commit();
		
	}
	
	public void updateLastGameDownloaded(int id) {
		// Update the last Game Downloaded.
		SharedPreferences settings = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();
		
		if(id > lastGameDownloaded){
			editor.putInt("lastGameDownloaded", id);		
			editor.commit();
			lastGameDownloaded = id;
		}
	}

	public void setLastDownloadDate() {
		// set Last Download Date.
		SharedPreferences settings = context.getSharedPreferences(constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();
		
		long timestamp = (new GregorianCalendar()).getTimeInMillis();
		editor.putLong("lastDownloadDate", timestamp);
		editor.commit();
		lastDateDownloaded = timestamp;
		
	}
	
	public int initializeGameView(){
		
		int index = 1;
		Cursor cursor = data_base.query_game_table(0);
		int gameId, count;
		GameViewAttributes gv;
		
		map = new TreeMap<Integer, GameViewAttributes>();
		
		if(cursor.moveToFirst()){
			
			count = cursor.getCount();
			gameId = cursor.getInt(cursor.getColumnIndex(Application.constants.getGameIdFieldName()));
			try {
				gv = new GameViewAttributes(gameId);
				map.put(index, gv);
				index++;
			} catch (Exception e) {
				// Could not add... that should be OK.
			}
			
			while(cursor.moveToNext()){
				gameId = cursor.getInt(cursor.getColumnIndex(Application.constants.getGameIdFieldName()));
				try {
					gv = new GameViewAttributes(gameId);
					map.put(index, gv);
					index++;
				} catch (Exception e) {
					// Could not add... that should be OK.
				}
				
			}
			
		}
		else{
			count = 0;
		}
		
		cursor.close();
		return count;
	}
	
}