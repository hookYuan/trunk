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
package com.yuan.simple.core.ui.title;

import yuan.core.mvp.BaseActivity;
import yuan.core.title.ActionBarUtil;

import com.yuan.simple.R;

/**
 * Created by YuanYe on 2018/8/4.
 */
public class TitleBarActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_title_bar;
    }

    @Override
    public void initData() {
        ActionBarUtil.hideActionBar(this);
        addFragmentReflex(R.id.content, TitleBarFragment.class);
    }

    @Override
    public void setListener() {

    }
}
