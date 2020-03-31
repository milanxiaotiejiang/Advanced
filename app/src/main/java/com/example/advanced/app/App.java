package com.example.advanced.app;

import androidx.multidex.MultiDexApplication;

import com.example.advanced.BuildConfig;
import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.log.inner.LogcatTree;


/**
 * User: milan
 * Time: 2020/3/31 10:19
 * Des:
 */
public class App extends MultiDexApplication {
    private static App mApp;

    public static App getApp() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        if (BuildConfig.DEBUG) {
            Logger.getLogConfig().configAllowLog(true).configShowBorders(false);
//            Logger.plant(new FileTree(context, Constants.PRINT_LOG_PATH));
//            Logger.plant(new ConsoleTree());
            Logger.plant(new LogcatTree());
        }
        // 在主进程初始化调用哈
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();

//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

    }
}
