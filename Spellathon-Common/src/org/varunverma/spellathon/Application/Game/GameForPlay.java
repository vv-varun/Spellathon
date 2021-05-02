package org.varunverma.spellathon.Application.Game;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import org.varunverma.spellathon.Application.Application;

import android.database.Cursor;
import android.os.Environment;

public class GameForPlay extends Game {

	public ArrayList<Word> MyWords, MyWrongWords, SortedWordList;
	public int MyScore, Result;
	public String MyResult;
	public File Image;
	
	public GameForPlay(int id) throws Exception {
		
		// Load Game by Id
		super(id);
		
		MyWords = new ArrayList<Word>();
		MyWrongWords = new ArrayList<Word>();
		
		// Load Game from DB.
		LoadGame();
		
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
		
		// Build Image File.
		buildImageFile();
		
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

	private void buildImageFile() {
		// Build the Image file.
		String file = String.valueOf(id) + ".png";
		
		// Point to our directory...
		File root = Environment.getExternalStorageDirectory();
		File directory = new File(root, Application.constants.getImageDir());
		Image = new File(directory, file);
		
	}

	public void AddWord(String word) throws Exception{
		
		validate_word(word);
		MyWords.add(new Word(word.toLowerCase(Locale.ENGLISH)));
		
		if(status == 0){	// 00 => Downloaded.
			status = 10;	// 10 => Started.
			
			try{
				// Update the view attribute to paused..
				GameViewAttributes gv = application.map.get(id);
				gv.imageId = Application.constants.getPauseDrawable();
			}catch (Exception e){
				// Ignore this. Because it's ok.
			}
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
		
		try{
			// Update the view attribute to paused..
			application.map.remove(id);
			GameViewAttributes gv = new GameViewAttributes(this);
			application.map.put(id, gv);
			
		}catch(Exception e){
			// Ignore here.
		}
		
		return MyResult;
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
		
		application.getDataBase().executeQuery(query);
		
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