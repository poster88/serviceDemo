package com.example.user.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 028 28.11.17.
 */

public class MyService extends Service{
    private Object someRes;
    private ExecutorService es;

    public MyService() {
    }

    private final static String MY_LOG = "myLog";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MY_LOG, "onCreate");

        es = Executors.newFixedThreadPool(3); //створюєм пулл з 1-м потоком, запуск задач відбувається по черзі
        someRes = new Object();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MY_LOG, "onStartCommand: intent - " + intent + " flags - " + flags + " startId - " + startId);

        int time = intent.getIntExtra("time", 1);
        MyRun myRun = new MyRun(time, startId);
        es.execute(myRun);
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(MY_LOG, "onDestroy");
        someRes = null;
    }

    class MyRun implements Runnable {
        int time;
        int startId;

        public MyRun(int time, int startId) {
            this.time = time;
            this.startId = startId;
            Log.d(MY_LOG, "MyRun " + startId + " created.");
        }

        @Override
        public void run() {
            Log.d(MY_LOG, "MyRun#" + startId + " start, time = " + time);
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Log.d(MY_LOG, "MyRun#" + startId + " someRes = " + someRes.getClass() );
            } catch (NullPointerException e) {
                Log.d(MY_LOG, "MyRun#" + startId + " error, null pointer");
            }
            stop();
        }

        private void stop() {
            Log.d(MY_LOG, "MyRun#" +
                    startId + " end, stopSelfResult(" +
                    startId + ") = " +
                    stopSelfResult(startId)); //вертає істине значення
        }

        //TODO: page#637 startAndroid
    }
}
