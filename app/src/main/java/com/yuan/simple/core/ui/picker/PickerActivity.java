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
package com.yuan.simple.core.ui.picker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.RecyclerAdapter;
import yuan.core.title.ActionBarUtil;
import yuan.core.tool.PickerUtil;
import yuan.core.tool.ToastUtil;
import yuan.core.widget.StateLayout;
import yuan.depends.ui.RecyclerViewActivity;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yuan.simple.R;
import com.yuan.simple.core.presenter.PickerPresenter;
import com.yuan.simple.main.contract.MainContract;

/**
 * 系统常用选择器
 * 1.图片选择器
 * 2.通讯录选择器
 * 3.相机拍照
 *
 * @author YuanYe
 * @date 2019/7/19  23:59
 */
public class PickerActivity extends RecyclerViewActivity<PickerPresenter, String>
        implements MainContract {

    @Override
    protected int getItemLayoutId(int position) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    protected void init(RecyclerView recyclerView, SmartRefreshLayout smartRefreshLayout
            , StateLayout mStateLayout) {
        ActionBarUtil.create(this)
                .setTitleText("PickerUtil")
                .setLeftClickFinish()
                .setTitleTextColor(getColor2(R.color.white))
                .setLeftIcon(getDrawable2(R.drawable.ic_base_back_white))
                .setBackgroundColor(getColor2(R.color.colorPrimary));
        super.init(recyclerView, smartRefreshLayout, mStateLayout);
        mStateLayout.showContent();
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
                    case 0:
                        PickerUtil.startCamera(mContext, new PickerUtil.SelectBack() {
                            @Override
                            public void onBack(String path) {
                                ToastUtil.showShort(mContext, path);
                            }
                        });
                        break;
                    case 1:
                        PickerUtil.startAlbum(mContext, new PickerUtil.SelectBack() {
                            @Override
                            public void onBack(String path) {
                                ToastUtil.showShort(mContext, path);
                            }
                        });
                        break;
                    case 2:
                        PickerUtil.startCameraAlbum(mContext, new PickerUtil.SelectBack() {
                            @Override
                            public void onBack(String path) {
                                ToastUtil.showShort(mContext, path);
                            }
                        });
                        break;
                    case 3:
                        PickerUtil.startAddressBook(mContext, new PickerUtil.ContactBack() {
                            @Override
                            public void onBack(String name, String phone) {
                                ToastUtil.showShort(mContext, "姓名：" + name + "  电话：" + phone);
                            }
                        });
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
