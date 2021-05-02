package com.ayansh.spellathon.android.Application;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility Class to check internet connection is on or not !
 */

/**
 * @author Varun Verma (http://varunverma.org)
 *
 */
public class ConnectionManager {
	
	private static final String backend = "http://www.google.com";
	
	public boolean IsInternetOn() {
		
		try{
			URL url = new URL(backend);
	        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	        urlc.setConnectTimeout(1000 * 5); // Timeout is in seconds
	        urlc.connect();
	        if (urlc.getResponseCode() == 200) {
	         	//http response is OK
	           	urlc.disconnect();
	           	return true;
	        }
	        else{
	          	return false;
	        }
		} catch(Exception e){
			return false;
		}
	}

}