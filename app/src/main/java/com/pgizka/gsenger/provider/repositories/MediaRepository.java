package com.pgizka.gsenger.provider.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pgizka.gsenger.provider.ContentValueUtils;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.pojos.Media;
import com.pgizka.gsenger.provider.pojos.Message;

public class MediaRepository {

    private Context context;
    private ProviderUtils providerUtils;
    private CommonTypeRepository commonTypeRepository;

    public MediaRepository(Context context, ProviderUtils providerUtils, CommonTypeRepository commonTypeRepository) {
        this.context = context;
        this.providerUtils = providerUtils;
        this.commonTypeRepository = commonTypeRepository;
    }

    public int insertMedia(Media media) {
        commonTypeRepository.insertCommonType(media);
        providerUtils.insertMedia(media);
        return media.getId();
    }

    public void updateMedia(Media media) {
        commonTypeRepository.updateCommonType(media);

        String id = String.valueOf(media.getId());
        Uri uri = GSengerContract.Medias.buildMediaUri(id);
        ContentValues contentValues = ContentValueUtils.createMedia(media);
        context.getContentResolver().update(uri, contentValues, null, null);
    }

    public Media getMediaById(int id) {
        Uri uri = GSengerContract.Medias.buildMediaUri(String.valueOf(id));
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        Media media = null;
        if (cursor.moveToFirst()) {
            media = buildMedia(cursor);
        }

        return media;
    }

    public Media buildMedia(Cursor cursor) {
        Media media = new Media();
        commonTypeRepository.buildCommonType(media, cursor);

        try {
            media.setMediaType(cursor.getInt(cursor.getColumnIndex(GSengerContract.Medias.TYPE)));
            media.setDescription(cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.DESCRIPTION)));
            media.setFileName(cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.FILE_NAME)));
            media.setPath(cursor.getString(cursor.getColumnIndex(GSengerContract.Medias.PATH)));
        } catch (Exception e) {
            media = null;
        }

        return media;
    }

}
