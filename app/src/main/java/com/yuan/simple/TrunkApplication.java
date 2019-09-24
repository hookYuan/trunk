/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuan.simple;

import android.app.Application;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.squareup.leakcanary.LeakCanary;
import com.yuan.simple.main.MainActivity;

import yuan.core.title.ActionBarUtil;
import yuan.core.title.TitleBar;
import yuan.depends.refresh.RefreshUtil;

public class TrunkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //调试检查内存泄漏
        LeakCanary.install(this);
        LeakCanary.installedRefWatcher();
//        LeakSentry.INSTANCE.getConfig().copy(true,
//                true,
//                true,
//                true, 5000);

        //设置全局Title样式
        ActionBarUtil.setDefaultTheme(new ActionBarUtil.DefaultTheme() {
            @Override
            public void defaultTheme(TitleBar titleBar) {
                titleBar.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white))
                        .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            }
        });

        //设置全局SmartRefresh样式
        RefreshUtil.init();

        //设置异常重启
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Intent intent = new Intent(TrunkApplication.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TrunkApplication.this.startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
            }
        });
    }
}
