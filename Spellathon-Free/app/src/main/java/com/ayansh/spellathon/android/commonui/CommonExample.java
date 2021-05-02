package com.ayansh.spellathon.android.commonui;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class CommonExample extends AppCompatActivity {
	
	protected TextView TV;
	protected String text;
	

	protected void initializeText() {
		// Initialize Text.
		text = "<HTML><BODY>";
		
		text = text + "<h3>How to play:</h3>" +
				"Goal is to make words of 4 or more alphabets. Each word must contain the centre alphabet." +
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
				"<FONT color = red>INIT</FONT> - This is not a word initself!" +
				"<br><br>";
		
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
}