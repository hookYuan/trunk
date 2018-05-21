package com.yuan.base.widget.title;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuan.base.R;
import com.yuan.base.tools.adapter.BaseListAdapter;
import com.yuan.base.tools.glide.GlideHelper;
import com.yuan.base.tools.other.Kits;
import com.yuan.base.tools.system.StatusBarUtil;

import java.util.List;

/**
 * Created by YuanYe on 2017/8/17.
 * Title基本布局加载
 */
public class TitleBar extends LinearLayout {
    protected static final String TAG = "TitleBar";
    private static final int EMPTY_RESOURCES_TYPE = -1;
    private static final String EMPTY_TEXT_TYPE = "";

    protected Context context;
    protected View rootView; //根布局

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

    protected View lineTitle; //Title最下方线条

    private PopupWindow popupWindowMenu;//弹窗菜单

    protected int floatZ = 4; //悬浮的高度默认悬浮4dp


    public TitleBar(Context _context, @Nullable AttributeSet attrs) {
        super(_context, attrs);
        this.context = _context;
        obtainAttributes(_context, attrs);
//        initDefault();
    }


    public TitleBar(Context _context) {
        super(_context);
        this.context = _context;
//        initDefault();
    }


    /**
     * 绑定自定义属性
     *
     * @param context
     * @param attrs
     */
    private void obtainAttributes(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        CharSequence leftText = ta.getText(R.styleable.TitleBar_leftText);
        CharSequence centerText = ta.getText(R.styleable.TitleBar_centerText);
        CharSequence rightText = ta.getText(R.styleable.TitleBar_rightText);
        Drawable leftIcon = ta.getDrawable(R.styleable.TitleBar_leftDrawable);
        Drawable rightIcon = ta.getDrawable(R.styleable.TitleBar_rightDrawable);
        //模拟状态栏的高度，默认0dp
        float statusBarHeight = ta.getDimension(R.styleable.TitleBar_statusBarHeight, 0);
        //TitleBar的高度,默认高度50dp
        float titleBarHeight = ta.getDimension(R.styleable.TitleBar_titleBarHeight, 50 * context.getResources().getDisplayMetrics().density);
        //底部线条的高度,默认0.8dp
        float bottomLineHeight = ta.getDimension(R.styleable.TitleBar_bottomLineHeight, 0.8f * context.getResources().getDisplayMetrics().density);
        boolean leftClickFinish = ta.getBoolean(R.styleable.TitleBar_leftClickFinish, false);
        int leftColor = ta.getColor(R.styleable.TitleBar_leftTextColor, ContextCompat.getColor(context, R.color.colorFont33));
        int centerColor = ta.getColor(R.styleable.TitleBar_centerTextColor, ContextCompat.getColor(context, R.color.colorFont33));
        int rightColor = ta.getColor(R.styleable.TitleBar_rightTextColor, ContextCompat.getColor(context, R.color.colorFont33));
        float leftSize = ta.getDimension(R.styleable.TitleBar_leftTextSize, 16 * context.getResources().getDisplayMetrics().scaledDensity);
        float centerSize = ta.getDimension(R.styleable.TitleBar_centerTextSize, 18 * context.getResources().getDisplayMetrics().scaledDensity);
        float rightSize = ta.getDimension(R.styleable.TitleBar_rightTextSize, 16 * context.getResources().getDisplayMetrics().scaledDensity);
        //根据类型设置选择加载布局类型
        int titleType = ta.getInt(R.styleable.TitleBar_titleBarType, 1);
        initView(titleType);
        //设置文字和图片显示
        setLeftText(leftText);
        setCenterText(centerText);
        setRightText(rightText);
        setLeftIcon(leftIcon);
        setRightIcon(rightIcon);
        //控件高度
        setStatusBarHeight(statusBarHeight);
        setTitleBarHeight(titleBarHeight);
        setBottomLineHeight(bottomLineHeight);
        //是否启用点击leftTextView关闭当前Activity
        setLeftClickFinish(leftClickFinish);
        //设置字体颜色
        setLeftTextColor(leftColor);
        setCenterTextColor(centerColor);
        setRightTextColor(rightColor);
        //设置字体大小
        setLeftTextSize(leftSize);
        setCenterTextSize(centerSize);
        setRightTextSize(rightSize);
        //

        ta.recycle();
    }

