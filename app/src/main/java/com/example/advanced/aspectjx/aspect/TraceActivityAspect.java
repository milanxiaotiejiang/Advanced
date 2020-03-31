//package com.example.advanced.aspectjx.aspect;
//
//import android.app.Activity;
//import android.text.TextUtils;
//import android.util.Log;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//
///**
// * 检测activity生命周期耗时
// */
//@Aspect
//public class TraceActivityAspect {
//
//    @Pointcut("execution(* android.app.Activity.on**(..))")
//    public void activityOnXXX() {
//    }
//
//    @Around("activityOnXXX()")
//    public Object activityOnXXXAdvice(ProceedingJoinPoint proceedingJoinPoint) {
//        Object result = null;
//        try {
//            Activity activity = (Activity) proceedingJoinPoint.getTarget();
//            long startTime = System.currentTimeMillis();
//            result = proceedingJoinPoint.proceed();
//            String activityName = activity.getClass().getCanonicalName();
//
//            Signature signature = proceedingJoinPoint.getSignature();
//            String sign = "";
//            String methodName = "";
//            if (signature != null) {
//                sign = signature.toString();
//                methodName = signature.getName();
//            }
//
//            long duration = System.currentTimeMillis() - startTime;
//            if (!TextUtils.isEmpty(activityName) && !TextUtils.isEmpty(sign) && sign
//                    .contains(activityName)) {
//                Log.e("TraceActivityAspect", String.format("Activity：%s, %s方法执行了，用时%d ms", activityName, methodName, duration));
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//}
