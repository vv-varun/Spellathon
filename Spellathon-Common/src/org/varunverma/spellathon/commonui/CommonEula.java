package org.varunverma.spellathon.commonui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.varunverma.spellathon.Application.Application;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public abstract class CommonEula extends Activity implements OnClickListener {
	
	protected Application application;
	protected String EULA_Text;
	protected TextView EULA;
	protected Button accept, decline;
	
	protected void readEULA(Resources res) {
        
        try {
        	InputStream is = res.getAssets().open("eula.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            EULA_Text = "";
            String line = "";
			while((line = reader.readLine()) != null){
				EULA_Text = EULA_Text + line;
			}
		} catch (IOException e) {
			// OOps... So let's pretend that user did not accept the EULA.
			EULADeclined();
		}
	}
	
	protected void EULAAccepted(){
		
		application.setEULAResult(true);
		setResult(RESULT_OK);       
		this.finish();
	}
	
	protected void EULADeclined(){
		
		application.setEULAResult(false);
		setResult(RESULT_OK);       
		this.finish();
	}

	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
	    // Ignore orientation change to keep activity from restarting
	    super.onConfigurationChanged(newConfig);
	}
}