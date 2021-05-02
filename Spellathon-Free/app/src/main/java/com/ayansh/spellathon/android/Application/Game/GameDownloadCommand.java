package com.ayansh.spellathon.android.Application.Game;

import com.ayansh.CommandExecuter.Command;
import com.ayansh.CommandExecuter.Invoker;
import com.ayansh.CommandExecuter.ResultObject;

import com.ayansh.spellathon.android.Application.Application;

import java.util.Date;


public class GameDownloadCommand extends Command {

	private int latest_game_id;

	public GameDownloadCommand(int game_id) {

		super(Command.DUMMY_CALLER);
		latest_game_id = game_id;
	}

	@Override
	protected void execute(ResultObject result) throws Exception {
		// Execute.
		GameManager game_manager = GameManager.getInstance();

		// Download Games.
		int to = latest_game_id + 50;
		game_manager.DownloadGames(latest_game_id, to);

		// Result code.
		result.setResultCode(0);

	}
}