package com.example.advanced.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.R;
import com.robot.seabreeze.log.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

//@Xml(layouts = "activity_u_i")
public class UIActivity extends AppCompatActivity {
    ImageView imageView;
    TextView tvName;
    TextView tvOhter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 使用AsyncLayoutInflater进行布局的加载
//        AsyncLayoutLoader.getInstance(App.getApp())
//                .inflate(R.layout.activity_u_i, null, (view, resid, parent) -> {
//                    setContentView(view);
//                    initView();
//                });
        super.onCreate(savedInstanceState);
        //内部分别使用了IO和反射的方式去加载布局解析器和创建对应的View
//        setContentView(R.layout.activity_u_i);


//        X2C.setContentView(this, R.layout.activity_u_i);
//        initView();

        //不推荐，推荐Flutter
//        // 1、将Activity的Context对象保存到ComponentContext中，并同时初始化
//        // 一个资源解析者实例ResourceResolver供其余组件使用。
//        ComponentContext componentContext = new ComponentContext(this);
//        // 2、Text内部使用建造者模式以实现组件属性的链式调用，下面设置的text、
//        // TextColor等属性在Litho中被称为Prop，此概念引申字React。
//        Text lithoText = Text.create(componentContext)
//                .text("Litho text")
//                .textSizeDip(64)
//                .textColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
//                .build();
//        // 3、设置一个LithoView去展示Text组件：LithoView.create内部新建了一个
//        // LithoView实例，并用给定的Component（lithoText）进行初始化
//        setContentView(LithoView.create(componentContext, lithoText));

//        ViewPropertyAnimator animator = v.animate().scaleY(2).setDuration(2000);
//        onStartBeforeConfig(animator, v);
//        animator.start();
    }

    private void initView() {
        // findViewById、视图操作等
        imageView = findViewById(R.id.imageView);
        tvName = findViewById(R.id.tv_name);
        tvOhter = findViewById(R.id.tv_other);
        if (tvName != null) {
            tvName.setText("xxxxxxxxxxxxx");
        }
        Logger.e(imageView != null);
    }

    /**
     * 使用反射的方式去创建对应View的ViewPropertyAnimatorRT(非hide类)
     */
    private static Object createViewPropertyAnimatorRT(View view) {
        try {
            Class<?> animRtClazz = Class.forName("android.view.ViewPropertyAnimatorRT");
            Constructor<?> animRtConstructor = animRtClazz.getDeclaredConstructor(View.class);
            animRtConstructor.setAccessible(true);
            Object animRt = animRtConstructor.newInstance(view);
            return animRt;
        } catch (Exception e) {
            Logger.d("创建ViewPropertyAnimatorRT出错,错误信息:" + e.toString());
            return null;
        }
    }

    private static void setViewPropertyAnimatorRT(ViewPropertyAnimator animator, Object rt) {
        try {
            Class<?> animClazz = Class.forName("android.view.ViewPropertyAnimator");
            Field animRtField = animClazz.getDeclaredField("mRTBackend");
            animRtField.setAccessible(true);
            animRtField.set(animator, rt);
        } catch (Exception e) {
            Logger.d("设置ViewPropertyAnimatorRT出错,错误信息:" + e.toString());
        }
    }

    /**
     * 在animator.start()即执行动画开始之前配置的方法
     */
    public static void onStartBeforeConfig(ViewPropertyAnimator animator, View view) {
        Object rt = createViewPropertyAnimatorRT(view);
        setViewPropertyAnimatorRT(animator, rt);
    }
}
