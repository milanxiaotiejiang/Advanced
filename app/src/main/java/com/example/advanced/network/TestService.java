package com.example.advanced.network;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * User: milan
 * Time: 2020/4/1 15:01
 * Des:
 */
public interface TestService {

    @GET("robot/check_update.php")
    Flowable<ResponseBody> updateProgram(@Query("type") int type);
}
