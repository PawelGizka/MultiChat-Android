package com.pgizka.gsenger.provider.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pgizka.gsenger.provider.ContentValueUtils;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.pojos.CommonType;

public class CommonTypeRepository {

    private Context context;
    private ProviderUtils providerUtils;

    public CommonTypeRepository(Context context, ProviderUtils providerUtils) {
        this.context = context;
        this.providerUtils = providerUtils;
    }

    public int insertCommonType(CommonType commonType) {
        Uri uri = providerUtils.insertCommonType(commonType);
        int id = Integer.parseInt(uri.getLastPathSegment());
        commonType.setId(id);
        return id;
    }

    public void updateCommonType(CommonType commonType) {
        String id = String.valueOf(commonType.getId());
        Uri uri = GSengerContract.CommonTypes.buildCommonTypeUri(id);
        ContentValues contentValues = ContentValueUtils.createCommonType(commonType);
        context.getContentResolver().update(uri, contentValues, null, null);
    }

    public CommonType buildCommonType(CommonType commonType, Cursor cursor) {
        commonType.setId(cursor.getInt(cursor.getColumnIndex(GSengerContract.CommonTypes._ID)));
        commonType.setServerId(cursor.getInt(cursor.getColumnIndex(GSengerContract.CommonTypes.COMMON_TYPE_SERVER_ID)));
        commonType.setChatId(cursor.getInt(cursor.getColumnIndex(GSengerContract.CommonTypes.CHAT_ID)));
        commonType.setSendDate(cursor.getLong(cursor.getColumnIndex(GSengerContract.CommonTypes.SEND_DATE)));
        commonType.setSenderId(cursor.getInt(cursor.getColumnIndex(GSengerContract.CommonTypes.SENDER_ID)));
        commonType.setState(cursor.getInt(cursor.getColumnIndex(GSengerContract.CommonTypes.STATE)));
        commonType.setType(cursor.getString(cursor.getColumnIndex(GSengerContract.CommonTypes.TYPE)));
        return commonType;
    }

}
