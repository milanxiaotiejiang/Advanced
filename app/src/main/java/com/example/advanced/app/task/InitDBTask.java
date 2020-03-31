package com.example.advanced.app.task;


import com.example.advanced.launchstarter.task.Task;

/**
 * User: milan
 * Time: 2019/6/10 16:09
 * Des:
 */
public class InitDBTask extends Task {

    @Override
    public boolean needWait() {
        return true;
    }

    @Override
    public void run() {
        //初始化数据库
    }
}
