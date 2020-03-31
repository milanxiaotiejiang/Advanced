package com.example.advanced.carton.runnable;

import android.content.res.AssetManager;
import android.util.Log;

import com.example.advanced.app.App;
import com.example.advanced.carton.model.Laser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: milan
 * Time: 2020/3/14 16:59
 * Des:
 */
public class LaserRunnable implements Runnable {

    @Override
    public void run() {
        List<Laser> laserList = new ArrayList<>();
        AssetManager manager = App.getApp().getResources().getAssets();
        try {
            InputStream is = manager.open("laser.log");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            boolean bool = false;
            while ((line = reader.readLine()) != null) {
                if (bool) {
                    String replace = line.replace(" ", "");
                    String[] split = replace.split(":");
                    if (split.length == 2) {
                        Laser laser = new Laser();
                        laser.theta = Double.parseDouble(split[0]);
                        laser.rho = Double.parseDouble(split[1]) * 20;
                        laserList.add(laser);
                    }
                }
                bool = true;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("laser", "执行完成");
    }
}
