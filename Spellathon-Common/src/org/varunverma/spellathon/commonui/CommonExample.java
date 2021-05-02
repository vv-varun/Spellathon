package org.varunverma.spellathon.commonui;

import android.app.Activity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class CommonExample extends Activity {
	
	protected TextView TV;
	protected String text;
	

	protected void initializeText() {
		// Initialize Text.
		text = "<HTML><BODY>";
		
		text = text + "<h3>How to play:</h3>" +
				"Goal is to make words of 4 or more letters. Each word must contain the centre alphabet." +
				"Each alphabet can be used only once. There must be atleast one 7 letter word. " +
				"Plurals, foriegn words and proper names are not allowed.<br>" +
				"English dictionary is taken as a reference";
		
		text = text + "<h3>Important:</h3>" +
				"The letter <FONT color = red> I </FONT> is in the centre.<br>" +
				"Hence, this must appear in all words. Since I is repeated 3 times, you can use I " +
				"maximum of 3 times in your words.";
		
		text = text + "<h3>Valid words:</h3>" +
				"<b><FONT color = green>INITIAL</FONT></b> (this is seven letter word)<br>" +
				"<b>la<FONT color = green>I</FONT>n, " +
				"l<FONT color = green>I</FONT>nt, " +
				"na<FONT color = green>I</FONT>l, " +
				"ta<FONT color = green>I</FONT>l</b>";
		
		text = text + "<h3>In-Valid words:</h3>" +
				"<FONT color = red>ANT</FONT> - This is not a 4 letter word<br>" +
				"<FONT color = red>INIT</FONT> - This is not a word initself!";
		
		text = text + "</BODY></HTML>";
		
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