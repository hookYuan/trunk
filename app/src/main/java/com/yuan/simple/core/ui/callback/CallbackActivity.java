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
package com.yuan.simple.core.ui.callback;

import android.view.View;

import com.yuan.simple.R;

import yuan.core.function.CallbackManager;
import yuan.core.function.CallbackNoParamNoResult;
import yuan.core.mvp.BaseActivity;
import yuan.core.tool.ToastUtil;

/**
 * 描述：
 * <p>
 * 1.0 2019.7.1  测试CallbackManager 是否有内存泄漏
 *
 * @author yuanye
 * @date 2019/7/11 15:06
 */
public class CallbackActivity extends BaseActivity {

    private static final String TAG = "CallbackActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_callback;
    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {
        findViewById(R.id.btn_test)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallbackManager.get().invoke(CallbackActivity.this);
                    }
                });

        CallbackManager.get().setCallback(this, new CallbackNoParamNoResult() {
            @Override
            public void callback() {
                ToastUtil.showShort(mContext, "测试回调点击事件");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallbackManager.get().remove(this);
    }

    /**
     * 以下代码会导致内存泄漏
     * 测试内存泄漏代码
     */
    private void checkMemoryLeak() {
        /*以下内容为测试内存泄漏*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
