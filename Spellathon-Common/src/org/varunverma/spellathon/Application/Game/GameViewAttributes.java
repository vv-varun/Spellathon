package org.varunverma.spellathon.Application.Game;
/*
 * This class contains the View Attributes of a GAME.
 * It is used to refresh Icons/Ratings etc on UI.
 */
import org.varunverma.spellathon.Application.Application;

public class GameViewAttributes {
	
	public int gameId;
	public int imageId;
	public int starRating;
	
	public GameViewAttributes(int gameId) throws Exception{
		
		// Load Game.
		GameForPlay game = new GameForPlay(gameId);
		// Set up View.
		setUp(game);	
		
	}
	
	public GameViewAttributes(GameForPlay game){
		// Set up Game View Attributes.
		setUp(game);
	}

	private void setUp(GameForPlay game) {
		// Set up the Game View Attributes
		gameId = game.id;
		
		switch(game.Result){
		
		case 0:
			starRating = Application.constants.getZeroStarDrawable();
			break;
		
		case 1:
			starRating = Application.constants.getOneStarDrawable();
			break;
			
		case 2:
			starRating = Application.constants.getTwoStarDrawable();
			break;
			
		case 3:
			starRating = Application.constants.getThreeStarDrawable();
			break;
			
		default:
			starRating = 0;
			break;
		
		}
		
		switch(game.status){
		
		case 15:
			imageId = Application.constants.getPauseDrawable();
			starRating = 0;
			break;
			
		case 20:
			imageId = Application.constants.getCompletedDrawable();
			break;
		
		default:
			imageId = starRating = 0;
			break;
		
		}
	}
	
}