package com.pgizka.gsenger.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.pgizka.gsenger.gcm.commands.NewTextMessageCommand;
import com.pgizka.gsenger.gcm.commands.TestCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GcmMessageHandler extends GcmListenerService {
    static final String TAG = GcmMessageHandler.class.getSimpleName();

    private static final Map<String, GCMCommand> MESSAGE_RECEIVERS;
    static {
        // Known messages and their GCM message receivers
        Map <String, GCMCommand> receivers = new HashMap<String, GCMCommand>();
        receivers.put("test", new TestCommand());
        receivers.put("NEW_TEXT_MESSAGE_ACTION", new NewTextMessageCommand());
        MESSAGE_RECEIVERS = Collections.unmodifiableMap(receivers);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String action = data.getString("action");
        String extraData = data.getString("extraData");

        Log.i(TAG, "Got GCM message, action=" + action + ", extraData=" + extraData);

        if (action == null) {
            Log.i(TAG, "Message received without command action");
            return;
        }

        GCMCommand command = MESSAGE_RECEIVERS.get(action);
        if (command == null) {
            Log.e(TAG, "Unknown command received: " + action);
        } else {
            command.execute(this, action, extraData);
        }

    }
}
