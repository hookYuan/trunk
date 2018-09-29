package com.yuan.base.ui;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by YuanYe on 2017/7/11.
 * 该接口主要用于约束Activity和Fragment的基本方法
 * 必须保证此接口中的方法只执行一次
 */
public interface IView {

    @LayoutRes
    int getLayoutId();

    View getLayoutView();

    void initData(Bundle savedInstanceState);

}
