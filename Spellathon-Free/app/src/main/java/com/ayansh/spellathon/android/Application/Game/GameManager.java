package com.ayansh.spellathon.android.Application.Game;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.Application.ConnectionManager;

import android.net.Uri;
import android.util.Xml;

public class GameManager {

	private static GameManager gm;

	List<GameForDownload> gameForDownloads;
	private static final String GamesUrl = "http://apps.ayansh.com/Spellathon/GetGames.php";
	private Application application;

	public static GameManager getInstance(){

		if(gm == null){
			gm = new GameManager();
		}

		return gm;
	}
	
	private GameManager() {
		
		gameForDownloads = new ArrayList<GameForDownload>();
		application = Application.get_instance();


	}
	
	public void DownloadGames(int from, int to) throws Exception{
		
		// Download gameForDownloads from Internet.
		GetGamesFromInternet(from, to);
		
		// Saves gameForDownloads into DB.
		SaveGames();
	}

	private void GetGamesFromInternet(int from, int to) throws Exception{
		// Get gameForDownloads from Internet.
		
		ConnectionManager cm = new ConnectionManager();
		if(!cm.IsInternetOn()){
			String message = "You are not connected to internet. " +
					"Connect to internet to download moe puzzles.";
			throw new Exception(message);
		}
		
		String From, To;
		From = String.valueOf(from);
		To = String.valueOf(to);
		
		InputStreamReader isr;
		XMLParser xml_parser = new XMLParser();
		
		URL url = new URL(GamesUrl);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		try{

			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);
			urlConnection.setRequestMethod("POST");

			Uri.Builder uriBuilder = new Uri.Builder()
					.appendQueryParameter("From", From)
					.appendQueryParameter("To", To)
					.appendQueryParameter("meanings", "Yes");
			String parameterQuery = uriBuilder.build().getEncodedQuery();

			OutputStream os = urlConnection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(parameterQuery);
			writer.flush();
			writer.close();
			os.close();

			urlConnection.connect();

			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			isr = new InputStreamReader(in);

			Xml.parse(isr, xml_parser);
			gameForDownloads = xml_parser.Games;

		}
		catch (Exception e){
			throw e;
		}
		finally {
			urlConnection.disconnect();
		}
		
	}
	
	public boolean SaveGames() {
		// Save gameForDownloads
		ListIterator<GameForDownload> iterator = gameForDownloads.listIterator();
		GameForDownload game;
		boolean success = true;

		while(iterator.hasNext()){
			
			game = iterator.next();

			try {
				game.saveGameToDB();
				iterator.remove();
			} catch (Exception e) {
				success = false;
			}
		}

		return success;
	}
	
}