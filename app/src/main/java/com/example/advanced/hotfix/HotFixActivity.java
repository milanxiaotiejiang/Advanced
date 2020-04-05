package com.example.advanced.hotfix;

import android.app.ActivityThread;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.R;
import com.example.advanced.app.App;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class HotFixActivity extends AppCompatActivity {

    private static AssetManager newAssetManager = null;

    private static Object currentActivityThread = null;
    // method
    private static Method addAssetPathMethod = null;
    private static Method ensureStringBlocksMethod = null;

    // field
    private static Field assetsFiled = null;
    private static Field resourcesImplFiled = null;
    private static Field resDir = null;
    private static Field packagesFiled = null;
    private static Field resourcePackagesFiled = null;
    private static Field publicSourceDirField = null;
    private static Field stringBlocksField = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_fix);

        //外部资源路径
        String externalResourceFile = Environment.getExternalStorageDirectory().toString()
                + File.separator + "ResApk.apk";

        //隐藏api
        Context context = App.getApp().getApplicationContext();
        try {
            Bundle metaData = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData;

            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Instrumentation baseInstrumentation = activityThread.getInstrumentation();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //反射
        try {
            Class<A> clz = A.class;
            Object o = clz.newInstance();
            Method m = clz.getMethod("foo", String.class);
            for (int i = 0; i < 16; i++) {
                m.invoke(o, Integer.toString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //动态代理
        Star star = (Star) Proxy.newProxyInstance(getClassLoader(), new Class[]{Star.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("ccccccccc");
                return method.invoke(proxy, args);
            }
        });

        star.sing("vvvvvv");

//        Class<? extends HotFixActivity> aClass = getClass();
//        aClass.get
//        final AssetManager assets = App.getApp().getApplicationContext().getAssets();
//
//
//        try {
//
//            newAssetManager = (AssetManager) findConstructor(assets).newInstance();
//
//
//            addAssetPathMethod = findMethod(assets, "addAssetPath", String.class);
//
//            if (((Integer) addAssetPathMethod.invoke(newAssetManager, externalResourceFile)) == 0) {
//                throw new IllegalStateException("Could not create new AssetManager");
//            }
//
//
//            Class<?> activityThread = Class.forName("android.app.ActivityThread");
//            currentActivityThread = getActivityThread(App.getApp().getApplicationContext(), activityThread);
//
//
//
//
//            stringBlocksField = findField(assets, "mStringBlocks");
//            ensureStringBlocksMethod = findMethod(assets, "ensureStringBlocks");
//
//
//            Logger.e(stringBlocksField);
//            Logger.e(ensureStringBlocksMethod);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Logger.e(e);
//        }


    }

    public static Object getActivityThread(Context context,
                                           Class<?> activityThread) {
        try {
            if (activityThread == null) {
                activityThread = Class.forName("android.app.ActivityThread");
            }
            Method m = activityThread.getMethod("currentActivityThread");
            m.setAccessible(true);
            Object currentActivityThread = m.invoke(null);
            if (currentActivityThread == null && context != null) {
                // In older versions of Android (prior to frameworks/base 66a017b63461a22842)
                // the currentActivityThread was built on thread locals, so we'll need to try
                // even harder
                Field mLoadedApk = context.getClass().getField("mLoadedApk");
                mLoadedApk.setAccessible(true);
                Object apk = mLoadedApk.get(context);
                Field mActivityThreadField = apk.getClass().getDeclaredField("mActivityThread");
                mActivityThreadField.setAccessible(true);
                currentActivityThread = mActivityThreadField.get(apk);
            }
            return currentActivityThread;
        } catch (Throwable ignore) {
            return null;
        }
    }

    public static Method findMethod(Object instance, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Method method = clazz.getDeclaredMethod(name, parameterTypes);

                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                return method;
            } catch (NoSuchMethodException e) {
                // ignore and search next
            }
        }

        throw new NoSuchMethodException("Method "
                + name
                + " with parameters "
                + Arrays.asList(parameterTypes)
                + " not found in " + instance.getClass());
    }

    public static Constructor<?> findConstructor(Object instance, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Constructor<?> ctor = clazz.getDeclaredConstructor(parameterTypes);

                if (!ctor.isAccessible()) {
                    ctor.setAccessible(true);
                }

                return ctor;
            } catch (NoSuchMethodException e) {
                // ignore and search next
            }
        }

        throw new NoSuchMethodException("Constructor"
                + " with parameters "
                + Arrays.asList(parameterTypes)
                + " not found in " + instance.getClass());
    }

    public static Field findField(Object instance, String name) throws NoSuchFieldException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                return field;
            } catch (NoSuchFieldException e) {
                // ignore and search next
            }
        }

        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }
}
