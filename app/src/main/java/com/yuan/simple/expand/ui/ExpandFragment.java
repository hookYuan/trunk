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
package com.yuan.simple.expand.ui;

import android.util.Log;

import com.yuan.simple.R;

import yuan.core.mvp.BaseFragment;

/**
 * Created by YuanYe on 2018/4/13.
 */
public class ExpandFragment extends BaseFragment {
    private static final String TAG = "ExpandFragment";

    @Override
    public int getLayoutId() {
        return R.layout.frag_three_layout;
    }

    @Override
    public void initData() {
        Log.i(TAG, "已经执行");
    }

    @Override
    public void setListener() {

    }

}
