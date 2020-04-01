package com.example.advanced.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.example.advanced.BuildConfig;
import com.example.advanced.MainActivity;
import com.example.advanced.app.task.InitAutoSizeTask;
import com.example.advanced.app.task.InitBlockTask;
import com.example.advanced.app.task.InitDBTask;
import com.example.advanced.app.task.InitDexposedBridgeTask;
import com.example.advanced.app.task.InitLeakTask;
import com.example.advanced.app.task.InitNetworkTask;
import com.example.advanced.app.task.InitToastTask;
import com.example.advanced.launchstarter.TaskDispatcher;
import com.example.advanced.shared.SharedPreferencesHelper;
import com.example.advanced.shared.SharedPreferencesImpl;
import com.example.advanced.start.LoadMultiDexActivity;
import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.log.inner.LogcatTree;

import java.io.File;
import java.util.HashMap;

import static com.example.advanced.utils.Utils.isMainProcess;
import static com.example.advanced.utils.Utils.isVMMultidexCapable;


/**
 * User: milan
 * Time: 2020/3/31 10:19
 * Des:
 */
public class App extends MultiDexApplication {
    private static final String TAG = "App";

    private static App mApp;

    public static App getApp() {
        return mApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        boolean isMainProcess = isMainProcess(base);

        //主进程并且vm不支持多dex的情况下才使用 MultiDex
        if (isMainProcess && !isVMMultidexCapable()) {
            loadMultiDex(base);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        initLogger(this);

//        String rootDir = MMKV.initialize(this);

        TaskDispatcher.init(this);
        TaskDispatcher dispatcher = TaskDispatcher.createInstance();

        dispatcher
                .addTask(new InitLeakTask())
                .addTask(new InitBlockTask())
//                .addTask(new InitStethoTask())
//                .addTask(new InitCrashTask())
                .addTask(new InitAutoSizeTask())
                .addTask(new InitToastTask())
                .addTask(new InitDBTask())
                .addTask(new InitDexposedBridgeTask())
                .addTask(new InitNetworkTask())
                .start();

        //假设数据库初始化需要完成，needWait为true
        dispatcher.await();

    }

    private static final HashMap<String, SharedPreferencesImpl> sSharedPrefs = new HashMap<String, SharedPreferencesImpl>();

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (!SharedPreferencesHelper.canUseCustomSp()) {
            return super.getSharedPreferences(name, mode);
        }
        SharedPreferencesImpl sp;
        synchronized (sSharedPrefs) {
            sp = sSharedPrefs.get(name);
            if (sp == null) {
                File prefsFile = SharedPreferencesHelper.getSharedPrefsFile(this, name);
                sp = new SharedPreferencesImpl(prefsFile, mode);
                sSharedPrefs.put(name, sp);
                return sp;
            }
        }

        if ((mode & Context.MODE_MULTI_PROCESS) != 0) {
            // If somebody else (some other process) changed the prefs
            // file behind our back, we reload it.  This has been the
            // historical (if undocumented) behavior.
            sp.startReloadIfChangedUnexpectedly();
        }

        return sp;
    }

    private void initLogger(@NonNull Context context) {
        if (BuildConfig.DEBUG) {
            Logger.getLogConfig().configAllowLog(true).configShowBorders(false);
//            Logger.plant(new FileTree(context, Constants.PRINT_LOG_PATH));
//            Logger.plant(new ConsoleTree());
            Logger.plant(new LogcatTree());
        }
    }

    private void loadMultiDex(Context context) {
        newTempFile(context); //创建临时文件

        //启动另一个进程去加载MultiDex
        Intent intent = new Intent(context, LoadMultiDexActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        //检查MultiDex是否安装完（安装完会删除临时文件）
        checkUntilLoadDexSuccess(context);

        //另一个进程以及加载 MultiDex，有缓存了，所以主进程再加载就很快了。
        //为什么主进程要再加载，因为每个进程都有一个ClassLoader
        long startTime = System.currentTimeMillis();
        MultiDex.install(context);
        Log.d(TAG, "第二次 MultiDex.install 结束，耗时: " + (System.currentTimeMillis() - startTime));

        preNewActivity();
    }

    private void preNewActivity() {
        long startTime = System.currentTimeMillis();
        MainActivity mainActivity = new MainActivity();
        Log.d(TAG, "preNewActivity 耗时: " + (System.currentTimeMillis() - startTime));
    }


    //创建一个临时文件，MultiDex install 成功后删除
    private void newTempFile(Context context) {
        try {
            File file = new File(context.getCacheDir().getAbsolutePath(), "load_dex.tmp");
            if (!file.exists()) {
                Log.d(TAG, "newTempFile: ");
                file.createNewFile();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * 检查MultiDex是否安装完,通过判断临时文件是否被删除
     *
     * @param context
     * @return
     */
    private void checkUntilLoadDexSuccess(Context context) {
        File file = new File(context.getCacheDir().getAbsolutePath(), "load_dex.tmp");
        int i = 0;
        int waitTime = 100; //睡眠时间
        try {
            Log.d(TAG, "checkUntilLoadDexSuccess: >>> ");
            while (file.exists()) {
                Thread.sleep(waitTime);
                Log.d(TAG, "checkUntilLoadDexSuccess: sleep count = " + ++i);
                if (i > 40) {
                    Log.d(TAG, "checkUntilLoadDexSuccess: 超时，等待时间： " + (waitTime * i));
                    break;
                }
            }

            Log.d(TAG, "checkUntilLoadDexSuccess: 轮循结束，等待时间 " + (waitTime * i));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
