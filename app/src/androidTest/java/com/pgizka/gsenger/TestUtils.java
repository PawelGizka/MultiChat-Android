package com.pgizka.gsenger;


import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import com.pgizka.gsenger.dagger.DaggerTestApplicationComponent;
import com.pgizka.gsenger.dagger.TestApiModule;
import com.pgizka.gsenger.dagger.TestApplicationComponent;
import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.dagger2.ApplicationModule;
import com.pgizka.gsenger.dagger2.DaggerApplicationComponent;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.provider.Chat;
import com.pgizka.gsenger.provider.Repository;
import com.pgizka.gsenger.provider.User;

import java.io.IOException;
import java.lang.reflect.Field;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestUtils {

    public static GSengerApplication getApplication() {
        return (GSengerApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }

    public static void setupRealm() {
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(getApplication())
                .inMemory()
                .deleteRealmIfMigrationNeeded()
                .build());
    }

    public static TestApplicationComponent getTestApplicationComponent() {
        return DaggerTestApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplication()))
                .apiModule(new TestApiModule())
                .build();
    }

    public static User getOrCreateOwner() {
        Realm realm = Realm.getDefaultInstance();
        realm.refresh();

        User owner = realm.where(User.class)
                .equalTo("id", 0)
                .findFirst();

        if (owner != null) {
            return owner;
        }

        owner = new User();
        owner.setId(0);
        owner.setServerId(0);
        owner.setUserName("Owner");
        owner.setStatus("my super status");
        realm.beginTransaction();
        owner = realm.copyToRealm(owner);
        realm.commitTransaction();

        return owner;
    }

    public static User createUser() {
        Repository repository = GSengerApplication.getApplicationComponent().repository();

        Realm realm = Realm.getDefaultInstance();
        realm.refresh();

        User user = new User();
        user.setId(repository.getUserNextId());
        user.setServerId(repository.getUserNextId());
        user.setUserName("Pawel");
        user.setStatus("my super status");
        realm.beginTransaction();
        user = realm.copyToRealm(user);
        realm.commitTransaction();

        return user;
    }

    public static Chat createChatBetweenUsers(User firstUser, User secondUser) {
        Repository repository = GSengerApplication.getApplicationComponent().repository();

        Realm realm = Realm.getDefaultInstance();
        realm.refresh();

        Chat chat = new Chat();
        chat.setId(repository.getChatNextId());
        chat.setType(Chat.Type.SINGLE_CONVERSATION.code);
        chat.setStartedDate(System.currentTimeMillis());

        realm.beginTransaction();
        chat = realm.copyToRealm(chat);
        chat.setUsers(new RealmList<>(firstUser, secondUser));
        firstUser.getChats().add(chat);
        secondUser.getChats().add(chat);
        realm.commitTransaction();

        return chat;
    }

    public static <T> Call<T> createCall(T response) {
        return new MockCall<>(response);
    }

    public static <T> Call<T> createCall(int responseCode, T response) {
        return new MockCall<>(response, responseCode);
    }

    private static class MockCall<T> implements Call<T> {

        final T mResponse;

        final int mCode;

        public MockCall(T response, int code) {
            mResponse = response;
            mCode = code;
        }

        public MockCall(T response) {
            this(response, 200);
        }

        @Override
        public Response<T> execute() throws IOException {
            return buildResponse();
        }

        @Override
        public void enqueue(Callback<T> callback) {

        }

        @NonNull
        private Response<T> buildResponse() {
            if (mCode > 199 && mCode < 300) {
                return Response.success(mResponse);
            } else {
                return Response.error(mCode, null);
            }
        }

        @Override
        public boolean isExecuted() {
            return false;
        }

        @Override
        public void cancel() {

        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @SuppressWarnings("CloneDoesntCallSuperClone")
        @Override
        public Call<T> clone() {
            return new MockCall<>(mResponse);
        }

        @Override
        public Request request() {
            return null;
        }
    }

    private static void resetSingleton(Class clazz, String fieldName) {
        Field instance;
        try {
            instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
