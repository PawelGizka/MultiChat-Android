package com.pgizka.gsenger.jobqueue;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.path.android.jobqueue.network.NetworkUtil;
import com.path.android.jobqueue.persistentQueue.sqlite.SqliteJobQueue;

import java.io.IOException;
import java.lang.reflect.Type;

public class JobMangerDAO {
    private static JobManager fastJobManager;
    private static JobManager slowJobManager;


    public static synchronized JobManager getFastJobManager(Context context) {

        if (fastJobManager == null) {
            initMangers(context);
        }

        return fastJobManager;
    }

    private static synchronized void initMangers(Context context) {

        Configuration.Builder builder = new Configuration.Builder(context);
        builder.id("slow");
        builder.injector(new DependencyInjector() {
            @Override
            public void inject(Job job) {

            }
        });

        builder.jobSerializer(new SqliteJobQueue.JobSerializer() {
            @Override
            public byte[] serialize(Object object) throws IOException {
                return new Gson().toJson(object).getBytes();
            }

            @Override
            public <T extends Job> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
                Type type = new TypeToken<T>() {}.getType();
                return new Gson().fromJson("newf", type);
            }
        });

        JobManager jobManager = new JobManager(context, builder.build());
        JobManager fastJobManager = new JobManager(context, "fast");

    }

}
