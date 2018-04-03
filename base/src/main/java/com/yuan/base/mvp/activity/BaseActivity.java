package com.yuan.base.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yuan.base.common.kit.SysTool;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yuan.base.mvp.mvp.IView;

/**
 * Created by YuanYe on 2017/4/30.
 * Activity的基本类。
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IView {

    protected BaseActivity mContext;

    public void open(Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        startActivity(intent);
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
        SysTool.Input.hideSoftInput(this);
        super.onDestroy();
    }

    protected abstract void initData(Bundle savedInstanceState);
}
