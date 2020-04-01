package com.example.advanced.carton;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.R;
import com.example.advanced.aspectjx.annotation.TimeSpend;
import com.example.advanced.carton.runnable.PgmRunnable;
import com.robot.seabreeze.log.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * adb shell
 */
public class CartonActivity extends AppCompatActivity {

    public static ProcessCpuTracker processCpuTracker = new ProcessCpuTracker(android.os.Process.myPid());
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carton);

        Button btnTime = findViewById(R.id.btn_time);
        btnTime.setOnClickListener(new View.OnClickListener() {

            @TimeSpend("btnTime")
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
//        Debug.startMethodTracing("sample");

//        new Thread(new LaserRunnable()).run();
        PgmRunnable runnable = new PgmRunnable();
//        new Thread(runnable).run();
//        new Thread(new PoseRunnable()).run();

        /**
         * 业界都使用 Choreographer 来监控应用的帧率。跟卡顿不同的是，需要排除掉页面没有操作的情况，我们应该只在界面存在绘制的时候才做统计。
         */
        getWindow().getDecorView().getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {

            }
        });

        final Button testGc = (Button) findViewById(R.id.test_gc);
        testGc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCpuTracker.update();
                testGc();
                processCpuTracker.update();
                Logger.e(processCpuTracker.printCurrentState(SystemClock.uptimeMillis()));
            }
        });


        final Button testIO = (Button) findViewById(R.id.test_io);
        testIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCpuTracker.update();

                testIO();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        processCpuTracker.update();
                        Logger.d(processCpuTracker.printCurrentState(SystemClock.uptimeMillis()));
                    }
                }, 5000);


            }
        });

        final Button processOut = (Button) findViewById(R.id.test_process);
        processOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCpuTracker.update();
                Logger.e(processCpuTracker.printCurrentState(SystemClock.uptimeMillis()));

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        Debug.stopMethodTracing();
    }

    private void testIO() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                writeSth();
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("SingleThread");
        thread.start();
    }


    private void testGc() {
        for (int i = 0; i < 10000; i++) {
            int[] test = new int[100000];
            System.gc();
        }
    }


    private void writeSth() {
        try {
            File f = new File(getFilesDir(), "aee.txt");

            if (f.exists()) {
                f.delete();
            }
            FileOutputStream fos = new FileOutputStream(f);

            byte[] data = new byte[1024 * 4 * 3000];

            for (int i = 0; i < 30; i++) {
                Arrays.fill(data, (byte) i);
                fos.write(data);
                fos.flush();
            }
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
