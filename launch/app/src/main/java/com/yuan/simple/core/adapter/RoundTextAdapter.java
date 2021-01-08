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

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.yuan.simple.R;

import java.util.List;

import yuan.core.list.ListAdapter;

/**
 * @author yuanye
 * @date 2019/7/21
 */
public class RoundTextAdapter extends ListAdapter<String> {
    /**
     * 当前选中的Position
     */
    private int mSelectPosition;

    public RoundTextAdapter(List mData, GridView listView) {
        super(mData, R.layout.shape_gv_item);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void bindView(ViewHolder holder, String obj) {
        holder.setText(R.id.rtv_text, obj);
        if (mSelectPosition == holder.getItemPosition()) {
            holder.getView(R.id.rtv_text).setSelected(true);
        } else {
            holder.getView(R.id.rtv_text).setSelected(false);
        }
    }
}
