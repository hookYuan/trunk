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
package com.yuan.simple.core.ui.title;

import android.widget.ImageView;

import yuan.core.mvp.BaseActivity;
import yuan.core.title.ActionBarUtil;
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
        ActionBarUtil.hideActionBar(this);
        ImageView imageView = Views.find(mContext, R.id.iv_background);
        GlideUtil.create()
                .showImage("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1563706162&di=e3977a38bfed8d389c1f20ba74dfd817&src=http://img.ph.126.net/v81Iaw5n-zOt0jzA1XWHNg==/2765773121175715604.jpg", imageView);
    }

    @Override
    public void setListener() {

    }
}
