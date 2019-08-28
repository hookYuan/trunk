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
package com.yuan.simple.core.ui.http;


import android.view.View;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.GridDivider;
import yuan.core.list.RecyclerAdapter;

import com.yuan.simple.core.adapter.TextAdapter;
import com.yuan.simple.core.module.SubjectBean;

import yuan.core.ui.Adapter;
import yuan.core.ui.RecyclerActivity;
import yuan.core.ui.Title;

/**
 * 使用OKHttpUtil的一个事例
 */
@Title(titleStr = "OKUtil")
@Adapter(adapter = TextAdapter.class)
public class OKHttpActivity extends RecyclerActivity<OkHttpPresenter, SubjectBean> {

    @Override
    public void initData() {
        mRecyclerView.addItemDecoration(new GridDivider());
        getPresenter().createData(mData);
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, View view, int position) {
                switch (position) {
                    case 0:
                        getPresenter().get();
                        break;
                    case 1:
                        getPresenter().get2();
                        break;
                    case 3:
                        getPresenter().downloadFile();
                        break;
                    case 4:
                        getPresenter().showCacheSize();
                        break;
                    case 5:
                        getPresenter().delCache();
                        break;
                }
            }
        });
    }
}
