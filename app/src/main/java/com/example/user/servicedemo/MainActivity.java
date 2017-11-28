package com.example.user.servicedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button startBtn;
    private Button stopBtn;
    private final static String MY_LOG = "myLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.btnStart);
        stopBtn = findViewById(R.id.btnStop);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, MyService.class).putExtra("time", 7));
                startService(new Intent(MainActivity.this, MyService.class).putExtra("time", 2));
                startService(new Intent(MainActivity.this, MyService.class).putExtra("time", 4));
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(MainActivity.this, MyService.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(MY_LOG, "requestCode : " + requestCode + " resultCode : " + requestCode + " data : " + data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
