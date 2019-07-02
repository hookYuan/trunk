package com.yuan.simple.one.toolbar;

import android.os.Bundle;
import android.support.annotation.Nullable;

import yuan.core.mvp.BaseActivity;
import yuan.core.function.CallbackManager;
import com.yuan.simple.R;

/**
 * Created by YuanYe on 2018/8/4.
 */
public class TitleBarActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.act_one_toolbar;
    }

    @Override
    public void findViews() {

    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void initData() {
        addFragment(R.id.content, TitleBarFragment.class);
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallbackManager.get().invoke("123");
    }
}
