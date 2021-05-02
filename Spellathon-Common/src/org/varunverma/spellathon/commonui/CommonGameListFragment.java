/**
 * 
 */
package org.varunverma.spellathon.commonui;

import java.util.List;

import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameViewAttributes;
import org.varunverma.spellathon.commonui.CommonGameGridFragment.Callbacks;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

/**
 * @author Varun
 *
 */
public class CommonGameListFragment extends ListFragment implements UIFragment{

	protected ListView listView;
	protected GameListAdapter adapter;
	protected Callbacks activity = sDummyCallbacks;
	protected List<GameViewAttributes> gameList;
	
	protected static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int id) {}
    };
	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        this.activity = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = sDummyCallbacks;
    }

	@Override
	public void onListItemClick(ListView lv, View view, int pos, long id) {
		// On click of List Item.
		super.onListItemClick(lv, view, pos, id);
		activity.onItemSelected(gameList.get(pos).gameId);
		
	}

	@Override
	public void reloadUI() {
		gameList.clear();
		gameList.addAll(Application.get_instance().map.values());
		adapter.notifyDataSetChanged();
	}
	
}