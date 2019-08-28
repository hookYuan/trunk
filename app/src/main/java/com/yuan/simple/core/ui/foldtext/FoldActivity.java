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

import com.yuan.simple.core.adapter.FoldAdapter;
import com.yuan.simple.core.module.SubjectBean;
import com.yuan.simple.core.presenter.FoldPresenter;
import com.yuan.simple.main.contract.MainContract;

import yuan.core.ui.Adapter;
import yuan.core.ui.RecyclerActivity;
import yuan.core.ui.Title;

@Title(titleStr = "折叠TextView")
@Adapter(adapter = FoldAdapter.class)
public class FoldActivity extends RecyclerActivity<FoldPresenter, SubjectBean>
        implements MainContract {

    @Override
    public void initData() {
        getPresenter().loadData(mData);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void notifyDataChange(boolean isSuccess) {
        mAdapter.notifyDataSetChanged();
    }
}
