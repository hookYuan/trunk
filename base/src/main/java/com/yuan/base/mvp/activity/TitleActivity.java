package com.yuan.base.mvp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.yuan.base.mvp.mvp.IView;
import com.yuan.base.widget.title.TitleBar;

/**
 * Created by YuanYe on 2017/9/7.
 * Activity中默认添加进入Title,继承该类默认使用Title
 */
public abstract class TitleActivity extends BaseActivity implements IView {

    private TitleBar titleBar;
    protected View contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (contentView == null) {
            setContentView(getLayoutId());
        } else {
            setContentView(contentView);
        }
        titleBar = new TitleBar(mContext);
        initData(savedInstanceState);
    }

    protected TitleBar getTitleBar() {
        if (titleBar == null) {
            Log.e("TitleBar", "TitleBar没有初始化，请选择TitleBar模式");
            return null;
        }
        return titleBar;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }
}
