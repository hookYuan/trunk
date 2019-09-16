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

import androidx.recyclerview.widget.DividerItemDecoration;
import yuan.core.list.BaseViewHolder;
import yuan.core.list.RecyclerAdapter;

/**
 * 描述：
 *
 * @author yuanye
 * @date 2019/8/28 14:21
 */
public class MultiTypeAdapter extends RecyclerAdapter<SubjectBean> {

    public MultiTypeAdapter(List<SubjectBean> data) {
        super(data);
        setMultipleType(position -> {
            if (position % 5 == 0) {
                return R.layout.simple_item2;
            }
            return R.layout.simple_item;
        });
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, SubjectBean item, int position) {
        switch (getItemViewType(position)) {
            case R.layout.simple_item:
                holder.setText(android.R.id.text1, item.getName());
                break;
            case R.layout.simple_item2:
                holder.setText(android.R.id.text1, "标题");
                break;
        }
    }
}
