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
package com.yuan.simple.expand.ui.db;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yuan.simple.R;
import com.yuan.simple.expand.presenter.DBPresenter;
import com.yuan.simple.main.contract.MainContract;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.GridDivider;
import yuan.core.list.RecyclerAdapter;
import yuan.core.title.ActionBarUtil;
import yuan.core.tool.Kits;
import yuan.core.widget.StateLayout;
import yuan.depends.ui.RecyclerViewActivity;

/**
 * 数据库操作
 */
public class DBActivity extends RecyclerViewActivity<DBPresenter, String>
        implements MainContract {

    @Override
    protected int getItemLayoutId(int position) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    protected void init(RecyclerView recyclerView, SmartRefreshLayout smartRefreshLayout, StateLayout mStateLayout) {
        ActionBarUtil.create(this)
                .setTitleText("DBActivity")
                .setLeftClickFinish()
                .setTitleTextColor(getColor2(R.color.white))
                .setLeftIcon(getDrawable2(R.drawable.ic_base_back_white))
                .setBackgroundColor(getColor2(R.color.colorPrimary));

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.addItemDecoration(new GridDivider((int) Kits.Dimens.dpToPx(mContext, 0.8f),
                ContextCompat.getColor(mContext, R.color.colorDivider)));
        getPresenter().loadData(mData);
        mStateLayout.showLoading();
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, String item, int position) {
        holder.setText(android.R.id.text1, mData.get(position));
    }


    @Override
    public void setListener() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, View view, int position) {
                switch (position) {
                    case 1:
                        getPresenter().initDatabase();
                        break;
                    case 2:
                        getPresenter().createTable();
                        break;
                    case 3:
                        getPresenter().insertData();
                        break;
                    case 5:
                        getPresenter().deleteTable();
                        break;
                    case 6:
                        getPresenter().searchTable();
                        break;
                }
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
