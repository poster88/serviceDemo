package com.example.user.servicedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
    private NotificationManager nm;
    private static final int NOTIFY_ID = 101;


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
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

        return START_STICKY;
    }

    private void sendNotification() {
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, SecondActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker("message")
                .setAutoCancel(true)
                .setContentTitle("my title")
                .setContentText("my text");

        Notification notification = builder.getNotification();

        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFY_ID, notification);
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

                if (time == 6){
                    sendNotification();
                }

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
        //https://material.io/guidelines/patterns/notifications.html#notifications-behavior notification arrival**
        //http://www.brevitysoftware.com/blog/different-types-of-notification-android/
        //http://androidblog.reindustries.com/check-if-an-android-activity-is-currently-running/
        //https://stackoverflow.com/questions/18038399/how-to-check-if-activity-is-in-foreground-or-in-visible-background
    }
}
