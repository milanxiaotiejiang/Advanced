package com.example.advanced.memory;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.R;
import com.example.advanced.app.App;
import com.robot.seabreeze.log.Logger;

import java.io.InputStream;

public class MemoryActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        ImageView imageView = findViewById(R.id.iv_memory);

        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = null;
                AssetManager manager = App.getApp().getResources().getAssets();
                try {
                    InputStream is = manager.open("bigpicture.jpg");
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

                Bitmap finalBitmap = bitmap;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(finalBitmap);
                    }
                });
            }
        }.run();

    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Logger.e("" + level);
    }
}
