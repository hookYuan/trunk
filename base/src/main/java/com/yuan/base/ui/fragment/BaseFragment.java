package com.yuan.base.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.base.tools.layout.Views;
import com.yuan.base.tools.log.ToastUtil;
import com.yuan.base.tools.router.jump.Jump;
import com.yuan.base.tools.router.jump.JumpParam;
import com.yuan.base.tools.common.KeyboardUtil;
import com.yuan.base.ui.BaseContract;

/**
 * Created by YuanYe on 2017/4/30.
 * 7/10日修复BaseFragment部分bug,完善功能
 */
abstract class BaseFragment extends Fragment implements BaseContract.View {

    protected Activity mContext;  //防止getActivity()空指针
    //保存Fragment的状态，防止重启后Fragment重叠
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected View mView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutView = getLayoutView();
        int layoutId = getLayoutId();
        if (layoutId != 0) mView = inflater.inflate(layoutId, container, false);
        else if (layoutView != null) mView = layoutView;
        else throw new NullPointerException("Fragment布局视图不能为空");
        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //保存状态
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @TargetApi(23) //API<23不对调用该方法
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        this.mContext = (Activity) context;
    }

    @Override
    public void onResume() {
        this.mContext = this.getActivity();
        super.onResume();
    }


    @Override
    public void onDestroy() {
        KeyboardUtil.hideSoftInput(mContext);
        super.onDestroy();
    }

    /**
     * Intent 跳转，不带参数
     *
     * @param clazz
     */
    protected void open(Class clazz) {
        Jump.open(mContext, clazz);
    }

    /**
     * Intent 跳转，携带参数
     *
     * @param clazz
     * @param param
     */
    protected void open(Class clazz, JumpParam param) {
        Jump.open(mContext, clazz, param);
    }

    protected <T extends View> T find(@IdRes int viewId) {
        return Views.find(mView, viewId);
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
        return ContextCompat.getColor(mContext, colorId);
    }

    /**
     * 获取Drawable
     * 避免23以上可用
     *
     * @param drawableId
     * @return
     */
    protected final Drawable getDrawable2(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(mContext, drawableId);
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
