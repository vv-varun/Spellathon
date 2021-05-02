package org.varunverma.spellathon.ui;

import java.util.Iterator;

import org.varunverma.spellathon.R;
import org.varunverma.spellathon.Application.Application;
import org.varunverma.spellathon.Application.Game.Word;
import org.varunverma.spellathon.commonui.CommonGameUIFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameUIFragment extends CommonGameUIFragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.gameui, container, false);
		
		if(game == null){
			return view;
		}
		
		ImageView gameImage = (ImageView) view.findViewById(R.id.game_image);
		
		if(game.Image.exists()){
			//image.setImageURI(uri);
			gameImage.setImageURI(Uri.parse(game.Image.toString()));
		}
		else{
			//Log Error !
			String error = "The game image does not exist!";
			Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
			Log.e(Application.constants.getLoggerTag(), error);
		}
		
		Word = (EditText) view.findViewById(R.id.word);
		add = (ImageButton) view.findViewById(R.id.add);
		WordList = (TextView) view.findViewById(R.id.WordList);
		TextView expectedScore = (TextView) view.findViewById(R.id.expected_score);
		
		// Add Event Listeners.
		add.setOnClickListener(this);
		
		//Build Initial text
		expectedScore.setText(game.ExpectedScore);
		
		// Load my Word List.
		Iterator<Word> iterator = game.MyWords.listIterator();
		while (iterator.hasNext()) {
			addWord(iterator.next().word);
		}

		return view;
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
					.setIcon(R.drawable.alert)
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
			alert.setIcon(R.drawable.alert);
			alert.show();

			return true;
			
		case R.id.example:
			Intent i = new Intent(getActivity(), Example.class);
			getActivity().startActivity(i);
			return true;
		
		}
		
		return false;
	}
	
	@Override
	public void onClick(View view) {
		// On Click of a View
		switch(view.getId()){
		
		case R.id.add:
			String word = Word.getText().toString();
			
			if(word.contentEquals("")){	}
			else{
				// Add the word
				try {
					game.AddWord(word);
					addWord(word);
					Word.setText("");
				} catch (Exception e) {
					Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
			
			break;
						
		default:
			break;
		}		
	}

}