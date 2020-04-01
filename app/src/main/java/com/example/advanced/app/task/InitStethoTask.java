package com.example.advanced.app.task;

import com.example.advanced.launchstarter.task.Task;
import com.facebook.stetho.Stetho;

/**
 * User: milan
 * Time: 2019/6/10 16:28
 * Des:
 */
public class InitStethoTask extends Task {
    @Override
    public void run() {
        Stetho.initializeWithDefaults(mContext);
    }
}
