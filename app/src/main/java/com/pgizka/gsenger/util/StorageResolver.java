package com.pgizka.gsenger.util;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StorageResolver {

    public static final String GSENGER_ROOT_PATH = Environment.getExternalStorageDirectory() + "/" + "GSenger";

    public static final String MEDIA_PATH = GSENGER_ROOT_PATH + "/" + "Media";
    public static final String IMAGES_PATH = MEDIA_PATH + "/" + "Images";
    public static final String IMAGES_SENT_PATH = IMAGES_PATH + "/" + "Sent";

    private Context context;

    public StorageResolver(Context context) {
        this.context = context;
    }

    public void makeAllDirs() {
        File imagesDir = new File(IMAGES_PATH);
        imagesDir.mkdirs();

        File sentImagesDir = new File(IMAGES_SENT_PATH);
        sentImagesDir.mkdirs();
    }

    public static String getRealPathFromUri(Uri uri, Context context) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

        if(cursor == null){
            return uri.getPath();
        }

        int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        if(column_index == -1){
            return uri.getPath();
        }
        cursor.moveToFirst();
        String path = cursor.getString(column_index);

        if (path == null) {
            path = uri.getPath();
        }

        return path;
    }

}
