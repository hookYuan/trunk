package com.yuan.base.mvp.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yuan.base.tools.other.ReflexUtil;
import com.yuan.base.mvp.activity.FragmentActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by YuanYe on 2018/1/4.
 */

public abstract class MVPFragmentActivity<T extends XPresenter> extends FragmentActivity {

    private T presenter;

    boolean useEvent = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = ReflexUtil.getT(MVPFragmentActivity.this, 0);
        if (presenter != null) {
            presenter.attachView(this);
        }
        super.onCreate(savedInstanceState);
        if (useEvent && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 开启Event
     * 必须在initData中或之前开启
     * 必须在开启Event中的勒种使用Subscribe接收注解
     */
    protected void openEvent() {
        useEvent = true;
    }

    public T getP() {
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
    protected void onDestroy() {
        super.onDestroy();
        if (useEvent && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
