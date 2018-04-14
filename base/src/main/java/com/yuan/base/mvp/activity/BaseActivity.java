package com.yuan.base.mvp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuan.base.tools.router.JumpHelper;
import com.yuan.base.tools.system.SystemUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by YuanYe on 2017/4/30.
 * Activity的基本类。
 */
abstract class BaseActivity extends RxAppCompatActivity {

    protected BaseActivity mContext;

    //实现界面跳转
    protected void open(Class clazz) {
        JumpHelper.open(mContext, clazz);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        //防止当onActivityResult后mContext为空
        mContext = this;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        SystemUtil.Input.hideSoftInput(this);
        super.onDestroy();
    }
}
