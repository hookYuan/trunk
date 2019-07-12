package com.yuan.simple;


import leakcanary.LeakSentry;
import yuan.BaseAPP;

public class TrunkApplication extends BaseAPP {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakSentry.INSTANCE.getConfig().copy(true,
                true,
                true,
                true, 5000);
    }
}
