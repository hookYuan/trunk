package com.yuan.ui;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.tools.adapter.recycler.GridDivider;
import com.yuan.tools.adapter.recycler.RLVAdapter;
import com.yuan.ui.kernel.BaseActivity;
import com.yuan.ui.kernel.Presenter;

import java.util.List;

/**
 * 集成RecyclerView 布局自定义
 * <p>
 * Created by YuanYe on 2018/8/11.
 */
public abstract class RLVActivity<T extends Presenter> extends BaseActivity<T> {

    protected RecyclerView rlvList;

    @Override
    public void findViews() {
        rlvList = findViewById(getRecyclerId());
    }


    @Override
    public void initData() {
        initRecyclerView(rlvList);
        rlvList.setAdapter(getAdapter());
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
     * 获取RecyclerId
     *
     * @return
     */
    protected abstract @IdRes
    int getRecyclerId();

    /**
     * 初始化RecyclerView，可以设置RecyclerView的列数
     * 设置LayoutManager
     *
     * @param rlvList
     */
    protected void initRecyclerView(RecyclerView rlvList) {
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvList.addItemDecoration(new GridDivider(mContext));
    }

    /**
     * 绑定item数据
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindHolder(RLVAdapter.ViewHolder holder, int position);

    /**
     * 获取itemLayout布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract @LayoutRes
    int getItemLayout(ViewGroup parent, int viewType);

    /**
     * item点击事件
     *
     * @param holder
     * @param view
     * @param position
     */
    protected void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public abstract List<?> getData();
}
