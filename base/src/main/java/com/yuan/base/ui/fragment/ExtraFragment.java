package com.yuan.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by YuanYe on 2017/8/9.
 */

public abstract class ExtraFragment extends LazyFragment {

    private static final String TAG = "ExtraFragment";

    private static final boolean INIT_EVENT_BUS = true;//初始化EventBus模块
    /**
     * 优先于getLayoutId执行,设置后代替onCreateView加载的布局文件
     * 可以通过setContentView方法设置修改
     */
    protected View contentView;
    /**
     * 是否启用EventBus,如果需要在继承BaseActivity的类中使用
     * 只需要调用openEvent方法即可，默认不启用EventBus
     */
    protected boolean useEvent = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView != null) mView = contentView;
        initExtra();
        return mView;
    }

    /**
     * 管理Fragment的扩展功能
     */
    private void initExtra() {
        if (INIT_EVENT_BUS) {
            if (INIT_EVENT_BUS) {
                if (useEvent && !EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().register(this);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (useEvent && !EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }


    protected void setContentView(View contentView) {
        this.contentView = contentView;
    }
}
