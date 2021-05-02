package com.ayansh.spellathon.android.UI;

import java.util.Iterator;

import com.ayansh.spellathon.android.Application.Game.Word;
import com.ayansh.spellathon.android.R;
import com.ayansh.spellathon.android.commonui.CommonGameUIFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GameUIFragment extends CommonGameUIFragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.gameui_new, container, false);
		
		if(game == null){
			return view;
		}

		ca = (TextView) view.findViewById(R.id.centre_alphabet);
		a1 = (TextView) view.findViewById(R.id.alphabet_one);
		a2 = (TextView) view.findViewById(R.id.alphabet_two);
		a3 = (TextView) view.findViewById(R.id.alphabet_three);
		a4 = (TextView) view.findViewById(R.id.alphabet_four);
		a5 = (TextView) view.findViewById(R.id.alphabet_five);
		a6 = (TextView) view.findViewById(R.id.alphabet_six);

		String[] alphabets = game.getGameAlphabets();
		ca.setText(alphabets[0]);
		a1.setText(alphabets[1]);
		a2.setText(alphabets[2]);
		a3.setText(alphabets[3]);
		a4.setText(alphabets[4]);
		a5.setText(alphabets[5]);
		a6.setText(alphabets[6]);
		
		Word = (TextView) view.findViewById(R.id.word);
		add = (ImageButton) view.findViewById(R.id.add);
		clear = (ImageButton) view.findViewById(R.id.clear);
		WordList = (TextView) view.findViewById(R.id.WordList);
		TextView expectedScore = (TextView) view.findViewById(R.id.expected_score);

		enableAlphabets();

		// Add Event Listeners.
		Word.setOnClickListener(this);
		add.setOnClickListener(this);
		clear.setOnClickListener(this);

		ca.setOnClickListener(this);
		a1.setOnClickListener(this);
		a2.setOnClickListener(this);
		a3.setOnClickListener(this);
		a4.setOnClickListener(this);
		a5.setOnClickListener(this);
		a6.setOnClickListener(this);
		
		//Build Initial text
		expectedScore.setText(game.ExpectedScore);
		
		// Load my Word List.
		Iterator<Word> iterator = game.MyWords.listIterator();
		while (iterator.hasNext()) {
			addWord(iterator.next().word);
		}

		return view;
	}

	private void enableAlphabets() {

		ca.setClickable(true);
		a1.setClickable(true);
		a2.setClickable(true);
		a3.setClickable(true);
		a4.setClickable(true);
		a5.setClickable(true);
		a6.setClickable(true);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.game_ui_menu, menu);
        
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		
		case R.id.Check:
			// Confirmation from the user.
			String question = "Are you sure you want to see the solution? " +
					"This means you have completed the game.";
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(question)
					.setTitle("Confirm Action")
					.setCancelable(false)
					.setIcon(R.mipmap.alert)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							checkGame();
						}
			    	   
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			
			AlertDialog alert = builder.create();
			alert.setIcon(R.mipmap.alert);
			alert.show();

			return true;
		
		}
		
		return false;
	}
	
	@Override
	public void onClick(View view) {
		// On Click of a View
		switch(view.getId()){

			case R.id.centre_alphabet:
			case R.id.alphabet_one:
			case R.id.alphabet_two:
			case R.id.alphabet_three:
			case R.id.alphabet_four:
			case R.id.alphabet_five:
			case R.id.alphabet_six:

				String alphabet = ((TextView) view).getText().toString();
				Word.append(alphabet.toLowerCase());
				view.setClickable(false);
				break;

			case R.id.add:
				String word = Word.getText().toString();

				if(word.contentEquals("")){	}
				else{
					// Add the word
					try {
						game.AddWord(word);
						addWord(word);
						Word.setText("");
						enableAlphabets();
					} catch (Exception e) {
						Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}

				break;

			case R.id.clear:
				Word.setText("");
				enableAlphabets();
				break;

			case R.id.word:
				showHelp();
				break;

			default:
				break;
		}		
	}

	private void showHelp() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Click on alphabets to type...")
				.setTitle("Note:")
				.setCancelable(true)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.setIcon(R.mipmap.alert);

		AlertDialog alert = builder.create();
		alert.setIcon(R.mipmap.alert);
		alert.show();

	}

}