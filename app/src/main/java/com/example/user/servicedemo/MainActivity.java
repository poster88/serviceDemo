package com.example.user.servicedemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button startBtn;
    private TextView task1;
    private TextView task2;
    private TextView task3;
    private final static String MY_LOG = "myLog";

    private final int TASK1_CODE = 1;
    private final int TASK2_CODE = 2;
    private final int TASK3_CODE = 3;

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;


    public final static String PARAM_TIME = "time";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, ThirdActivity.class));

        startBtn = findViewById(R.id.btnStart);
        task1 = findViewById(R.id.tvTask1);
        task1.setText("Task1");
        task2 = findViewById(R.id.tvTask2);
        task2.setText("Task2");
        task3 = findViewById(R.id.tvTask3);
        task3.setText("Task3");

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PendingIntent pi;
                Intent intent;

                intent = new Intent(MainActivity.this, MyService.class);
                pi = createPendingResult(TASK1_CODE, intent, 0);
                intent.putExtra(PARAM_TIME, 7).putExtra(PARAM_PINTENT, pi);
                startService(intent);

                intent = new Intent(MainActivity.this, MyService.class);
                pi = createPendingResult(TASK2_CODE, intent, 0);
                intent.putExtra(PARAM_TIME, 4).putExtra(PARAM_PINTENT, pi);
                startService(intent);

                intent = new Intent(MainActivity.this, MyService.class);
                pi = createPendingResult(TASK3_CODE, intent, 0);
                intent.putExtra(PARAM_TIME, 6).putExtra(PARAM_PINTENT, pi);
                startService(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(MY_LOG, "requestCode : " + requestCode + " resultCode : " + resultCode+ " data : " + data);
        if (resultCode == STATUS_START){
            if (requestCode == TASK1_CODE){
                task1.setText("Task1 start");
            }
            if (requestCode == TASK2_CODE){
                task2.setText("Task2 start");
            }
            if (requestCode == TASK3_CODE){
                task3.setText("Task3 start");
            }
        }

        if (resultCode == STATUS_FINISH){
            int result = data.getIntExtra(PARAM_RESULT, 0);
            if (requestCode == TASK1_CODE){
                task1.setText("Task1 finish, result = " + result);
            }
            if (requestCode == TASK2_CODE){
                task2.setText("Task2 finish, result = " + result);
            }
            if (requestCode == TASK3_CODE){
                task3.setText("Task3 finish, result = " + result);
            }
        }
    }
}
