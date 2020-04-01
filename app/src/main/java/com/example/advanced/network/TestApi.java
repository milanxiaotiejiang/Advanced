package com.example.advanced.network;

import io.reactivex.Flowable;

/**
 * User: milan
 * Time: 2020/4/1 15:01
 * Des:
 */
public interface TestApi {

    Flowable updateProgram(int type);
}
