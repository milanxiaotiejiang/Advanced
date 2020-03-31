package com.example.advanced.aspectjx.aspect;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * User: milan
 * Time: 2019/12/21 16:27
 * Des:
 */
@Aspect
public class ContentViewAspect {

    /**
     * Joinpoint(连接点):所谓连接点是指那些被拦截到的点。
     * Pointcut(切入点):所谓切入点是指我们要对哪些 Joinpoint 进行拦截的定义。
     * Advice(通知/增强):所谓通知是指拦截到 Joinpoint 之后所要做的事情就是通知。
     * Advice分类：
     * Before	前置通知, 在目标执行之前执行通知
     * After	后置通知, 目标执行后执行通知
     * Around	环绕通知, 在目标执行中执行通知, 控制目标执行时机
     * AfterReturning	后置返回通知, 目标返回时执行通知
     * AfterThrowing	异常通知, 目标抛出异常时执行通知
     * Introduction(引介)：引介是一种特殊的通知在不修改类代码的前提下, Introduction 可以在运行期为类 动态地添加一些方法或 Field。
     * Target(目标对象)：代理的目标对象。
     * Weaving(织入)：是指把增强应用到目标对象来创建新的代理对象的过程. AspectJ 采用编译期织入和类装在期织入 。
     * Proxy（代理）：一个类被 AOP 织入增强后，就产生一个结果代理类 。
     * Aspect(切面)：是切入点和通知（引介）的结合 。
     * <p>
     * 匹配的通配符
     * *：匹配任何数量字符；
     * ..：匹配任何数量字符的重复，如在类型模式中匹配任何数量子包；而在方法参数模式中匹配任何数量参数。
     * +：匹配指定类型的子类型；仅能作为后缀放在类型模式后边。
     * <p>
     * 切入点指示符
     * execution	用于匹配方法执行的连接点
     * within	用于匹配指定类型内的方法执行
     * this	用于匹配当前AOP代理对象类型的执行方法；注意是AOP代理对象的类型匹配，这样就可能包括引入接口也类型匹配
     * target	用于匹配当前目标对象类型的执行方法；注意是目标对象的类型匹配，这样就不包括引入接口也类型匹配
     * args	用于匹配当前执行的方法传入的参数为指定类型的执行方法
     *
     * @within 用于匹配所以持有指定注解类型内的方法
     * @target 用于匹配当前目标对象类型的执行方法，其中目标对象持有指定的注解
     * @args 用于匹配当前执行的方法传入的参数持有指定注解的执行
     * @annotation 用于匹配当前执行方法持有指定注解的方法
     */
    @Pointcut("execution(* android.app.Activity.setContentView(..))")
    public void activityContentView() {
    }

    @Around("activityContentView()")
    public Object activityContentViewAround(ProceedingJoinPoint proceedingJoinPoint) {
        Object result = null;
        try {
            Activity activity = (Activity) proceedingJoinPoint.getTarget();
            long startTime = System.currentTimeMillis();
            result = proceedingJoinPoint.proceed();
            String activityName = activity.getClass().getCanonicalName();

            Signature signature = proceedingJoinPoint.getSignature();
            String sign = "";
            String methodName = "";
            if (signature != null) {
                sign = signature.toString();
                methodName = signature.getName();
            }

            long duration = System.currentTimeMillis() - startTime;
            if (!TextUtils.isEmpty(activityName) && !TextUtils.isEmpty(sign) && sign
                    .contains(activityName)) {
                Log.e("ContentViewAspect", String.format("Activity：%s, %s方法执行了，用时%d ms", activityName, methodName, duration));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }
}
