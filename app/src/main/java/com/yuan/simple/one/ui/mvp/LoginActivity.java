package com.yuan.simple.one.ui.mvp;

import android.os.Bundle;

import com.yuan.base.ui.mvp.MvpActivity;
import com.yuan.simple.R;

public class LoginActivity extends MvpActivity<PLogin> implements ILogin.Views {

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getP().login();
    }

    @Override
    public void setData() {

    }
}
