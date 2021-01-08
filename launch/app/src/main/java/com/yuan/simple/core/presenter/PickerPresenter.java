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

import com.yuan.simple.core.module.SubjectBean;
import com.yuan.simple.main.contract.MainContract;

import java.util.List;

import yuan.core.mvp.Presenter;

/**
 * 这里Presenter一定要使用BaseContract,不要直接时使用
 * Fragment和Activity，避免Presenter与View耦合度高，不能复用
 *
 * @author yuanye
 * @date 2019/7/19
 */
public class PickerPresenter extends Presenter<MainContract> {

    /**
     * 获取数据
     *
     * @param data Recycler集合
     */
    public void loadData(List<SubjectBean> data) {
        new Handler(Looper.myLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.add(new SubjectBean("启动相机"));
                        data.add(new SubjectBean("启动图库"));
                        data.add(new SubjectBean("启动相机和图库"));
                        data.add(new SubjectBean("启动通讯录"));
                        //更新列表
                        getView().notifyDataChange(true);
                    }
                }, 200);
    }
}
