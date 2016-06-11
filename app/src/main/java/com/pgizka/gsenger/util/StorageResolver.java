package com.pgizka.gsenger.util;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.pgizka.gsenger.provider.MediaMessage;

import java.io.File;

public class StorageResolver {

    public static final String GSENGER_ROOT_PATH = Environment.getExternalStorageDirectory() + "/" + "GSenger";

    public static final String MEDIA_PATH = GSENGER_ROOT_PATH + "/" + "Media";
    public static final String IMAGES_PATH = MEDIA_PATH + "/" + "Images";
    public static final String IMAGES_SENT_PATH = IMAGES_PATH + "/" + "Sent";
    public static final String VIDEO_PATH = MEDIA_PATH + "/" + "Video";
    public static final String VIDEO_SENT_PATH = VIDEO_PATH + "/" + "Sent";
    public static final String FILE_PATH = MEDIA_PATH + "/" + "File";
    public static final String FILE_SENT_PATH = FILE_PATH + "/" + "Sent";

    private Context context;

    public StorageResolver(Context context) {
        this.context = context;
    }

    public static void makeAllDirs() {
        new File(IMAGES_PATH).mkdirs();
        new File(IMAGES_SENT_PATH).mkdirs();
        new File(VIDEO_PATH).mkdirs();
        new File(VIDEO_SENT_PATH).mkdirs();
        new File(FILE_PATH).mkdirs();
        new File(FILE_SENT_PATH).mkdirs();
    }

    public static String getPathForIncomingMediaData(MediaMessage mediaMessage) {
        int type = mediaMessage.getMediaType();
        if (type == MediaMessage.Type.PHOTO.code) {
            return IMAGES_PATH;
        } else if (type == MediaMessage.Type.VIDEO.code) {
            return VIDEO_PATH;
        } else if (type == MediaMessage.Type.FILE.code) {
            return FILE_PATH;
        } else {
            return GSENGER_ROOT_PATH;
        }
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
