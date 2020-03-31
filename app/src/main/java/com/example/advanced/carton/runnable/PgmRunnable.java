package com.example.advanced.carton.runnable;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;

import com.example.advanced.app.App;
import com.example.advanced.aspectjx.annotation.TimeSpend;
import com.example.advanced.carton.model.PGM;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: milan
 * Time: 2020/3/14 17:20
 * Des:
 */
public class PgmRunnable implements Runnable {
    private static final String TAG = "PgmRunnable";

    @TimeSpend("run")
    @Override
    public void run() {

//        Trace.beginSection("sampleTrace");
        PGM pgm = new PGM();

        AssetManager manager = App.getApp().getResources().getAssets();
        try {
            InputStream is = manager.open("map.pgm");

            DataInputStream in = new DataInputStream(is);
            pgm.ch0 = (char) in.readByte();
            pgm.ch1 = (char) in.readByte();
            if (pgm.ch0 != 'P' || pgm.ch1 != '6') {
                Log.e(TAG, "Not a pgm image!" + " [0]=" + pgm.ch0 + ", [1]=" + pgm.ch1);
                return;
            }

            //读空格
            in.readByte();
            char c = (char) in.readByte();

            //读注释行
            if (c == '#') {
                do {
                    c = (char) in.readByte();
                } while ((c != '\n') && (c != '\r'));
                c = (char) in.readByte();
            }

            //读出宽度
            if (c < '0' || c > '9') {
                Log.e(TAG, "Errow!");
                return;
            }

            int k = 0;
            do {
                k = k * 10 + c - '0';
                c = (char) in.readByte();
            } while (c >= '0' && c <= '9');

            pgm.width = k;

            //读出高度
            c = (char) in.readByte();
            if (c < '0' || c > '9') {
                Log.e(TAG, "Errow!");
                return;
            }

            k = 0;
            do {
                k = k * 10 + c - '0';
                c = (char) in.readByte();
            } while (c >= '0' && c <= '9');

            pgm.height = k;

            //读出灰度最大值(尚未使用)
            c = (char) in.readByte();
            if (c < '0' || c > '9') {
                Log.e(TAG, "Errow!");
                return;
            }

            k = 0;
            do {
                k = k * 10 + c - '0';
                c = (char) in.readByte();
            } while (c >= '0' && c <= '9');
            pgm.maxpix = k;

            for (int i = 0; i < pgm.width * pgm.height; i++) {

                int r = in.readByte();
                if (r < 0) r = r + 256;

                int g = in.readByte();
                if (g < 0) g = g + 256;

                int b = in.readByte();
                if (b < 0) b = b + 256;

                int rgb = Color.rgb(r, g, b);
                pgm.map.put(i, rgb);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("pgm", "执行完成");
//        Trace.endSection();
    }
}
