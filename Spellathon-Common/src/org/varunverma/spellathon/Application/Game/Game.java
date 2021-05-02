package org.varunverma.spellathon.Application.Game;

import java.util.ArrayList;
import java.util.List;

import org.varunverma.spellathon.Application.Application;

import android.database.Cursor;

public abstract class Game {
	
	int id;							// ID
	String level;					// Level
	int status;						//Status: 0(Downloaded), 10(Started), 15(Paused), 20(Finished)
	String alphabets;				// Alphabets
	int[] expected_score;			// Only 3 elements in array expected. Array represents boundaries of Average Score
	public String ExpectedScore;	// 3:Average;4:Good;5:Outstanding (From DB)
	public List<Word> Words;		// Word List => Answers
	int average_rating;				// On scale of 5
	int high_score;
	int average_score;
	long sync_date;
	
	protected Application application;
	
	public Game(){
		// Initialize Application and DataBase.
		initialize();
	}
	
	public Game(int id){
		
		// Load Game from Id.
		this.id = id;
		initialize();
		
	}
	
	protected void initialize(){
		application = Application.get_instance();
		Words = new ArrayList<Word>();
		expected_score = new int[3];
	}
	
	protected void LoadGame(Cursor cursor) throws Exception{
		// Loads only Basic Data of Game.
		if(cursor.moveToFirst()){
			
			level = cursor.getString(cursor.getColumnIndex("Level"));
			status = cursor.getInt(cursor.getColumnIndex("Status"));
			
			// We expect this to be simply concatenation
			alphabets = cursor.getString(cursor.getColumnIndex("Alphabets"));
					
			// We expect this to be in ; separated
			ExpectedScore = cursor.getString(cursor.getColumnIndex("ExpectedScore"));
			String[] score = ExpectedScore.split(";");
			score[0] = score[0].substring(0, score[0].indexOf(":"));
			expected_score[0] = Integer.parseInt(score[0]);
			score[1] = score[1].substring(0, score[1].indexOf(":"));
			expected_score[1] = Integer.parseInt(score[1]);
			score[2] = score[2].substring(0, score[2].indexOf(":"));
			expected_score[2] = Integer.parseInt(score[2]);

			// Load Words and Meanings (Solution)
			LoadGameSolution();
		}
		else{
			// Raise Exception that Game could not be loaded.
			throw new Exception("Game could not be loaded!");
		}
	}
	
	protected void LoadGameSolution(){
		// Load the Game Solution.
		Cursor cursor = application.getDataBase().QueryGameSolution(id);
		Word word;
		
		if(cursor.moveToFirst()){
			
			word = new Word();
			word.word = cursor.getString(cursor.getColumnIndex("Word"));
			word.meaning1 = cursor.getString(cursor.getColumnIndex("Meaning1"));
			word.meaning2 = cursor.getString(cursor.getColumnIndex("Meaning2"));
			word.meaning3 = cursor.getString(cursor.getColumnIndex("Meaning3"));
			Words.add(word);
			
			while(cursor.moveToNext()){
				word = new Word();
				word.id = id;
				word.word = cursor.getString(cursor.getColumnIndex("Word"));
				word.meaning1 = cursor.getString(cursor.getColumnIndex("Meaning1"));
				word.meaning2 = cursor.getString(cursor.getColumnIndex("Meaning2"));
				word.meaning3 = cursor.getString(cursor.getColumnIndex("Meaning3"));
				Words.add(word);
			}
		}
		
		cursor.close();
	}
	
	public void markCompleted(){
		// Mark game as completed.
		status = 20;
		
		// Update the view attribute to paused..
		try{
			// We try this separetly becuase we do not want to catch any exceptions here.
			GameViewAttributes gv = application.map.get(id);
			gv.imageId = Application.constants.getCompletedDrawable();
		}catch(Exception e){
			// Ignore here.
		}
			
	}
	
	public int getStatus(){
		return status;
	}
	
	protected abstract void LoadGame() throws Exception;
	
}