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
package com.yuan.simple.core.ui.input;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;

import yuan.core.mvp.BaseActivity;

import com.yuan.simple.R;

/**
 * 解决键盘弹出引起的布局上窜
 */
public class InputActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_input;
    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

    }

}
