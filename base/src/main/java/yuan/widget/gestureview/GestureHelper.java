package yuan.widget.gestureview;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * 手势判断辅助类
 * <p>
 * 手势监听执行顺序：
 * onDown >  onShowPress > onSingleTapUp > onScrollLeft > onScrollTop
 * > onScrollRight > onScrollBottom > onScaleBegin > onScale > onScaleEnd
 * > onLongPress > onFling
 * <p>
 * 使用方法，在自定View或者OnTouchEvent回调中调用OnEvent()方法即可
 *
 * @author yuanye
 * @date 2018/12/21
 */
public class GestureHelper extends GestureDetector {

    private static final String TAG = "GestureHelper";
    /**
     * 向左滑动
     */
    private static final int SCROLLLEFT = 0x102401;
    /**
     * 向上滑动
     */
    private static final int SCROLLTOP = 0x102402;
    /**
     * 向右滑动
     */
    private static final int SCROLLRIGHT = 0x102403;
    /**
     * 向下滑动
     */
    private static final int SCROLLBOTTOM = 0x102404;
    /**
     * 上一次的滑动方向
     */
    private static int lastScrollOrientation = SCROLLLEFT;
    /**
     * 上一次X方向的滑动距离
     */
    private static float lastScrollX = 0;
    /**
     * 上一次Y方向的滑动距离
     */
    private static float lastScrollY = 0;
    /**
     * 处理普通手势监听器
     */
    private static GestureListener gestureListener = new GestureListener();

    /**
     * 处理缩放手势
     */
    private static ScaleGestureDetector scaleGestureDetector;

    /**
     * 修复系统GestureDetector当
     */
    private static MotionEvent newDownPoint;

    /**
     * 设置按下监听
     */
    private static OnDownListener onDownListener;

    /**
     * 设置按压监听
     */
    private static OnShowPressListener onShowPressListener;

    /**
     * 设置单次点击监听
     */
    private static OnSingleTapUpListener onSingleTapUpListener;
    /**
     * 设置左滑监听
     */
    private static OnScrollLeftListener onScrollLeftListener;
    /**
     * 设置上滑监听
     */
    private static OnScrollTopListener onScrollTopListener;

    /**
     * 设置右滑监听
     */
    private static OnScrollRightListener onScrollRightListener;

    /**
     * 设置下滑监听
     */
    private static OnScrollBottomListener onScrollBottomListener;

    /**
     * 设置长按监听
     */
    private static OnLongPressListener onLongPressListener;

    /**
     * 快速滑动监听
     */
    private static OnFlingListener onFlingListener;

    /**
     * 缩放滑动监听
     */
    private static OnScaleListener onScaleListener;


    public GestureHelper(Context context) {
        super(context, gestureListener);
        init(context);
    }

    public GestureHelper(Context context, Handler handler) {
        super(context, gestureListener, handler);
        init(context);
    }

