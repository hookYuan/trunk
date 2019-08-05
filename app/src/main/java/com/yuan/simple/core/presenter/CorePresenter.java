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
package com.yuan.simple.core.presenter;

import android.os.Handler;
import android.os.Looper;

import com.yuan.simple.main.contract.MainContract;
import com.yuan.simple.main.module.CoreFunctionInfo;
import com.yuan.simple.core.ui.callback.CallbackActivity;
import com.yuan.simple.expand.ui.db.DBActivity;
import com.yuan.simple.core.ui.dialog.AlertDialogActivity;
import com.yuan.simple.core.ui.foldtext.FoldActivity;
import com.yuan.simple.core.ui.http.OKHttpActivity;
import com.yuan.simple.core.ui.input.InputActivity;
import com.yuan.simple.expand.ui.multi.MultiActivity;
import com.yuan.simple.core.ui.roundview.RoundTextActivity;
import com.yuan.simple.core.ui.picker.PickerActivity;
import com.yuan.simple.core.ui.sort.SortActivity;
import com.yuan.simple.core.ui.title.TitleBarActivity;

import java.util.List;

import yuan.core.mvp.Presenter;

/**
 * 这里Presenter一定要使用BaseContract,不要直接时使用
 * Fragment和Activity，避免Presenter与View耦合度高，不能复用
 *
 * @author yuanye
 * @date 2019/7/19
 */
public class CorePresenter extends Presenter<MainContract> {

    /**
     * 获取数据
     *
     * @param mData Recycler集合
     */
    public void loadData(List<CoreFunctionInfo> mData) {
        new Handler(Looper.myLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.add(new CoreFunctionInfo("titleBar", TitleBarActivity.class));
                        mData.add(new CoreFunctionInfo("roundView", RoundTextActivity.class));
                        mData.add(new CoreFunctionInfo("foldTextView", FoldActivity.class));
                        mData.add(new CoreFunctionInfo("sortChinese", SortActivity.class));
                        mData.add(new CoreFunctionInfo("input", InputActivity.class));
                        mData.add(new CoreFunctionInfo("okHttp", OKHttpActivity.class));
                        mData.add(new CoreFunctionInfo("alertDialog", AlertDialogActivity.class));
                        mData.add(new CoreFunctionInfo("multiAdapter", MultiActivity.class));
                        mData.add(new CoreFunctionInfo("DBUtil", DBActivity.class));
                        mData.add(new CoreFunctionInfo("PickerUtil", PickerActivity.class));
                        mData.add(new CoreFunctionInfo("CallbackManager", CallbackActivity.class));
                        //更新列表
                        getView().notifyDataChange(true);
                    }
                }, 200);
    }
}
