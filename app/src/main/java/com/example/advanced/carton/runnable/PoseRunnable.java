package com.example.advanced.carton.runnable;

import android.content.res.AssetManager;
import android.util.Log;

import com.example.advanced.app.App;
import com.example.advanced.carton.model.Pose;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * User: milan
 * Time: 2020/3/14 17:31
 * Des:
 */
public class PoseRunnable implements Runnable {

    @Override
    public void run() {
        Pose pose = new Pose();
        AssetManager manager = App.getApp().getResources().getAssets();
        try {
            InputStream is = manager.open("pose.log");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            boolean bool = false;
            while ((line = reader.readLine()) != null) {
                if (bool) {
                    String replace = line.replace(" ", "");
                    String[] split = replace.split(",");
                    if (split.length == 3) {

                        pose.x = Double.parseDouble(split[0]) * 20;
                        pose.y = Double.parseDouble(split[1]) * 20;
                        pose.theta = Double.parseDouble(split[2]);
                    }
                }
                bool = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("pose", "执行完成");
    }
}
