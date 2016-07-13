package com.pgizka.gsenger.gcm;

import android.content.Context;

public interface GcmCommand {

    void execute(Context context, String action, String extraData);

}
