package com.yuan.simple.one.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.base.tools.adapter.recycler.RLVAdapter;
import com.yuan.base.tools.router.jump.JumpHelper;

import java.util.ArrayList;

public class OneListAdapter extends RLVAdapter {

    ArrayList<OneListBean> mData;

    public OneListAdapter(Context context, ArrayList<OneListBean> mData) {
        super(context);
        this.mData = mData;
    }

    @Override
    public int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {
        holder.setText(android.R.id.text1, mData.get(position).getToolbar());
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        JumpHelper.open(mContext, mData.get(position).getClazz());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class OneListBean {

        private String toolbar;
        private Class clazz;

        public OneListBean(String toolbar, Class clazz) {
            this.toolbar = toolbar;
            this.clazz = clazz;
        }

        public String getToolbar() {
            return toolbar;
        }

        public void setToolbar(String toolbar) {
            this.toolbar = toolbar;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }
    }
}
