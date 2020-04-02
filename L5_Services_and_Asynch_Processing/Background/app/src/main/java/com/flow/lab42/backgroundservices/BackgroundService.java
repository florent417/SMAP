package com.flow.lab42.backgroundservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.Executor;

public class BackgroundService extends Service {

    private long waitTime = 20000l;
    private static final long DEFAULT_WAIT = 30*1000; //default = 30s
    private static final String LOG = "BG_SERVICE";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "Background service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        // Doing this stops/freezes the app
        // Probably because it blocks the UI thread
        /*
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        Executor exe = new Executor() {
            @Override
            public void execute(Runnable command) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        /*
        for (int i = 0; 10 > i; i++){
            doBackgroundThing(waitTime);
        }*/
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Service not for binding
        return null;
    }

    //using recursion for running this as a loop
    private void doBackgroundThing(final Long waitTimeInMilis){


        //example 1
        //Using AsychTask (now deprecated since Jan 2020)
        //create asynch tasks that sleeps X ms and then sends broadcast

        // BackgroundThingTask task = new BackgroundThingTask(this);
        // task.execute(5000L); //L means long number format



        //example 2
        //Using ExecutorService and Runnable
        //recursiveSleepWork(5000L);

    }
}
