package org.varunverma.spellathon.commonui;

import java.util.List;

import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.Word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GameSolutionAdapter extends ArrayAdapter<Word> {

	private List<Word> wordList;
	private int layoutId;

	public GameSolutionAdapter(Context c, int layoutId, int textId, List<Word> objects) {
		
		super(c, textId, objects);
		this.layoutId = layoutId;
		wordList = objects;
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
		
		return rowView;

	}

}