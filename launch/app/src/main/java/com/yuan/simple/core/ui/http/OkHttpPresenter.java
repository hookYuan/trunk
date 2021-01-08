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
package com.yuan.simple.core.ui.http;

import android.os.Environment;

import com.yuan.simple.core.module.SubjectBean;

import java.util.List;

import yuan.core.mvp.Presenter;
import yuan.core.tool.ToastUtil;
import yuan.depends.okhttp.OKUtil;

/**
 * Created by YuanYe on 2018/8/15.
 */
public class OkHttpPresenter extends Presenter<OKHttpActivity> {

    /**
     * 创建数据
     *
     * @param mData
     */
    public void createData(List<SubjectBean> mData) {
        mData.add(new SubjectBean("Get请求"));
        mData.add(new SubjectBean("Post请求"));
        mData.add(new SubjectBean("上传文件"));
        mData.add(new SubjectBean("下载文件"));
        mData.add(new SubjectBean("获取缓存文件大小"));
        mData.add(new SubjectBean("删除缓存文件"));
    }

    /**
     * get 请求
     */
    public void get() {
        OKUtil.init(new OKUtil.Config.Builder()
                .setCache(false)
                .setCommonHead("yuanye", "987654")
                .setReConnect(true)
                .build());

        OKUtil.with(getContext())
                .get("http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455")
                .execute(new OKUtil.JsonBack() {
                    @Override
                    public void onSuccess(Object o, String json) {
                        ToastUtil.showShort(getContext(), json);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        ToastUtil.showShort(getContext(), e.getMessage());
                    }
                });
    }

    public void get2() {
        OKUtil.with(getContext(), new OKUtil.Config.Builder()
                .setCache(true)
                .build())
                .get("http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455")
                .put("yuanye", "123123")
                .execute(new OKUtil.JsonBack() {
                    @Override
                    public void onSuccess(Object o, String json) {
                        ToastUtil.showShort(getContext(), json);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        ToastUtil.showShort(getContext(), e.getMessage());
                    }
                });
    }

    /**
     * 显示缓存大小
     */
    public void showCacheSize() {
        long size = OKUtil.getCacheSize();
        ToastUtil.showShort(getContext(), size + "B");
    }

    /**
     * 删除缓存
     */
    public void delCache() {
        OKUtil.delCache();
        ToastUtil.showShort(getContext(), "删除成功");
    }

    /**
     * 下载文件
     */
    public void downloadFile() {
        OKUtil.with(getContext())
                .get("http://cp-dev.yqcx.faw.cn/ftp/file/signDownlaodUrl?fileKey=FgpjStJHE2bL7_gWao5Umfpe6RLs")
                .execute(new OKUtil.FileBack(Environment.getExternalStorageDirectory() + "/Download/", "232.pptx") {
                    @Override
                    public void onSuccess(String fileDir, byte[] bytes) {
//                        ToastUtil.showShort(getV(), "文件大小：" + bytes.length);
                    }

                    @Override
                    public void onFail(Exception e) {
//                        ToastUtil.showShort(getV(), "下载异常：" + e.getMessage());
                    }
                });
    }
}
