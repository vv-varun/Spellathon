package org.varunverma.spellathon.ui;

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.commonui.CommonSettings;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Settings extends CommonSettings {
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		setTitle("Game Settings");
		
		application = Application.get_instance();
		
		purge = (Button) findViewById(R.id.purge);
		purge.setOnClickListener(this);
		
		ExampleOnStart = (CheckBox) findViewById(R.id.example);
		ExampleOnStart.setTextColor(getResources().getColor(R.color.black));
		
		// Enable app icon as back button
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
        	ActionBar actionBar = getActionBar();
        	actionBar.setDisplayHomeAsUpEnabled(true);
        }
		
		loadDefaults();
		
	}
	
	public void onClick(View view) {
		
		switch(view.getId()){
		
		case R.id.purge:
			confirmAction();
			break;
		
		}
		
	}
	
	private void confirmAction() {
		// 
		String question = "";
		question = "Are you sure you want to delete completed puzzles? " +
					"You will not be able to download and play them again!";
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(question)
		       .setCancelable(false)
		       .setIcon(R.drawable.alert)
		       .setPositiveButton("Yes", new SettingsDialogListener(this))
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		
		AlertDialog alert = builder.create();
		alert.setIcon(R.drawable.alert);
		alert.show();
		
	}

}

class SettingsDialogListener implements DialogInterface.OnClickListener {
	
	protected CommonSettings caller;
	
	public SettingsDialogListener(CommonSettings caller){
		this.caller = caller;
	}
	
	public void onClick(DialogInterface d, int button) {
		
		if(button == DialogInterface.BUTTON_POSITIVE){
			// Delete completed Games.
			caller.PurgeCompletedGames();
		}
	}
}