package org.varunverma.spellathon.commonui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

public class CommonDisplayInfo extends Activity {
	
	protected static final String TAG = "DisplayInfo";
	protected String html_text;
	protected WebView my_web_view;
	
	protected void show_from_direct_source() {
		// Show from direct source
		my_web_view.loadData(html_text, "text/html", "utf-8");
	}

	protected void show_from_raw_source(String fileName) {
		// Show from RAW Source
        get_html_from_file(fileName);
        my_web_view.loadData(html_text, "text/html", "utf-8");
	}

	protected void get_html_from_file(String fileName) {
		// Get HTML File from RAW Source
		Resources res = getResources();
		html_text = "";
		if(fileName == null){
			return;
		}
        try {
        	InputStream is = res.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            
            String line = "";
			while((line = reader.readLine()) != null){
				html_text = html_text + line;
			}
		} catch (IOException e) {
			// Oops !
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e(TAG, e.getMessage(), e);
		}
	}	

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance().activityStop(this);
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