/**
 * 
 */
package org.varunverma.spellathon.commonui;

import java.util.List;

import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameViewAttributes;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * @author Varun
 *
 */
public class CommonGameGridFragment extends Fragment implements OnItemClickListener, UIFragment {

	protected GridView gridView;
	protected GameListAdapter adapter;
	protected Callbacks activity = sDummyCallbacks;
	protected List<GameViewAttributes> gameList;
	
	public interface Callbacks {

        public void onItemSelected(int id);
    }
	
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
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// On click of List Item.
		activity.onItemSelected(gameList.get(pos).gameId);
		
	}

	@Override
	public void reloadUI() {
		gameList.clear();
		gameList.addAll(Application.get_instance().map.values());
		adapter.notifyDataSetChanged();
	}
	
}