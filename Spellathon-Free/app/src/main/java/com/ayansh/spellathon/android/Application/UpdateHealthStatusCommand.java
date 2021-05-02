package com.ayansh.spellathon.android.Application;

import android.net.Uri;

import com.ayansh.CommandExecuter.Command;
import com.ayansh.CommandExecuter.Invoker;
import com.ayansh.CommandExecuter.ResultObject;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Varun on 1/26/2017.
 */

public class UpdateHealthStatusCommand extends Command{


    private HashMap<String,Integer> healthStatus;

    public UpdateHealthStatusCommand(HashMap<String,Integer> healthStatus) {

        super(Command.DUMMY_CALLER);
        this.healthStatus = healthStatus;
    }

    @Override
    protected void execute(ResultObject result) throws Exception {

        Application app = Application.get_instance();

        String player_id = app.readStringPreference("PlayerID");
        String player_name = app.readStringPreference("PlayerName");
        String iid = app.readStringPreference("InstanceID");

        String postURL = "http://apps.ayansh.com/Spellathon/SyncHealthStatus.php";

        JSONObject player_data = new JSONObject();
        player_data.put("TrialStatus",app.readStringPreference("TrialStatus"));
        player_data.put("IsPremiumUser",Application.constants.isPremiumVersion());
        player_data.put("AppVersion",app.getCurrentAppVersionCode());

        JSONObject health_status_data = new JSONObject();
        Iterator<Map.Entry<String,Integer>> i = healthStatus.entrySet().iterator();
        while(i.hasNext()){

            Map.Entry<String,Integer> entry = i.next();
            health_status_data.put(entry.getKey(),entry.getValue());
        }

        URL url = new URL(postURL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("POST");

            Uri.Builder uriBuilder = new Uri.Builder()
                    .appendQueryParameter("InstanceID", iid)
                    .appendQueryParameter("PlayerID", player_id)
                    .appendQueryParameter("PlayerName", player_name)
                    .appendQueryParameter("player_data", player_data.toString())
                    .appendQueryParameter("health_status", health_status_data.toString());

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

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        }finally {
            urlConnection.disconnect();
        }

    }
}
