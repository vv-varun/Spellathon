/**
 * 
 */
package org.varunverma.spellathon.ui;

import java.util.ArrayList;
import java.util.Iterator;

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.Application.Game.Word;
import org.varunverma.spellathon.commonui.CommonGameSolutionFragment;
import org.varunverma.spellathon.commonui.GameSolutionAdapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Varun
 *
 */
public class GameSolutionFragment extends CommonGameSolutionFragment {
	
	@SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.game_solution, container, false);
		
		ArrayList<Word> list = new ArrayList<Word>();
		
		adapter = new GameSolutionAdapter(getActivity(), R.layout.game_solution_row, R.id.word, list);
		
		ListView listView = (ListView) view.findViewById(android.R.id.list);
		listView.setAdapter(adapter);
		
		Iterator<Word> i = game.MyWords.listIterator();
		while(i.hasNext()){
			adapter.add(i.next());
		}
		
		switchButton = (CompoundButton) view.findViewById(R.id.togglebutton);
		solHeading = (TextView) view.findViewById(R.id.solution_heading);
		
		/*
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			Switch sb = (Switch) switchButton;
			String t = (String) sb.getTextOn();
			t = (String) sb.getTextOff();
			sb.setTextOn("Solution");
			sb.setTextOff("My Words");
		}
		*/
		
		if(game.getStatus() != 20){
			switchButton.setVisibility(View.GONE);
			solHeading.setVisibility(View.VISIBLE);
		}
		else{
			switchButton.setVisibility(View.VISIBLE);
			solHeading.setVisibility(View.GONE);
			switchButton.setOnCheckedChangeListener(this);
		}

		return view;
	}

}