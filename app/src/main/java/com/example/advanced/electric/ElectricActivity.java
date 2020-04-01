package com.example.advanced.electric;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
    }
}
