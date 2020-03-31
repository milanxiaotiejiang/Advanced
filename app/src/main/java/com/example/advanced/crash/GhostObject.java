package com.example.advanced.crash;

import android.util.Log;

/**
 * User: milan
 * Time: 2020/3/31 14:27
 * Des:
 */
public class GhostObject {
    @Override
    protected void finalize() throws Throwable {
        Log.e("ghost", "=============fire finalize=============" + Thread.currentThread().getName());
        super.finalize();
        Thread.sleep(120000);//每个手机触发 Timeout 的时长不同，比如 vivo 的某些rom 是2分钟，模拟器统一都是10秒钟，所以在模拟器上效果明显
    }
}
