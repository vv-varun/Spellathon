package com.ayansh.spellathon.android.Application.Game;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.Achievements;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;

/**
 * Created by Varun Verma on 1/20/2017.
 */

public class UpdateAchievementResultCallBack implements ResultCallback<Achievements.UpdateAchievementResult> {

    private Application app;

    public UpdateAchievementResultCallBack(){

        app = Application.get_instance();

    }

    @Override
    public void onResult(@NonNull Achievements.UpdateAchievementResult result) {

        if(result.getStatus().getStatusCode() == GamesStatusCodes.STATUS_ACHIEVEMENT_UNLOCKED){

            // Achievement was unlocked. Wow.
            String current_achievement_id = result.getAchievementId();
            String next_achievement_id = "";
            int my_current_score = 0;

            // Set Status
            app.achievement_status.put(current_achievement_id,Achievement.STATE_UNLOCKED);

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_beginner))){
                next_achievement_id = getAchievementString(R.string.achievement_proficient);
                my_current_score = app.readIntegerPreference("TotalScore");
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_proficient))){
                next_achievement_id = getAchievementString(R.string.achievement_god_level);
                my_current_score = app.readIntegerPreference("TotalScore");
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_word_maker))){
                next_achievement_id = getAchievementString(R.string.achievement_master_of_words);
                my_current_score = app.readIntegerPreference("TotalBigWordScore");
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_master_of_words))){
                next_achievement_id = getAchievementString(R.string.achievement_champion_of_words);
                my_current_score = app.readIntegerPreference("TotalBigWordScore");
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_pitcher))){
                next_achievement_id = getAchievementString(R.string.achievement_barrel);
                my_current_score = app.readIntegerPreference("TotalFullHouseScore");
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_barrel))){
                next_achievement_id = getAchievementString(R.string.achievement_definer_of_dictionary);
                my_current_score = app.readIntegerPreference("TotalFullHouseScore");
            }

            Games.Achievements.unlock(app.mGoogleApiClient,next_achievement_id);
            Games.Achievements.increment(app.mGoogleApiClient, next_achievement_id, my_current_score);

        }
        else{
            Log.i("Spellathon","Achievenemtn ID " + result.getAchievementId());
            Log.i("Spellathon","Status Code " + result.getStatus());
            Log.i("Spellathon","Status Code " + result.getStatus().getStatusCode());
        }

    }

    private String getAchievementString(int stringID){

        return app.get_context().getResources().getString(stringID);

    }
}