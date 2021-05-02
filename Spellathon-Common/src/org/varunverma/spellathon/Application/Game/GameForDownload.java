package org.varunverma.spellathon.Application.Game;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.ApplicationDB;
import org.varunverma.spellathon.Application.DBContentValues;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;

public class GameForDownload extends Game {
	
	String image_url;
	private String dir;
	
	public GameForDownload(int id) {
		super(id);
		dir = Application.constants.getImageDir();
	}
	
	public GameForDownload(){
		super();
		dir = Application.constants.getImageDir();
	}

	@Override
	protected void LoadGame() {
		// Load Game.
		// Nothing, because I have to download.

	}
	
	public void DownloadImage() throws Exception{
		
		String file = String.valueOf(id) + ".png";
		// Create directory if it does not exists yet...
		File root = Environment.getExternalStorageDirectory();
		File directory = new File(root, dir);
		File image_file = new File(directory, file);
		
		if (directory.exists()){}
		else{
			directory.mkdirs();
		}
		
		URL url = new URL(image_url);
			
		/* Open a connection to that URL. */
        URLConnection ucon = url.openConnection();
            
        //* Define InputStreams to read from the URLConnection.
		InputStream is = ucon.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
			
	    /* * Read bytes to the Buffer until there is nothing more to read(-1). */
	    ByteArrayBuffer baf = new ByteArrayBuffer(50);
	    int current = 0;
	    while ((current = bis.read()) != -1) {
	    	baf.append((byte) current);
	    }
	    /* Convert the Bytes read to a String. */
	    FileOutputStream fos = new FileOutputStream(image_file);
	    fos.write(baf.toByteArray());
	    fos.close();
	    
	    // If download was success, then update DB.
	    // We should validate if the download was success !
	    File Image = new File(directory, file);
	    if(Image.exists()){
	    	UpdateDB();
	    }
	    else{
	    	// Oops !
	    	throw new Exception("Error while downloading the image!");
	    }

	}
	
	private void UpdateDB() throws Exception{
		
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
		
		// Get Content Values for Main Table.
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
		success = data_base.insertData(data);
		
		
		if(success){
			// Wow :) Now update the Application.
			application.updateLastGameDownloaded(id);
		}
		else{
			// Oops!
			throw new Exception("Error while save data into DB!");
		}
		
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