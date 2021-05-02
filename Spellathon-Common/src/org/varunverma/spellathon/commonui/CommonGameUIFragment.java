package org.varunverma.spellathon.commonui;

import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.GameForPlay;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public abstract class CommonGameUIFragment extends Fragment implements OnClickListener {

	protected GameForPlay game;
	protected EditText Word;
	protected ImageButton add;
	protected TextView WordList;
	protected Callbacks activity = sDummyCallbacks;
	
	public interface Callbacks {
		public void showSolution();
		public void addWord(String word);
	}
	
	protected static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void showSolution() {}

		@Override
		public void addWord(String word) {}
    };
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		int gameId = 1;
		if(getArguments() != null){
			if (getArguments().containsKey("GameId")) {
				gameId = getArguments().getInt("GameId");
	        }
		}
		
		// Build the Game Object.
		try {
			game = new GameForPlay(gameId);
			Application.get_instance().game = game;
		} catch (Exception e) {
			// Log Error !
			Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e(Application.constants.getLoggerTag(), e.getMessage(), e);
		}
		
		// This will enable this fragment to add menu
		setHasOptionsMenu(true);

	}
	
	
	protected void checkGame() {
		// Check Game
		GameForPlay game = Application.get_instance().game;

		// Check my Score and Get Rating.
		game.GetMyRating();

		activity.showSolution();
	}

	protected void addWord(String word) {
		// Add word to the View.
		if(WordList != null){
			WordList.append(word + "; ");
		}
		else{
			activity.addWord(word);
		}
		
	}
	

	@Override
	public void onDestroy(){
		// Save Game before Exiting !
		if(game != null){
			game.saveCurrentGameState();
		}
		super.onDestroy();
	}
	
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

}