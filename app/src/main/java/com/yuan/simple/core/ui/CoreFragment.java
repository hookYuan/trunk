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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.GridDivider;
import yuan.core.list.RecyclerAdapter;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yuan.simple.R;
import com.yuan.simple.main.contract.MainContract;
import com.yuan.simple.main.module.CoreFunctionInfo;
import com.yuan.simple.core.presenter.CorePresenter;

import yuan.core.tool.Kits;
import yuan.core.tool.RouteUtil;
import yuan.core.widget.StateLayout;
import yuan.depends.ui.RecyclerViewFragment;

/**
 * core 示例首页
 *
 * @author YuanYe
 * @date 2019/7/19  12:26
 */
public class CoreFragment extends RecyclerViewFragment<CorePresenter, CoreFunctionInfo>
        implements MainContract {

    @Override
    protected int getItemLayoutId() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    protected void init(RecyclerView recyclerView, SmartRefreshLayout smartRefreshLayout, StateLayout mStateLayout) {
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.addItemDecoration(new GridDivider((int) Kits.Dimens.dpToPx(mContext, 0.8f),
                ContextCompat.getColor(mContext, R.color.colorDivider)));
        getPresenter().loadData(mData);
        mStateLayout.showLoading();
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, CoreFunctionInfo item, int position) {
        holder.setText(android.R.id.text1, mData.get(position).getName());
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, View view, int position) {
                RouteUtil.open(mContext, mData.get(position).getClazz());
            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {

            }
        });
    }

    @Override
    public void notifyDataChange(boolean isSuccess) {
        mAdapter.notifyDataSetChanged();
        //状态显示控制
        if (isSuccess) mStateLayout.showContent();
        else mStateLayout.showEmpty();
    }
}
