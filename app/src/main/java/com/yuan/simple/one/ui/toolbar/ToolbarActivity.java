package com.yuan.simple.one.ui.toolbar;


import android.os.Bundle;

import com.yuan.base.tools.adapter.recycler.GridDivider;
import com.yuan.base.tools.other.Kits;
import com.yuan.base.tools.other.Views;
import com.yuan.base.ui.mvp.MvpActivity;
import com.yuan.base.widget.title.TitleBar;
import com.yuan.simple.R;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yuan.simple.one.adapter.ToolbarAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * create by Yuan ye.
 * download truck before use this
 */
public class ToolbarActivity extends MvpActivity {

    private RecyclerView rlvRecycler;

    public void initData(Bundle savedInstanceState) {
        //init title bar.
        TitleBar titleBar = Views.find(mContext, R.id.title_bar);

        List<ToolbarAdapter.ToolbarBean> mData = new ArrayList<>();
        mData.add(new ToolbarAdapter.ToolbarBean("系统状态栏黑色文字", 1));
        mData.add(new ToolbarAdapter.ToolbarBean("系统状态栏白色文字", 2));
        mData.add(new ToolbarAdapter.ToolbarBean("系统状态栏背景色", 3));
        mData.add(new ToolbarAdapter.ToolbarBean("系统状态栏透明背景", 4));
        mData.add(new ToolbarAdapter.ToolbarBean("Toolbar透明背景", 5));
        mData.add(new ToolbarAdapter.ToolbarBean("Toolbar颜色背景", 6));
        mData.add(new ToolbarAdapter.ToolbarBean("Toolbar中间文字", 7));
        mData.add(new ToolbarAdapter.ToolbarBean("Toolbar左边文字", 8));
        mData.add(new ToolbarAdapter.ToolbarBean("Toolbar左边图标", 9));
        mData.add(new ToolbarAdapter.ToolbarBean("Toolbar右边文字", 10));
        mData.add(new ToolbarAdapter.ToolbarBean("Toolbar右边图标", 11));
        mData.add(new ToolbarAdapter.ToolbarBean("Toolbar右边弹窗菜单", 12));

        rlvRecycler = Views.find(mContext, R.id.rlv_recycler);
        rlvRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        //add divider.
        rlvRecycler.addItemDecoration(new GridDivider((int) Kits.Dimens.dpToPx(mContext, 0.8f)
                , ContextCompat.getColor(mContext, R.color.colorDivider)));
        rlvRecycler.setAdapter(new ToolbarAdapter(mContext, mData, titleBar));
    }

    public int getLayoutId() {
        return R.layout.act_one_toolbar;
    }
}
