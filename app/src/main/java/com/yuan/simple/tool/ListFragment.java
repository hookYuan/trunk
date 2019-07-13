package com.yuan.simple.tool;

import androidx.recyclerview.widget.RecyclerView;

import yuan.core.mvp.Presenter;
import com.yuan.simple.R;
import yuan.ui_extend.RLVFragment;
import yuan.widget.title.TitleBar;


public abstract class ListFragment<T extends Presenter> extends RLVFragment<T> {

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
