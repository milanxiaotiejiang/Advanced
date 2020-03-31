package com.example.advanced.app.task;

import com.example.advanced.BuildConfig;
import com.example.advanced.app.AppBlockCanaryContext;
import com.example.advanced.launchstarter.task.MainTask;
import com.github.moduth.blockcanary.BlockCanary;

/**
 * User: milan
 * Time: 2020/3/31 18:43
 * Des:
 */
public class InitBlockTask extends MainTask {
    @Override
    public void run() {
        if (BuildConfig.DEBUG) {
            BlockCanary.install(mContext, new AppBlockCanaryContext()).start();
        }
    }
}
