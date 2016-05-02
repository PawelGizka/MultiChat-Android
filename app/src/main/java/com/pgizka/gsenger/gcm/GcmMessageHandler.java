package com.pgizka.gsenger.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.pgizka.gsenger.gcm.commands.MessageStateChangedCommand;
import com.pgizka.gsenger.gcm.commands.NewGroupChatCommand;
import com.pgizka.gsenger.gcm.commands.NewMediaMessageCommand;
import com.pgizka.gsenger.gcm.commands.NewTextMessageCommand;
import com.pgizka.gsenger.gcm.data.MessageStateChangedData;
import com.pgizka.gsenger.gcm.data.NewChatData;
import com.pgizka.gsenger.gcm.data.NewMediaMessageData;
import com.pgizka.gsenger.gcm.data.NewTextMessageData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GcmMessageHandler extends GcmListenerService {
    static final String TAG = GcmMessageHandler.class.getSimpleName();

    private static final Map<String, GCMCommand> MESSAGE_RECEIVERS;
    static {
        // Known messages and their GCM message receivers
        Map <String, GCMCommand> receivers = new HashMap<>();
        MessageStateChangedCommand messageStateChangedCommand = new MessageStateChangedCommand();

        receivers.put(NewTextMessageData.ACTION, new NewTextMessageCommand());
        receivers.put(NewMediaMessageData.ACTION, new NewMediaMessageCommand());
        receivers.put(MessageStateChangedData.MESSAGE_DELIVERED_ACTION, messageStateChangedCommand);
        receivers.put(MessageStateChangedData.MESSAGE_VIEWED_ACTION, messageStateChangedCommand);
        receivers.put(NewChatData.ACTION, new NewGroupChatCommand());

        MESSAGE_RECEIVERS = Collections.unmodifiableMap(receivers);
    }

    @Override
    public synchronized void onMessageReceived(String from, Bundle data) {
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
