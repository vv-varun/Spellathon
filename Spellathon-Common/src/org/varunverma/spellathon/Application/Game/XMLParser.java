package org.varunverma.spellathon.Application.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.varunverma.spellathon.Application.Application;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler {
	
	List<GameForDownload> Games;
	private GameForDownload Game;
	private Application application;
	
	public XMLParser(){
		
		Games = new ArrayList<GameForDownload>();
		application = Application.get_instance();
		
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes){
		
		if(localName.equals("Game")){
			// A new Game Starts here.
			Game = new GameForDownload();
		}
		
		else if(localName.equals("GameInfo")){
			// Game Master Data.
			Game.id = Integer.parseInt(attributes.getValue("ID"));
			Game.level = attributes.getValue("Level");
			Game.alphabets = attributes.getValue("Alphabets").toUpperCase(Locale.ENGLISH);
			Game.ExpectedScore = attributes.getValue("ExpectedScore");
			Game.average_rating = Integer.parseInt(attributes.getValue("AverageUserRating"));
			Game.high_score = Integer.parseInt(attributes.getValue("HighScore"));
			Game.average_score = Integer.parseInt(attributes.getValue("AverageScore"));
		}
		
		else if(localName.equals("GameImageURL")){
			// Get the Image URL.
			if(application.ScreenSize == 1){
				// Screen size is low.
				Game.image_url = attributes.getValue("small");
			}
			else if(application.ScreenSize == 2){
				// Screen size is medium.
				Game.image_url = attributes.getValue("med");
			}
			else{
				// Screen size is Large !
				Game.image_url = attributes.getValue("large");
			}
		}
		
		else if(localName.equals("Word")){
			// Game Result Words.
			Word word = new Word();
			word.id = Game.id;
			word.word = attributes.getValue("word");
			word.meaning1 = attributes.getValue("m1");
			word.meaning2 = attributes.getValue("m2");
			word.meaning3 = attributes.getValue("m3");
			Game.Words.add(word);
		}
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{

		if(localName.equals("Game")){
			Games.add(Game);
		}
		else if (localName.equals("GameResult")){
			// Nothing to do as of now.
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length){
		// Nothing to do :)
	}

}