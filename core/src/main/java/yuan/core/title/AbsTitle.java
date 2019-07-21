/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package yuan.core.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 如果需要自定义Title,可以继承AbsTitle
 *
 * @author YuanYe
 * @date 2018/8/4  10:33
 */
abstract class AbsTitle<T extends AbsTitle> extends RelativeLayout implements ITitle {

    public static final int EMPTY_RES = -1;
    public static final String EMPTY_TEXT = "";

    protected Context context;

    protected LinearLayout leftRoot;//左侧根布局
    protected LinearLayout centerRoot;//中间根布局
    protected LinearLayout rightRoot;//右侧根布局

    private int defaultHeight = 50; //默认高度，单位dp
    private ImageView background; //背景图片

    public AbsTitle(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public AbsTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //当设置宽度wrap_content是，设置宽度为match_parent
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int matchMeasureSpec = 0;
        if (widthSpecMode == MeasureSpec.AT_MOST) {
            matchMeasureSpec = MeasureSpec.makeMeasureSpec(widthSpecSize, MeasureSpec.EXACTLY);
            widthSpecSize = MeasureSpec.getSize(matchMeasureSpec);
        }

        super.onMeasure(matchMeasureSpec == 0 ? widthMeasureSpec : matchMeasureSpec, heightMeasureSpec);
        //当设置高度为wrap_content时设置默认高度50dp
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            heightSpecSize = (int) (defaultHeight * context.getResources().getDisplayMetrics().density);
        }
        //更新子控件的高度
        setTitleBarHeight(heightSpecSize);
        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    public void init(@Nullable AttributeSet attrs) {

        //创建背景布局
        if (background == null) background = new ImageView(context);
        background.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(background);

        //创建左侧布局
        leftRoot = new LinearLayout(context);
        LayoutParams leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftParams.addRule(ALIGN_PARENT_LEFT);
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
        LayoutParams rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightParams.addRule(ALIGN_PARENT_RIGHT);
        rightParams.addRule(CENTER_VERTICAL);
        rightParams.addRule(RIGHT_OF, android.R.id.autofill);
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

    protected final <T extends View> T find(@IdRes int viewId) {
        return this.findViewById(viewId);
    }

    /**
     * 给View 添加默认水波纹效果
     *
     * @param view
     */
    protected final void setClickStyle(View view) {
        //点击水波纹效果
        TypedValue typedValue = new TypedValue();
        getContext().getTheme()
                .resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        int[] attribute = new int[]{android.R.attr.selectableItemBackground};
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
        view.setBackground(typedArray.getDrawable(0));
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

            RelativeLayout.LayoutParams leftRootParams = (LayoutParams) leftRoot.getLayoutParams();
            leftRootParams.height = (int) height;
            leftRoot.setLayoutParams(leftRootParams);

            RelativeLayout.LayoutParams centerRootParams = (LayoutParams) centerRoot.getLayoutParams();
            centerRootParams.height = (int) height;
            centerRoot.setLayoutParams(centerRootParams);

            RelativeLayout.LayoutParams rightRootParams = (LayoutParams) rightRoot.getLayoutParams();
            rightRootParams.height = (int) height;
            rightRoot.setLayoutParams(rightRootParams);

            RelativeLayout.LayoutParams backgroundParams = (LayoutParams) background.getLayoutParams();
            backgroundParams.height = (int) height;
            background.setLayoutParams(backgroundParams);
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

    /*
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
     * 获取TitleBar背景ImageView
     *
     * @return
     */
    public ImageView getBackgroundView() {
        background.setVisibility(VISIBLE);
        return background;
    }

    /**
     * *********************设置背景颜色、背景图片***********************************************************
     */
    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (background != null) background.setVisibility(GONE);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        if (background != null) background.setVisibility(GONE);
    }

    @Override
    public void setBackground(Drawable drawable) {
        super.setBackground(drawable);
        if (background != null) background.setVisibility(GONE);
    }

}
