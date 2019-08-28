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
package com.yuan.simple.core.ui.sort;

import yuan.core.list.GridDivider;
import yuan.core.sort.SideBar;

import com.yuan.simple.R;
import com.yuan.simple.core.adapter.SortAdapter;
import com.yuan.simple.core.module.ChineseBean;
import com.yuan.simple.core.presenter.SortPresenter;
import com.yuan.simple.main.contract.MainContract;

import yuan.core.sort.ChineseSortUtil;
import yuan.core.ui.Adapter;
import yuan.core.ui.RecyclerActivity;
import yuan.core.ui.Title;


/**
 * 按照汉字排序
 */
@Adapter(adapter = SortAdapter.class)
@Title(titleStr = "拼音排序")
public class SortActivity extends RecyclerActivity<SortPresenter, ChineseBean>
        implements MainContract {

    /**
     * 侧边栏
     */
    private SideBar sideBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sort;
    }


    @Override
    public void initData() {
        mRecyclerView.addItemDecoration(new GridDivider());
        getPresenter().loadData(mData);
        sideBar = findViewById(R.id.sideBar);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void notifyDataChange(boolean isSuccess) {
        ChineseSortUtil.sortData(mData);
        sideBar.setRecyclerView(mRecyclerView, mData);
        mAdapter.notifyDataSetChanged();
    }
}