    /**
     * 初始化各个手势处理器
     */
    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureListener());
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (newDownPoint != null) {
                    newDownPoint.recycle();
                    newDownPoint = null;
                }
                break;
        }
        return result;
    }

    /**
     * 设置按下时的监听
     *
     * @param onDownListener
     */
    public void setOnDownListener(OnDownListener onDownListener) {
        this.onDownListener = onDownListener;
    }

    /**
     * 移除按下监听
     */
    public void removeOnDownListener() {
        this.onDownListener = null;
    }

    /**
     * 设置按下时的监听
     *
     * @param onShowPressListener
     */
    public void setOnShowPressListener(OnShowPressListener onShowPressListener) {
        this.onShowPressListener = onShowPressListener;
    }

    /**
     * 移除按下监听
     */
    public void removeOnShowPressListener() {
        this.onShowPressListener = null;
    }

    /**
     * 设置单次点击的监听
     *
     * @param onSingleTapUpListener
     */
    public void setOnSingleTapUpListener(OnSingleTapUpListener onSingleTapUpListener) {
        this.onSingleTapUpListener = onSingleTapUpListener;
    }

    /**
     * 移除单次点击的监听
     */
    public void removeOnSingleTapUpListener() {
        this.onSingleTapUpListener = null;
    }

    /**
     * 设置左滑监听
     *
     * @param onScrollLeftListener
     */
    public void setOnScrollLeftListener(OnScrollLeftListener onScrollLeftListener) {
        this.onScrollLeftListener = onScrollLeftListener;
    }

    /**
     * 移除左滑监听
     */
    public void removeOnScrollLeftListener() {
        this.onScrollLeftListener = null;
    }

    /**
     * 设置上滑监听
     *
     * @param onScrollTopListener
     */
    public void setOnScrollTopListener(OnScrollTopListener onScrollTopListener) {
        this.onScrollTopListener = onScrollTopListener;
    }

    /**
     * 移除上滑监听
     */
    public void removeOnScrollTopListener() {
        this.onScrollTopListener = null;
    }

    /**
     * 设置右滑监听
     *
     * @param onScrollRightListener
     */
    public void setOnScrollRightListener(OnScrollRightListener onScrollRightListener) {
        this.onScrollRightListener = onScrollRightListener;
    }

    /**
     * 移除右滑监听
     */
    public void removeOnScrollRightListener() {
        this.onScrollRightListener = null;
    }

    /**
     * 设置下滑监听
     */
    public void setOnScrollBottomListener(OnScrollBottomListener onScrollBottomListener) {
        GestureHelper.onScrollBottomListener = onScrollBottomListener;
    }

    /**
     * 移除下滑监听
     */
    public void removeOnScrollBottomListener() {
        this.onScrollBottomListener = null;
    }

    /**
     * 设置长按监听
     *
     * @param onLongPressListener
     */
    public static void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        GestureHelper.onLongPressListener = onLongPressListener;
    }

    /**
     * 移除长按监听
     */
    public void removeOnLongPressListener() {
        this.onLongPressListener = null;
    }

    /**
     * 快速滑动监听
     */
    public void setOnFlingListener(OnFlingListener onFlingListener) {
        GestureHelper.onFlingListener = onFlingListener;
    }

    /**
     * 移除快速滑动监听
     */
    public void removeOnFlingListener() {
        this.onFlingListener = null;
    }

    /**
     * 设置缩放滑动监听
     */
    public void setOnScaleListener(OnScaleListener onScaleListener) {
        GestureHelper.onScaleListener = onScaleListener;
    }

    /**
     * 移除缩放滑动监听
     */
    public void removeOnScaleListener() {
        this.onScaleListener = null;
    }

    /**
     * 接管GestureDetector,扩展处理手势操作
     */
    private static class GestureListener implements OnGestureListener {

        /**
         * 偏转因子
         * transformFactor>0,transformFactor越小越灵敏，最小0
         */
        public static int transformFactor = 40;

        /**
         * 多点滑动时判断多点之间的滑动差
         * 差值越大，多点滑动越一致
         */
        public static int togetherFactor = 200;


        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(TAG, "onDown");
            return onDownListener != null ? onDownListener.onDown(e) : (
                    onShowPressListener != null ? true : false
                            || onSingleTapUpListener != null ? true : false
                            || onScrollLeftListener != null ? true : false
                            || onScrollTopListener != null ? true : false
                            || onScrollRightListener != null ? true : false
                            || onScrollBottomListener != null ? true : false
                            || onScaleListener != null ? true : false
                            || onLongPressListener != null ? true : false
                            || onFlingListener != null ? true : false
            );
        }

        @Override
        public void onShowPress(MotionEvent e) {
            if (onShowPressListener != null) {
                onShowPressListener.onShowPress(e);
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return onSingleTapUpListener != null ? onSingleTapUpListener.onSingleTapUp(e) : (
                    onScrollLeftListener != null ? true : false
                            || onScrollTopListener != null ? true : false
                            || onScrollRightListener != null ? true : false
                            || onScrollBottomListener != null ? true : false
                            || onScaleListener != null ? true : false
                            || onLongPressListener != null ? true : false
                            || onFlingListener != null ? true : false
            );
        }

        @Override
        public boolean onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
            boolean isContinue = false;
            if (!isContinue) { //处理上下左右滑动检测
                //当有新的手指加入时，更新起点
                boolean addFinger = downEvent.getPointerCount() != currentEvent.getPointerCount() && newDownPoint == null ? true : false;
                boolean newFinger = newDownPoint != null && newDownPoint.getPointerCount() != currentEvent.getPointerCount() ? true : false;

                boolean newOrientation = false;

                float scrollX = 0;
                float scrollY = 0;
                //计算当前滑动方向
                if (addFinger) {
                    scrollX = currentEvent.getX() - downEvent.getX();
                    scrollY = currentEvent.getY() - downEvent.getY();
                } else if (newFinger) {
                    scrollX = currentEvent.getX() - newDownPoint.getX();
                    scrollY = currentEvent.getY() - newDownPoint.getY();
                }

                if (Math.abs(scrollX) - Math.abs(scrollY) >= transformFactor && scrollX < 0) {
                    //向左滑动
                    newOrientation = lastScrollOrientation != SCROLLLEFT &&
                            Math.abs(Math.abs(lastScrollX) - Math.abs(scrollX)) < transformFactor ? true : false;
                } else if (Math.abs(scrollX) <= Math.abs(scrollY) - transformFactor && scrollY < 0) {
                    //向上滑动
                    newOrientation = lastScrollOrientation != SCROLLTOP &&
                            Math.abs(Math.abs(lastScrollY) - Math.abs(scrollY)) < transformFactor ? true : false;
                } else if (Math.abs(scrollX) - Math.abs(scrollY) >= transformFactor && scrollX > 0) {
                    //向右滑动
                    newOrientation = lastScrollOrientation != SCROLLRIGHT &&
                            Math.abs(Math.abs(lastScrollX) - Math.abs(scrollX)) < transformFactor ? true : false;
                } else if (Math.abs(scrollX) <= Math.abs(scrollY) - transformFactor && scrollY > 0) {
                    //向下滑动
                    newOrientation = lastScrollOrientation != SCROLLBOTTOM &&
                            Math.abs(Math.abs(lastScrollY) - Math.abs(scrollY)) < transformFactor ? true : false;
                }

                if (addFinger || newFinger || newOrientation) {
                    updateDownEvent(currentEvent);
                }
                isContinue = discretionOrientation(newDownPoint == null ? downEvent : newDownPoint, currentEvent, distanceX, distanceY);
            }

            if (!isContinue) {  //处理缩放检测
                isContinue = scaleGestureDetector.onTouchEvent(currentEvent);
            }

            return isContinue;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (onLongPressListener != null) {
                onLongPressListener.onLongPress(e);
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {



            return onFlingListener != null ? onFlingListener.onFling(e1, e2, velocityX, velocityY) : false;
        }

        /**
         * 根据滑动监听判断滑动上下左右方向
         *
         * @param downEvent
         * @param currentEvent
         * @param distanceX
         * @param distanceY
         */
        private boolean discretionOrientation(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
            //判断多点滑动方向是否一致，如果不一致则不拦截处理
            if (currentEvent.getPointerCount() > 1) {
                int pointCount = currentEvent.getPointerCount();
                float lastPointX = 0;
                float lastPointY = 0;
                for (int i = 0; i < pointCount; i++) {
                    if (i != 0 && (Math.abs(Math.abs(lastPointY) - Math.abs(downEvent.getY(i) - currentEvent.getY(i))) < togetherFactor
                            && Math.abs(Math.abs(lastPointX) - Math.abs(downEvent.getX(i) - currentEvent.getX(i))) < togetherFactor)
                            && lastPointY * (downEvent.getY(i) - currentEvent.getY(i)) < 0
                            && lastPointX * (downEvent.getX(i) - currentEvent.getX(i)) < 0) {
                        return false;
                    }
                    lastPointX = downEvent.getX(i) - currentEvent.getX(i);
                    lastPointY = downEvent.getY(i) - currentEvent.getY(i);
                }
            }

            //判断上下左右滑动
            float scrollX = currentEvent.getX() - downEvent.getX();
            float scrollY = currentEvent.getY() - downEvent.getY();
            lastScrollX = scrollX;
            lastScrollY = scrollY;

            if (Math.abs(scrollX) - Math.abs(scrollY) >= transformFactor && scrollX < 0) {
                lastScrollOrientation = SCROLLLEFT;
                //向左滑动
                return onScrollLeftListener != null ? onScrollLeftListener.onScrollLeft(downEvent, currentEvent, distanceX, distanceY) : (
                        onScrollTopListener != null ? true : false
                                || onScrollRightListener != null ? true : false
                                || onScrollBottomListener != null ? true : false
                                || onScaleListener != null ? true : false
                                || onLongPressListener != null ? true : false
                                || onFlingListener != null ? true : false
                );
            } else if (Math.abs(scrollX) <= Math.abs(scrollY) - transformFactor && scrollY < 0) {
                lastScrollOrientation = SCROLLTOP;
                //向上滑动
                return onScrollTopListener != null ? onScrollTopListener.onScrollTop(downEvent, currentEvent, distanceX, distanceY) : (
                        onScrollRightListener != null ? true : false
                                || onScrollBottomListener != null ? true : false
                                || onScaleListener != null ? true : false
                                || onLongPressListener != null ? true : false
                                || onFlingListener != null ? true : false
                );
            } else if (Math.abs(scrollX) - Math.abs(scrollY) >= transformFactor && scrollX > 0) {
                lastScrollOrientation = SCROLLRIGHT;
                //向右滑动
                return onScrollRightListener != null ? onScrollRightListener.onScrollRight(downEvent, currentEvent, distanceX, distanceY) : (
                        onScrollBottomListener != null ? true : false
                                || onScaleListener != null ? true : false
                                || onLongPressListener != null ? true : false
                                || onFlingListener != null ? true : false
                );
            } else if (Math.abs(scrollX) <= Math.abs(scrollY) - transformFactor && scrollY > 0) {
                lastScrollOrientation = SCROLLBOTTOM;
                //向下滑动
                return onScrollBottomListener != null ? onScrollBottomListener.onScrollBottom(downEvent, currentEvent, distanceX, distanceY) : (
                        onScaleListener != null ? true : false
                                || onLongPressListener != null ? true : false
                                || onFlingListener != null ? true : false
                );
            } else {
                return false;
            }
        }
    }

    /**
     * 更新按下时的起点
     */
    private static void updateDownEvent(MotionEvent currentEvent) {
        //触控点发生变化，更新起点的触控点
        if (newDownPoint != null) {
            newDownPoint.recycle();
            newDownPoint = null;
        }
        newDownPoint = MotionEvent.obtain(currentEvent);
    }

    private class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return onScaleListener != null ? onScaleListener.onScale(detector) : (
                    onLongPressListener != null ? true : false
                            || onFlingListener != null ? true : false
            );
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return onScaleListener != null ? onScaleListener.onScaleBegin(detector) : (
                    onLongPressListener != null ? true : false
                            || onFlingListener != null ? true : false
            );
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            if (onScaleListener != null) {
                onScaleListener.onScaleEnd(detector);
            }
        }
    }

    /**
     * @author yuanye
     * @date 2018/12/25
     */
    public interface OnDownListener {
        /**
         * 按下的时候触发，每次该方法都是最先触发
         *
         * @param e 按下点
         * @return 是否向下传递，true继续传递
         */
        boolean onDown(MotionEvent e);
    }

    /**
     * @author yuanye
     * @date 2018/12/25
     */
    public interface OnShowPressListener {
        /**
         * 按压了但是未移动或抬起
         *
         * @param e 当前点
         */
        void onShowPress(MotionEvent e);
    }

    /**
     * @author yuanye
     * @date 2018/12/25
     */
    public interface OnSingleTapUpListener {

        /**
         * 单次点击的时候
         *
         * @param e 当前点
         * @return 是否拦截事件
         */
        boolean onSingleTapUp(MotionEvent e);
    }

    /**
     * @author yuanye
     * @date 2018/12/25
     */
    public interface OnScrollLeftListener {
        /**
         * 向左滑动
         *
         * @param downEvent    按下点
         * @param currentEvent 当前点
         * @param distanceX    x方向多点的平均滑动距离
         * @param distanceY    y方向多点的平均滑动距离
         */
        boolean onScrollLeft(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY);

    }

    /**
     * @author yuanye
     * @date 2018/12/25
     */
    public interface OnScrollTopListener {
        /**
         * 向上滑动
         *
         * @param downEvent    按下点
         * @param currentEvent 当前点
         * @param distanceX    x方向多点的平均滑动距离
         * @param distanceY    y方向多点的平均滑动距离
         */
        boolean onScrollTop(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY);
    }

    public interface OnScrollRightListener {
        /**
         * 向右滑动
         *
         * @param downEvent    按下点
         * @param currentEvent 当前点
         * @param distanceX    x方向多点的平均滑动距离
         * @param distanceY    y方向多点的平均滑动距离
         */
        boolean onScrollRight(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY);
    }

    public interface OnScrollBottomListener {
        /**
         * 向下滑动
         *
         * @param downEvent    按下点
         * @param currentEvent 当前点
         * @param distanceX    x方向多点的平均滑动距离
         * @param distanceY    y方向多点的平均滑动距离
         */
        boolean onScrollBottom(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY);
    }

    public interface OnLongPressListener {
        /**
         * 长按事件
         *
         * @param e 当前点
         */
        void onLongPress(MotionEvent e);
    }

    public interface OnFlingListener {
        /**
         * 快速滑动时
         *
         * @param e1        按下点
         * @param e2        当前点
         * @param velocityX x方向速度
         * @param velocityY y方向速度
         */
        boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
    }


    public interface OnScaleListener {
        /**
         * 开始处理缩放手势
         *
         * @param detector
         * @return
         */
        boolean onScaleBegin(ScaleGestureDetector detector);

        /**
         * 缩放手势
         *
         * @param detector
         * @return true--不执行End
         */
        boolean onScale(ScaleGestureDetector detector);

        /**
         * 缩放处理完毕
         *
         * @param detector 缩放数据
         */
        void onScaleEnd(ScaleGestureDetector detector);
    }
}
