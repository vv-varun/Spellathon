package org.varunverma.spellathon.Application.Game;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.varunverma.CommandExecuter.ProgressInfo;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.ConnectionManager;

import android.util.Xml;

public class GameManager {
	
	private List<GameForDownload> Games;
	private String GamesUrl;
	private Application application;
	
	public GameManager() {
		
		Games = new ArrayList<GameForDownload>();
		application = Application.get_instance();
		
		// Build the URL from where the Games will be downloaded.
		GamesUrl = "http://varunverma.org/Spellathon/GetGames.php";
	}
	
	public void DownloadGames(int from, int to, GameDownloadCommand cmd) throws Exception{
		
		if(application.downloading){
			throw new Exception("Download already in progress!");
		}
		else{
			application.downloading = true;
		}
		
		// Download Games from Internet.
		GetGamesFromInternet(from, to);
		
		// Saves Games into DB.
		SaveGames(cmd);
	}

	private void GetGamesFromInternet(int from, int to) throws Exception{
		// Get Games from Internet.
		
		ConnectionManager cm = new ConnectionManager();
		if(!cm.IsInternetOn()){
			String message = "You are not connected to internet. " +
					"Connect to internet to download moe puzzles.";
			throw new Exception(message);
		}
		
		String From, To;
		From = String.valueOf(from);
		To = String.valueOf(to);
		
		InputStream is;
		InputStreamReader isr;
		XMLParser xml_parser = new XMLParser();
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(GamesUrl);

        try {
    		// Prepare Form Data for Posting.
    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  
        	nameValuePairs.add(new BasicNameValuePair("From", From));
        	nameValuePairs.add(new BasicNameValuePair("To", To));
        	nameValuePairs.add(new BasicNameValuePair("meanings", "Yes"));
        	
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			//Execute HTTP Post Request  
	    	HttpResponse response = httpclient.execute(httppost);
	    	
	    	// Open Stream for Reading.
			is = response.getEntity().getContent();
			// Get Input Stream Reader.
			isr = new InputStreamReader(is);
			// Parse the input XML.
			Xml.parse(isr, xml_parser);
			Games = xml_parser.Games;
	    	
        }
        catch (Exception e){
        	throw e;
        }
		
	}
	
	private void SaveGames(GameDownloadCommand cmd) throws Exception {
		// Save Games
		ListIterator<GameForDownload> iterator = Games.listIterator();
		GameForDownload game;
		int i = 0;
		int perct;
		int total;
		
		if(cmd.isFirstRun()){
			total = 5;
		}
		else{
			total = Games.size();
		}
		
		while(iterator.hasNext()){
			
			i++;
			game = iterator.next();
			game.DownloadImage();
			
			perct = 100 * i / total;
			
			if(cmd.isFirstRun()){
				if(i <= 5){
					cmd.publishProgress(new ProgressInfo(perct));
				}
				if(i == 5){
					cmd.publishProgress(new ProgressInfo("Update UI"));
				}
			}
			else{
				cmd.publishProgress(new ProgressInfo(perct));
			}
			
		}
	}
	
}