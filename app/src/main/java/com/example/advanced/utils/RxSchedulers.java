package com.example.advanced.utils;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulers {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelperObservable() { //compose简化线程
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        //作用于该操作符之前的 Observable 的创建操符作以及 doOnSubscribe 操作符
                        .subscribeOn(Schedulers.io())//请求网络线程
                        //作用于该操作符之后操作符直到出现新的observeOn操作符
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> rxSchedulerHelperFlowable() { //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream
                        //作用于该操作符之前的 Observable 的创建操符作以及 doOnSubscribe 操作符
                        .subscribeOn(Schedulers.io())//请求网络线程
                        //作用于该操作符之后操作符直到出现新的observeOn操作符
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}