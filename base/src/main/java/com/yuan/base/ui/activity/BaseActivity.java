package com.yuan.base.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuan.base.tools.layout.Views;
import com.yuan.base.tools.router.jump.JumpHelper;
import com.yuan.base.tools.router.jump.JumpParam;
import com.yuan.base.tools.common.KeyboardUtil;
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
        KeyboardUtil.hideSoftInput(this);
        super.onDestroy();
    }

    /**
     * Intent 跳转，不带参数
     *
     * @param clazz 需要打开的Activity
     */
    public void open(Class clazz) {
        JumpHelper.open(mContext, clazz);
    }

    /**
     * Intent 跳转，携带参数
     *
     * @param clazz Activity类名
     * @param param 跳转需要传递的参数
     */
    public void open(Class clazz, JumpParam param) {
        JumpHelper.open(mContext, clazz, param);
    }

    protected <T extends View> T find(@IdRes int viewId) {
        return Views.find(this, viewId);
    }
}
