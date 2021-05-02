package com.ayansh.spellathon.android.commonui;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Varun Verma on 24 Nov 2016.
 */

public class MyInterstitialAd {

    private static InterstitialAd mInterstitialAd;
    private static String pub_id = "ca-app-pub-4571712644338430/4877830704";

    public static InterstitialAd getInterstitialAd(Context context){

        if(mInterstitialAd == null){

            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(pub_id);

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                }
            });

        }

        return mInterstitialAd;
    }

    public static void requestNewInterstitial(){

        if(mInterstitialAd == null){
            return;
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("8C44DABF2A81BBE341ECAFD882A49F09")
                .build();

        mInterstitialAd.loadAd(adRequest);

    }
}
