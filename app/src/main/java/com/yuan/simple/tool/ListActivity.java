package com.yuan.simple.tool;

import android.support.v7.widget.RecyclerView;

import yuan.core.mvp.Presenter;
import com.yuan.simple.R;
import yuan.ui_extend.RLVActivity;
import yuan.widget.title.TitleBar;

/**
 * 重新绑定RecyclerView布局
 *
 * @param <T>
 */
public abstract class ListActivity<T extends Presenter> extends RLVActivity<T> {

    private TitleBar titleBar;

    @Override
    public int getLayoutId() {
        return R.layout.frag_simple_list;
    }

    @Override
    protected RecyclerView bindRecyclerView() {
        return findViewById(R.id.rlv_list);
    }

    @Override
    public void findViews() {
        super.findViews();
        titleBar = findViewById(R.id.title_bar);
    }

    protected TitleBar getTitleBar() {
        return titleBar;
    }
}
