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
package com.yuan.simple.core.ui.roundview;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import yuan.core.mvp.BaseActivity;
import yuan.core.title.ActionBarUtil;
import yuan.core.title.TitleBar;

import com.yuan.simple.R;
import com.yuan.simple.core.adapter.RoundTextAdapter;
import com.yuan.simple.core.presenter.RoundTextPresenter;

/**
 * 通过RoundView 实现各种常见按钮的各种样式
 *
 * @author YuanYe
 * @date 2019/7/21  10:04
 */
public class RoundTextActivity extends BaseActivity<RoundTextPresenter> {

    private static final String TAG = "RoundTextActivity";
    /**
     * gridView
     */
    private GridView gridView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_round_text;
    }

    @Override
    public void findViews() {
        gridView = (GridView) findViewById(R.id.gv_simple_demo);
    }

    @Override
    public void initData() {

        gridView.setAdapter(new RoundTextAdapter(getPresenter().loadData(), gridView));

        ActionBarUtil.create(this)
                .setLeftIcon(R.drawable.ic_base_back_white)
                .setLeftClickFinish()
                .setTitleText("RoundView");
    }

    @Override
    public void setListener() {

    }
}
