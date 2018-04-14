package com.yuan.base.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.base.widget.title.TitleInterface;
import com.yuan.base.widget.title.TitleBar;

/**
 * Created by YuanYe on 2017/9/7.
 */

public abstract class TitleFragment extends StatedFragment {

    private TitleBar titleBar;
    private View layoutView; //内容View

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = super.onCreateView(inflater, container, savedInstanceState);
//        switch (showToolBarType()) {
//            case NO_TITLE:
//                //不显示toolbar(全屏显示)
//                if (layoutView != null) {
//                    mview = layoutView;
//                }
//                break;
//            case SIMPLE_TITLE:
//                setSimpleTitle(container);
//                break;
//            case OVERLAP_TITLE:  //显示透明toolbar
//                setOverlap(container);
//                break;
//        }
        return mview;
    }


    protected TitleBar getTitleBar() {
        if (titleBar == null) {
            Log.e("TitleBar", "TitleBar没有初始化，请选择TitleBar模式");
            return null;
        }
        return titleBar;
    }

    /**
     * 控制Activity是否显示toolbar,重写该方法可修改
     */
    protected int showToolBarType() {
        return TitleInterface.STATUS_BAR_FONT_BLACK;
    }

    /**
     * 可以对getLayoutID的内容包装后统一返回
     *
     * @param layoutView
     */
    protected void setLayoutView(View layoutView) {
        this.layoutView = layoutView;
    }

}
