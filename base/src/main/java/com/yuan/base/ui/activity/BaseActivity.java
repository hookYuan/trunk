package com.yuan.base.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuan.base.tools.other.Views;
import com.yuan.base.tools.router.jump.JumpHelper;
import com.yuan.base.tools.router.jump.JumpParam;
import com.yuan.base.tools.system.SystemUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by YuanYe on 2017/4/30.
 * Activity的基本类。
 */

abstract class BaseActivity extends RxAppCompatActivity {

    protected BaseActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        //防止当onActivityResult后mContext为空
        if (mContext == null) mContext = this;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        SystemUtil.Input.hideSoftInput(this);
        super.onDestroy();
    }

    /**
     * Intent 跳转，不带参数
     *
     * @param clazz
     */
    public void open(Class clazz) {
        JumpHelper.open(mContext, clazz);
    }

    /**
     * Intent 跳转，携带参数
     *
     * @param clazz
     * @param param
     */
    public void open(Class clazz, JumpParam param) {
        JumpHelper.open(mContext, clazz, param);
    }

    protected <T extends View> T find(@IdRes int viewId) {
        return Views.find(this, viewId);
    }
}
