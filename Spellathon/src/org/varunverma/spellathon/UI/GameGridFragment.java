/**
 * 
 */
package org.varunverma.spellathon.ui;

import java.util.ArrayList;

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameViewAttributes;
import org.varunverma.spellathon.commonui.CommonGameGridFragment;
import org.varunverma.spellathon.commonui.GameListAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * @author Varun
 *
 */
public class GameGridFragment extends CommonGameGridFragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.game_grid, container, false);
		
		Context c = getActivity().getApplicationContext();
		int id = R.id.game_id;
		gameList = new ArrayList<GameViewAttributes>();
		gameList.addAll(Application.get_instance().map.values());
		adapter = new GameListAdapter(c, R.layout.game_grid_row, id, gameList);
		
		gridView = (GridView) view.findViewById(R.id.gridview);
		//gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		
		return view;
	}
	
}