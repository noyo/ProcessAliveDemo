package com.zzj.chris.processalivedemo.aliveservice;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zzj.chris.processalivedemo.IServiceAidlInterface;
import com.zzj.chris.processalivedemo.R;

/**
 * package: com.zzj.chris.processalivedemo.aliveservice
 *
 * description: RemoteService 双进程保活——RemoteService
 *
 * Created by chris on 2017/8/7.
 */
class RemoteService extends Service implements ServiceConnection {
    
    private static final String TAG = "RemoteService";
    
    private RemoteServiceBinder remoteBinder;
    
    private Notification notification;
    
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        if (null == remoteBinder) {
            remoteBinder = new RemoteServiceBinder();
        }
        super.onCreate();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        //提高服务优先级，降低被杀的概率
        startForeground(startId, getNotification());
        bindService(new Intent(this, LocalService.class), this, Context.BIND_IMPORTANT);
        return START_STICKY;
    }
    
    @NonNull
    private Notification getNotification() {
        if (null == notification) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setDefaults(NotificationCompat.FLAG_AUTO_CANCEL);
            builder.setContentTitle(TAG);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSubText(TAG);
            builder.setContentText(TAG);
            builder.setWhen(System.currentTimeMillis());
            /*PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pi);*/
            notification = builder.build();
        }
        return notification;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return remoteBinder;
    }
    
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected");
    }
    
    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected");
        startService(new Intent(this, LocalService.class));
        bindService(new Intent(this, LocalService.class), this, Context.BIND_IMPORTANT);
    }
    
    private class RemoteServiceBinder extends IServiceAidlInterface.Stub {
        
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            Log.d(TAG, "basicTypes");
        }
    }
}
