package com.example.advanced.app.task;

import com.example.advanced.BuildConfig;
import com.example.advanced.launchstarter.task.MainTask;
import com.squareup.leakcanary.LeakCanary;

/**
 * User: milan
 * Time: 2020/3/31 18:42
 * Des:
 */
public class InitLeakTask extends MainTask {
    @Override
    public void run() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(mContext)) {
                return;
            }
            LeakCanary.install(mContext);
        }
    }
}
