package com.yuan.base.widget.title.titlebar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yuan.base.R;
import com.yuan.base.tools.glide.GlideHelper;
import com.yuan.base.tools.other.Views;

/**
 * Created by YuanYe on 2018/8/4.
 * 如果需要自定义Title,可以继承AbsTitle
 */
abstract class AbsTitle<T extends AbsTitle> extends RelativeLayout implements ITitle {

    public static final int EMPTY_RES = -1;
    public static final String EMPTY_TEXT = "";

    protected Context context;

    private LinearLayout leftRoot;//左侧根布局
    private LinearLayout centerRoot;//中间根布局
    private LinearLayout rightRoot;//右侧根布局
    private ImageView background; //背景图片


    public AbsTitle(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AbsTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AbsTitle);
        CharSequence bgImg = ta.getText(R.styleable.AbsTitle_backgroundImg);
        setBackgroundImg((String) bgImg);
        init();
    }

    public void init() {
        //创建背景布局
        background = new ImageView(context);
        background.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(background);

        //创建左侧布局
        leftRoot = new LinearLayout(context);
        LayoutParams leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(ALIGN_PARENT_LEFT);
        leftParams.addRule(CENTER_VERTICAL);
        leftParams.addRule(LEFT_OF, android.R.id.autofill);
        leftRoot.setLayoutParams(leftParams);
        leftRoot.setOrientation(LinearLayout.HORIZONTAL);
        addView(leftRoot);

        //创建中间布局
        centerRoot = new LinearLayout(context);
        LayoutParams centerParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        centerParams.addRule(CENTER_HORIZONTAL);
        centerParams.addRule(CENTER_VERTICAL);
        centerRoot.setId(android.R.id.autofill);
        centerRoot.setLayoutParams(centerParams);
        centerRoot.setOrientation(LinearLayout.HORIZONTAL);
        addView(centerRoot);

        //右侧布局
        rightRoot = new LinearLayout(context);
        LayoutParams rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(ALIGN_PARENT_RIGHT);
        rightParams.addRule(CENTER_VERTICAL);
        leftParams.addRule(RIGHT_OF, android.R.id.autofill);
        rightRoot.setLayoutParams(rightParams);
        rightRoot.setOrientation(LinearLayout.HORIZONTAL);
        rightRoot.setGravity(Gravity.RIGHT);
        addView(rightRoot);
    }

    @Override
    public void addLeftView(View view) {
        leftRoot.addView(view);
    }

    @Override
    public void addRightView(View view) {
        rightRoot.addView(view);
    }

    @Override
    public void addCenterView(View view) {
        centerRoot.addView(view);
    }

    public <T extends View> T find(@IdRes int viewId) {
        return Views.find(this, viewId);
    }

    /**
     * 设置TitleBar的高度
     *
     * @param height
     * @return
     */
    public T setTitleBarHeight(float height) {
        if (height >= 0) {
            this.getLayoutParams().height = (int) height;
        }
        return (T) this;
    }


    /**
     * 获取TitleBar的高度
     *
     * @return
     */
    public int getTitleBarHeight() {
        return this.getLayoutParams().height;
    }

    /**
     * -------------------------------------toolbar平移动画---------------------------------------------
     **/

    /**
     * 设置进入动画
     *
     * @return
     */
    public T setAnimationIn() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, -this.getHeight(), 0);
        animation.setDuration(200);//设置动画持续时间
        this.setAnimation(animation);
        animation.startNow();
        this.setVisibility(View.VISIBLE);
        return (T) this;
    }

    /**
     * 开启结束动画
     *
     * @return
     */
    public T setAnimationOut() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -this.getHeight());
        animation.setDuration(200);//设置动画持续时间
        this.setAnimation(animation);
        animation.startNow();
        this.setVisibility(View.GONE);
        return (T) this;
    }

    /**
     * 重置动画
     *
     * @return
     */
    public T restoreAnimation() {
        if (this.getVisibility() == View.GONE) {
            //显示title
            this.setVisibility(VISIBLE);
        }
        return (T) this;
    }

    /**
     * *********************设置背景颜色、背景图片***********************************************************
     */
    /**
     * 设置Title的背景图片
     * 替换系统的设置背景图，采用Glide加载，不会使图片变形
     *
     * @return
     */
    public T setBackgroundImg(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl) && background != null)
            GlideHelper.load(imgUrl, background);
        return (T) this;
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        background.setImageDrawable(drawable);
    }

    @Override
    public void setBackgroundResource(int resid) {
        GlideHelper.load(resid, background);
    }

    @Override
    public void setBackgroundColor(int color) {
        Bitmap bitmap = Bitmap.createBitmap(10, 10,
                Bitmap.Config.ARGB_4444);
        bitmap.eraseColor(color);//填充颜色
        background.setImageBitmap(bitmap);
    }
}
