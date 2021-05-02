package com.ayansh.spellathon.android.commonui;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.Application.Game.GameForPlay;

public abstract class CommonSettings extends AppCompatActivity implements OnClickListener {
	
	protected static final String TAG = "Settings";
	protected Application application;
	
	protected Button purge;
	protected CheckBox ExampleOnStart;
	
	protected void saveSettings(){
		// Save Settings.
		SharedPreferences settings = getSharedPreferences(Application.constants.getPreferencesFile(), 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putBoolean("ShowExampleOnStart", ExampleOnStart.isChecked());
		editor.commit();
		
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