package com.pgizka.gsenger.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.pgizka.gsenger.gcm.commands.AddOtherUsersToChatCommand;
import com.pgizka.gsenger.gcm.commands.OwnerAddedToChatCommand;
import com.pgizka.gsenger.gcm.commands.MessageStateUpdatedCommand;
import com.pgizka.gsenger.gcm.commands.NewGroupChatCommand;
import com.pgizka.gsenger.gcm.commands.NewMediaMessageCommand;
import com.pgizka.gsenger.gcm.commands.NewTextMessageCommand;
import com.pgizka.gsenger.api.dtos.messages.ReceiverData;
import com.pgizka.gsenger.api.dtos.chats.ChatData;
import com.pgizka.gsenger.api.dtos.messages.MediaMessageData;
import com.pgizka.gsenger.api.dtos.messages.TextMessageData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GcmMessageHandler extends GcmListenerService {
    static final String TAG = GcmMessageHandler.class.getSimpleName();

    private static final Map<String, GcmCommand> MESSAGE_RECEIVERS;
    static {
        // Known messages and their GCM message receivers
        Map <String, GcmCommand> receivers = new HashMap<>();

        receivers.put(TextMessageData.ACTION, new NewTextMessageCommand());
        receivers.put(MediaMessageData.ACTION, new NewMediaMessageCommand());
        receivers.put(ReceiverData.UPDATE_RECEIVER_ACTION, new MessageStateUpdatedCommand());
        receivers.put(ChatData.NEW_GROUP_CHAT_ACTION, new NewGroupChatCommand());
        receivers.put(ChatData.ADDED_TO_CHAT_ACTION, new OwnerAddedToChatCommand());
        receivers.put(ChatData.USERS_ADDED_TO_CHAT_ACTION, new AddOtherUsersToChatCommand());

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

        GcmCommand command = MESSAGE_RECEIVERS.get(action);
        if (command == null) {
            Log.e(TAG, "Unknown command received: " + action);
        } else {
            command.execute(this, action, extraData);
        }

    }
}
