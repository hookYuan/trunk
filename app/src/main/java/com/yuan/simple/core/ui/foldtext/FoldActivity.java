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
package com.yuan.simple.core.ui.foldtext;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;

import yuan.core.list.BaseViewHolder;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yuan.simple.core.presenter.FoldPresenter;
import com.yuan.simple.R;
import com.yuan.simple.main.contract.MainContract;

import yuan.core.title.ActionBarUtil;
import yuan.core.widget.FoldTextView;
import yuan.core.widget.StateLayout;
import yuan.depends.ui.RecyclerViewActivity;

public class FoldActivity extends RecyclerViewActivity<FoldPresenter, String>
    implements MainContract {

    /**
     * 用于记录是否展开的状态
     */
    private SparseBooleanArray collapsedStatus = new SparseBooleanArray();

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_fold;
    }

    @Override
    protected void init(RecyclerView recyclerView, SmartRefreshLayout smartRefreshLayout, StateLayout mStateLayout) {
        ActionBarUtil.create(this).setTitleText("折叠TextView")
                .setLeftClickFinish()
                .setTextColor(ContextCompat.getColor(mContext, R.color.white))
                .setLeftIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_base_back_white))
                .setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        super.init(recyclerView, smartRefreshLayout, mStateLayout);
        getPresenter().loadData(mData);
        mStateLayout.showLoading();
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, String item, int position) {
        FoldTextView textView = holder.getView(R.id.ctv_content);
        textView.setTextList(mData.get(position), collapsedStatus, position);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void notifyDataChange(boolean isSuccess) {
        mAdapter.notifyDataSetChanged();
        //状态显示控制
        if (isSuccess) mStateLayout.showContent();
        else mStateLayout.showEmpty();
    }
}
