package com.example.advanced.network;

import android.content.Context;

import androidx.annotation.NonNull;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

/**
 * User: milan
 * Time: 2020/4/1 15:03
 * Des:
 */
public class TestImpl extends BaseImpl<TestService> implements TestApi {

    public TestImpl(@NonNull Context context) {
        super(context);
    }

    @Override
    public Flowable<ResponseBody> updateProgram(int type) {
        return mService.updateProgram(type);
    }

}
