package com.pgizka.gsenger;

import android.support.annotation.NonNull;

import com.pgizka.gsenger.dagger2.ApplicationComponent;
import com.pgizka.gsenger.dagger2.DaggerApplicationComponent;
import com.pgizka.gsenger.dagger2.GSengerApplication;
import com.pgizka.gsenger.dagger2.TestApiModule;
import com.pgizka.gsenger.dagger2.TestApplicationModule;

import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class TestUtils {

    public static ApplicationComponent getTestApplicationComponent(GSengerApplication gSengerApplication) {
        return DaggerApplicationComponent.builder()
                .applicationModule(new TestApplicationModule(gSengerApplication))
                .apiModule(new TestApiModule())
                .build();
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