    /**
     * 初始化布局文件
     */
    private void initView(int titleType) {
        switch (titleType) {
            case 0: //中间文字居左布局
                rootView = LayoutInflater.from(context).inflate(R.layout.title_bar_left, this, true);
                break;
            case 1: //中间文字居中
            case 2: //TODO 中级搜索框
                rootView = LayoutInflater.from(context).inflate(R.layout.title_bar_center, this, true);
                break;
        }

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
     * 初始化各个控件的默认状态
     */
    private void initDefault() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootView.setTranslationZ(Kits.Dimens.dpToPx(context, floatZ));
        }
        //设置状态栏颜色
        setTheme(new int[]{
                TitleTheme.STATUS_BAR_FONT_BLACK
                , TitleTheme.TITLE_FONT_BLACK
                , TitleTheme.TITLE_BG_WHITE
        });

        if (!isInEditMode()) {
            //设置title背景图高度
            titleStatusBackground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , getStatusBarHeight() + getTitleBarHeight() + getBottomLineHeight()));
        }

    }

    /**
     * ------------------------------------左侧toolbar按钮设置----------------------------------------
     **/
    public TitleBar setLeftTextIcon(CharSequence text, @DrawableRes int icon) {
        if (leftTextView != null && text != null) {
            leftTextView.setText(text);
        }
        if (icon != EMPTY_RESOURCES_TYPE) {
            Drawable drawable = getResources().getDrawable(icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            leftTextView.setCompoundDrawables(drawable, null, null, null);//画在左边
        } else {
            leftTextView.setCompoundDrawables(null, null, null, null);//画在左边
        }
        return this;
    }

    public TitleBar setLeftClickFinish(boolean finish) {
        if (finish) setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                if (activity != null)
                    activity.finish();
            }
        });
        return this;
    }

    public TitleBar setLeftOnClickListener(OnClickListener listener) {
        if (leftTextView != null && listener != null) leftTextView.setOnClickListener(listener);
        return this;
    }

    public TitleBar setLeftIcon(Drawable icon) {
        if (leftTextView != null && icon != null) {
            icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight()); //设置边界
            leftTextView.setCompoundDrawables(icon, null, null, null);//画在左边
        }
        return this;
    }

    public TitleBar setLeftIcon(@DrawableRes int icon) {
        return setLeftTextIcon(EMPTY_TEXT_TYPE, icon);
    }

    public TitleBar setLeftText(CharSequence text) {
        return setLeftTextIcon(text, EMPTY_RESOURCES_TYPE);
    }

    /**
     * ------------------------------------中间toolbar按钮设置----------------------------------------
     **/
    public TitleBar setCenterText(CharSequence text) {
        if (centerTextView != null && text != null) {
            centerTextView.setText(text);
        }
        return this;
    }

    //自定义titleBar中间布局(如果view设置layoutParams,必须是以)
    public TitleBar addCenterView(View centerView) {
        if (centerView.getLayoutParams() == null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            centerView.setLayoutParams(params);
        }
        centerRoot.addView(centerView);
        return this;
    }

    /**
     * ------------------------------------右侧toolbar按钮设置----------------------------------------
     **/
    public TitleBar setRightTextIcon(CharSequence text, @DrawableRes int icon) {
        if (rightTextView != null && text != null) {
            rightTextView.setText(text);
        }
        if (icon != EMPTY_RESOURCES_TYPE) {
            Drawable drawable = getResources().getDrawable(icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            rightTextView.setCompoundDrawables(drawable, null, null, null);//画在左边
        } else {
            rightTextView.setCompoundDrawables(null, null, null, null);
        }


        return this;
    }

    public TitleBar setRightIcon(Drawable icon) {
        if (rightTextView != null && icon != null) {
            icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight()); //设置边界
            rightTextView.setCompoundDrawables(icon, null, null, null);//画在左边
        }
        return this;
    }

    public TitleBar setRightIcon(@DrawableRes int icon) {
        return setRightTextIcon(EMPTY_TEXT_TYPE, icon);
    }

    public TitleBar setRightText(CharSequence text) {
        return setRightTextIcon(text, EMPTY_RESOURCES_TYPE);
    }

    public TitleBar setRightOnClick(OnClickListener listener) {
        if (listener != null) {
            rightRoot.setOnClickListener(listener);
        }
        return this;
    }

    public TitleBar setRightClickEnable(boolean clickAble) {
        if (rightTextView != null) {
            rightTextView.setEnabled(clickAble);
            rightRoot.setClickable(clickAble);
        }
        return this;
    }

    public TitleBar setRightAsButton(@DrawableRes int res) {
        if (rightTextView != null) {
            rightTextView.setBackgroundResource(res);
            int left = Kits.Dimens.dpToPxInt(context, 8);
            int top = Kits.Dimens.dpToPxInt(context, 4);
            int right = Kits.Dimens.dpToPxInt(context, 8);
            int bottom = Kits.Dimens.dpToPxInt(context, 4);
            rightTextView.setPadding(left, top, right, bottom);
        }
        return this;
    }

    /**
     * ------------------------------------右侧toolbar的menu菜单按钮设置----------------------------------------
     **/
    public TitleBar setRightMenu(final List<String> popupData, final OnMenuItemClickListener listener) {
        setRightOnClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(rightTextView, popupData, listener);
            }
        });
        return this;
    }


    /**
     * @param view
     * @param popupData
     * @param listener
     */
    public void showPopMenu(View view, List<String> popupData, final OnMenuItemClickListener listener) {
        if (popupWindowMenu != null && popupWindowMenu.isShowing()) {
            //关闭popupWindow
            popupWindowMenu.dismiss();
        } else {
            final View popupView = LayoutInflater.from(context).inflate(R.layout.title_menu, null);
            popupWindowMenu = new PopupWindow(popupView, ListPopupWindow.WRAP_CONTENT, ListPopupWindow.WRAP_CONTENT, true);

            //设置弹出的popupWindow不遮挡软键盘
            popupWindowMenu.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindowMenu.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            //设置点击外部让popupWindow消失
            popupWindowMenu.setFocusable(true);//可以试试设为false的结果
            popupWindowMenu.setOutsideTouchable(true); //点击外部消失

            //必须设置的选项
            popupWindowMenu.setBackgroundDrawable(ContextCompat.getDrawable(context, android.R.color.transparent));
            popupWindowMenu.setTouchInterceptor(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                }
            });
            //将window视图显示在点击按钮下面(向上偏移20像素)
            popupWindowMenu.showAsDropDown(view, 0, 0);
            ListView listView = (ListView) popupView.findViewById(R.id.pop_listView);
            listView.setAdapter(new BaseListAdapter<String>(popupData, R.layout.title_menu_item) {
                @Override
                public void bindView(ViewHolder holder, String obj) {
                    holder.setText(R.id.tv_item_content, obj);
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    listener.onItemClick(popupWindowMenu, adapterView, view, i);
                }
            });
        }
    }

    /**
     * 显示底部线条
     *
     * @return
     */
    public TitleBar showBottomLine() {
        if (lineTitle != null) {
            lineTitle.setVisibility(VISIBLE);
        }
        return this;
    }

    /**
     * 隐藏底部线条
     *
     * @return
     */
    public TitleBar hideBottomLine() {
        if (lineTitle != null) {
            lineTitle.setVisibility(GONE);
        }
        return this;
    }


    /**
     * -------------------------------------toolbar默认主题样式组合---------------------------------------------
     **/

    /**
     * 设置ToolBar默认主题
     *
     * @param titleType 默认主题泛型
     * @return
     */
    public TitleBar setTheme(int... titleType) {
        for (int type : titleType) {
            switch (type) {
                case TitleTheme.STATUS_BAR_FONT_BLACK:
                    break;
                case TitleTheme.STATUS_BAR_FONT_WHITE:
                    break;
                case TitleTheme.STATUS_BAR_BG_TRANSPARENT_OVERLAY:
                    break;
                case TitleTheme.STATUS_BAR_BG_BLACK_OVERLAY:
                    break;
                case TitleTheme.STATUS_BAR_BG_WHITE_OVERLAY:
                    break;
                case TitleTheme.STATUS_BAR_BG_TRANSPARENT_TOP:
                    break;
                case TitleTheme.STATUS_BAR_BG_BLACK_TOP:
                    break;
                case TitleTheme.STATUS_BAR_BG_WHITE_TOP:
                    break;
                case TitleTheme.TITLE_H:
                    rootView.setVisibility(GONE);
                    break;
                case TitleTheme.TITLE_FONT_BLACK:
                    setLeftTextColor(ContextCompat.getColor(context, R.color.colorFont33));
                    setCenterTextColor(ContextCompat.getColor(context, R.color.colorFont33));
                    setRightTextColor(ContextCompat.getColor(context, R.color.colorFont33));
                    break;
                case TitleTheme.TITLE_FONT_WHITE:
                    setLeftTextColor(ContextCompat.getColor(context, R.color.white));
                    setCenterTextColor(ContextCompat.getColor(context, R.color.white));
                    setRightTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
                case TitleTheme.TITLE_BG_TRANSPARENT:
                    setTitleBarColor(ContextCompat.getColor(context, android.R.color.transparent));
                    break;
                case TitleTheme.TITLE_BG_BLACK:
                    setTitleBarColor(ContextCompat.getColor(context, R.color.black));
                    break;
                case TitleTheme.TITLE_BG_WHITE:
                    setTitleBarColor(ContextCompat.getColor(context, R.color.white));
                    break;
                case TitleTheme.TITLE_BG_STATUE_HEIGHT:
                    setStatusBarHeight(StatusBarUtil.getStatusBarHeight(context));
                    break;
            }
        }
        return this;
    }


    /**
     * -------------------------------------设置toolbar字体颜色、背景颜色、背景图片---------------------------------------------
     **/

    public TitleBar setLeftTextColor(@ColorInt int textColor) {
        if (leftTextView != null && textColor != 0)
            leftTextView.setTextColor(textColor);
        return this;
    }

    public TitleBar setCenterTextColor(@ColorInt int textColor) {
        if (centerTextView != null && textColor != 0)
            centerTextView.setTextColor(textColor);
        return this;
    }

    public TitleBar setRightTextColor(@ColorInt int textColor) {
        if (rightTextView != null && textColor != 0)
            rightTextView.setTextColor(textColor);
        return this;
    }

    /**
     * 设置系统状态栏字体黑色
     *
     * @return
     */
    public TitleBar setSystemStatusBarFontBlack() {
        if (!isInEditMode()) {
            StatusBarUtil.darkMode((Activity) context, true);
        }
        return this;
    }

    /**
     * 设置系统状态栏字体白色
     *
     * @return
     */
    public TitleBar setSystemStatusBarFontWhite() {
        if (!isInEditMode()) {
            StatusBarUtil.darkMode((Activity) context, false);
        }
        return this;
    }

    /**
     * 设置title是否覆盖在内容之上
     *
     * @return
     */
    public TitleBar setSystemStatusBarOverlyBg(boolean isOverly, @ColorInt int bgColor, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (!isInEditMode()) {
            if (bgColor != 0 && alpha != 0)
                StatusBarUtil.immersive((Activity) context, isOverly, bgColor, alpha);
            else if (bgColor != 0)
                StatusBarUtil.immersive((Activity) context, isOverly, bgColor);
            else
                StatusBarUtil.immersive((Activity) context, isOverly);
        }
        return this;
    }


    /**
     * 设置TitleBar背景颜色
     *
     * @param backgroundColor
     * @return
     */
    public TitleBar setTitleBarColor(@ColorInt int backgroundColor) {
        if (titleBackground != null && backgroundColor != -1) {
            titleBackground.setBackgroundColor(backgroundColor);
        }
        return this;
    }

    /**
     * 设置模拟状态栏的颜色
     *
     * @param backgroundColor
     * @return
     */
    public TitleBar setStatusBarColor(@ColorInt int backgroundColor) {
        if (statusBackground != null && backgroundColor != -1) {
            statusBackground.setBackgroundColor(backgroundColor);
        }
        return this;
    }

    /**
     * 设置TitleBar和模拟状态栏的背景图片
     *
     * @param drawableId
     * @return
     */
    public TitleBar setTitleStatusBgImg(@DrawableRes int drawableId) {
        titleStatusBackground.setBackgroundResource(drawableId);
        setStatusBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        setTitleBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        return this;
    }

    /**
     * 设置TitleBar和模拟状态栏的背景图片
     *
     * @param imgUrl 网络图片地址
     * @return
     */
    public TitleBar setTitleStatusBgImg(String imgUrl) {
        GlideHelper.load(imgUrl, titleStatusBackground);
        setStatusBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        setTitleBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        return this;
    }

    /**
     * -------------------------------------设置字体的大小---------------------------------------------------
     */
    public TitleBar setLeftTextSize(float size) {
        if (leftTextView != null && size != 0)
            leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setCenterTextSize(float size) {
        if (centerTextView != null && size != 0)
            centerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setRightTextSize(float size) {
        if (rightTextView != null && size != 0)
            rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    /**
     * -------------------------------------设置背景的透明度---------------------------------------------------
     */

    /**
     * 设置TitleBar的背景透明度
     *
     * @param alpha
     * @return
     */
    public TitleBar setTitleBgAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        titleBackground.setAlpha(alpha);
        return this;
    }

    /**
     * 设置模拟状态栏透明度
     *
     * @param alpha
     * @return
     */
    public TitleBar setStatuBgAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        statusBackground.setAlpha(alpha);
        return this;
    }

    /**
     * 设置模拟状态栏+TitleBar的透明度
     *
     * @param alpha
     * @return
     */
    public TitleBar setTitleStatusAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        setStatusBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        setTitleBarColor(ContextCompat.getColor(context, android.R.color.transparent));
        titleStatusBackground.setAlpha(alpha);
        return this;
    }

    /**
     * -------------------------------------设置toolbar高度---------------------------------------------
     **/
    /**
     * 设置模拟状态栏的高度
     *
     * @param height
     * @return
     */
    public TitleBar setStatusBarHeight(float height) {
        //版本判断，5.0以上有效
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Log.e(TAG, "系统版本不支持StatueBar,无须设置模拟状态栏高度");
            return this;
        }
        if (height >= 0 && statusBarView != null) {
            statusBarView.getLayoutParams().height = (int) height;
            statusBackground.getLayoutParams().height = (int) height;
        }
        return this;
    }

    /**
     * 设置TitleBar的高度
     *
     * @param height
     * @return
     */
    public TitleBar setTitleBarHeight(float height) {
        if (height >= 0 && titleRootView != null) {
            titleRootView.getLayoutParams().height = (int) height;
            titleBackground.getLayoutParams().height = (int) height;
        }
        return this;
    }

    public TitleBar setBottomLineHeight(float height) {
        if (height >= 0 && lineTitle != null) {
            lineTitle.getLayoutParams().height = (int) height;
        }
        return this;
    }

    /**
     * 获取模拟状态栏的高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int h = 0;
        if (statusBarView != null) {
            h = statusBarView.getLayoutParams().height;
        }
        return h;
    }

    /**
     * 获取TitleBar的高度
     *
     * @return
     */
    public int getTitleBarHeight() {
        int h = 0;
        if (titleRootView != null) {
            h = titleRootView.getLayoutParams().height;
        }
        return h;
    }

    /**
     * 获取底部线条的高度
     *
     * @return
     */
    public int getBottomLineHeight() {
        if (lineTitle != null && (lineTitle.getVisibility() == VISIBLE ||
                lineTitle.getVisibility() == INVISIBLE)) {
            return lineTitle.getLayoutParams().height;
        }
        return 0;
    }

    /**
     * -------------------------------------toolbar平移动画---------------------------------------------
     * TODO 需要在activity初始化完成后调用，rootview.getHeight（）可能为空
     **/

    /**
     * 设置进入动画
     *
     * @return
     */
    public TitleBar setAnimationIn() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, -rootView.getHeight(), 0);
        animation.setDuration(300);//设置动画持续时间
        rootView.setAnimation(animation);
        animation.startNow();
        rootView.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 开启结束动画
     *
     * @return
     */
    public TitleBar setAnimationOut() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -rootView.getHeight());
        animation.setDuration(300);//设置动画持续时间
        rootView.setAnimation(animation);
        animation.startNow();
        rootView.setVisibility(View.GONE);
        return this;
    }

    /**
     * 重置动画
     *
     * @return
     */
    public TitleBar restoreAnimation() {
        if (rootView.getVisibility() == View.GONE) {
            //显示title
            rootView.setVisibility(VISIBLE);
        }
        return this;
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
