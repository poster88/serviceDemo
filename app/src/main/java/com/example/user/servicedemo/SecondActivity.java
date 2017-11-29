package com.example.user.servicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by User on 029 29.11.17.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String MY_LOG = "myLog";
    private final int TASK1_CODE = 1;
    private final int TASK2_CODE = 2;
    private final int TASK3_CODE = 3;

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    public final static String PARAM_TIME = "time";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";
    public final static String PARAM_TASK = "task";
    public final static String PARAM_STATUS = "status";

    public final static String BROADCAST_ACTION = "broadcastaction";

    private Button startBtn;
    private TextView task1;
    private TextView task2;
    private TextView task3;
    private BroadcastReceiver br;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        startBtn = findViewById(R.id.btnStart);
        task1 = findViewById(R.id.tvTask1);
        task1.setText("Task1");
        task2 = findViewById(R.id.tvTask2);
        task2.setText("Task2");
        task3 = findViewById(R.id.tvTask3);
        task3.setText("Task3");
        startBtn.setOnClickListener(this);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int task = intent.getIntExtra(PARAM_TASK, 0);
                int status = intent.getIntExtra(PARAM_STATUS, 0);
                Log.d(MY_LOG, "onReceive: task = " + task + ", status = " + status);

                // Ловим сообщения о старте задач
                if (status == STATUS_START){
                    if (task == TASK1_CODE){
                        task1.setText("Task1 start");
                    }
                    if (task == TASK2_CODE){
                        task2.setText("Task2 start");
                    }
                    if (task == TASK3_CODE){
                        task3.setText("Task3 start");
                    }
                }

                // Ловим сообщения об окончании задач
                if (status == STATUS_FINISH){
                    int result = intent.getIntExtra(PARAM_RESULT, 0);
                    if (task == TASK1_CODE){
                        task1.setText("Task1 finish, result = " + result);
                    }
                    if (task == TASK2_CODE){
                        task2.setText("Task2 finish, result = " + result);
                    }
                    if (task == TASK3_CODE){
                        task3.setText("Task3 finish, result = " + result);
                    }
                }
            }
        };

        // создаем фильтр для BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        // Создаем Intent для вызова сервиса,
        // кладем туда параметр времени и код задачи
        intent = new Intent(this, SecondService.class)
                .putExtra(PARAM_TIME, 7)
                .putExtra(PARAM_TASK, TASK1_CODE);
        startService(intent);

        intent = new Intent(this, SecondService.class)
                .putExtra(PARAM_TIME, 4)
                .putExtra(PARAM_TASK, TASK2_CODE);
        startService(intent);

        intent = new Intent(this, SecondService.class)
                .putExtra(PARAM_TIME, 6)
                .putExtra(PARAM_TASK, TASK3_CODE);
        startService(intent);
    }
}
