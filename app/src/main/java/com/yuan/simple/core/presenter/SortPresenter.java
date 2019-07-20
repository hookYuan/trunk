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

import com.yuan.simple.core.module.ChineseBean;
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
public class SortPresenter extends Presenter<MainContract> {

    /**
     * 获取数据
     *
     * @param data Recycler集合
     */
    public void loadData(List<ChineseBean> data) {
        new Handler(Looper.myLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.add(new ChineseBean("马云"));
                        data.add(new ChineseBean("刘强东"));
                        data.add(new ChineseBean("柳传志"));
                        data.add(new ChineseBean("彭万里"));
                        data.add(new ChineseBean("高大山"));
                        data.add(new ChineseBean("谢大海"));
                        data.add(new ChineseBean("林莽"));
                        data.add(new ChineseBean("黄强辉"));
                        data.add(new ChineseBean("章汉夫"));
                        data.add(new ChineseBean("范长江"));
                        data.add(new ChineseBean("林君雄"));
                        data.add(new ChineseBean("朱希亮"));
                        data.add(new ChineseBean("李四光"));
                        data.add(new ChineseBean("甘铁生"));
                        data.add(new ChineseBean("张伍绍祖"));
                        data.add(new ChineseBean("马继祖"));
                        data.add(new ChineseBean("赵进喜"));
                        data.add(new ChineseBean("程孝先"));
                        data.add(new ChineseBean("宗敬先"));
                        data.add(new ChineseBean("赵大华"));
                        data.add(new ChineseBean("年广嗣"));
                        data.add(new ChineseBean("赵德荣"));
                        //更新列表
                        getView().notifyDataChange(true);
                    }
                }, 200);
    }
}
