package com.yuan.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yuan.kernel.BaseActivity;
import com.yuan.kernel.Presenter;

/**
 * 图片查看Activity
 */
public abstract class PhotoActivity<T extends Presenter> extends BaseActivity<T> {

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void findViews() {

    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

    }
}
