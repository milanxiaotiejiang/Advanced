package com.example.advanced.launchstarter.task;

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
public abstract class MainTask extends Task {

    @Override
    public boolean runOnMainThread() {
        return true;
    }
}
