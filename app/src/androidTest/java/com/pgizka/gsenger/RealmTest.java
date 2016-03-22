package com.pgizka.gsenger;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.pgizka.gsenger.provider.realm.Message;
import com.pgizka.gsenger.provider.realm.TextMessage;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.Realm;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class RealmTest {

    @Test
    public void testRealm() throws Exception {

        Realm realm = Realm.getDefaultInstance();

        Message message = new Message();
        message.setId(1);
        message.setSendDate(System.currentTimeMillis());

        TextMessage textMessage = new TextMessage();
        textMessage.setText("messageText");

        message.setTextMessage(textMessage);

        realm.beginTransaction();
        realm.copyToRealm(message);
        realm.copyToRealm(textMessage);
        realm.commitTransaction();

        realm.where(Message.class).equalTo("chat.id", 12).findAllAsync();

    }

}
