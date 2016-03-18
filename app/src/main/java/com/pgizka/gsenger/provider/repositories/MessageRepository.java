package com.pgizka.gsenger.provider.repositories;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pgizka.gsenger.provider.ContentValueUtils;
import com.pgizka.gsenger.provider.GSengerContract;
import com.pgizka.gsenger.provider.ProviderUtils;
import com.pgizka.gsenger.provider.pojos.Friend;
import com.pgizka.gsenger.provider.pojos.Message;

public class MessageRepository {

    private Context context;
    private ProviderUtils providerUtils;
    private CommonTypeRepository commonTypeRepository;

    public MessageRepository(Context context, ProviderUtils providerUtils, CommonTypeRepository commonTypeRepository) {
        this.context = context;
        this.providerUtils = providerUtils;
        this.commonTypeRepository = commonTypeRepository;
    }

    public int insertMessage(Message message) {
        commonTypeRepository.insertCommonType(message);
        providerUtils.insertMessage(message);
        return message.getId();
    }

    public void updateMessage(Message message) {
        commonTypeRepository.updateCommonType(message);

        String id = String.valueOf(message.getId());
        Uri uri = GSengerContract.Messages.buildMessageUri(id);
        ContentValues contentValues = ContentValueUtils.createMessage(message);
        context.getContentResolver().update(uri, contentValues, null, null);
    }

    public Message getMessageById(int id) {
        Uri uri = GSengerContract.Messages.buildMessageUri(String.valueOf(id));
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        Message message = null;
        if (cursor.moveToFirst()) {
            message = buildMessage(cursor);
        }

        return message;
    }

    public Message buildMessage(Cursor cursor) {
        Message message = new Message();
        commonTypeRepository.buildCommonType(message, cursor);
        message.setText(cursor.getString(cursor.getColumnIndex(GSengerContract.Messages.TEXT)));

        return message;
    }


}
