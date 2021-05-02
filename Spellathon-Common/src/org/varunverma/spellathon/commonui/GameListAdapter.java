package org.varunverma.spellathon.commonui;

import java.util.List;

import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameViewAttributes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameListAdapter extends ArrayAdapter<GameViewAttributes> {

	private List<GameViewAttributes> gameList;
	private int layoutId;

	public GameListAdapter(Context c, int layoutId, int textId, List<GameViewAttributes> objects) {
		
		super(c, layoutId, textId, objects);
		this.layoutId = layoutId;
		gameList = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView;
		
		GameViewAttributes gv = gameList.get(position);
		
		if (convertView == null) {
			
			rowView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
			rowView.setTag(String.valueOf(gv.gameId));
			
		} else {
			
			if(convertView.getTag().toString() == String.valueOf(gv.gameId)){
				rowView = convertView;
			}
			else{
				rowView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
				rowView.setTag(String.valueOf(gv.gameId));
			}
		}
		
		TextView gameId = (TextView) rowView.findViewById(Application.constants.getIdForGameId());
		if(gameId != null){
			
			gameId.setText(String.valueOf(gv.gameId));
			
			if(gv.starRating != 0){
				ImageView rating = (ImageView) rowView.findViewById(Application.constants.getIdForRating());
				rating.setImageResource(gv.starRating);
			}
			
			if(gv.imageId != 0){
				ImageView gameStatus = (ImageView) rowView.findViewById(Application.constants.getIdForStatus());
				gameStatus.setImageResource(gv.imageId);
			}
			
		}
		
		return rowView;

	}

}