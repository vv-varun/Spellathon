package org.varunverma.spellathon.commonui;

import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameForPlay;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public abstract class CommonSettings extends Activity implements OnClickListener {
	
	protected static final String TAG = "Settings";
	protected Application application;
	
	protected Button purge;
	protected CheckBox ExampleOnStart;
		
	protected void loadDefaults(){
		// Show the default Value
		ExampleOnStart.setChecked(application.showExampleonStart);
	}
	
	protected void saveSettings(){
		// Save Settings.
		SharedPreferences settings = getSharedPreferences(Application.constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putBoolean("ShowExampleOnStart", ExampleOnStart.isChecked());
		editor.commit();
		
	}
	

	public void PurgeCompletedGames() {
		// Delete completed games !
		int gameId;
		GameForPlay game;
		
		// Fetch completed games.
		Cursor cursor = application.getDataBase().query_game_table(3);
		
		try{
			
			if(cursor.moveToFirst()){
				// Delete Game.
				gameId = cursor.getInt(cursor.getColumnIndex("GameId"));
				game = new GameForPlay(gameId);
				game.deleteGame();
				
				while(cursor.moveToNext()){
					// Delete Game.
					gameId = cursor.getInt(cursor.getColumnIndex("GameId"));
					game = new GameForPlay(gameId);
					game.deleteGame();
				}
				
				Toast.makeText(this, "Completed games purged!", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(this, "Nothing to be purged.", Toast.LENGTH_LONG).show();
			}
			
		}catch (Exception e){
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e(TAG, e.getMessage(), e);
		}
		
	}

	@Override
	protected void onDestroy(){
		// Dispatch and Stop.
		saveSettings();
		super.onDestroy();
		
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
	public void onConfigurationChanged(final Configuration newConfig) {
		// Do Nothing.
		super.onConfigurationChanged(newConfig);
	}

}