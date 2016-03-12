
package com.pgizka.gsenger.jobqueue;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.JobStatus;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.path.android.jobqueue.TagConstraint;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.network.NetworkUtil;
import com.path.android.jobqueue.persistentQueue.sqlite.SqliteJobQueue;

import java.io.IOException;
import java.lang.reflect.Type;

public class TestJobQueue {

    class SendMessageJob extends Job {

        protected SendMessageJob() {
            super(new Params(1).requireNetwork().addTags("refreshFriends", "1").groupBy("messages").persist());
        }

        @Override
        public void onAdded() {

        }

        @Override
        public void onRun() throws Throwable {

        }

        @Override
        protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
            return super.shouldReRunOnThrowable(throwable, runCount, maxRunCount);
        }

        @Override
        protected void onCancel() {

        }
    }


    public void sendMessage() {
        Context context = null;

        Configuration.Builder builder = new Configuration.Builder(context);
        builder.id("slow");
        builder.networkUtil(new NetworkUtil() {
            @Override
            public boolean isConnected(Context context) {
                return false;
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

        fastJobManager.addJobInBackground(new SendMessageJob());


//        if (JobMangerDAO.getFastJobManager(context).getJobStatus() == JobStatus.UNKNOWN) {
//            JobMangerDAO.getFastJobManager(context).addJob(new RefreshFriendsJob());
//            JobMangerDAO.getFastJobManager(context).cancelJobs(TagConstraint.ALL, "sendMessage", "1");
//        }
    }

}
