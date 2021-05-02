package com.ayansh.spellathon.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import com.ayansh.CommandExecuter.Invoker;
import com.ayansh.CommandExecuter.ProgressInfo;
import com.ayansh.CommandExecuter.ResultObject;
import com.ayansh.spellathon.android.UI.TryPremiumFeatures;
import com.ayansh.spellathon.android.UI.WordOfDay;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.UI.DisplayInfo;

import java.util.Map;

public class AppGcmListenerService extends FirebaseMessagingService implements Invoker {

	private Application app;

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {

		// Be Safe. Set Context.
		app = Application.get_instance();
		app.set_context(getApplicationContext());

		String message = remoteMessage.getData().get("message");

		if(message.contentEquals("InfoMessage")){
			// Show message.
			showInfoMessage(remoteMessage.getData());
		}
		if(message.contentEquals("WordOfTheDay")){

			// Read Preferences
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			boolean word_of_day = prefs.getBoolean("word_of_day", true);

			if(word_of_day){
				// Show word of the day
				show_word_of_the_day(remoteMessage.getData());
			}

		}
		else if(message.contentEquals("DownloadGames")){

			app.downloadNewGames();

		}
		else if(message.contentEquals("SetPreference")){

			String param_name = remoteMessage.getData().get("ParameterName");
			String param_value = remoteMessage.getData().get("ParameterValue");

			try{

				int value = Integer.valueOf(param_value);
				app.savePreference(param_name,value);

			}catch(NumberFormatException e){

				app.savePreference(param_name,param_value);

			}

		}
		else if(message.contentEquals("UpdateHealthStatus")){

			app.updateHealthStatus();
		}
		else if(message.contentEquals("TryPremiumFeatures")){

			boolean is_premium_user = app.readBooleanPreference("IsPremiumUser");
			String trail_status = app.readStringPreference("TrialStatus");

			if(	trail_status.contentEquals("TrialON") ||
				trail_status.contentEquals("TrialCompleted") ||
				is_premium_user ){
				// DO Nothing
			}
			else{
				show_premium_trial_message();
			}

		}
		else{
			// Do Nothing
		}

	}

	private void show_premium_trial_message() {

		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Create Intent and Set Extras
		Intent notificationIntent = new Intent(this, TryPremiumFeatures.class);

		String content = "Try premium features for Free !";
		String title = "Try premium features for Free !";

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Notification notification = new NotificationCompat.Builder(this)
				.setContentTitle(title)
				.setContentText(content)
				.setSmallIcon(R.mipmap.icon)
				.setContentIntent(pendingIntent).build();

		notification.icon = R.mipmap.icon;
		notification.tickerText = title;
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;

		nm.notify(3, notification);

	}

	private void show_word_of_the_day(Map<String, String> data) {

		String word = data.get("word");
		String meaning = data.get("meaning");
		String alt_m1 = data.get("alt_m1");
		String alt_m2 = data.get("alt_m2");
		String example = data.get("example");

		String content = "Meaning: " + meaning;
		String title = "Today's word: " + word;

		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Create Intent and Set Extras
		Intent notificationIntent = new Intent(this, WordOfDay.class);

		notificationIntent.putExtra("Word", word);
		notificationIntent.putExtra("Meaning", meaning);
		notificationIntent.putExtra("Alt_M1", alt_m1);
		notificationIntent.putExtra("Alt_M2", alt_m2);
		notificationIntent.putExtra("Example", example);

		notificationIntent.addCategory(word);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Notification notification = new NotificationCompat.Builder(this)
				.setContentTitle(title)
				.setContentText(content)
				.setSmallIcon(R.mipmap.icon)
				.setContentIntent(pendingIntent).build();

		notification.icon = R.mipmap.icon;
		notification.tickerText = title;
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		nm.notify(2, notification);

	}

	private void showInfoMessage(Map<String,String> data) {
		// Show Info Message

		String subject = data.get("subject");
		String content = data.get("content");
		String mid = data.get("message_id");
		String message = data.get("message");

		if(mid == null || mid.contentEquals("")){
			mid = "0";
		}
		int id = Integer.valueOf(mid);

		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Create Intent and Set Extras
		Intent notificationIntent = new Intent(this, DisplayInfo.class);

		notificationIntent.putExtra("Type", 3);
		notificationIntent.putExtra("Title", "Info:");
		notificationIntent.putExtra("Subject", subject);
		notificationIntent.putExtra("Content", content);
		notificationIntent.addCategory(subject);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Notification notification = new NotificationCompat.Builder(this)
				.setContentTitle(subject)
				.setContentText(content)
				.setSmallIcon(R.mipmap.icon)
				.setContentIntent(pendingIntent).build();

		notification.icon = R.mipmap.icon;
		notification.tickerText = subject;
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		nm.notify(id, notification);
	}

	@Override
	public void NotifyCommandExecuted(ResultObject result) {
		// Nothing to do
	}

	@Override
	public void ProgressUpdate(ProgressInfo result) {
		// Nothing to do

	}
}