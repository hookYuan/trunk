package com.yuan.simple.three.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yuan.base.tools.layout.Views;
import com.yuan.base.widget.title.TitleBar;
import com.yuan.simple.R;
import com.yuan.simple.one.BaseFragment;

/**
 * Created by YuanYe on 2018/4/13.
 */
public class ThreeFragment extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.frag_three_layout;
    }

    @Override
    public void findViews() {
        TitleBar titleBar = Views.find(mView, R.id.title_bar);
        titleBar.setTitleText("关于");
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
