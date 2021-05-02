package org.varunverma.spellathon.Application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ApplicationDB extends SQLiteOpenHelper {

	private SQLiteDatabase data_base;

	static private ApplicationDB instance;

	public static ApplicationDB get_instance(Context context) {

		if (instance == null) {
			instance = new ApplicationDB(context);
		}

		return instance;
	}

	private ApplicationDB(Context context) {
		// Call super constructor.
		super(context, Application.constants.getDBName(), null, Application.constants.getDBVersion());

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// Create Database Table.
		String create_game_table = "CREATE TABLE GameInfo (" + 
				Application.constants.getGameIdFieldName() + " INTEGER, " + // Game Id
				"Level VARCHAR, " + 		// Game level - Easy, Medium, Difficult
				"Status INTEGER, " + 		// Status: 0(Downloaded), 10(Started), 15(Paused), 20(Finished)
				"Alphabets VARCHAR, " + 	// Alphabets - 1st is center, rest are surrounding
				"ExpectedScore VARCHAR, " +	// Expected Score: 4:Average; 6:Good; 8:Exceptional
				"MyWordList VARCHAR, " +	// Words created by User
				"MyResult INTEGER, " + 		// Result in Star Rating - 0, 1, 2, 3 stars.
				"MyScore INTEGER, " + 		// Score by User (in Integer)
				"MyRating INTEGER, " +		// My Rating - as rated by me
				"AverageRating INTEGER, " +	// Average Rating from Internet
				"HighScore INTEGER, " +		// Highest Score from Internet
				"AverageScore INTEGER, " + 	// Average Score from Internet
				"SyncDate INTEGER" + 		// Timestamp when sync was done.
				")";
		
		String create_game_solution_table = "CREATE TABLE GameSolution (" + 
				Application.constants.getGameIdFieldName() + " INTEGER, " + 			// Game Id
				"Word VARCHAR, " + 			// Word
				"Meaning1 VARCHAR, " + 		// Meaning
				"Meaning2 VARCHAR, " + 		// Meaning
				"Meaning3 VARCHAR" + 		// Meanings
				")";

		
		// create a new table - if not existing
		try {
			// Create Table.
			db.execSQL(create_game_table);
			db.execSQL(create_game_solution_table);
			
		} catch (SQLException e) {
			// To do !
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
		//When upgrading -- not as of now !

	}

	public void open_db_for_writing() {
		data_base = instance.getWritableDatabase();
	}

	public int getRandomGame() {
		// Return a random un-played Game.
		int gameId;
		Cursor cursor = query_game_table(1);
		if(cursor.moveToFirst()){
			gameId = cursor.getInt(cursor.getColumnIndex(Application.constants.getGameIdFieldName()));
			cursor.close();
			return gameId;
		}
		else{
			return -1;
		}

	}

	public Cursor query_game_table(int type) {

		String selection = null;

		switch (type) {
		case 0: // Full table
			break;

		case 1: // Unplayed Games
			selection = "Status < 10";
			break;
		
		case 2: // Paused Games.
			selection = "Status = 15";
			break;
			
		case 3: // Completed Games
			selection = "Status = 20";
			break;
			
		default: // Full Table.
			break;
		}

		return data_base.query(Application.constants.getGameInfoTableName(), null, selection, null, null, null, Application.constants.getGameIdFieldName());

	}

	public Cursor QueryGameById(int id) {
		
		String selection = Application.constants.getGameIdFieldName() + " = " + id;
		return data_base.query(Application.constants.getGameInfoTableName(), null, selection, null, null, null, null);
		
	}

	public Cursor QueryGameSolution(int id) {
		
		String selection = Application.constants.getGameIdFieldName() + " = " + id;
		return data_base.query(Application.constants.getGameSolutionTableName(), null, selection, null, null, null, null);
		
	}
	
	public boolean removeGame(int id) {
		// Remove Game from DB.
		String query;
		List<String> queries = new ArrayList<String>();
		String where = " WHERE " + Application.constants.getGameIdFieldName() + " = '" + id + "'";
		
		query = "DELETE FROM " + Application.constants.getGameInfoTableName() + where;
		queries.add(query);
		
		query = "DELETE FROM " + Application.constants.getGameSolutionTableName() + where;
		queries.add(query);
		
		return executeQueries(queries);	
	}
	
	public boolean executeQuery(String query) {
		
		try {
			data_base.beginTransaction();
			data_base.execSQL(query);
			data_base.setTransactionSuccessful();
			data_base.endTransaction();
			return true;
		}
		catch (Exception e){
			Log.e(Application.constants.getLoggerTag(), e.getMessage(), e);
			data_base.endTransaction();
			return false;
		}
		
	}

	public boolean executeQueries(List<String> Queries) {
		
		String query = "";
		Iterator<String> iterator = Queries.listIterator();
		
		try {
			
			data_base.beginTransaction();
			
			while(iterator.hasNext()){
				query = iterator.next();
				data_base.execSQL(query);
			}
			
			data_base.setTransactionSuccessful();
			data_base.endTransaction();
			return true;
		}
		catch (Exception e){
			// Do nothing! -- Track the error causing query 
			Log.e(Application.constants.getLoggerTag(), e.getMessage(), e);
			data_base.endTransaction();
			return false;
		}
		
	}

	public boolean insertData(List<DBContentValues> data) {
		// Insert Data.
		
		Iterator<DBContentValues> iterator = data.listIterator();
		DBContentValues map = new DBContentValues();
		@SuppressWarnings("unused")
		long id;
		
		try {
			
			data_base.beginTransaction();
			
			while(iterator.hasNext()){
				map = iterator.next();
				id = data_base.insertOrThrow(map.TableName, null, map.Content);
			}
			
			data_base.setTransactionSuccessful();
			data_base.endTransaction();
			return true;
		}
		catch (Exception e){
			// Do nothing! -- Track the error causing query 
			Log.e(Application.constants.getLoggerTag(), e.getMessage(), e);
			data_base.endTransaction();
			return false;
		}
		
	}

}