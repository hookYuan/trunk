package com.yuan.simple.core.ui.adapter;

import com.yuan.simple.core.adapter.MultiTypeAdapter;
import com.yuan.simple.core.module.SubjectBean;
import com.yuan.simple.core.presenter.AdapterPresenter;

import androidx.recyclerview.widget.GridLayoutManager;
import yuan.core.list.GridDivider;
import yuan.core.ui.Adapter;
import yuan.core.ui.RecyclerActivity;
import yuan.core.ui.Title;

@Title(titleStr = "多类型Adapter")
@Adapter(adapter = MultiTypeAdapter.class)
public class AdapterActivity extends RecyclerActivity<AdapterPresenter, SubjectBean> {

    @Override
    protected void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
    }

    @Override
    public void initData() {
        mRecyclerView.addItemDecoration(new GridDivider());
    }

    @Override
    public void setListener() {

    }
}
