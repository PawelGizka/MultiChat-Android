package com.pgizka.gsenger.util;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyFileTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Uri source;
    private String fileName;
    private String path;
    private CopyFinishedListener copyFinishedListener;

    public CopyFileTask(Context context) {
        this.context = context;
    }

    public CopyFileTask from(Uri source) {
        this.source = source;
        return this;
    }

    public  CopyFileTask to(String destinationPath) {
        this.path = destinationPath;
        return this;
    }

    public CopyFileTask newFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            if (fileName == null) {
                String path = StorageResolver.getRealPathFromUri(source, context);
                File file = new File(path);
                fileName = file.getName();
            }

            InputStream inputStream = context.getContentResolver().openInputStream(source);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path, fileName));

            byte[] buffer = new byte[8192];
            int cnt = 0;
            while ((cnt = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, cnt);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (copyFinishedListener != null) {
            copyFinishedListener.onCopyFinished(fileName, path);
        }

    }

    public interface CopyFinishedListener {
        void onCopyFinished(String newFileName, String path);
    }

    public CopyFileTask onCopyingFinished(CopyFinishedListener copyFinishedListener) {
        this.copyFinishedListener = copyFinishedListener;
        return this;
    }
}
