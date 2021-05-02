package com.ayansh.spellathon.android;


import android.util.Log;

import com.ayansh.CommandExecuter.CommandExecuter;
import com.ayansh.CommandExecuter.Invoker;
import com.ayansh.CommandExecuter.ProgressInfo;
import com.ayansh.CommandExecuter.ResultObject;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.ayansh.spellathon.android.Application.Application;
import com.ayansh.spellathon.android.Application.SaveRegIdCommand;

public class AppInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        // Be Safe. Set Context.
        Application app = Application.get_instance();
        app.set_context(getApplicationContext());

        app.savePreference("RegistrationStatus","");
        app.savePreference("RegistrationId","");

        try {

            FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();

            if(app.readStringPreference("InstanceID").contentEquals("")){
                // If we don't have, then get Instance ID and save.
                String iid = instanceID.getId();
                app.savePreference("InstanceID", iid);
            }

            String token = instanceID.getToken();
            // [END get_token]

            // Implement this method to send any registration to your app's servers.
            Log.v(Application.TAG, "Registration with FCM success");
            app.savePreference("RegistrationId", token);

            CommandExecuter ce = new CommandExecuter();

            SaveRegIdCommand command = new SaveRegIdCommand(new Invoker(){

                @Override
                public void NotifyCommandExecuted(ResultObject arg0) {
                    // Nothing to do.
                    // Save will happen in the command itself.
                }

                @Override
                public void ProgressUpdate(ProgressInfo arg0) {
                    // Nothing to do.
                }}, token);

            //ce.execute(command);
            command.execute();

        } catch (Exception ex) {

        }
    }

}