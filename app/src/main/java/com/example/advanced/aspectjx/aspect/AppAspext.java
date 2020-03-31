//package com.example.advanced.aspectjx.aspect;
//
//import com.robot.seabreeze.log.Logger;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//
///**
// * User: milan
// * Time: 2020/3/31 19:07
// * Des:
// */
//@Aspect
//public class AppAspext {
//    @Around("call (* com.example.advanced.app.App.**(..))")
//    public void getTime(ProceedingJoinPoint joinPoint) {
//        Signature signature = joinPoint.getSignature();
//        String name = signature.toShortString();
//        long time = System.currentTimeMillis();
//        try {
//            joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        Logger.i(name + " cost" + (System.currentTimeMillis() - time));
//    }
//
//}
