package com.example.advanced.app.task;

import com.example.advanced.launchstarter.task.Task;
import com.example.advanced.network.Network;

/**
 * User: milan
 * Time: 2020/4/1 15:12
 * Des:
 */
public class InitNetworkTask extends Task {
    @Override
    public void run() {
        Network.init(mContext);
    }
}
