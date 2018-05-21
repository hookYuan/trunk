package com.yuan.base.ui.mvp;

/**
 * Created by YuanYe on 2017/9/19.
 */
public class MvpPresenter<V> {

    private V view;

    protected void attachView(V View) {
        this.view = View;
    }

    protected V getV() {
        if (view != null) {
            try {
                throw new NullPointerException("Presenter中获取Activity实例为空");
            } catch (NullPointerException e) {

            }
        }
        return view;
    }
}
