package com.yuan.base.widget.gestureview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 自定义固定头部
 * 实现与 NestedScrollChild 联动
 *
 * @author yuanye
 * @date 2018/12/25
 */
public class FixHeadLayout extends LinearLayout implements NestedScrollingParent2 {


    private static final String TAG = "yuanye";
    /**
     * 头部的最大高度
     */
    private float maxHeadHeight = 0;
    /**
     * 头部的最小高度
     */
    private float minHeadHeight = 0;

    /**
     * 当前头部可见高度
     */
    private float currentHeadVisibilityHeight = 0;
    /**
     * 头部偏移高度
     */
    private float offsetHeadHeight = 0;
    /**
     * 头部的View
     */
    private View headView;

    /**
     * 滑动部分的View
     */
    private View scrollView;
    /**
     * 设置初始值
     */
    private boolean isInit = true;
    /**
     * 处理滑动手势判断
     */
    private GestureHelper gestureHelper;
    /**
     * 按下时的Event
     */
    private MotionEvent downMotionEvent;
    /**
     * 偏转因子
     * transformFactor>0,transformFactor越小越灵敏，最小0
     */
    private int transformFactor = 80;
    /**
     * 用于完成滚动操作的实例
     */
    private Scroller mScroller;
    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;


    public FixHeadLayout(Context context) {
        super(context);
        init(context);
    }

    public FixHeadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        //处理头部向下滑动
        gestureHelper = new GestureHelper(context);
        gestureHelper.setIsLongpressEnabled(false);
        gestureHelper.setOnScrollBottomListener(new GestureHelper.OnScrollBottomListener() {
            @Override
            public boolean onScrollBottom(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
                //处理非NestedScroll向下滑动
                if (currentHeadVisibilityHeight < maxHeadHeight) {
                    //增加阻尼效果


                    scrollBy(0, (int) distanceY);
                }
                return true;
            }
        });

        gestureHelper.setOnScrollTopListener(new GestureHelper.OnScrollTopListener() {
            @Override
            public boolean onScrollTop(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
                //处理非NestedScroll向上滑动
                if (currentHeadVisibilityHeight > minHeadHeight) {
                    scrollBy(0, (int) distanceY);
                }
                return true;
            }
        });

        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);
//        // 获取TouchSlop值
//        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean parentBoolean = super.onInterceptTouchEvent(event);
        //拦截子View滑动事件
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                gestureHelper.onTouchEvent(event);
                downMotionEvent = MotionEvent.obtain(event);
            case MotionEvent.ACTION_MOVE:
                //判断上下左右滑动
                float scrollX = event.getX() - downMotionEvent.getX();
                float scrollY = event.getY() - downMotionEvent.getY();
                if (Math.abs(scrollX) <= Math.abs(scrollY) - transformFactor && scrollY < 0) {
                    //向上滑动
                    if (currentHeadVisibilityHeight > minHeadHeight) {
                        return true;
                    }
                } else if (Math.abs(scrollX) <= Math.abs(scrollY) - transformFactor && scrollY > 0) {
                    //向下滑动
                    if (currentHeadVisibilityHeight < maxHeadHeight) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //清空缓存
                if (downMotionEvent != null) {
                    downMotionEvent.recycle();
                    downMotionEvent = null;
                }
                gestureHelper.onTouchEvent(event);
                break;
        }
        return parentBoolean;
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            Log.i(TAG, "getCurrX=" + mScroller.getCurrX() + ",getCurrY=" + mScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                int dx = (int) ((event.getY() - downMotionEvent.getY())) / 10 * 11  ;
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
//                mScroller.startScroll(0, (int) downMotionEvent.getY(), 0, dx);
                Log.i(TAG, "startY=" + downMotionEvent.getY() + ",y=" + event.getY() + ",dx=" + dx);
                invalidate();
                break;
        }
        return gestureHelper.onTouchEvent(event);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isInit) {
            headView = getChildCount() > 0 ? getChildAt(0) : null;
            scrollView = getChildCount() > 1 ? getChildAt(1) : null;
            maxHeadHeight = headView.getMeasuredHeight();
            currentHeadVisibilityHeight = maxHeadHeight;
            isInit = false;
        }
        ViewGroup.LayoutParams params = scrollView.getLayoutParams();
        params.height = getMeasuredHeight();
        //修改FixHeadLayout 高度： 2*maxHeadView+ scrollViewHeight
        setMeasuredDimension(getMeasuredWidth(), (int) (maxHeadHeight + params.height));
    }

    /**
     * 子类开始滑动监听
     *
     * @param parent
     * @param child
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean onStartNestedScroll(@NonNull View parent, @NonNull View child, int i, int i1) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && child.isNestedScrollingEnabled()) {
            //是否拦截
            return true;
        }
        return false;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View view, @NonNull View view1, int i, int i1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onNestedScrollAccepted(view, view1, i);
        }
    }

    /**
     * 子View停止滑动
     *
     * @param child
     * @param i
     */
    @Override
    public void onStopNestedScroll(@NonNull View child, int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onStopNestedScroll(child);
        }
    }

    /**
     * 滑动的实时监听
     *
     * @param child        实现NestedScrollingChild接口的子View
     * @param dxConsumed   x方向滑动距离
     * @param dyConsumed   y方向滑动距离
     * @param dxUnconsumed x方向滑动但未移动的距离
     * @param dyUnconsumed y方向滑动但未移动的距离
     * @param type
     */
    @Override
    public void onNestedScroll(@NonNull View child, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
    }


    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, int[] consumed, int i2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onNestedPreScroll(target, dx, dy, consumed);

            boolean hiddenTop = dy > 0 && getScrollY() < maxHeadHeight;
            boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);
            if (hiddenTop || showTop) {
                scrollBy(0, dy);
                consumed[1] = dy;
            }
        }
    }

    /**
     * 限定滑动的上下边界
     *
     * @param x
     * @param y
     */
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = (int) minHeadHeight;
        }
        if (y > maxHeadHeight) {
            y = (int) maxHeadHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
        currentHeadVisibilityHeight = maxHeadHeight - y;
        Log.i(TAG + TAG, "当前=" + y);
    }
}
