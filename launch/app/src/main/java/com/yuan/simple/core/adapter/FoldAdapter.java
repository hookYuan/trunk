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
package com.yuan.simple.core.adapter;

import com.yuan.simple.R;
import com.yuan.simple.core.module.SubjectBean;

import java.util.List;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.RecyclerAdapter;
import yuan.core.widget.FoldTextView;

/**
 * 描述：
 *
 * @author yuanye
 * @date 2019/8/28 14:21
 */
public class FoldAdapter extends RecyclerAdapter<SubjectBean> {

    public FoldAdapter(List<SubjectBean> data) {
        super(data, R.layout.item_fold);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, SubjectBean item, int position) {

        FoldTextView textView = holder.getView(R.id.ctv_content);
        textView.setTextList(item.getName(), position);

        //设置颜色
        int code = item.getType();
        if (code == 1000 || code == 2000 || code == 3000
                || code == 4000) {
            holder.setBackgroundColor(android.R.id.text1, mContext.getResources().getColor(R.color.lightblue100));
        }
    }
}
