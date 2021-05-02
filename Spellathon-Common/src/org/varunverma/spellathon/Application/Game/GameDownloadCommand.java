package org.varunverma.spellathon.Application.Game;

import java.util.Date;

import org.varunverma.CommandExecuter.Command;
import org.varunverma.CommandExecuter.Invoker;
import org.varunverma.CommandExecuter.ResultObject;
import org.varunverma.spellathon.Application.Application;

public class GameDownloadCommand extends Command {

	private int from, to;
	private boolean firstRun;

	public GameDownloadCommand(Invoker caller, int from, int to, boolean firstRun) {
		super(caller);
		this.from = from;
		this.to = to;
		this.firstRun = firstRun;
	}

	@Override
	protected void execute(ResultObject result) throws Exception {
		// Execute.
		Application application = Application.get_instance();
		GameManager game_manager;
		
		if (!Application.constants.isPremiumVersion()) {
			// If this is not a premium version, check download limit.
			if (application.lastDateDownloaded > 0) {
				// This is to ensure that this is not first time download.
				Date last = new Date(application.lastDateDownloaded);
				Date now = new Date();

				int d1 = last.getDate();
				int d2 = now.getDate();
				int m1 = last.getMonth();
				int m2 = now.getMonth();
				int y1 = last.getYear();
				int y2 = now.getYear();
				if (d1 == d2 && m1 == m2 && y1 == y2 && application.lastGameDownloaded >= 20) {
					// Checking if the user has already downloaded today.
					result.setResultCode(-1);
					throw new Exception("Your today's limit of puzzle download is over.");
				}
			}
		}

		// Create Game Manager.
		game_manager = new GameManager();

		// Download Games.
		game_manager.DownloadGames(from, to, this);
		
		// Result code.
		result.setResultCode(0);
		
		// Set Last Download Date.
		application.setLastDownloadDate();

	}
	
	public boolean isFirstRun(){
		return firstRun;
	}

}