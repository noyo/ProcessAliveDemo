package com.zzj.chris.processalivedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zzj.chris.processalivedemo.aliveservice.AliveServiceManagerImpl;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        AliveServiceManagerImpl.getInstance().start(this);
    }
}
