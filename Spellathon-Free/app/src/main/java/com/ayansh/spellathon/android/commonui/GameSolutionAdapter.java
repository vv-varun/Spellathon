package com.ayansh.spellathon.android.commonui;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.Application.Game.Word;

public class GameSolutionAdapter extends ArrayAdapter<Word> {

	private List<Word> wordList;
	private int layoutId;
	private boolean premium_trial_on;

	public GameSolutionAdapter(Context c, int layoutId, int textId, List<Word> objects) {
		
		super(c, textId, objects);
		this.layoutId = layoutId;
		wordList = objects;

		String trail_status = Application.get_instance().readStringPreference("TrialStatus");
		if(trail_status.contentEquals("TrialON")){
			premium_trial_on = true;
		}
		else{
			premium_trial_on = false;
		}

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView;

		if (convertView == null) {
			rowView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
		} else {
			rowView = convertView;
		}
		
		TextView word = (TextView) rowView.findViewById(Application.constants.getIdForWord());
		TextView meaning = (TextView) rowView.findViewById(Application.constants.getIdForMeaning());
		
		Word w = wordList.get(position);
		
		word.setText(w.word);
		meaning.setText(w.meaning1);
		
		if(!Application.constants.isPremiumVersion()){
			meaning.setVisibility(View.GONE);
		}

		if(premium_trial_on){
			meaning.setVisibility(View.VISIBLE);
		}

		return rowView;

	}

}