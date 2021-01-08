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

import com.yuan.simple.core.module.ChineseBean;

import java.util.List;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.RecyclerAdapter;

/**
 * @author yuanye
 * @date 2019/7/19
 */
public class SortAdapter extends RecyclerAdapter<ChineseBean> {

    public SortAdapter(List<ChineseBean> data) {
        super(data, android.R.layout.simple_list_item_1);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, ChineseBean item, int position) {
        holder.setText(android.R.id.text1, item.getName());
    }
}
