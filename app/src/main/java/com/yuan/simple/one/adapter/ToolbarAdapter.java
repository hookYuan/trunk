package com.yuan.simple.one.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.yuan.base.tools.adapter.RLVAdapter;
import com.yuan.base.tools.system.StatusBarUtil;
import com.yuan.base.widget.title.OnMenuItemClickListener;
import com.yuan.base.widget.title.TitleBar;
import com.yuan.base.widget.title.TitleTheme;
import com.yuan.simple.R;
import com.yuan.simple.one.ui.toolbar.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * create by Yuan ye.
 */
public class ToolbarAdapter extends RLVAdapter {

    private List<ToolbarAdapter.ToolbarBean> mData;
    private TitleBar titleBar;

    public ToolbarAdapter(Context context, List<ToolbarAdapter.ToolbarBean> list, TitleBar titleBar) {
        super(context);
        this.mData = list;
        this.titleBar = titleBar;
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
                titleBar.setSystemStatusBarFontBlack();
                break;
            case 2:
                titleBar.setSystemStatusBarFontWhite();
                break;
            case 3:
                titleBar.setSystemStatusBarOverlyBg(false, ContextCompat.getColor(mContext, R.color.colorPrimaryDark), 0);
                break;
            case 4:
                titleBar.setSystemStatusBarOverlyBg(true, 0, 0);
                break;
            case 5:
                titleBar.setTitleBarColor(ContextCompat.getColor(mContext, R.color.transparent));
                break;
            case 6:
                titleBar.setTitleBarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case 7:
                titleBar.setCenterText("TitleBar");
                break;
            case 8:
                titleBar.setLeftText("取消");
                break;
            case 9:
                titleBar.setLeftIcon(R.drawable.ic_base_back_black);
                break;
            case 10:
                titleBar.setRightText("菜单");
                break;
            case 11:
                titleBar.setRightIcon(R.drawable.ic_base_menu_more_black);
                break;
            case 12:
                ArrayList<String> menuData = new ArrayList<String>();
                menuData.add("选项一");
                menuData.add("选项二");
                menuData.add("选项三");
                titleBar.setRightMenu(menuData, new OnMenuItemClickListener() {
                    @Override
                    public void onItemClick(PopupWindow popupWindow, AdapterView<?> adapterView, View view, int i) {

                    }
                });
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
