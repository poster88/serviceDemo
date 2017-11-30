package com.example.user.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by User on 029 29.11.17.
 */

public class ThirdService extends Service{
    private final static String MY_LOG = "myLog";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MY_LOG, "ThirdService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(MY_LOG, "ThirdService onBind");
        return new Binder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(MY_LOG, "ThirdService onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(MY_LOG, "ThirdService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(MY_LOG, "ThirdService onDestroy");
    }
}
