package com.yuan.base.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuan.base.tools.common.ReflexUtil;
import com.yuan.base.ui.MvpPresenter;

/**
 * Created by YuanYe on 2017/9/19.
 */
public abstract class MvpFragment<T extends MvpPresenter> extends ExtraFragment {

    private T presenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter = ReflexUtil.getT(MvpFragment.this, 0);
        if (presenter != null) {
            presenter.attachView(this);
        }
        super.onViewCreated(view, savedInstanceState);
        if (presenter != null) presenter.onCreate(savedInstanceState);
    }

    protected T getP() {
        if (presenter == null) {
            try {
                throw new NullPointerException("使用presenter,MVPActivity泛型不能为空");
            } catch (NullPointerException e) {
                throw e;
            }
        }
        return presenter;
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (presenter != null) presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }
}
