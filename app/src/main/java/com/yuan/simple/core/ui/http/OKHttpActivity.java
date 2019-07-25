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

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.RecyclerAdapter;
import yuan.core.title.ActionBarUtil;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yuan.simple.R;

import yuan.core.widget.StateLayout;
import yuan.depends.ui.RecyclerViewActivity;

/**
 * 使用OKHttpUtil的一个事例
 */
public class OKHttpActivity extends RecyclerViewActivity<OkHttpPresenter, String> {

    @Override
    protected void init(RecyclerView recyclerView, SmartRefreshLayout smartRefreshLayout, StateLayout mStateLayout) {
        ActionBarUtil.create(this).setTitleText("OKUtil")
                .setLeftClickFinish()
                .setTextColor(getResources().getColor(R.color.white))
                .setLeftIcon(getResources().getDrawable(R.drawable.ic_base_back_white))
                .setBackgroundColor(
                        getResources().getColor(R.color.colorPrimary));

        getPresenter().createData(mData);
        super.init(recyclerView, smartRefreshLayout, mStateLayout);
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

    @Override
    protected int getItemLayoutId(int position) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, String item, int position) {
        holder.setText(android.R.id.text1, mData.get(position));
    }
}
