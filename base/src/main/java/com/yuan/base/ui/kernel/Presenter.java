package com.yuan.base.ui.kernel;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * 描述：Presenter
 * 接管Activity或Fragment中的逻辑操作，优化View中的代码逻辑
 * <p>
 * 特性：
 * 1.支持快捷加载 String,View,Drawable,color等资源
 * 2.支持主线程切换runOnUiThread
 * 3.接管Activity生命周期： onCreate,onResume,onDestroy(执行顺序低于Activity生命周期)
 * 4.通过泛型，自动绑定对应的Activity或Fragment,可通过getV()获取View实例
 *
 * @author yuanye
 * @date 2019/4/4 13:21
 */
public class Presenter<V extends Contract.View> implements Contract.IPresenter {

    private V view;

    /**
     * 切换到主线程
     */
    private Handler mainHandler;

    /**
     * 传递加载View
     *
     * @param View
     */
    public final void attachView(V View) {
        this.view = View;
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 获取 view实例
     *
     * @return
     */
    protected final V getV() {
        return view;
    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Context getContext() {
        if (view != null && view instanceof Activity) {
            return (Activity) view;
        }

        if (view != null && view instanceof Fragment) {
            return ((Fragment) view).getActivity();
        }

        if (view != null && view instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) view).getActivity();
        }

        throw new NullPointerException("没有找到Context，请检查View是否为空");
    }

    /**
     * 获取颜色
     *
     * @param colorId
     * @return
     */
    @ColorInt
    protected final int getColor2(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    /**
     * 获取Drawable
     *
     * @param drawableId
     * @return
     */
    @Nullable
    protected final Drawable getDrawable2(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(getContext(), drawableId);
    }

    /**
     * 获取String
     *
     * @param id
     * @return
     */
    @NonNull
    protected final String getString2(@StringRes int id) {
        return getContext().getResources().getString(id);
    }

    /**
     * Toast,系统默认样式
     *
     * @param msg 提示内容
     */
    protected final void showToast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 运行在主线程
     *
     * @param runnable
     */
    protected final void runOnUiThread(Runnable runnable) {
        if (mainHandler != null) mainHandler.post(runnable);
    }

}
