package com.yuan.simple.three.ui;

import android.os.Bundle;

import com.yuan.base.tools.layout.Views;
import com.yuan.base.ui.mvp.MvpFragment;
import com.yuan.base.widget.title.titlebar.TitleBar;
import com.yuan.simple.R;

/**
 * Created by YuanYe on 2018/4/13.
 */

public class ThreeFragment extends MvpFragment {

    @Override
    public int getLayoutId() {
        return R.layout.frag_three_layout;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        TitleBar titleBar = Views.find(mView, R.id.title_bar);
        titleBar.setTitleText("关于");
    }

}
