package com.ayansh.spellathon.android.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.achievement.Achievement;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Varun Verma on 1/24/2017.
 */
public class TryPremiumFeatures extends AppCompatActivity implements View.OnClickListener {

    private Application app;
    private SimpleDateFormat sdf;
    private TextView header_text, desc_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.try_premium);
        setTitle("Try Premium Features");

        app = Application.get_instance();
        app.set_context(getApplicationContext());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get Views
        header_text = (TextView) findViewById(R.id.heading);
        desc_text = (TextView) findViewById(R.id.description);

        sdf = new SimpleDateFormat();

        // Show Ad.
        if(Application.constants.isPremiumVersion()){

            // User is premium user. Change text and return.
            header_text.setText("You are a premium user !");
            desc_text.setText("You have already activated premium features. \n No need for trial.");
            return;
        }

        setViewData();

    }

    private void setViewData() {

        String trail_status = app.readStringPreference("TrialStatus");

        if(trail_status.contentEquals("TrialON")){
            // Trial is ON.
            String tx = app.readStringPreference("TrialExpiry");
            long trial_expiry = Long.parseLong(tx);
            Date date = new Date();
            date.setTime(trial_expiry);
            String date_text = sdf.format(date);
            // Show when the trial will expire.
            header_text.setText("Your Premium Features trial is ON !");
            desc_text.setText("Trial will expire on: " + date_text);

            findViewById(R.id.try_premium).setVisibility(View.GONE);

        }
        else if (trail_status.contentEquals("TrialCompleted")){

            // Trial has Expired.
            String tx = app.readStringPreference("TrialExpiry");
            long trial_expiry = Long.parseLong(tx);
            Date date = new Date();
            date.setTime(trial_expiry);
            String date_text = sdf.format(date);
            // Show when the trial will expire.
            header_text.setText("Your Premium Features trial has Expired !");
            desc_text.setText("Your trial expired on: " + date_text);

            findViewById(R.id.try_premium).setVisibility(View.GONE);
        }
        else{

            findViewById(R.id.try_premium).setOnClickListener(this);
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
    public void onClick(View v) {

        if(v.getId() == R.id.try_premium){

            validate();

        }

    }

    private void validate() {

        /*
        1. Check if achievement is unlocked.
        2. Check if he has already tried before.
        3. Should not be a premium user
         */

        boolean achievement_unlocked = false;
        Iterator<Integer> i = app.achievement_status.values().iterator();
        while(i.hasNext()){
            if(i.next() == Achievement.STATE_UNLOCKED){
                achievement_unlocked = true;
                break;
            }
        }

        if(!achievement_unlocked){
            header_text.setText("You are not eligible for trying Premium Features");
            desc_text.setText("You must earn / unlock at least one achievement to qualify for trial.");
            return;
        }

        // If all Success.
        Date date = new Date();
        long expiry = date.getTime() + 1*24*60*60*1000;

        // Activate Trial. Set Expiry.
        app.savePreference("TrialStatus","TrialON");
        app.savePreference("TrialExpiry",String.valueOf(expiry));

        Toast.makeText(this,"Premium features activated on trial basis.",Toast.LENGTH_LONG).show();

        // Update View.
        setViewData();

    }
}