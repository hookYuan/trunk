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
package com.yuan.simple.expand.presenter;

import android.os.Handler;
import android.os.Looper;

import com.yuan.simple.expand.module.UserBean;
import com.yuan.simple.main.contract.MainContract;

import java.util.ArrayList;
import java.util.List;

import yuan.core.dialog.DialogUtils;
import yuan.core.mvp.Presenter;
import yuan.expand.database.DBUtil;

/**
 * 这里Presenter一定要使用BaseContract,不要直接时使用
 * Fragment和Activity，避免Presenter与View耦合度高，不能复用
 *
 * @author yuanye
 * @date 2019/7/19
 */
public class DBPresenter extends Presenter<MainContract> {

    /**
     * 获取数据
     *
     * @param mData Recycler集合
     */
    public void loadData(List<String> mData) {
        new Handler(Looper.myLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.add("初始化数据库");
                        mData.add("创建数据表");
                        mData.add("插入数据");
                        mData.add("更新数据");
                        mData.add("删除整张表");
                        mData.add("查询整张表");
                        //更新列表
                        getView().notifyDataChange(true);
                    }
                }, 200);
    }

    /**
     * 初始化数据库
     */
    public void initDatabase() {
        DBUtil.getDB(getContext()).createTable(UserBean.class);
    }

    /**
     * 创建数据表
     */
    public void createTable() {
        ArrayList<UserBean> list = new ArrayList<>();
        UserBean bean = new UserBean();
        bean.setAge(27);
        bean.setName("袁冶");
        list.add(bean);

        UserBean bea2 = new UserBean();
        bea2.setAge(23);
        bea2.setName("袁2冶");
        list.add(bea2);

        DBUtil.getDB(getContext()).insertList(list);
    }

    /**
     * 插入一条数据
     */
    public void insertData() {
        UserBean bean2 = new UserBean();
        bean2.setAge(26);
        DBUtil.getDB(getContext()).update(bean2, "age=?", new String[]{"27"});
    }

    /**
     * 删除表
     */
    public void deleteTable() {
        DBUtil.getDB(getContext()).delete(UserBean.class.getName());
    }

    /**
     * 查询整张表
     */
    public void searchTable() {
        List<UserBean> list2 = DBUtil.getDB(getContext()).querySQL("SELECT * FROM UserBean", null, UserBean.class);
        DialogUtils.alertText(list2.toString()).create(getContext());
    }
}
