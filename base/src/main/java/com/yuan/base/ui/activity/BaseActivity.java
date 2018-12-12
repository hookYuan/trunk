package com.yuan.base.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.yuan.base.tools.layout.Views;
import com.yuan.base.tools.log.ToastUtil;
import com.yuan.base.tools.router.jump.JumpActivity;
import com.yuan.base.tools.router.jump.JumpParam;
import com.yuan.base.tools.common.KeyboardUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by YuanYe on 2017/4/30.
 * Activity的基本类。
 */

abstract class BaseActivity extends RxAppCompatActivity {

    /**
     * 上下文对象
     */
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
        JumpActivity.open(mContext, clazz);
    }

    /**
     * Intent 跳转，携带参数
     *
     * @param clazz Activity类名
     * @param param 跳转需要传递的参数
     */
    public void open(Class clazz, JumpParam param) {
        JumpActivity.open(mContext, clazz, param);
    }

    /**
     * 代替findViewById
     *
     * @param viewId
     * @param <T>
     * @return
     */
    protected <T extends View> T find(@IdRes int viewId) {
        return Views.find(this, viewId);
    }

    /**
     * 获取颜色
     * 避免23以上可用
     *
     * @param colorId
     * @return
     */
    protected @ColorInt
    final int getColor2(@ColorRes int colorId) {
        return ContextCompat.getColor(this, colorId);
    }

    /**
     * 获取Drawable
     * 避免23以上可用
     *
     * @param drawableId
     * @return
     */
    protected final Drawable getDrawable2(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(this, drawableId);
    }

    /**
     * Toast,系统默认样式，如需修改，请调用ToastUtil自定义
     *
     * @param msg 提示内容
     */
    protected final void toast(String msg) {
        ToastUtil.showShort(mContext, msg);
    }


}
