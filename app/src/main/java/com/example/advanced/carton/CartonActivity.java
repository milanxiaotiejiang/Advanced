package com.example.advanced.carton;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.R;
import com.example.advanced.aspectjx.annotation.TimeSpend;
import com.example.advanced.carton.runnable.PgmRunnable;

/**
 * adb shell
 */
public class CartonActivity extends AppCompatActivity {

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
        new Thread(runnable).run();
//        new Thread(new PoseRunnable()).run();

        /**
         * 业界都使用 Choreographer 来监控应用的帧率。跟卡顿不同的是，需要排除掉页面没有操作的情况，我们应该只在界面存在绘制的时候才做统计。
         */
        getWindow().getDecorView().getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        Debug.stopMethodTracing();
    }
}
