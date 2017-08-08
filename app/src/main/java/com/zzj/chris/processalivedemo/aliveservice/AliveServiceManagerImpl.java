package com.zzj.chris.processalivedemo.aliveservice;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.zzj.chris.processalivedemo.NotificationHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * package: com.zzj.chris.processalivedemo.aliveservice
 * <p>
 * description: 保活service管理
 * <p>
 * Created by chris on 2017/8/7.
 */

public class AliveServiceManagerImpl implements AliveServiceManager {
    private static final Byte[] LOCK = new Byte[0];
    
    private static AliveServiceManager mInstance;
    
    private List<JobSchedule> mJobObservers;
    
    private AliveServiceManagerImpl() {}
    
    public static AliveServiceManager getInstance() {
        if (null == mInstance){
            synchronized (LOCK) {
                if (null == mInstance){
                    mInstance = new AliveServiceManagerImpl();
                }
            }
        }
        return mInstance;
    }
    
    @Override
    public void start(Context context) {//app.getContext
        context.startService(new Intent(context, LocalService.class));
        context.startService(new Intent(context, RemoteService.class));
        if (Build.VERSION.SDK_INT >= 21) {
            context.startService(new Intent(context, LocalJobService.class));
        }
    }
    
    @Override
    public void stop(Context context) {
        
    }
    
    @Override
    public void destroy() {
        if (null != mJobObservers) {
            mJobObservers.clear();
            mJobObservers = null;
        }
        mInstance = null;
    }
    
    @Override
    public boolean startJob() {
        boolean isWorking = false;
        getObservers();
        for (JobSchedule schedule : mJobObservers) {
            if (!schedule.isWorking()) {
                schedule.start();
                isWorking = true;
            }
        }
        return isWorking;
    }
    
    private void getObservers() {
        if (null == mJobObservers) {
            mJobObservers = new ArrayList<>();
        }
        if (mJobObservers.size() <= 0) {
            mJobObservers.add(NotificationHelper.getInstance());
        }
    }
}
