package com.zzj.chris.processalivedemo.aliveservice;

import android.content.Context;

/**
 * package: com.zzj.chris.processalivedemo.aliveservice
 * <p>
 * description: 保活service管理接口
 * <p>
 * Created by chris on 2017/8/7.
 */

public interface AliveServiceManager {
    
    void start(Context context);
    
    void stop(Context context);
    
    void destroy();
    
    boolean startJob();
    
    /**
     * 后台工作接口
     */
    interface JobSchedule {
        
        void start();
        
        void stop();
        
        boolean isWorking();
    }
}
