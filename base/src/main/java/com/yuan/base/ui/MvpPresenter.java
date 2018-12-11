package com.yuan.base.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import com.yuan.base.tools.log.ToastUtil;

/**
 * 新增生命周期
 *
 * @author YuanYe on 2018/11/28.
 */
public class MvpPresenter<V extends BaseContract.View> implements BaseContract.Presenter {

    private V view;

    /**
     * 传递加载View
     *
     * @param View
     */
    public void attachView(V View) {
        this.view = View;
    }

    protected V getV() {
        if (view != null) {
            try {
                throw new NullPointerException("Presenter中获取Activity实例为空");
            } catch (NullPointerException e) {

            }
        }
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
            return (Activity)view;
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
     * Toast,系统默认样式，如需修改，请调用ToastUtil自定义
     *
     * @param msg 提示内容
     */
    protected final void toast(String msg) {
        ToastUtil.showShort(getContext(), msg);
    }


}
