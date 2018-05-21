package com.yuan.simple.one.ui.mvp;

/**
 * Created by YuanYe on 2018/5/18.
 */

public interface ILogin {
    interface Views {
        void setData();
    }

    interface Presenter {
        void login();
    }
}
