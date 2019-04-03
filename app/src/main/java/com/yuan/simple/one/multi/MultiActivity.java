package com.yuan.simple.one.multi;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yuan.base.ui.activity.MvpActivity;
import com.yuan.simple.R;

/**
 * @author yuanye
 * @date 2018-12-12 12:20:59
 */
public class MultiActivity extends MvpActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_multi;
    }

    @Override
    public void findViews() {
        RecyclerView rlvList = find(R.id.rlv_list);
        rlvList.setLayoutManager(new GridLayoutManager(mContext, 1));
        MultiAdapter adapter = new MultiAdapter(mContext);
        adapter.addSection("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addSection("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addSection("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        adapter.addItem("");
        rlvList.setAdapter(adapter);
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

    }
}
