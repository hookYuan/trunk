package com.yuan.base.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yuan.base.R;
import com.yuan.base.tools.adapter.recycler.RLVAdapter;
import com.yuan.base.tools.other.Views;
import com.yuan.base.ui.extra.HRefresh;
import com.yuan.base.ui.extra.IRefresh;
import com.yuan.base.ui.fragment.RLVFragment;
import com.yuan.base.ui.mvp.MvpActivity;
import com.yuan.base.ui.mvp.MvpPresenter;
import com.yuan.base.widget.state.StateController;
import com.yuan.base.widget.title.Title;
import com.yuan.base.widget.title.statusbar.StatusBar;
import com.yuan.base.widget.title.titlebar.TitleBar;

import java.util.List;

/**
 * Created by YuanYe on 2018/8/11.
 */
public abstract class RLVActivity<T extends MvpPresenter> extends MvpActivity<T> {

    protected SmartRefreshLayout refreshView;
    protected StateController stateView;
    protected RecyclerView rlvList;
    protected Title title;

    @Override
    public int getLayoutId() {
        return R.layout.frag_simple_list;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        refreshView = Views.find(mContext, R.id.srl_refresh);
        stateView = Views.find(mContext, R.id.sc_state);
        rlvList = Views.find(mContext, R.id.rlv_list);
        title = Views.find(mContext, R.id.title_bar);

        if (this instanceof IRefresh) HRefresh.init(refreshView, (IRefresh) this);
        else refreshView.setEnableRefresh(false);

        initRecyclerView(rlvList);
        rlvList.setAdapter(getAdapter());
    }

    public TitleBar getTitleBar() {
        return title.getTitleBar();
    }

    public StatusBar getStatusBar() {
        return title.getStatusBar();
    }

    /**
     * 初始化Adapter
     *
     * @return
     */
    private RLVAdapter getAdapter() {
        RLVAdapter adapter = new RLVAdapter(mContext) {
            @Override
            public int getItemLayout(ViewGroup parent, int viewType) {
                return RLVActivity.this.getItemLayout(parent, viewType);
            }

            @Override
            public void onBindHolder(ViewHolder holder, int position) {
                RLVActivity.this.onBindHolder(holder, position);
            }

            @Override
            public int getItemCount() {
                return getData().size();
            }

            @Override
            public void onItemClick(ViewHolder holder, View view, int position) {
                RLVActivity.this.onItemClick(holder, view, position);
            }
        };
        return adapter;
    }

    /**
     * 初始化RecyclerView，可以设置RecyclerView的列数
     * 设置LayoutManager
     *
     * @param rlvList
     */
    public abstract void initRecyclerView(RecyclerView rlvList);

    /**
     * 绑定item数据
     *
     * @param holder
     * @param position
     */
    public abstract void onBindHolder(RLVAdapter.ViewHolder holder, int position);

    /**
     * 获取itemLayout布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract @LayoutRes
    int getItemLayout(ViewGroup parent, int viewType);

    /**
     * item点击事件
     *
     * @param holder
     * @param view
     * @param position
     */
    public abstract void onItemClick(RLVAdapter.ViewHolder holder, View view, int position);

    /**
     * 获取数据源
     *
     * @return
     */
    public abstract List<?> getData();
}
