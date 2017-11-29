package com.example.user.servicedemo;

import android.app.PendingIntent;
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

    private final static String MY_LOG = "myLog";

    public MyService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MY_LOG, "onCreate");

        es = Executors.newFixedThreadPool(2); //створюєм пулл з 2-мa потоком, запуск задач відбувається по черзі
        //someRes = new Object();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MY_LOG, "onStartCommand: intent - " + intent + " flags - " + flags + " startId - " + startId);

        int time = intent.getIntExtra(MainActivity.PARAM_TIME, 1);
        PendingIntent pi = intent.getParcelableExtra(MainActivity.PARAM_PINTENT);

        MyRun myRun = new MyRun(time, startId, pi);
        es.execute(myRun);
        return super.onStartCommand(intent, flags, startId);
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
        PendingIntent pi;

        public MyRun(int time, int startId, PendingIntent pi) {
            this.time = time;
            this.startId = startId;
            this.pi = pi;
            Log.d(MY_LOG, "MyRun " + startId + " created.");
        }

        @Override
        public void run() {
            Log.d(MY_LOG, "MyRun#" + startId + " start, time = " + time);
            try {
                pi.send(MainActivity.STATUS_START);
                TimeUnit.SECONDS.sleep(time);

                Intent intent = new Intent(MyService.this, MainActivity.class).putExtra(MainActivity.PARAM_RESULT, time * 100);
                pi.send(MyService.this, MainActivity.STATUS_FINISH, intent);
            }catch (InterruptedException e){
                e.printStackTrace();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            stop();
        }

        private void stop() {
            Log.d(MY_LOG, "MyRun#" +
                    startId + " end, stopSelfResult(" +
                    startId + ") = " +
                    stopSelfResult(startId)); //вертає істине значення
        }
    }
}
