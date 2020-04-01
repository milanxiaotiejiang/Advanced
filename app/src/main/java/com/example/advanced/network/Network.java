package com.example.advanced.network;

import android.content.Context;

import androidx.annotation.NonNull;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

/**
 * User: milan
 * Time: 2020/4/1 15:10
 * Des:
 */
public class Network implements TestApi {

    private static TestImpl sTestImpl;

    private volatile static Network mYoutucode;

    private Network() {
    }

    public static Network getSingleInstance() {
        if (null == mYoutucode) {
            synchronized (Network.class) {
                if (null == mYoutucode) {
                    mYoutucode = new Network();
                }
            }
        }
        return mYoutucode;
    }

    public static Network init(@NonNull Context context) {

        OkhttpManager.getInstance().init();
        initImplement(context);
        return getSingleInstance();
    }


    private static void initImplement(Context context) {
        try {
            sTestImpl = new TestImpl(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Flowable<ResponseBody> updateProgram(int type) {
        return sTestImpl.updateProgram(type);
    }
}
