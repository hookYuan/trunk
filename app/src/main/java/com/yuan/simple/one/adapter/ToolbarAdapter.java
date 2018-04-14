package com.yuan.simple.one.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;

import com.yuan.base.tools.adapter.RLVAdapter;
import com.yuan.base.tools.system.StatusBarUtil;
import com.yuan.simple.R;
import com.yuan.simple.one.ui.toolbar.ToolbarActivity;

import java.util.List;

/**
 * create by Yuan ye.
 */
public class ToolbarAdapter extends RLVAdapter {

    private List<ToolbarAdapter.ToolbarBean> mData;

    public ToolbarAdapter(Context context, List<ToolbarAdapter.ToolbarBean> list) {
        super(context);
        this.mData = list;
    }

    /**
     * You can select other method
     */
    @Override
    public int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_2;
    }

    @Override
    public void onBindHolder(ViewHolder holder, final int position) {
        holder.setText(android.R.id.text1, mData.get(position).getName());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        ToolbarActivity activity = (ToolbarActivity) mContext;
        switch (mData.get(position).getCode()) {
            case 1:
                StatusBarUtil.darkMode(activity);
                break;
            case 2:
                StatusBarUtil.darkMode(activity, false);
                break;
            case 3:
                //设置状态栏颜色为透明
                StatusBarUtil.immersive(activity, true, ContextCompat.getColor(mContext, R.color.orange500));
                break;
            case 4:
                //设置状态栏颜色为透明
                StatusBarUtil.immersive(activity, true);
                break;
        }
    }

    @Override
    public int getItemCount() {
        //TODO set list count.
        return mData == null ? 0 : mData.size();
    }

    public static class ToolbarBean {
        String name;
        int code;//标记

        public ToolbarBean(String name, int code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

}
