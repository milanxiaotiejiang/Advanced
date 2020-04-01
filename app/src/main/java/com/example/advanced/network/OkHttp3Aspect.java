package com.example.advanced.network;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import okhttp3.OkHttpClient;

/**
 * User: milan
 * Time: 2020/4/1 13:48
 * Des:
 */
@Aspect
public class OkHttp3Aspect {
    @Pointcut("call(public okhttp3.OkHttpClient build())")
    public void build() {

    }

    @Around("build()")
    public Object aroundBuild(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();

        if (target instanceof OkHttpClient.Builder) {
            OkHttpClient.Builder builder = (OkHttpClient.Builder) target;
            builder.addInterceptor(new AspectInterceptor());
        }

        return joinPoint.proceed();
    }
}
