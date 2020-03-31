package com.example.advanced.start;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.R;
import com.example.advanced.launchstarter.DelayInitDispatcher;
import com.example.advanced.launchstarter.task.Task;
import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;

@Xml(layouts = "activity_start")
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        X2C.setContentView(this, R.layout.activity_start);

        DelayInitDispatcher delayInitDispatcher = new DelayInitDispatcher();
        delayInitDispatcher.addTask(new Task() {
            @Override
            public void run() {
                //IdleHandler
            }
        });
        delayInitDispatcher.addTask(new Task() {
            @Override
            public void run() {
                //IdleHandler
            }
        });
        delayInitDispatcher.start();
    }
}
