package com.example.advanced.network;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.advanced.app.Constants;

import java.lang.reflect.ParameterizedType;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
public class BaseImpl<Service> {

    private static Retrofit mRetrofit;
    protected Service mService;

    public BaseImpl() {
    }

    public BaseImpl(@NonNull Context context) {

        initRetrofit(context);

        mService = mRetrofit.create(getServiceClass());
    }

    private void initRetrofit(Context context) {
        if (null != mRetrofit)
            return;

        // 配置 Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_LIBRARY_BASE)                         // 设置 base url
                .client(OkhttpManager.getInstance().getOkhttpClient())                                     // 设置 client
                .addConverterFactory(GsonConverterFactory.create()) // 设置 Json 转换工具
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    protected Class<Service> getServiceClass() {
        return (Class<Service>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
