package com.yuan.base.widget.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yuan.base.R;
import com.yuan.base.tools.other.Kits;
import com.yuan.base.widget.title.statusbar.StatusBar;
import com.yuan.base.widget.title.statusbar.StatusUtil;
import com.yuan.base.widget.title.titlebar.TitleBar;

/**
 * Created by YuanYe on 2018/8/5.
 * 结合TitleBar 和 自定 statueBar
 * 如果这里的TitleBar不符合需求，可以自由定义
 */
public class Title extends LinearLayout {

    private int titleBarHeight = 50; //titleBar的默认高度

    private TitleBar titleBar;
    private StatusBar statusBar;

    private Context context;

    public Title(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Title(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        titleBar.obtainAttributes(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Title);
        int statusBarColor = ta.getColor(R.styleable.Title_statusBackgroundColor, ContextCompat.getColor(context, R.color.colorPrimaryDark));
        statusBar.setBackgroundColor(statusBarColor);
        boolean statusVisibility = ta.getBoolean(R.styleable.Title_statusVisibility, false);
        statusBar.setVisibility(statusVisibility ? VISIBLE : GONE);
        ta.recycle();

    }

    public void init() {
        titleBar = new TitleBar(context);
        LinearLayout.LayoutParams titleBarParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) Kits.Dimens.dpToPx(context, titleBarHeight));
        titleBar.setLayoutParams(titleBarParams);
        statusBar = new StatusBar(context);
        LinearLayout.LayoutParams statusBarParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, StatusUtil.getStatusBarHeight(context));
        statusBar.setLayoutParams(statusBarParams);
        this.setOrientation(LinearLayout.VERTICAL);
        this.addView(statusBar);
        this.addView(titleBar);
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public void setTitleBar(TitleBar titleBar) {
        this.titleBar = titleBar;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public void setStatusBar(StatusBar statusBar) {
        this.statusBar = statusBar;
    }
}
