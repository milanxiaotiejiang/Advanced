package com.example.advanced;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.carton.CartonActivity;
import com.example.advanced.crash.CrashActivity;
import com.example.advanced.electric.ElectricActivity;
import com.example.advanced.memory.MemoryActivity;
import com.example.advanced.network.SocketActivity;
import com.example.advanced.shared.SharedActivity;
import com.example.advanced.start.StartActivity;
import com.example.advanced.thread.ThreadActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRxPermissions = new RxPermissions(this);

        Disposable disposable = mRxPermissions.request(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Log.d("MainActivity", "正常");
                } else {
                    Log.d("MainActivity", "未授权权限，部分功能不能使用");
                }
            }
        });

        Button carton = findViewById(R.id.btn_carton);
        carton.setOnClickListener(this);
        Button crash = findViewById(R.id.btn_crash);
        crash.setOnClickListener(this);
        Button memory = findViewById(R.id.btn_memory);
        memory.setOnClickListener(this);
        Button start = findViewById(R.id.btn_start);
        start.setOnClickListener(this);
        Button thread = findViewById(R.id.btn_thread);
        thread.setOnClickListener(this);
        Button shared = findViewById(R.id.btn_shared);
        shared.setOnClickListener(this);
        Button network = findViewById(R.id.btn_network);
        network.setOnClickListener(this);
        Button electric = findViewById(R.id.btn_electric);
        electric.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_carton:
                startActivity(new Intent(this, CartonActivity.class));
                break;
            case R.id.btn_crash:
                startActivity(new Intent(this, CrashActivity.class));
                break;
            case R.id.btn_memory:
                startActivity(new Intent(this, MemoryActivity.class));
                break;
            case R.id.btn_start:
                startActivity(new Intent(this, StartActivity.class));
                break;
            case R.id.btn_thread:
                startActivity(new Intent(this, ThreadActivity.class));
                break;
            case R.id.btn_shared:
                startActivity(new Intent(this, SharedActivity.class));
                break;
            case R.id.btn_network:
                startActivity(new Intent(this, SocketActivity.class));
                break;
            case R.id.btn_electric:
                startActivity(new Intent(this, ElectricActivity.class));
                break;
        }
    }
}
