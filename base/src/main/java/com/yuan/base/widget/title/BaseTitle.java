package com.yuan.base.widget.title;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuan.base.R;
import com.yuan.base.tools.other.Views;

/**
 * Created by YuanYe on 2017/8/17.
 * Title基本布局加载
 */
class BaseTitle extends LinearLayout {

    protected Context context;
    protected LinearLayout rootView; //根布局

    protected View statusBarView;//状态栏
    protected RelativeLayout statusRoot;//状态栏根View

    protected RelativeLayout titleRootView;// titleBarRootView

    protected ImageView titleStatusBackground;//titleBar和status的背景图片
    protected ImageView titleBackground;//titleBar的背景
    protected ImageView statusBackground;//statusBar的背景

    protected RelativeLayout leftRoot;//左侧根布局
    protected RelativeLayout centerRoot;//中间根布局
    protected RelativeLayout rightRoot;//右侧根布局

    protected TextView leftTextView;//左侧
    protected TextView centerTextView;//中间
    protected TextView rightTextView;//右侧

    protected View lineTitle;
    protected int floatZ = 4; //悬浮的高度默认悬浮4dp

    protected String TAG = "TitleBar";

    protected BaseTitle(Context _context, @Nullable AttributeSet attrs) {
        super(_context, attrs);
        this.context = _context;
        initView();
    }

    public BaseTitle(Context _context) {
        super(_context);
        this.context = _context;
        initView();
    }

    /**
     * 初始化布局文件
     */
    private void initView() {
        rootView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.title_tool_bar, this, true);

        statusRoot = findViewById(R.id.rl_status);
        statusBarView = findViewById(R.id.status_bar);

        titleStatusBackground = findViewById(R.id.iv_title_bar_status_bg);
        titleBackground = findViewById(R.id.iv_title_bar_bg);
        statusBackground = findViewById(R.id.iv_status_bg);

        titleRootView = findViewById(R.id.rl_title_bar);
        leftRoot = findViewById(R.id.rl_left_toolbar);
        centerRoot = findViewById(R.id.rl_center_toolbar);
        rightRoot = findViewById(R.id.rl_right_toolbar);
        leftTextView = findViewById(R.id.tv_left);
        centerTextView = findViewById(R.id.tv_center);
        rightTextView = findViewById(R.id.tv_right);

        lineTitle = findViewById(R.id.title_line);
    }

    /**
     * *************************************获取重要参数*******************************************************
     * TODO 待优化
     */
    public ViewGroup getLeftRoot() {
        return leftRoot;
    }

    public ViewGroup getCenterRoot() {
        return centerRoot;
    }

    public ViewGroup getRightRoot() {
        return rightRoot;
    }

}
