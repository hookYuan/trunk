package com.yuan.simple.one.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yuan.base.tools.adapter.recycler.GridDivider;
import com.yuan.base.tools.other.Kits;
import com.yuan.base.tools.other.Views;
import com.yuan.base.ui.mvp.MvpFragment;
import com.yuan.simple.R;
import com.yuan.simple.one.adapter.OneListAdapter;
import com.yuan.simple.one.ui.toolbar.ToolbarActivity;

import java.util.ArrayList;

/**
 * Created by YuanYe on 2018/4/13.
 */

public class OneFragment extends MvpFragment {

    @Override
    public int getLayoutId() {
        return R.layout.frag_one_layout;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        ArrayList<OneListAdapter.OneListBean> mData
                = new ArrayList<>();
        mData.add(new OneListAdapter.OneListBean("toolbar", ToolbarActivity.class));

        RecyclerView rlvList = Views.find(mContext, R.id.rlv_list);
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvList.addItemDecoration(new GridDivider((int) Kits.Dimens.dpToPx(mContext, 0.8f),
                ContextCompat.getColor(mContext, R.color.colorDivider)));
        rlvList.setAdapter(new OneListAdapter(mContext, mData));
    }
}
