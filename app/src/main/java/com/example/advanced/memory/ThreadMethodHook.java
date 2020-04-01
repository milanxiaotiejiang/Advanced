package com.example.advanced.memory;

import com.robot.seabreeze.log.Logger;
import com.taobao.android.dexposed.XC_MethodHook;

/**
 * User: milan
 * Time: 2020/3/31 15:30
 * Des:
 */
public class ThreadMethodHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        Thread t = (Thread) param.thisObject;
        Logger.d("ThreadMethodHook:" + t + ", started..");
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        Thread t = (Thread) param.thisObject;
        Logger.d("ThreadMethodHook:" + t + ", exit..");
    }
}