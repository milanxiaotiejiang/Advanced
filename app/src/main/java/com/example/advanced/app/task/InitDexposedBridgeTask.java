package com.example.advanced.app.task;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.example.advanced.launchstarter.task.Task;
import com.example.advanced.memory.ImageHook;
import com.example.advanced.memory.ThreadMethodHook;
import com.robot.seabreeze.log.Logger;
import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodHook;

/**
 * User: milan
 * Time: 2020/3/31 18:57
 * Des:
 */
public class InitDexposedBridgeTask extends Task {
    @Override
    public void run() {

        DexposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Thread thread = (Thread) param.thisObject;
                Class<?> clazz = thread.getClass();
                if (clazz != Thread.class) {
                    Logger.d("found class extend Thread:" + clazz);
                    DexposedBridge.findAndHookMethod(clazz, "run", new ThreadMethodHook());
                }
                Logger.d("Thread: " + thread.getName() + " class:" + thread.getClass() + " is created.");
            }
        });
        DexposedBridge.findAndHookMethod(Thread.class, "run", new ThreadMethodHook());

        DexposedBridge.hookAllConstructors(ImageView.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                DexposedBridge.findAndHookMethod(ImageView.class, "setImageBitmap", Bitmap.class, new ImageHook());
            }
        });

        //锁定线程创建，便于统一线程库
        DexposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Thread thread = (Thread) param.thisObject;
                Logger.i(thread.getName() + " stack " + Log.getStackTraceString(new Throwable()));
            }
        });
    }
}
