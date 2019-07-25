package yuan.core.list;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;

/**
 * Created by YuanYe on 2017/12/4.
 * recyclerView
 * Grid布局样式分割线
 */
public class GridDivider extends RecyclerView.ItemDecoration {

    private final static String TAG = "GridDivider";

    /**
     * 默认分割线颜色
     */
    private final static int defaultSeparatorColor = Color.parseColor("#dddddd");
    /**
     * 默认分割线高度 ，单位为dp
     */
    private final static float defaultSeparatorHeight = 0.8f;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 分割线宽度
     */
    private int mDividerHeight = -1;

    /**
     * 背景颜色
     */
    private int bgColor;


    public GridDivider() {
        this(-1, defaultSeparatorColor);
    }

    /**
     * @param height
     */
    public GridDivider(int height) {
        this(height, defaultSeparatorColor);
    }

    /**
     * @param height
     * @param color
     */
    public GridDivider(int height, @ColorInt int color) {
        this(height, color, 0);
    }

    /**
     * @param height          分割线高度
     * @param separatorColor  分割线颜色
     * @param backgroundColor RecyclerView背景颜色
     */
    public GridDivider(int height, @ColorInt int separatorColor, @ColorInt int backgroundColor) {
        mDividerHeight = height;
        this.bgColor = backgroundColor;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(separatorColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    /**
     * 设置item偏移量，通过outRect.set(0, 0, 0, 100);设置
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        boolean isLastRow = isLastRow(parent, itemPosition, spanCount, childCount);
        boolean isfirstRow = isfirstRow(parent, itemPosition, spanCount, childCount);

        int top;
        int left;
        int right;
        int bottom;
        int eachWidth = (spanCount - 1) * mDividerHeight / spanCount;
        int dl = mDividerHeight - eachWidth;

        left = itemPosition % spanCount * dl;
        right = eachWidth - left;
        bottom = mDividerHeight;
        //Log.e("zzz", "itemPosition:" + itemPosition + " |left:" + left + " right:" + right + " bottom:" + bottom);
//        if (isLastRow) { //控制是否绘制底部分割线
//            bottom = 0;
//        }
        if (isfirstRow) { //控制是否绘制顶部分割线
            top = (spanCount - 1) * mDividerHeight / spanCount;
        } else {
            top = 0;
        }
        outRect.set(left, top, right, bottom);
    }


    /**
     * 绘制item同级别
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //设置默认分割线高度度
        if (mDividerHeight < 0) {
            mDividerHeight = (int) (parent.getResources().getDisplayMetrics().density * defaultSeparatorHeight);
        }
        drawSeparator(c, parent);
        parent.setBackgroundColor(bgColor);
    }

    /**
     * 绘制在item顶层
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 绘制分割线
     * 此分割线可能被item背景覆盖，可以设置相应的分割线偏移量
     *
     * @param canvas
     * @param parent
     */
    private void drawSeparator(Canvas canvas, RecyclerView parent) {
        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            //画水平分隔线
            int left = child.getLeft();
            int right = child.getRight();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerHeight;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

            //画垂直分割线
            top = child.getTop();
            bottom = child.getBottom() + mDividerHeight;
            left = child.getRight() + layoutParams.rightMargin;
            right = left + mDividerHeight;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
                                 int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    /**
     * 是否是最后一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRow(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // childCount = childCount - childCount % spanCount;
            int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
            return lines == pos / spanCount + 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是第一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isfirstRow(RecyclerView parent, int pos, int spanCount,
                               int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // childCount = childCount - childCount % spanCount;
            int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
            //如是第一行则返回true
            if ((pos / spanCount + 1) == 1) {
                return true;
            } else {
                return false;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取列数
     *
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        } else if (layoutManager instanceof LinearLayoutManager) {
            spanCount = 1;
        }
        return spanCount;
    }
}
