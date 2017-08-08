package com.zzj.chris.processalivedemo.aliveservice;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * package: com.zzj.chris.processalivedemo.aliveservice
 *
 * description: LocalJobService 进程保活——仅在Android5.0以上版本中有效
 *
 * Created by chris on 2017/8/7.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LocalJobService extends JobService {
    private static final String TAG = "LocalJobService";
    private static final String KEY_LOCAL_SERVICE_NAME = LocalService.class.getName();
    private static final String KEY_REMOTE_SERVICE_NAME = RemoteService.class.getName();
    
    private JobInfo jobInfo;
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        scheduleJob(getJobInfo());
        return START_NOT_STICKY;
    }
    
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob");
        boolean isLocalServiceWork = isServiceWork(this, KEY_LOCAL_SERVICE_NAME);
        boolean isRemoteServiceWork = isServiceWork(this, KEY_REMOTE_SERVICE_NAME);
        if (!isLocalServiceWork) {
            this.startService(new Intent(this, LocalService.class));
        }
        if (!isRemoteServiceWork) {
            this.startService(new Intent(this, RemoteService.class));
        }
        return true;
    }
    
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob");
        scheduleJob(getJobInfo());
        return true;
    }
    
    /**
     * 将任务作业发送到作业调度中去
     */
    public void scheduleJob(JobInfo info) {
        Log.i(TAG, "scheduleJob");
        JobScheduler js = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        js.schedule(info);
    }
    
    public JobInfo getJobInfo() {
        Log.i(TAG, "getJobInfo");
        if (null == jobInfo) {
            JobInfo.Builder builder = new JobInfo.Builder(0, new ComponentName(this, LocalJobService.class));
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setPersisted(true);
            builder.setRequiresCharging(false);
            builder.setRequiresDeviceIdle(false);
            //间隔100毫秒
            builder.setPeriodic(100);
            jobInfo = builder.build();
        }
        return jobInfo;
    }
    
    /**
     * 判断服务是否正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        if (TextUtils.isEmpty(serviceName)) {
            return false;
        }
        
        boolean isWorking = false;
        ActivityManager manager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : list) {
            String name = info.service.getClassName();
            if (serviceName.equals(name)) {
                isWorking = true;
                break;
            }
        }
        return isWorking;
    }
}

