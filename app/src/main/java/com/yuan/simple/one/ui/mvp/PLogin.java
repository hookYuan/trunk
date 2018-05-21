package com.yuan.simple.one.ui.mvp;

import com.yuan.base.ui.mvp.MvpPresenter;

/**
 * Created by YuanYe on 2018/5/18.
 */

public class PLogin extends MvpPresenter<ILogin.Views> implements ILogin.Presenter {

    @Override
    public void login() {
        getV().setData();
    }
}
