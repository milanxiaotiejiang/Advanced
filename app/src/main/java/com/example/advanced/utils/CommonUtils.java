package com.example.advanced.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.advanced.R;
import com.example.advanced.app.App;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * @author quchao
 * @date 2017/11/27
 */

public class CommonUtils {

    /**
     * 判断是否是主线程
     *
     * @return 是否是主线程
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        final float scale = App.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Show message
     *
     * @param activity Activity
     * @param msg message
     */
    public static void showSnackMessage(Activity activity, String msg) {
        //去掉虚拟按键
        activity.getWindow().getDecorView().setSystemUiVisibility(
                //隐藏虚拟按键栏 | 防止点击屏幕时,隐藏虚拟按键栏又弹了出来
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
        final Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        snackbar.setAction("知道了", v -> {
            snackbar.dismiss();
            //隐藏SnackBar时记得恢复隐藏虚拟按键栏,不然屏幕底部会多出一块空白布局出来,很难看
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }).show();
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
    }

    /**
     * 判断2个对象是否相等
     *
     * @param a Object a
     * @param b Object b
     * @return isEqual
     */
    public static boolean isEquals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getApp().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取随机rgb颜色值
     */
    public static int randomColor() {
        Random random = new Random();
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        int red =random.nextInt(150);
        //0-190
        int green =random.nextInt(150);
        //0-190
        int blue =random.nextInt(150);
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red,green, blue);
    }

    /**
     * 泛型转换工具方法 eg:object ==> map<String, String>
     *
     * @param object Object
     * @param <T> 转换得到的泛型对象
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }

}
