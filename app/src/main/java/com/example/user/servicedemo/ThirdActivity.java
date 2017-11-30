package com.example.user.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by User on 029 29.11.17.
 */

public class ThirdActivity extends AppCompatActivity {
    private final static String MY_LOG = "myLog";
    private Button unBindBtn;

    private boolean bound = false;
    ServiceConnection sConn;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_activity);

        unBindBtn = findViewById(R.id.btnUnBind);
        unBindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUnBind();
            }
        });

        intent = new Intent(ThirdActivity.this, ThirdService.class);
        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d(MY_LOG, "ThirdActivity onServiceConnected");
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(MY_LOG, "ThirdActivity onServiceDisconnected");
                bound = false;
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onClickUnBind();
    }

    private void onClickUnBind() {
        if (!bound) return;
        unbindService(sConn);
        bound = false;
    }

    public void onClickBind(View v){
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }

    public void onClickStart(View v){
        startService(intent);
    }

    public void onClickStop(View v){
        stopService(intent);
    }

    //TODO: 675 page
}
