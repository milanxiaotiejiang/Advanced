package com.example.advanced.electric;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

/**
 * User: milan
 * Time: 2020/4/1 18:46
 * Des:
 */
public class JobSchedulerService extends JobService {
    private String TAG = JobSchedulerService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob:" + jobParameters.getJobId());

        if (true) {
            // JobService在主线程运行，如果我们这里需要处理比较耗时的业务逻辑需单独开启一条子线程来处理并返回true，
            // 当给定的任务完成时通过调用jobFinished(JobParameters params, boolean needsRescheduled)告知系统。

            //假设开启一个线程去下载文件
            new DownloadTask().execute(jobParameters);

            return true;

        } else {
            //如果只是在本方法内执行一些简单的逻辑话返回false就可以了
            return false;
        }
    }

    /**
     * 比如我们的服务设定的约束条件为在WIFI状态下运行，结果在任务运行的过程中WIFI断开了系统
     * 就会通过回掉onStopJob()来通知我们停止运行，正常的情况下不会回掉此方法
     *
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob:" + jobParameters.getJobId());

        //如果需要服务在设定的约定条件再次满足时再次执行服务请返回true，反之false
        return true;
    }

    class DownloadTask extends AsyncTask<JobParameters, Object, Object> {
        JobParameters mJobParameters;

        @Override
        protected Object doInBackground(JobParameters... jobParameterses) {
            mJobParameters = jobParameterses[0];

            //比如说我们这里处理一个下载任务
            //或是处理一些比较复杂的运算逻辑
            //...

            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //如果在onStartJob()中返回true的话,处理完成逻辑后一定要执行jobFinished()告知系统已完成，
            //如果需要重新安排服务请true，反之false
            jobFinished(mJobParameters, false);
        }
    }
}
