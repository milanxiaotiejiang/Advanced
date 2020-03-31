package com.example.advanced.aspectjx.aspect;


import android.util.Log;

import com.example.advanced.aspectjx.annotation.TimeSpend;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * User: milan
 * Time: 2019/11/19 16:38
 * Des:
 */
@Aspect
public class TimeSpendAspect {

    @Pointcut("execution(@com.example.advanced.aspectjx.annotation.TimeSpend * *(..))")
    public void methodTime() {
    }

    @Around("methodTime()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String funName = methodSignature.getMethod().getAnnotation(TimeSpend.class).value();
        //统计时间
        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - begin;
        Log.e("TimeSpendAspect", String.format("功能：%s,%s类的%s方法执行了，用时%d ms", funName, className, methodName, duration));
        return result;
    }
}
