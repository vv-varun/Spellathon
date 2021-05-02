package com.ayansh.spellathon.android.Application;

import android.net.Uri;
import android.util.Log;

import com.ayansh.CommandExecuter.Command;
import com.ayansh.CommandExecuter.Invoker;
import com.ayansh.CommandExecuter.ResultObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimeZone;

public class SavePlayerDetailsCommand extends Command {

	private String regId;

	public SavePlayerDetailsCommand(Invoker caller) {
		super(caller);
	}

	@Override
	protected void execute(ResultObject result) throws Exception {

		Log.v(Application.TAG, "Saving Player Details with our Server");
		Application app = Application.get_instance();
		String packageName = app.get_context().getPackageName();

		String iid = app.readStringPreference("InstanceID");
		String player_id = app.readStringPreference("PlayerID");
		String player_name = app.readStringPreference("PlayerName");

		if(player_id.contentEquals("")){
			return;
		}

		String postURL = "http://apps.ayansh.com/Spellathon/SavePlayerDetails.php";

		URL url = new URL(postURL);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		try{

			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);
			urlConnection.setRequestMethod("POST");

			Uri.Builder uriBuilder = new Uri.Builder()
					.appendQueryParameter("iid", iid)
					.appendQueryParameter("PlayerID", player_id)
					.appendQueryParameter("PlayerName", player_name);

			String parameterQuery = uriBuilder.build().getEncodedQuery();

			OutputStream os = urlConnection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(parameterQuery);
			writer.flush();
			writer.close();
			os.close();

			urlConnection.connect();

			// Get Input Stream Reader.
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String line = reader.readLine();

			if(line.toString().contentEquals("Success")){
				// Success
				app.savePreference("PlayerDetailsStatus", "Success");
				Log.v(Application.TAG, "Player Details saved successfully on our server");
			}

		}
		catch (Exception e){
			// Nothing to do
			Log.w(Application.TAG, "Following error occured while saving GCM RegId with our servers:");
			Log.e(Application.TAG, e.getMessage(), e);
		}
		finally {
			urlConnection.disconnect();
		}
	}
}