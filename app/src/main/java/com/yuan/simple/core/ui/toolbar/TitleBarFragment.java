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
package com.yuan.simple.core.ui.toolbar;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.GridDivider;
import yuan.core.list.RecyclerAdapter;
import yuan.core.title.ActionBarUtil;
import yuan.core.title.StatusUtil;
import yuan.core.title.TitleBar;
import yuan.core.tool.RouteUtil;
import yuan.core.tool.ToastUtil;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yuan.simple.R;
import com.yuan.simple.core.module.SubjectBean;
import com.yuan.simple.core.presenter.TitleBarPresenter;
import com.yuan.simple.main.contract.MainContract;

import yuan.core.tool.Kits;
import yuan.core.widget.StateLayout;
import yuan.depends.ui.RecyclerViewActivity;
import yuan.depends.ui.RecyclerViewFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

/**
 * create by Yuan ye.
 * download truck before use this
 *
 * @author YuanYe
 * @date 2019/7/19  23:55
 */
public class TitleBarFragment extends RecyclerViewFragment<TitleBarPresenter, SubjectBean>
        implements MainContract {

    @Override
    protected int getItemLayoutId() {
        return android.R.layout.simple_list_item_2;
    }

    @Override
    protected void init(RecyclerView recyclerView, SmartRefreshLayout smartRefreshLayout, StateLayout mStateLayout) {
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        //动态更改列数
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int code = mData.get(position).getType();
                if (code == 1000 || code == 2000 || code == 3000
                        || code == 4000) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(manager);
        //add divider.
        recyclerView.addItemDecoration(new GridDivider((int) Kits.Dimens.dpToPx(mContext, 0.8f)
                , ContextCompat.getColor(mContext, R.color.colorDivider)));

        mStateLayout.showLoading();
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, SubjectBean item, int position) {
        holder.setText(android.R.id.text1, mData.get(position).getName());
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, View view, int position) {
                switch (mData.get(position).getType()) {
                    case 1001:
                        StatusUtil.darkMode(mContext, true);
                        break;
                    case 1002:
                        StatusUtil.darkMode(mContext, false);
                        break;
                    case 1003:
                        StatusUtil.setStatusBarColor(mContext, ContextCompat.getColor(mContext, R.color.colorPrimary));
                        break;
                    case 1004:
                        StatusUtil.hideBar(mContext);
                        break;
                    case 10041:
                        StatusUtil.showBar(mContext);
                        break;
                    case 1005:
                        StatusUtil.setStatusBarColor(mContext,
                                ContextCompat.getColor(mContext, android.R.color.transparent));
                        break;
                    case 1006:
                        StatusUtil.setFloat(mContext);
                        break;
                    case 2001:
//                getTitleBar().setBackground("http://img1.imgtn.bdimg.com/it/u=626186099,3046696268&fm=27&gp=0.jpg");
                        break;
                    case 2002:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                        break;
                    case 2003:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setLeftText("取消");
                        break;
                    case 2004:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setLeftIcon(R.drawable.ic_base_back_black);
                        break;
                    case 2005:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setTitleText("Title");
                        break;
                    case 20051:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setSubtitleText("副标题");
                        break;
                    case 2006:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setRightText("菜单");
                        break;
                    case 2007:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setRightIcon(R.drawable.ic_base_menu_more_black);
                        break;
                    case 2008:
                        final ArrayList<String> menuData = new ArrayList<String>();
                        menuData.add("选项一");
                        menuData.add("选项二");
                        menuData.add("选项三");
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext))
                                .setRightMenu(menuData, new TitleBar.OnMenuItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        ToastUtil.showShort(mContext, menuData.get(position));
                                    }
                                });
                        break;
                    case 2009:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setAnimationIn();
                        break;
                    case 2010:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setAnimationOut();
                        break;
                    case 2011:
                        ActionBarUtil.create(AppCompatActivity.class.cast(mContext)).setLeftClickFinish();
                        break;
                    case 3001:
//                getStatusBar().setVisibility(View.VISIBLE);
                        break;
                    case 3002:
//                getStatusBar().setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                        break;
                    case 3003:
//                getStatusBar().setBackgroundImg("http://img1.imgtn.bdimg.com/it/u=626186099,3046696268&fm=27&gp=0.jpg");
                        break;
                    case 3004:
//                getStatusBar().setVisibility(View.GONE);
                        break;
                    case 4001:
                        RouteUtil.open(mContext, TitleFullScreenActivity.class);
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
