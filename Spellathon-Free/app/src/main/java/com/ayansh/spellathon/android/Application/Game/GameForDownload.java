package com.ayansh.spellathon.android.Application.Game;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.Application.ApplicationDB;
import com.ayansh.spellathon.android.Application.DBContentValues;

import android.content.ContentValues;
import android.database.Cursor;

public class GameForDownload extends Game {
	
	String image_url;

	public GameForDownload(int id) {
		super(id);
	}
	
	public GameForDownload(){
		super();
	}

	@Override
	protected void LoadGame() {
		// Load Game.
		// Nothing, because I have to download.

	}
	
	public void DownloadImage() throws Exception{

		/*

		String file = String.valueOf(id) + ".png";
		// Create directory if it does not exists yet...
		File root = Application.get_instance().getFilesDirectory();
		File image_dir = new File(root, "images");

		if (image_dir.exists()){}
		else{
			image_dir.mkdirs();
		}

		File image_file = new File(image_dir, file);

		URL url = new URL(image_url);
			
		// Open a connection to that URL.
        URLConnection ucon = url.openConnection();


		// Define InputStreams to read from the URLConnection.
		InputStream is = ucon.getInputStream();
		FileOutputStream fos = new FileOutputStream(image_file);

		int bytesRead = -1;
		byte[] buffer = new byte[4096];
		while ((bytesRead = is.read(buffer)) != -1) {
			fos.write(buffer, 0, bytesRead);
		}

		fos.close();
		is.close();

	    // If download was success, then update DB.
	    // We should validate if the download was success !
	    File Image = new File(image_dir, file);
	    if(Image.exists()){
			saveGameToDB();
	    }
	    else{
	    	// Oops !
	    	throw new Exception("Error while downloading the image!");
	    }

		//*/

	}
	
	public void saveGameToDB() throws Exception{
		
		boolean success;
		List<DBContentValues> data = new ArrayList<DBContentValues>();
		DBContentValues map = new DBContentValues();
		ContentValues content = new ContentValues();
		
		List<String> queries = new ArrayList<String>();
		ApplicationDB data_base = application.getDataBase();
			
		Cursor cursor = data_base.QueryGameById(id);
		if(cursor.moveToFirst()){
			data_base.removeGame(id);
		}
			
		// Build Query for Game Table.
		queries.add(getGameInfoQuery());
		
		// Get Content Values for Main_Old Table.
		content = getGameInfoContents();
		map.TableName = Application.constants.getGameInfoTableName();
		map.Content = content;
		data.add(map);
		
		// Build Query for Game Solution Table.
		Word word;
		Iterator<Word> words = Words.listIterator();
		while(words.hasNext()){
			
			word = words.next();
			queries.add(word.getInsertQuery());
			
			content = word.getGameSolutionContents();
			map = new DBContentValues();
			map.TableName = Application.constants.getGameSolutionTableName();
			map.Content = content;
			data.add(map);
			
		}
		
		//success = data_base.executeQueries(queries);
		data_base.insertData(data);
		
	}

	private String getGameInfoQuery() {
		// Prepare Traditional Query for Game Table.
		long sync_date = new GregorianCalendar().getTimeInMillis();
		
		String query = "INSERT INTO " + Application.constants.getGameInfoTableName() +
		" (" + Application.constants.getGameIdFieldName() + ", Level, Status, Alphabets, ExpectedScore, MyWordList, MyResult, MyScore, " +
		"MyRating, AverageRating, HighScore, AverageScore, SyncDate) VALUES (" +
		"'" + id + "'," +					// Game Id
		"'" + level + "'," +				// Level
		"'" + status + "'," +				// Status = 0
		"'" + alphabets + "'," +			// Alphabets
		"'" + ExpectedScore + "'," +		// Expected Score
		"'" + "" + "'," +					// My Word List - Will be empty
		"'" + "" + "'," +					// My Result - Will be Empty
		"'" + "" + "'," +					// My Score - Will be Empty
		"'" + "" + "'," +					// My Rating - Will be Empty
		"'" + average_rating + "'," +		// Average Rating
		"'" + high_score + "'," +			// High Score
		"'" + average_score + "'," +		// Average Score
		"'" + sync_date + "'" +				// Sync date.
		")";
		
		return query;
	}

	private ContentValues getGameInfoContents() {
		// We will prepare ContentValues data for Insert into DB.
		ContentValues content = new ContentValues();
		
		content.put(Application.constants.getGameIdFieldName(), id);
		content.put("Level", level);
		content.put("Status", status);
		content.put("Alphabets", alphabets);
		content.put("ExpectedScore", ExpectedScore);
		content.put("AverageRating", average_rating);
		content.put("HighScore", high_score);
		content.put("AverageScore", average_score);
		content.put("SyncDate", sync_date);
		
		// Empty Values
		content.put("MyWordList", "");
		content.put("MyResult", "");
		content.put("MyScore", "");
		content.put("MyRating", "");
		
		return content;
		
	}
	
}