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

import android.widget.ImageView;

import yuan.core.mvp.BaseActivity;
import yuan.core.title.StatusUtil;
import yuan.core.tool.Views;
import yuan.depends.glide.GlideUtil;

import com.yuan.simple.R;

/**
 * 全屏Activity
 *
 * @author YuanYe
 * @date 2019/7/19  23:56
 */
public class TitleFullScreenActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.act_title_demo1;
    }

    @Override
    public void initData() {
        StatusUtil.setFloat(mContext);
        StatusUtil.setTransparent(mContext);
        ImageView imageView = Views.find(mContext, R.id.iv_background);
        GlideUtil.create()
                .showImage("http://static.oneplus.cn/data/attachment/forum/201701/05/165732nmebemxb1my1e08e.jpg", imageView);
    }

    @Override
    public void setListener() {

    }
}
