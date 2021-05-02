package com.ayansh.spellathon.android.Application.Game;

import android.database.Cursor;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.R;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class GameForPlay extends Game {

	public ArrayList<Word> MyWords, MyWrongWords, SortedWordList;
	public int MyScore, Result;
	private boolean madeBigWord;	// User Made the 7 letter word.
	public String MyResult;
	public File Image;
	
	public GameForPlay(int id) throws Exception {
		
		// Load Game by Id
		super(id);
		
		MyWords = new ArrayList<Word>();
		MyWrongWords = new ArrayList<Word>();
		madeBigWord = false;
		
		// Load Game from DB.
		LoadGame();
		
	}

	public String[] getGameAlphabets(){
		return alphabets.split(";");
	}
	
	@Override
	protected void LoadGame() throws Exception {
		
		// Select from DB.
		Cursor cursor = application.getDataBase().QueryGameById(id);
		
		// Load Basic Data of Game.
		LoadGame(cursor);
		
		// Load Existing words -- In case user already played the game before..
		// This should be in ; separated format !
		String my_words = cursor.getString(cursor.getColumnIndex("MyWordList"));
		if(my_words.contentEquals("")){
			// Nothing !
		}
		else{
			String[] word_list = my_words.split(";");
			
			int count = word_list.length;
			int index = 0;
			
			while(index < count){
				MyWords.add(new Word(word_list[index]));
				index++;
			}
		}
		
		MyScore = cursor.getInt(cursor.getColumnIndex("MyScore"));
		
		Result = cursor.getInt(cursor.getColumnIndex("MyResult"));
		switch(Result){
		case 0: MyResult = "Below Average!"; break;
		case 1: MyResult = "Average."; break;
		case 2: MyResult = "Good Score."; break;
		case 3: MyResult = "Exceptional!"; break;
		}
		
		// Data Massage.
		formatDataforGamePlay();
		
		cursor.close();
	}
	
	private void formatDataforGamePlay() {
		// Let's format the Data for Game Play.
		
		Word word = new Word();
		
		// Sort the Words List
		Collections.sort(Words, Word.SORT);
		
		// Keep the Words only list separate in Sorted format.
		SortedWordList = new ArrayList<Word>();
		Iterator<Word> words = Words.listIterator();
		
		while(words.hasNext()){
			word = words.next();
			SortedWordList.add(word);
		}
		
		// Sort the Word List before returning.
		Collections.sort(SortedWordList, Word.SORT);
		
	}

	public void AddWord(String word) throws Exception{
		
		validate_word(word);
		MyWords.add(new Word(word.toLowerCase(Locale.ENGLISH)));
		
		if(status == 0){	// 00 => Downloaded.
			status = 10;	// 10 => Started.
		}
		
	}

	private void validate_word(String word) throws Exception {
		
		String message;
		char center_alphabet = alphabets.charAt(0);
		word = word.toUpperCase(Locale.ENGLISH);	// Because we assume alphabets are in CAPS

		// Length must be more than 4
		if(word.length() < 4){
			message = "Not a valid word, The word must be minimum of 4 characters.";
			throw new Exception(message);
		}
		
		// Length cannot be more than 7
		if(word.length() > 7){
			message = "Not a valid word, The word cannot be more than 7 characters.";
			throw new Exception(message);
		}
		
		// Each word must have the main character (the central one).
		if(word.indexOf(center_alphabet) > -1){
			// The main character is found !
		}
		else{
			message = "Not a valid word, The word must be contain:" + center_alphabet;
			throw new Exception(message);
		}
		
		//Validate that word has only allowed alphabets
		int index = 0;
		while(index < word.length()){
			
			if(alphabets.indexOf(word.charAt(index)) > -1){ 
				index++;
			}
			else{
				message = "You cannot use: " + word.charAt(index);
				throw new Exception(message);
			}
			
		}
		
		// TODO Validate that alphabet is not repeated except in cases where alphabets are duplicate.
		
		// Do we already have this word ?
		Iterator<Word> i = MyWords.listIterator();
		while(i.hasNext()){
			if(word.equalsIgnoreCase(i.next().word)){
				message = "This word is already added !";
				throw new Exception(message);
			}
		}
		
	}

	private int CheckResults() {
		
		// Reset My Score to 0.
		int index;
		MyScore = 0;
		
		// Get Iterator.
		Iterator<Word> MyWords = this.MyWords.listIterator();
		// Loop over list...
		while(MyWords.hasNext()){
			
			Word word = MyWords.next();
			index = Collections.binarySearch(SortedWordList, word, Word.SORT);
			if(index >= 0){
				MyScore++;
				if(word.word.length() == 7){
					madeBigWord = true;
				}
			}
			else{
				MyWrongWords.add(word);
				MyWords.remove();
			}
		}
		
		Collections.sort(this.MyWords,Word.SORT);
		
		// Since results are checked, update state to finished...
		markCompleted();
		
		return MyScore;
	}

	public String GetMyRating() {
		
		Result = 0;
		MyResult = "";

		if(status < 10){
			return MyResult;
		}
		
		// Check Results.
		CheckResults();

		//3: Average, 5:Good, 7;Exceptional.
		
		if(MyScore < expected_score[0]){
			// Below Average !
			Result = 0;
			MyResult = "Below Average!";
		}
		else if(MyScore >= expected_score[2]){
			//Exceptional Score
			Result = 3;
			MyResult = "Exceptional!";
		}
		else if(MyScore >= expected_score[0] && MyScore < expected_score[1]){
			// Average Score
			Result = 1;
			MyResult = "Average Score.";
		}
		else {
			// Good Score.
			Result = 2;
			MyResult = "Good Score.";
		}

		// Update Play Services
		updatePlayServices();

		// If this is in paused list, remove.
		application.paused_game_list.remove(id);

		// Remove from new game list
		application.new_game_list.remove(id);
		
		return MyResult;
	}

	private void updatePlayServices(){

		if(MyScore == 0){
			return;
		}

		Application app = Application.get_instance();

		int my_total_score, my_bigword_score, my_fullhouse_score;

		// Update Total Score
		int my_old_score = app.readIntegerPreference("TotalScore");
		my_total_score = my_old_score + MyScore;
		app.savePreference("TotalScore", my_total_score);

		// Update How many times I made 7 letter words
		my_bigword_score = app.readIntegerPreference("TotalBigWordScore");
		if(madeBigWord){
			my_bigword_score = my_bigword_score + 1;
			app.savePreference("TotalBigWordScore", my_bigword_score);
		}

		// Update How many times I made full house
		my_fullhouse_score = app.readIntegerPreference("TotalFullHouseScore");
		if(Result == 3){
			my_fullhouse_score = my_fullhouse_score + 1;
			app.savePreference("TotalFullHouseScore", my_fullhouse_score);
		}


		// Update Achievements
		String acheivement_id;

		// Handle the Total Words Scenario
		acheivement_id = app.get_context().getResources().getString(R.string.achievement_beginner);

		if(app.achievement_status.get(acheivement_id) == Achievement.STATE_UNLOCKED){
			// Ok this is unlocked. So Go for next higher.
			acheivement_id = app.get_context().getResources().getString(R.string.achievement_proficient);

			if(app.achievement_status.get(acheivement_id) == Achievement.STATE_UNLOCKED){
				// OK This is also unlocked !! So go for next higher
				acheivement_id = app.get_context().getResources().getString(R.string.achievement_god_level);
			}
		}

		Games.Achievements.incrementImmediate(app.mGoogleApiClient, acheivement_id, MyScore)
				.setResultCallback(new UpdateAchievementResultCallBack());

		// Handle the 7 letter words scenario
		if(madeBigWord){

			acheivement_id = app.get_context().getResources().getString(R.string.achievement_word_maker);

			if(app.achievement_status.get(acheivement_id) == Achievement.STATE_UNLOCKED){
				// Ok this is unlocked. So Go for next higher.
				acheivement_id = app.get_context().getResources().getString(R.string.achievement_master_of_words);

				if(app.achievement_status.get(acheivement_id) == Achievement.STATE_UNLOCKED){
					// OK This is also unlocked !! So go for next higher
					acheivement_id = app.get_context().getResources().getString(R.string.achievement_champion_of_words);
				}
			}

			Games.Achievements.incrementImmediate(app.mGoogleApiClient, acheivement_id, 1)
					.setResultCallback(new UpdateAchievementResultCallBack());

		}

		// Handle the All Words scenario
		if(Result == 3){
			// I scored all words !!
			acheivement_id = app.get_context().getResources().getString(R.string.achievement_pitcher);

			if(app.achievement_status.get(acheivement_id) == Achievement.STATE_UNLOCKED){
				// Ok this is unlocked. So Go for next higher.
				acheivement_id = app.get_context().getResources().getString(R.string.achievement_barrel);

				if(app.achievement_status.get(acheivement_id) == Achievement.STATE_UNLOCKED){
					// OK This is also unlocked !! So go for next higher
					acheivement_id = app.get_context().getResources().getString(R.string.achievement_definer_of_dictionary);
				}
			}

			Games.Achievements.incrementImmediate(app.mGoogleApiClient, acheivement_id, 1)
					.setResultCallback(new UpdateAchievementResultCallBack());

		}

		// Update my lifetime score for Leaderboard

		String top_scorers = app.get_context().getResources().getString(R.string.leaderboard_top_scorers);
		Games.Leaderboards.submitScore(app.mGoogleApiClient, top_scorers, my_total_score);

		String word_champs = app.get_context().getResources().getString(R.string.leaderboard_word_champ);
		Games.Leaderboards.submitScore(app.mGoogleApiClient, word_champs, my_bigword_score);

		String marathon = app.get_context().getResources().getString(R.string.leaderboard_marathon_of_spellathon);
		Games.Leaderboards.submitScore(app.mGoogleApiClient, marathon, my_fullhouse_score);


	}
	
	public void saveCurrentGameState(){
		
		if(status < 10){
			return;
		}
		
		if(status > 0 && status < 20){
			status = 15;	// This means it is paused.
		}
		
		String my_wordlist = "";
		int index = 0;
		
		Iterator<Word> MyWords = this.MyWords.listIterator();
		while(MyWords.hasNext()){
			
			if(index == 0){
				my_wordlist = MyWords.next().word;
			}
			else{
				my_wordlist = my_wordlist + ";" + MyWords.next();
			}
			index++;
		}
		
		String query = "UPDATE " + Application.constants.getGameInfoTableName() + " SET " +
		"Status = '" + status + "', " +				// Status
		"MyWordList = '" + my_wordlist + "', " +	// My WordList
		"MyResult = '" + Result + "', " +			// My Result
		"MyScore = '" + MyScore + "' " +			// My Score
		"WHERE " + Application.constants.getGameIdFieldName() + " = '" + id + "'";	// Where GameId = 
		
		boolean success = application.getDataBase().executeQuery(query);

		if(success && status == 15){
			application.paused_game_list.put(id,id);
			application.new_game_list.remove(id);
		}
		
	}
	
	public boolean deleteGame(){
		
		application.getDataBase().removeGame(id);
		Image.delete();
		return true;
	}
	
	public Word getWord(String word){
		
		int index = Collections.binarySearch(SortedWordList, new Word(word), Word.SORT);
		if(index >= 0){
			return Words.get(index);
		}
		else{
			return new Word(word);
		}
	}
	
}