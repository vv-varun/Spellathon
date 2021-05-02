package com.ayansh.spellathon.android.Application.Game;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;

import com.ayansh.CommandExecuter.Command;
import com.ayansh.CommandExecuter.Invoker;
import com.ayansh.CommandExecuter.ProgressInfo;
import com.ayansh.CommandExecuter.ResultObject;

import com.ayansh.spellathon.android.Application.Application;

import java.io.InputStream;
import java.io.InputStreamReader;

public class LoadGamesFromFileCommand extends Command {

	public LoadGamesFromFileCommand(Invoker caller) {
		super(caller);
	}

	@Override
	protected void execute(ResultObject result) throws Exception {

		GameManager game_manager = GameManager.getInstance();

		Context c = Application.get_instance().get_context();
		AssetManager assetManager = c.getAssets();

		Log.v(Application.TAG, "Loading default games from file");
		InputStream is = assetManager.open("default_data.xml");
		InputStreamReader isr;
		XMLParser xml_parser = new XMLParser();

		// Get Input Stream Reader.
		isr = new InputStreamReader(is);

		// Parse the input XML.
		Log.v(Application.TAG, "Parsing the games data");
		Xml.parse(isr, xml_parser);
		game_manager.gameForDownloads = xml_parser.Games;

		// Save to DB.
		boolean success = game_manager.SaveGames();

		if (success) {
			result.setResultCode(200);
			ProgressInfo pi = new ProgressInfo("Show UI");
			publishProgress(pi);
		}
	}
}