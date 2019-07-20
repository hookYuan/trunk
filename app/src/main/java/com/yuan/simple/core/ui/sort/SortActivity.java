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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import yuan.core.list.GridDivider;
import yuan.core.mvp.BaseActivity;
import yuan.core.sort.SideBar;
import yuan.core.tool.Views;

import com.yuan.simple.R;
import com.yuan.simple.core.adapter.SortAdapter;
import com.yuan.simple.core.module.ChineseBean;
import com.yuan.simple.core.presenter.SortPresenter;
import com.yuan.simple.main.contract.MainContract;

import yuan.core.sort.ChineseSortUtil;

import java.util.ArrayList;


/**
 * 按照汉字排序
 */
public class SortActivity extends BaseActivity<SortPresenter>
        implements MainContract {

    /**
     * 数据源
     */
    private ArrayList<ChineseBean> data = new ArrayList<>();

    /**
     * recyclerView
     */
    private RecyclerView recyclerView;

    /**
     * 侧边栏
     */
    private SideBar sideBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sort;
    }

    @Override
    public void findViews() {
        recyclerView = Views.find(this, R.id.rlv_list);
        sideBar = findViewById(R.id.sideBar);
    }

    @Override
    public void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new GridDivider(mContext));
        recyclerView.setAdapter(new SortAdapter(data));
        sideBar.setRecyclerView(recyclerView, data);
        sideBar.setSortData(data);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void notifyDataChange(boolean isSuccess) {
        ChineseSortUtil.sortData(data);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
