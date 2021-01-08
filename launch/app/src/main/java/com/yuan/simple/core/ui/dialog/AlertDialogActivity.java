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
package com.yuan.simple.core.ui.dialog;


import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.yuan.simple.core.adapter.TextAdapter;
import com.yuan.simple.core.module.SubjectBean;
import com.yuan.simple.core.presenter.DialogPresenter;
import com.yuan.simple.main.contract.MainContract;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.GridDivider;
import yuan.core.list.RecyclerAdapter;
import yuan.core.ui.Adapter;
import yuan.core.ui.RecyclerActivity;
import yuan.core.ui.Title;

/**
 * AlertDialog使用示例
 */
@Adapter(adapter = TextAdapter.class)
@Title(titleStr = "AlertDialog")
public class AlertDialogActivity extends RecyclerActivity<DialogPresenter, SubjectBean>
        implements MainContract {

    @Override
    public void initData() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerView.addItemDecoration(new GridDivider());
        mAdapter.showLoading();
        getPresenter().loadData(mData);
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, View view, int position) {
                switch (mData.get(position).getType()) {
                    case 1:
                        getPresenter().showDialog1();
                        break;
                    case 2:
                        getPresenter().showDialog2();
                        break;
                    case 3:
                        getPresenter().showDialog3();
                        break;
                    case 4:
                        getPresenter().showDialog4();
                        break;
                    case 5:
                        getPresenter().showDialog5();
                        break;
                    case 6:
                        getPresenter().showDialog6();
                        break;
                    case 7:
                        getPresenter().showDialog7();
                        break;
                    case 8:
                        getPresenter().showDialog8();
                        break;
                    case 9:
                        getPresenter().showDialog9();
                        break;
                    case 10:
                        getPresenter().showDialog10();
                        break;
                    case 11:
                        getPresenter().showDialog11();
                        break;
                    case 12:
                        getPresenter().showDialog12();
                        break;
                    case 13:
                        getPresenter().showDialog13();
                        break;
                    case 14:
                        getPresenter().showDialog14();
                        break;
                    case 15:
                        getPresenter().showDialog15();
                        break;
                    case 16:
                        getPresenter().showDialog16();
                        break;
                    case 17:
                        getPresenter().showDialog17();
                        break;
                    case 18:
                        break;
                }
            }
        });
    }

    @Override
    public void notifyDataChange(boolean isSuccess) {
        mAdapter.notifyDataSetChanged();
    }


}
