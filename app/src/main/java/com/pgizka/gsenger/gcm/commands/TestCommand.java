package com.pgizka.gsenger.gcm.commands;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgizka.gsenger.gcm.GCMCommand;
import com.pgizka.gsenger.gcm.GcmTest;

import java.io.IOException;

public class TestCommand extends GCMCommand {


    @Override
    public void execute(final Context context, String action, String extraData) {
        Gson gson = new Gson();
        GcmTest gcmTest = null;
        try {
            gcmTest = gson.getAdapter(GcmTest.class).fromJson(extraData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Handler h = new Handler(Looper.getMainLooper());
        final GcmTest finalGcmTest = gcmTest;
        h.post(new Runnable() {
            public void run() {
                Toast.makeText(context, "Message from: " + finalGcmTest.getFrom() + " message :" + finalGcmTest.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
