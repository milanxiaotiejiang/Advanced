package com.example.advanced.aspectjx.aspect;

import android.util.Log;
import android.view.View;

import com.example.advanced.R;
import com.example.advanced.aspectjx.annotation.ClickLimit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * User: milan
 * Time: 2019/11/20 16:20
 * Des:
 */

@Aspect
public class ClickLimitAspect {

    private static final int CHECK_FOR_DEFAULT_TIME = 500;

    private static final String POINTCUT_ON_ANNOTATION =
            "execution(@com.example.advanced.aspectjx.annotation.ClickLimit * *(..))";

    @Pointcut(POINTCUT_ON_ANNOTATION)
    public void onAnnotationClick() {
    }

    /**
     * joinPoint.proceed() 执行注解所标识的代码
     *
     * @After 可以在方法前插入代码
     * @Before 可以在方法后插入代码
     * @Around 可以在方法前后各插入代码
     */
    @Around("onAnnotationClick()")
    public void processJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Signature signature = joinPoint.getSignature();
            if (!(signature instanceof MethodSignature)) {
                joinPoint.proceed();
                return;
            }

            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            boolean isHasLimitAnnotation = method.isAnnotationPresent(ClickLimit.class);
            int intervalTime = CHECK_FOR_DEFAULT_TIME;
            if (isHasLimitAnnotation) {
                ClickLimit clickLimit = method.getAnnotation(ClickLimit.class);
                int limitTime = clickLimit.value();
                if (limitTime <= 0) {
                    joinPoint.proceed();
                    return;
                }
                intervalTime = limitTime;
            }


            Object[] args = joinPoint.getArgs();
            View view = getViewFromArgs(args);
            if (view == null) {
                joinPoint.proceed();
                return;
            }

            Object viewTimeTag = view.getTag(R.integer.yxhuang_click_limit_tag_view);
            if (viewTimeTag == null) {
                proceedAnSetTimeTag(joinPoint, view);
                return;
            }

            long lastClickTime = (long) viewTimeTag;
            if (lastClickTime <= 0) {
                proceedAnSetTimeTag(joinPoint, view);
                return;
            }

            if (!canClick(lastClickTime, intervalTime)) {
                return;

            }
            proceedAnSetTimeTag(joinPoint, view);
        } catch (Throwable e) {
            e.printStackTrace();
            joinPoint.proceed();
        }
    }

    private void proceedAnSetTimeTag(ProceedingJoinPoint joinPoint, View view) throws Throwable {
        view.setTag(R.integer.yxhuang_click_limit_tag_view, System.currentTimeMillis());
        joinPoint.proceed();
    }


    private View getViewFromArgs(Object[] args) {
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof View) {
                return (View) arg;
            }
        }
        return null;
    }

    /**
     * 判断是否达到可以点击的时间间隔
     */
    private boolean canClick(long lastClickTime, int intervalTime) {
        long currentTime = System.currentTimeMillis();
        long realIntervalTime = currentTime - lastClickTime;
        Log.e("ClickLimitAspect", "与上次点击相差 " + realIntervalTime + " 毫秒");
        return realIntervalTime >= intervalTime;
    }
}
