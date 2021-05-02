/**
 * 
 */
package org.varunverma.spellathon.commonui;

import java.util.Iterator;

import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameForPlay;
import org.varunverma.spellathon.Application.Game.Word;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * @author Varun
 *
 */
public class CommonGameSolutionFragment extends Fragment implements OnCheckedChangeListener {

	protected GameForPlay game;
	protected GameSolutionAdapter adapter;
	protected CompoundButton switchButton;
	protected TextView solHeading;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		game = Application.get_instance().game;
	}
	
	public void showSolution() {
		switchButton.setVisibility(View.VISIBLE);
		solHeading.setVisibility(View.GONE);
		switchButton.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		Iterator<Word> i;

		if(isChecked){
			// Show Solution
			i = game.SortedWordList.listIterator();
		}
		else{
			// Show my list
			i = game.MyWords.listIterator();
		}

		adapter.clear();
		while(i.hasNext()){
			adapter.add(i.next());
		}
		
		adapter.notifyDataSetChanged();
	}

	public void addWord(String word) {
		if(switchButton != null){
			if(!switchButton.isChecked()){
				adapter.add(new Word(word));
			}
		}
	}
	
}