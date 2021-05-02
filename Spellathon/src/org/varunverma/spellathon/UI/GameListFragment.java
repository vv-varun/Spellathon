/**
 * 
 */
package org.varunverma.spellathon.ui;

import java.util.ArrayList;

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameViewAttributes;
import org.varunverma.spellathon.commonui.CommonGameListFragment;
import org.varunverma.spellathon.commonui.GameListAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author Varun
 *
 */
public class GameListFragment extends CommonGameListFragment{

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.game_list, container, false);
		listView = (ListView) view.findViewById(android.R.id.list);
    	listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
		
		Context c = getActivity().getApplicationContext();
		
		gameList = new ArrayList<GameViewAttributes>();
		gameList.addAll(Application.get_instance().map.values());
		adapter = new GameListAdapter(c, R.layout.game_list_row, R.id.game_id, gameList);
		
    	listView.setAdapter(adapter);
		
	}
		
}