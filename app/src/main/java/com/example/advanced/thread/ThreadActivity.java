package com.example.advanced.thread;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.R;
import com.robot.seabreeze.log.Logger;

public class ThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        Button btnHook = findViewById(R.id.btn_hook);
        Button btnNew = findViewById(R.id.newthread);
        btnHook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadHook.enableThreadHook();
                Toast.makeText(ThreadActivity.this, "开启成功", Toast.LENGTH_SHORT).show();
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.e("thread name:" + Thread.currentThread().getName());
                        Logger.e("thread id:" + Thread.currentThread().getId());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.e("inner thread name:" + Thread.currentThread().getName());
                                Logger.e("inner thread id:" + Thread.currentThread().getId());

                            }
                        }).start();
                    }
                }).start();
            }
        });
    }
}
