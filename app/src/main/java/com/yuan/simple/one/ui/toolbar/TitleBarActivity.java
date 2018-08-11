package com.yuan.simple.one.ui.toolbar;

import android.os.Bundle;

import com.yuan.base.ui.mvp.MvpActivity;
import com.yuan.simple.R;

/**
 * Created by YuanYe on 2018/8/4.
 */
public class TitleBarActivity extends MvpActivity {

    @Override
    public int getLayoutId() {
        return R.layout.act_one_toolbar;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        addFragment(R.id.content, TitleBarFragment.class);
    }
}
