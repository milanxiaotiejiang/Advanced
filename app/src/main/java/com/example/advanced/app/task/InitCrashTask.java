package com.example.advanced.app.task;

import com.example.advanced.crash.CrashHandler;
import com.example.advanced.launchstarter.task.Task;

/**
 * User: milan
 * Time: 2020/3/31 18:44
 * Des:
 */
public class InitCrashTask extends Task {
    @Override
    public void run() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(mContext);
    }
}
