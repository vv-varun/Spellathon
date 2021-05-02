/**
 * 
 */
package com.ayansh.spellathon.android.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.Application.Game.Word;
import com.ayansh.spellathon.android.R;
import com.ayansh.spellathon.android.commonui.CommonGameSolutionFragment;
import com.ayansh.spellathon.android.commonui.GameSolutionAdapter;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Varun
 *
 */
public class GameSolutionFragment extends CommonGameSolutionFragment {
	
	private TextView upgradeView;

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
		upgradeView = (TextView) view.findViewById(R.id.upgrade_view);

		if(game.getStatus() != 20){
			switchButton.setVisibility(View.GONE);
			upgradeView.setVisibility(View.GONE);
		}
		else{
			
			switchButton.setVisibility(View.VISIBLE);
			switchButton.setOnCheckedChangeListener(this);
						
			if(Application.constants.isPremiumVersion()){
				upgradeView.setVisibility(View.GONE);
			}
			else{
				upgradeView.setVisibility(View.VISIBLE);
			}
			
		}
		
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.game_solution_menu, menu);

	}
	
	private void showSolution() {

		/*
		//super.showSolution();
		
		if(Application.constants.isPremiumVersion()){
			upgradeView.setVisibility(View.GONE);
		}
		else{
			upgradeView.setVisibility(View.VISIBLE);
			upgrade.setOnClickListener(this);
		}
		*/
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		
		case R.id.upgrade:
			
			Intent intent = new Intent(getActivity(), ActivatePremiumFeatures.class);
			startActivity(intent);
			break;
		
		}

		return true;
	}

}