package com.yuan.base.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.yuan.base.ui.BaseContract;
import com.yuan.base.ui.extra.HSwipeBack;
import com.yuan.base.ui.extra.ISwipeBack;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by YuanYe on 2017/7/12.
 * <p>
 * 扩展功能基类，面对接口初始化
 * 如需使用扩展功能，应该继承该类
 */
abstract class ExtraActivity extends BaseActivity implements BaseContract.View {

    private static final String TAG = "ExtraActivity";

    private static final boolean INIT_DEFAULT = true; //初始化默认执行模块
    private static final boolean INIT_SWIPE_BACK = true; //初始化滑动返回模块
    private static final boolean INIT_EVENT_BUS = true;//初始化EventBus模块
    /**
     * Activity实现滑动返回功能,只需要在继承ExtraActivity后实现
     * ISwipeBack即可实现
     */
    private ISwipeBack iSwipeBack;
    /**
     * 是否启用EventBus,如果需要在继承BaseActivity的类中使用
     * 只需要调用openEvent方法即可，默认不启用EventBus
     */
    protected boolean useEvent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtra(savedInstanceState);
    }

    /**
     * 管理Activity的扩展功能
     */
    private void initExtra(@Nullable Bundle bundle) {
        if (INIT_DEFAULT) {
            View layoutView = getLayoutView();
            int layoutId = getLayoutId();
            if (layoutId != 0) {
                setContentView(layoutId);
            } else if (layoutView != null) {
                setContentView(layoutView);
            } else {
                Log.e(TAG, "没有给Activity设置显示视图");
            }
            findViews();
            parseBundle(bundle);
            initData();
            setListener();
        }

        if (INIT_SWIPE_BACK) {
            /**
             * 这里有一个bug,当系统高于8.1时，android:screenOrientation="portrait"与
             *    <item name="android:windowIsTranslucent">true</item> 冲突
             */
            if (this instanceof ISwipeBack && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                iSwipeBack = (ISwipeBack) this;
                HSwipeBack.init(this);
            }
        }

        if (INIT_EVENT_BUS) {
            if (useEvent && !EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (iSwipeBack != null) HSwipeBack.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iSwipeBack != null) HSwipeBack.onDestroy(this);
        if (useEvent && !EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

    }


    @Override
    public View getLayoutView() {
        return null;
    }

    protected void openEvent() {
        useEvent = true;
    }
}
