package com.ayansh.spellathon.android.Application;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

	HashMap<Integer,Integer> query_game_table(int type) {

		HashMap<Integer,Integer> game_ids = new HashMap<Integer,Integer>();
		int game_id;
		String selection = null;
		String[] columns = {Application.constants.getGameIdFieldName()};

		String difficulty_level = Application.get_instance().readStringPreference("DifficultyLevel");
		if(difficulty_level.contentEquals("") || difficulty_level.contentEquals("Easy")){
			difficulty_level = " AND Level='Easy'";
		}
		else if(difficulty_level.contentEquals("Medium")){
			difficulty_level = " AND Level IN ('Easy','Medium')";
		}
		else{
			difficulty_level = "";
		}

		switch (type) {
		case 0: // Full table
			break;

		case 1: // Unplayed Games
			selection = "Status < 10" + difficulty_level;
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

		Cursor cursor = data_base.query(Application.constants.getGameInfoTableName(), columns, selection, null, null, null, null);

		if(cursor.moveToFirst()){

			do{

				game_id = cursor.getInt(cursor.getColumnIndex(Application.constants.getGameIdFieldName()));
				game_ids.put(game_id,game_id);

			}while(cursor.moveToNext());

			cursor.close();
		}

		cursor.close();
		return game_ids;
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

	HashMap<String, Integer> getHealthStatus() {

		String game_table = Application.constants.getGameInfoTableName();
		String query = "SELECT Status, count(*) FROM " + game_table + " GROUP BY Status";
		Cursor cursor = data_base.rawQuery(query,null);
		HashMap<String,Integer> result = new HashMap<String, Integer>();
		String status_name;

		if(cursor.moveToFirst()){

			do{

				int status = cursor.getInt(0);
				int count = cursor.getInt(1);

				switch (status){
					case 20: status_name = "Completed"; break;
					case 15: status_name = "Paused"; break;
					default: status_name = "New"; break;
				}

				result.put(status_name,count);

			}while(cursor.moveToNext());

		}

		cursor.close();
		return result;

	}

	int getLatestGameID() {

		String game_table = Application.constants.getGameInfoTableName();
		String game_id_field = Application.constants.getGameIdFieldName();
		String query = "SELECT max(" + game_id_field + ") FROM " + game_table + " GROUP BY Status";
		Cursor cursor = data_base.rawQuery(query,null);
		int game_id = -1;

		if(cursor.moveToFirst()){

			game_id = cursor.getInt(0);
		}

		cursor.close();
		return game_id;

	}
}