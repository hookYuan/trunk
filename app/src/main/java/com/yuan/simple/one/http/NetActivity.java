package com.yuan.simple.one.http;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.base.tools.adapter.recycler.GridDivider;
import com.yuan.base.tools.adapter.recycler.RLVAdapter;
import com.yuan.base.ui.activity.RLVActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用OKHttpUtil的一个事例
 */
public class NetActivity extends RLVActivity<PNet> {

    private ArrayList<String> mData;

    @Override
    public void initRecyclerView(RecyclerView rlvList) {
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvList.addItemDecoration(new GridDivider(mContext));
        getTitleBar().setTitleText("OKUtil")
                .setLeftClickFinish()
                .setTextColor(getResources().getColor(com.yuan.base.R.color.white))
                .setLeftIcon(getResources().getDrawable(com.yuan.base.R.drawable.ic_base_back_white))
                .setBackgroundColor(
                        getResources().getColor(com.yuan.base.R.color.colorPrimary));
    }

    @Override
    public void onBindHolder(RLVAdapter.ViewHolder holder, int position) {
        holder.setText(android.R.id.text1, mData.get(position));
    }

    @Override
    public int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {
        switch (position) {
            case 0:
                getP().get();
                break;
        }
    }

    @Override
    public List<?> getData() {
        if (mData == null) {
            mData = new ArrayList<>();
            mData.add("Get请求");
            mData.add("Post请求");
            mData.add("上传文件");
            mData.add("下载文件");
        }
        return mData;
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void setListener() {

    }
}
