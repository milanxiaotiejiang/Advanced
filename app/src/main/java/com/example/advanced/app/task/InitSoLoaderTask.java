package com.example.advanced.app.task;

import com.example.advanced.launchstarter.task.MainTask;
import com.facebook.soloader.SoLoader;

/**
 * User: milan
 * Time: 2020/4/2 10:17
 * Des:
 */
public class InitSoLoaderTask extends MainTask {
    @Override
    public void run() {
        SoLoader.init(mContext, false);
    }
}
