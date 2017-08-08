package com.zzj.chris.processalivedemo;

import android.util.Log;

import com.zzj.chris.processalivedemo.aliveservice.AliveServiceManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * package: com.zzj.chris.processalivedemo
 * <p>
 * description: 通知定时拉取
 * <p>
 * Created by chris on 2017/8/8.
 */

public class NotificationHelper implements AliveServiceManager.JobSchedule {
    private static final String TAG = "NotificationHelper";
    
    private static NotificationHelper mHelper;
    
    private boolean isWork;
    
    private NotificationHelper() {}
    
    public static NotificationHelper getInstance() {
        if (null == mHelper) {
            mHelper = new NotificationHelper();
        }
        return mHelper;
    }
    
    @Override
    public void start() {
        if (!isWork) {
            startTimer();
            isWork = true;
        }
    }
    
    @Override
    public void stop() {
//        stopTimer();
        isWork = false;
    }
    
    @Override
    public boolean isWorking() {
        return isWork;
    }
    
    
    
    private Timer timer;
    private int count = 0;
    private TimerTask task;
    
    private void startTimer() {
        if (null != timer) {
            timer.cancel();
        }
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "task " + count++);
            }
        };
        timer.schedule(task, 0, 1000);
    }
}
