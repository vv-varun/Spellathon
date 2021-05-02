package com.ayansh.spellathon.android.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;
import com.ayansh.spellathon.android.commonui.CommonExample;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.w3c.dom.Text;

public class WordOfDay extends AppCompatActivity {

	private String word, meaning, alt_m1, alt_m2, example;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_of_day);
		setTitle("Today's Word");

		Application app = Application.get_instance();
		app.set_context(getApplicationContext());

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

		// Show Ad.
		if(!Application.constants.isPremiumVersion()){

			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice("8C44DABF2A81BBE341ECAFD882A49F09").build();

			AdView adView = (AdView) findViewById(R.id.adView);

			// Start loading the ad in the background.
			adView.loadAd(adRequest);
		}

		TextView tv_word = (TextView) findViewById(R.id.word);
		TextView tv_meaning = (TextView) findViewById(R.id.meaning);
		TextView tv_alt_m1 = (TextView) findViewById(R.id.alt_m1);
		TextView tv_alt_m2 = (TextView) findViewById(R.id.alt_m2);
		TextView tv_example = (TextView) findViewById(R.id.example);

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		word = getIntent().getStringExtra("Word");
		meaning = getIntent().getStringExtra("Meaning");
		alt_m1 = getIntent().getStringExtra("Alt_M1");
		alt_m2 = getIntent().getStringExtra("Alt_M2");
		example = getIntent().getStringExtra("Example");

		if(alt_m1 == null || alt_m1.contentEquals("")){
			tv_alt_m1.setVisibility(View.GONE);
		}
		else{
			tv_alt_m1.setText(alt_m1);
		}

		if(alt_m2 == null || alt_m2.contentEquals("")){
			tv_alt_m2.setVisibility(View.GONE);
		}
		else{
			tv_alt_m2.setText(alt_m2);
		}

		if(example == null || example.contentEquals("")){
			tv_example.setVisibility(View.GONE);
		}
		else{
			tv_example.setText(example);
		}

		tv_word.setText(word);
		tv_meaning.setText(meaning);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.word_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){

			case R.id.ShareWord:
				share_word();
				break;
		}

		return true;
	}

	private void share_word() {

		String word_content = "I learnt a new word today: " + word + "\n\n Meaning: " + meaning;

		if(alt_m1 == null || alt_m1.contentEquals("")){}
		else{
			word_content = word_content + "\n\n Alternate Meaning:" + alt_m1;
		}

		if(alt_m2 == null || alt_m2.contentEquals("")){}
		else{
			word_content = word_content + "\n\n Aleternate Meaning: " + alt_m1;
		}

		if(example == null || example.contentEquals("")){}
		else{
			word_content = word_content + "\n\n Example" + alt_m1;
		}

		word_content += "\n\n via ~ ayansh.com/spellathon";
		Intent send = new Intent(android.content.Intent.ACTION_SEND);
		send.setType("text/plain");
		send.putExtra(android.content.Intent.EXTRA_SUBJECT, word);
		send.putExtra(android.content.Intent.EXTRA_TEXT, word_content);
		startActivity(Intent.createChooser(send, "Share with..."));

		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, word);
		bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "word_share");
		Application.get_instance().getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SHARE, bundle);

	}
}