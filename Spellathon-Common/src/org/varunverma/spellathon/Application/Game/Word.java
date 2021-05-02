package org.varunverma.spellathon.Application.Game;

import java.util.Comparator;

import org.varunverma.spellathon.Application.Application;

import android.content.ContentValues;

public class Word {
	
	public static final Sort SORT = new Sort();
	int id;
	public String word, meaning1, meaning2, meaning3;
	
	public Word(){
		word = meaning1 = meaning2 = meaning3 = "";
	}
	
	public Word(String word){
		this.word = word;
	}
	
	public String toString(){
		return word;
	}
	
	String getInsertQuery(){
			
		String query = "INSERT INTO " + Application.constants.getGameSolutionTableName() +
		" (" + Application.constants.getGameIdFieldName() + ", Word, Meaning1, Meaning2, Meaning3) VALUES (" +
		"'" + id + "'," +		// Game Id
		"'" + word + "'," +		// Word
		"'" + meaning1 + "'," +		// Meaning
		"'" + meaning2 + "'," +		// Meaning
		"'" + meaning3 + "'" +		// Meaning
		")";
		
		return query;
	}

	ContentValues getGameSolutionContents() {
		// Prepare ContentValue data for Insert in DB.
		ContentValues content = new ContentValues();
		
		content.put(Application.constants.getGameIdFieldName(), id);
		content.put("Word", word);
		content.put("Meaning1", meaning1);
		content.put("Meaning2", meaning2);
		content.put("Meaning3", meaning3);
		
		return content;
	}

	private static class Sort implements Comparator<Word>{

		public int compare(Word word1, Word word2) {
			return word1.word.compareToIgnoreCase(word2.word);
		}
		
	}

	
}