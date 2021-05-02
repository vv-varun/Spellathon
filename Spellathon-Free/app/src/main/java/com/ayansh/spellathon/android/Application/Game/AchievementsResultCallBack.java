package com.ayansh.spellathon.android.Application.Game;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;

import java.util.Iterator;

/**
 * Created by Varun Verma on 1/20/2017.
 */

public class AchievementsResultCallBack implements ResultCallback<LoadAchievementsResult> {

    private Application app;

    @Override
    public void onResult(@NonNull LoadAchievementsResult result) {

        app = Application.get_instance();

        AchievementBuffer achievements = result.getAchievements();
        Iterator<Achievement> iterator = achievements.iterator();

        while(iterator.hasNext()){

            Achievement a = iterator.next();
            String current_achievement_id = a.getAchievementId();
            app.achievement_status.put(current_achievement_id,a.getState());
        }

        Iterator<String> i = app.achievement_status.keySet().iterator();

        while(i.hasNext()){

            String current_achievement_id = i.next();
            String next_achievement_id = "";

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_beginner))){
                next_achievement_id = getAchievementString(R.string.achievement_proficient);
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_proficient))){
                next_achievement_id = getAchievementString(R.string.achievement_god_level);
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_word_maker))){
                next_achievement_id = getAchievementString(R.string.achievement_master_of_words);
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_master_of_words))){
                next_achievement_id = getAchievementString(R.string.achievement_champion_of_words);
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_pitcher))){
                next_achievement_id = getAchievementString(R.string.achievement_barrel);
            }

            if(current_achievement_id.contentEquals(getAchievementString(R.string.achievement_barrel))){
                next_achievement_id = getAchievementString(R.string.achievement_definer_of_dictionary);
            }

            if(next_achievement_id.contentEquals("")){
                continue;
            }

            if(app.achievement_status.get(current_achievement_id) == Achievement.STATE_UNLOCKED &&
                    app.achievement_status.get(next_achievement_id) == Achievement.STATE_HIDDEN){

                // Un Reveal the next one.
                if(next_achievement_id.length() > 0){

                    Games.Achievements.reveal(app.mGoogleApiClient,next_achievement_id);
                    app.achievement_status.put(next_achievement_id,Achievement.STATE_REVEALED);
                }

            }

        }

        // Set Difficulty Level
        String beginner_id = getAchievementString(R.string.achievement_beginner);
        String proficient_id = getAchievementString(R.string.achievement_proficient);
        String difficulty_level;

        if(app.achievement_status.get(proficient_id) == Achievement.STATE_UNLOCKED){
            // If proficient is unlocked then allow Difficult Games
            difficulty_level = "Difficult";
        }
        else if(app.achievement_status.get(beginner_id) == Achievement.STATE_UNLOCKED){
            // If proficient is locked and beginner is unlocked then allow Medium Games
            difficulty_level = "Medium";
        }
        else{
            // If both are unlocked... then keep Easy level
            difficulty_level = "Easy";
        }

        app.savePreference("DifficultyLevel",difficulty_level);

    }

    private String getAchievementString(int stringID){

        return app.get_context().getResources().getString(stringID);

    }
}