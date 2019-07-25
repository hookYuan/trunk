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
public class TitleBarPresenter extends Presenter<MainContract> {

    /**
     * 获取数据
     *
     * @param mData Recycler集合
     */
    public void loadData(List<SubjectBean> mData) {
        new Handler(Looper.myLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.add(new SubjectBean("系统状态栏", 1000));
                        mData.add(new SubjectBean("黑色文字", 1001));
                        mData.add(new SubjectBean("白色文字", 1002));
                        mData.add(new SubjectBean("设置背景色", 1003));
                        mData.add(new SubjectBean("隐藏状态栏", 1004));
                        mData.add(new SubjectBean("显示状态栏", 10041));
                        mData.add(new SubjectBean("悬浮状态栏", 1006));

                        mData.add(new SubjectBean("TitleBar", 2000));
                        mData.add(new SubjectBean("背景图片", 2001));
                        mData.add(new SubjectBean("背景颜色", 2002));
                        mData.add(new SubjectBean("左边文字", 2003));
                        mData.add(new SubjectBean("左边图标", 2004));
                        mData.add(new SubjectBean("主标题", 2005));
                        mData.add(new SubjectBean("副标题", 20051));
                        mData.add(new SubjectBean("右边文字", 2006));
                        mData.add(new SubjectBean("右边图标", 2007));
                        mData.add(new SubjectBean("右边弹窗菜单", 2008));
                        mData.add(new SubjectBean("进入动画", 2009));
                        mData.add(new SubjectBean("出场动画", 2010));
                        mData.add(new SubjectBean("左侧点击返回", 2011));

                        mData.add(new SubjectBean("自定义状态栏", 3000));
                        mData.add(new SubjectBean("显示状态栏", 3001));
                        mData.add(new SubjectBean("背景颜色", 3002));
                        mData.add(new SubjectBean("背景图", 3003));
                        mData.add(new SubjectBean("隐藏状态栏", 3004));

                        mData.add(new SubjectBean("精彩案例", 4000));
                        mData.add(new SubjectBean("全屏沉浸式", 4001));
                        //更新列表
                        getView().notifyDataChange(true);
                    }
                }, 200);
    }
}
