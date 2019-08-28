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
package com.yuan.simple.core.ui;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.GridDivider;
import yuan.core.list.RecyclerAdapter;

import com.yuan.simple.core.adapter.TextAdapter;
import com.yuan.simple.core.module.SubjectBean;
import com.yuan.simple.main.contract.MainContract;
import com.yuan.simple.core.presenter.CorePresenter;

import yuan.core.tool.RouteUtil;
import yuan.core.ui.Adapter;
import yuan.core.ui.RecyclerFragment;

/**
 * core 示例首页
 *
 * @author YuanYe
 * @date 2019/7/19  12:26
 */
@Adapter(adapter = TextAdapter.class)
public class CoreFragment extends RecyclerFragment<CorePresenter, SubjectBean>
        implements MainContract {

    @Override
    protected void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerView.addItemDecoration(new GridDivider());
    }

    @Override
    public void initData() {
        getPresenter().loadData(mData);
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, View view, int position) {
                RouteUtil.open(mContext, mData.get(position).getClazz());
            }
        });
    }

    @Override
    public void notifyDataChange(boolean isSuccess) {
        mAdapter.notifyDataSetChanged();
    }
}
