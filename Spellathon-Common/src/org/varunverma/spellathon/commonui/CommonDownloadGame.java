package org.varunverma.spellathon.commonui;

import org.varunverma.CommandExecuter.CommandExecuter;
import org.varunverma.CommandExecuter.Invoker;
import org.varunverma.CommandExecuter.ProgressInfo;
import org.varunverma.CommandExecuter.ResultObject;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameDownloadCommand;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class CommonDownloadGame extends Activity implements Invoker {
	
	protected TextView TV;
	protected ProgressDialog pd;
	protected Application application;
		
	protected void downloadPuzzles(){
		
		// Show progress Dialog.
		pd = new ProgressDialog(this);
		pd.setTitle("Downloading Puzzles...");
		pd.setMessage("Downloading new Puzzles. Please wait...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setProgress(0);
		pd.setMax(100);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		
		// Download Games.
		int from = application.lastGameDownloaded + 1;
		int to;
		if(from < 30){
			to = from + 19;	// 20 games for the first time.
		}
		else{
			to = from + 9;	// 10 games maximum.
		}

		CommandExecuter commander = new CommandExecuter();
		GameDownloadCommand command = new GameDownloadCommand(this, from, to, false);
		commander.execute(command);
	}
	
	public void NotifyCommandExecuted(ResultObject result) {
		// Command Executed.
		pd.dismiss();
		application.downloading = false;
		
		if(result.getResultCode() < 0){
			TV.append(result.getErrorMessage() + "\n");
			TV.append("Try downloading again.\n");
		}
		else{
			TV.append("New Puzzles downloaded successfully.\n");
			TV.append("Happy Gaming.\n");
		}
		
	}
	
	@Override
	protected void onDestroy(){
		// Stop the application.
		super.onDestroy();
	}
	
	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
	    // Ignore orientation change to keep activity from restarting
	    super.onConfigurationChanged(newConfig);
	}

	@Override
	public void ProgressUpdate(ProgressInfo progressInfo) {
		
		String message = progressInfo.getProgressMessage();
		
		if (message != null && !message.contentEquals("")) {
			pd.setMessage(message);
		}

		if (progressInfo.getProgressPercentage() > 0 && pd.isShowing()) {
			pd.setProgress(progressInfo.getProgressPercentage());
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
			case android.R.id.home:
				finish();
				return true;
				
			default:
	            return super.onOptionsItemSelected(item);
		}
		
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance().activityStop(this);
	}	
}