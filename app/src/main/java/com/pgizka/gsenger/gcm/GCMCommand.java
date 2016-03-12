package com.pgizka.gsenger.gcm;

import android.content.Context;

public abstract class GCMCommand {
    public abstract void execute(Context context, String action, String extraData);
}
