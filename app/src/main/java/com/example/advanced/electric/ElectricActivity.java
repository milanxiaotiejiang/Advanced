package com.example.advanced.electric;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.advanced.MainActivity;
import com.example.advanced.R;
import com.example.advanced.app.App;
import com.example.advanced.utils.AppUtils;
import com.robot.seabreeze.log.Logger;

public class ElectricActivity extends AppCompatActivity {
    private JobScheduler mJobScheduler;
    private final int JOB_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric);

        Button hookAlarm = (Button) findViewById(R.id.hook_alarm);
        hookAlarm.setOnClickListener(v -> {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            new ProxyHook(alarmManager, "mService", (method, args) -> {
                // 设置 Alarm
                if (method.getName().equals("set")) {
                    // 不同版本参数类型的适配，获取应用堆栈等等
                    Logger.d("ElectricActivity set Alarm" + new Throwable());
                    // 清除 Alarm
                } else if (method.getName().equals("remove")) {
                    // 清除的逻辑
                    Logger.d("ElectricActivity 清除 Alarm" + new Throwable());
                }
            });
            if (alarmManager != null) {
                PendingIntent pendingIntent = PendingIntent.getService(ElectricActivity.this,
                        1, new Intent(), PendingIntent.FLAG_ONE_SHOT);
                alarmManager.cancel(pendingIntent);
                AlarmManagerCompat.setAlarmClock(alarmManager,
                        System.currentTimeMillis() + 10 * 1000, pendingIntent, pendingIntent);
            }
        });


        final Button hookWakelock = (Button) findViewById(R.id.hook_wakelock);
        hookWakelock.setOnClickListener(v -> {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (null != powerManager) {
                new ProxyHook(powerManager, "mService", (method, args) -> {
                    // 申请 Wakelock
                    if (method.getName().equals("acquireWakeLock")) {
                        if (AppUtils.isAppBackground(App.getApp())) {
                            // 应用后台逻辑，获取应用堆栈等等
                            Logger.d("ElectricActivity acquireWakeLock background" + new Throwable());
                        } else {
                            // 应用前台逻辑，获取应用堆栈等等
                            Logger.d("ElectricActivity acquireWakeLock" + new Throwable());
                        }
                        // 释放 Wakelock
                    } else if (method.getName().equals("releaseWakeLock")) {
                        // 释放的逻辑
                        Logger.d("ElectricActivity releaseWakeLock" + new Throwable());
                    }
                });
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MainActivity.class.getName());
                wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
                wakeLock.release();
            }

        });

        Button hookGPS = (Button) findViewById(R.id.hook_gps);
        hookGPS.setOnClickListener(v -> {
            final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (null != locationManager) {
                new ProxyHook(locationManager, "mService", (method, args) -> {
                    // 请求一次定位
                    if (method.getName().equals("requestLocationUpdates")) {
                        // 不同版本参数类型的适配，获取应用堆栈等等
                        Logger.d("ElectricActivity requestLocationUpdates" + new Throwable());
                        // 清除 定位请求
                    } else if (method.getName().equals("removeUpdates")) {
                        // 清除的逻辑
                        Logger.d("ElectricActivity Location removeUpdates" + new Throwable());
                    }
                });
                if (ContextCompat.checkSelfPermission(ElectricActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Criteria criteria = new Criteria();
                    HandlerThread handlerThread = new HandlerThread("locationThread");
                    handlerThread.start();
                    LocationListener listener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Logger.d("ElectricActivity" + location.toString());
                            handlerThread.quit();
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            handlerThread.quit();
                            locationManager.removeUpdates(this);
                        }
                    };
                    locationManager.requestSingleUpdate(criteria, listener, handlerThread.getLooper());
                }

            }
        });

        /**
         * 我们可以通过下面的代码来获取手机的当前充电状态：
         */
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, filter);
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean acCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_AC);
        if (acCharge) {
            Logger.e("ElectricActivity" + acCharge);
        }

        //JobSchedulerService
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        //通过JobInfo.Builder来设定触发服务的约束条件，最少设定一个条件
        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_ID, new ComponentName(this, JobSchedulerService.class));

        //循环触发，设置任务每三秒定期运行一次
        jobBuilder.setPeriodic(3000);

        //单次定时触发，设置为三秒以后去触发。这是与setPeriodic(long time)不兼容的，
        // 并且如果同时使用这两个函数将会导致抛出异常。
        jobBuilder.setMinimumLatency(3000);

        //在约定的时间内设置的条件都没有被触发时三秒以后开始触发。类似于setMinimumLatency(long time)，
        // 这个函数是与 setPeriodic(long time) 互相排斥的，并且如果同时使用这两个函数，将会导致抛出异常。
        jobBuilder.setOverrideDeadline(3000);

        //在设备重新启动后设置的触发条件是否还有效
        jobBuilder.setPersisted(false);

        // 只有在设备处于一种特定的网络状态时，它才触发。
        // JobInfo.NETWORK_TYPE_NONE,无论是否有网络均可触发，这个是默认值；
        // JobInfo.NETWORK_TYPE_ANY，有网络连接时就触发；
        // JobInfo.NETWORK_TYPE_UNMETERED，非蜂窝网络中触发；
        // JobInfo.NETWORK_TYPE_NOT_ROAMING，非漫游网络时才可触发；
        jobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);

        //设置手机充电状态下触发
        jobBuilder.setRequiresCharging(true);

        //设置手机处于空闲状态时触发
        jobBuilder.setRequiresDeviceIdle(true);

        //得到JobInfo对象
        JobInfo jobInfo = jobBuilder.build();

        //设置开始安排任务，它将返回一个状态码
        //JobScheduler.RESULT_SUCCESS，成功
        //JobScheduler.RESULT_FAILURE，失败
        if (mJobScheduler.schedule(jobInfo) == JobScheduler.RESULT_FAILURE) {
            //安排任务失败
        }

        //停止指定JobId的工作服务
        mJobScheduler.cancel(JOB_ID);
        //停止全部的工作服务
        mJobScheduler.cancelAll();
    }

    /**
     * 判断只有当前手机为AC充电状态时 才去执行一些非常耗电的操作。
     */
//    private boolean checkForPower() {
//
//        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        Intent batteryStatus = this.registerReceiver(null, filter);
//        // There are currently three ways a device can be plugged in. We should check them all.
//        boolean usbCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_USB);
//        boolean acCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_AC);
//        boolean wirelessCharge = false;
//        wirelessCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS);
//        return (usbCharge || acCharge || wirelessCharge);
//    }
}
