package yuan.widget.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by YuanYe on 2018/8/4.
 * 如果需要自定义Title,可以继承AbsTitle
 */
abstract class AbsTitle<T extends AbsTitle> extends RelativeLayout implements ITitle {

    public static final int EMPTY_RES = -1;
    public static final String EMPTY_TEXT = "";

    private int defaultHeight = 50; //默认高度，单位dp

    protected Context context;

    private LinearLayout leftRoot;//左侧根布局
    private LinearLayout centerRoot;//中间根布局
    private LinearLayout rightRoot;//右侧根布局
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置宽度
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {

        }
        //获取高度
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        //wrap_content
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            heightSpecSize = (int) (defaultHeight * context.getResources().getDisplayMetrics().density);
        }
        setMeasuredDimension(specSize, heightSpecSize);
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
        LayoutParams rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
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
     * *********************设置背景颜色、背景图片***********************************************************
     */
    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(0);
        if (background != null) background.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(0);
        if (background != null) background.setBackgroundResource(resid);
    }

    @Override
    public void setBackground(Drawable drawable) {
        super.setBackground(null);
        if (background != null) background.setBackground(drawable);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
        super.setBackgroundTintMode(null);
        if (background != null) background.setBackgroundTintMode(tintMode);
    }
}
