package com.yuan.base.widget.title;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.ContentFrameLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yuan.base.R;
import com.yuan.base.tools.glide.GlideHelper;
import com.yuan.base.tools.other.Kits;
import com.yuan.base.tools.other.ToastUtil;
import com.yuan.base.tools.system.StatusBarUtil;
import com.yuan.base.tools.system.SystemUtil;

/**
 * Created by YuanYe on 2017/9/1.
 * 设置titleBar的主题,高度效果
 */
public class TitleThemeHelper<T extends TitleThemeHelper> extends TitleContentHelper<TitleThemeHelper> {

    private T child;

    private ViewGroup viewGroupContent; //内容父布局

    protected TitleThemeHelper(Context _context, @Nullable AttributeSet attrs) {
        super(_context, attrs);
        child = (T) this;
        init();
    }

    protected TitleThemeHelper(Context _context) {
        super(_context);
        child = (T) this;
        init();
    }

    /**
     * 初始化各个控件的默认状态
     */
    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootView.setTranslationZ(Kits.Dimens.dpToPx(context, floatZ));
        }
        //设置title背景图高度
        titleStatusBackground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , getStatusBarHeight() + getTitleBarHeight()));
        //设置状态栏颜色
        setDefaultTheme(new int[]{
                TitleInterface.STATUS_BAR_FONT_BLACK
                , TitleInterface.STATUS_BAR_BG_BLACK_TOP
                , TitleInterface.TITLE_FONT_BLACK
                , TitleInterface.TITLE_BG_WHITE
                , TitleInterface.TITLE_CONTENT_TOP
        });
    }

    /**
     * -------------------------------------toolbar默认主题样式组合---------------------------------------------
     **/
    public T setDefaultTheme(int... titleType) {
        for (int type : titleType) {
            switch (type) {
                case TitleInterface.STATUS_BAR_FONT_BLACK:
                    StatusBarUtil.darkMode((Activity) context, true);
                    break;
                case TitleInterface.STATUS_BAR_FONT_WHITE:
                    StatusBarUtil.darkMode((Activity) context, false);
                    break;
                case TitleInterface.STATUS_BAR_BG_TRANSPARENT_OVERLAY:
                    StatusBarUtil.immersive((Activity) context, true);
                    break;
                case TitleInterface.STATUS_BAR_BG_BLACK_OVERLAY:
                    StatusBarUtil.immersive((Activity) context, true, ContextCompat.getColor(context, android.R.color.black));
                    break;
                case TitleInterface.STATUS_BAR_BG_WHITE_OVERLAY:
                    StatusBarUtil.immersive((Activity) context, true, ContextCompat.getColor(context, R.color.white));
                    break;
                case TitleInterface.STATUS_BAR_BG_TRANSPARENT_TOP:
                    StatusBarUtil.immersive((Activity) context, false, ContextCompat.getColor(context, R.color.white));
                    break;
                case TitleInterface.STATUS_BAR_BG_BLACK_TOP:
                    StatusBarUtil.immersive((Activity) context, false, ContextCompat.getColor(context, R.color.white));
                    break;
                case TitleInterface.STATUS_BAR_BG_WHITE_TOP:
                    StatusBarUtil.immersive((Activity) context, false, ContextCompat.getColor(context, R.color.white));
                    break;
                case TitleInterface.TITLE_H:
                    rootView.setVisibility(GONE);
                    break;
                case TitleInterface.TITLE_FONT_BLACK:
                    setFontColor(ContextCompat.getColor(context, R.color.colorFont33));
                    break;
                case TitleInterface.TITLE_FONT_WHITE:
                    setFontColor(ContextCompat.getColor(context, R.color.white));
                    break;
                case TitleInterface.TITLE_BG_TRANSPARENT:
                    setTitleBarColor(ContextCompat.getColor(context, android.R.color.transparent));
                    break;
                case TitleInterface.TITLE_BG_BLACK:
                    setTitleBarColor(ContextCompat.getColor(context, R.color.black));
                    break;
                case TitleInterface.TITLE_BG_WHITE:
                    setTitleBarColor(ContextCompat.getColor(context, R.color.white));
                    break;
                case TitleInterface.TITLE_BG_STATUE_HEIGHT:
                    setStatuBarHeight(StatusBarUtil.getStatusBarHeight(context));
                    break;
                case TitleInterface.TITLE_CONTENT_TOP:
                    if (viewGroupContent == null) {
                        viewGroupContent = ((Activity) context).findViewById(android.R.id.content);
                        viewGroupContent.addView(this);
                    }
                    ContentFrameLayout.LayoutParams params = (ContentFrameLayout.LayoutParams) viewGroupContent.getChildAt(0).getLayoutParams();
                    params.topMargin = getTitleBarHeight() + getStatusBarHeight();
                    viewGroupContent.getChildAt(0).setLayoutParams(params);
                    break;
                case TitleInterface.TITLE_CONTENT_OVERLAY:
                    //默认覆盖
                    if (viewGroupContent == null) {
                        viewGroupContent = ((Activity) context).findViewById(android.R.id.content);
                        viewGroupContent.addView(this);
                    }
                    ContentFrameLayout.LayoutParams params2 = (ContentFrameLayout.LayoutParams) viewGroupContent.getChildAt(0).getLayoutParams();
                    params2.topMargin = 0;
                    viewGroupContent.getChildAt(0).setLayoutParams(params2);
                    break;
            }
        }
        return child;
    }


    /**
     * -------------------------------------设置toolbar颜色---------------------------------------------
     **/
    public T setFontColor(@ColorInt int textColor) {
        leftTextView.setTextColor(textColor);
        centerTextView.setTextColor(textColor);
        rightTextView.setTextColor(textColor);
        return child;
    }

    public T setTitleBarColor(@ColorInt int backgroundColor) {
        if (titleBackground != null && backgroundColor != -1) {
            titleBackground.setBackgroundColor(backgroundColor);
        }
        return child;
    }

    public T setStatusBarColor(@ColorInt int backgroundColor) {
        if (statusBackground != null && backgroundColor != -1) {
            statusBackground.setBackgroundColor(backgroundColor);
        }
        return child;
    }

    public T setTitleStatusBgImg(@DrawableRes int drawableId) {
        titleStatusBackground.setBackgroundResource(drawableId);
        setStatusBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        setTitleBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        return child;
    }

    public T setTitleStatusBgImg(String imgUrl) {
        GlideHelper.load(imgUrl, titleStatusBackground);
        setStatusBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        setTitleBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        return child;
    }

    /**
     * -------------------------------------设置背景的透明度---------------------------------------------------
     */
    public T setTitleBgAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        titleBackground.setAlpha(alpha);
        return child;
    }

    public T setStatuBgAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        statusBackground.setAlpha(alpha);
        return child;
    }

    public T setTitleStatusAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        setStatusBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        setTitleBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        titleStatusBackground.setAlpha(alpha);
        return child;
    }

    /**
     * -------------------------------------设置toolbar高度---------------------------------------------
     **/
    public T setStatuBarHeight(int height) {
        //版本判断，5.0以上有效
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Log.e(TAG, "系统版本不支持StatueBar");
            return child;
        }
        if (height >= 0 && statusBarView != null) {
            statusBarView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            statusBackground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        }
        return child;
    }

    public T setTitleBarHeight(int height) {
        if (height >= 0 && titleRootView != null) {
            titleRootView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        }
        return child;
    }

    public int getStatusBarHeight() {
        int h = 0;
        if (statusBarView != null) {
            h = statusBarView.getLayoutParams().height;
        }
        return h;
    }

    public int getTitleBarHeight() {
        int h = 0;
        if (titleRootView != null) {
            h = titleRootView.getLayoutParams().height;
        }
        return h;
    }
}
