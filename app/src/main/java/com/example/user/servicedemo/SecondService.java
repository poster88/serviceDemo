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
 * Created by User on 029 29.11.17.
 */

public class SecondService extends Service{
    private ExecutorService es;
    private final static String MY_LOG = "myLog";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MY_LOG, "SecondService onCreate");
        es = Executors.newFixedThreadPool(2);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(MY_LOG, "SecondService onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MY_LOG, "SecondService onStartCommand");

        int time = intent.getIntExtra(SecondActivity.PARAM_TIME, 1);
        int task = intent.getIntExtra(SecondActivity.PARAM_TASK, 0);

        MyRun myRun = new MyRun(time, startId, task);
        es.execute(myRun);

        return super.onStartCommand(intent, flags, startId);
    }

    class MyRun implements Runnable {
        int time;
        int startId;
        int task;

        public MyRun(int time, int startId, int task) {
            this.time = time;
            this.startId = startId;
            this.task = task;
            Log.d(MY_LOG, "MyRun " + startId + " created.");
        }

        @Override
        public void run() {
            Intent intent = new Intent(SecondActivity.BROADCAST_ACTION);
            Log.d(MY_LOG, "MyRun#" + startId + " start, time = " + time);
            try {
                // сообщаем об старте задачи
                intent.putExtra(SecondActivity.PARAM_TASK, task);
                intent.putExtra(SecondActivity.PARAM_STATUS, SecondActivity.STATUS_START);
                sendBroadcast(intent);

                TimeUnit.SECONDS.sleep(time);

                // сообщаем об окончании задачи
                intent.putExtra(SecondActivity.PARAM_STATUS, SecondActivity.STATUS_FINISH);
                intent.putExtra(SecondActivity.PARAM_RESULT, time * 100);
                sendBroadcast(intent);
            }catch (InterruptedException e){
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
