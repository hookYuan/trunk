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
package yuan.core.list;

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

import android.util.Log;
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
    private final static float defaultSeparatorHeight = 15f;
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
        //item的顺序位置
        ItemInfo itemInfo = getItemInfo(parent, view, state);
        //获取最大列数
        int spanCount = itemInfo.getSpanSizeLookup();
        //item的总数量
        int childCount = itemInfo.getItemCount();

        //是否是第一个
        boolean lastRow = itemInfo.isLastRow();
        //是否是最后一个
        boolean firstRow = itemInfo.isFirstRow();

        int top = 0;
        int left;
        int right;
        int bottom;

        int eachWidth = (spanCount - 1) * mDividerHeight / spanCount;
        int dl = mDividerHeight - eachWidth;

        left = mDividerHeight;
        right = mDividerHeight;
        bottom = mDividerHeight;
        top = mDividerHeight;

        if (itemInfo.isFirstInRow()) left = 0;
        if (itemInfo.isLastInRow()) right = 0;

        Log.e(TAG, " spanCount:" + spanCount + " childCount:" + childCount +
                " lastRow:" + lastRow + " firstRow:" + firstRow +
                " |left:" + left + " right:" + right + " bottom:" + bottom);
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

            //画水平Item底部分隔线
            int left = child.getLeft() - mDividerHeight;
            int right = child.getRight() + mDividerHeight;
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerHeight;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

            //画水平Item顶部分隔线
            left = child.getLeft() - mDividerHeight;
            right = child.getRight() + mDividerHeight;
            top = child.getTop() - layoutParams.bottomMargin - mDividerHeight;
            bottom = top + mDividerHeight;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

            //画垂直Item右侧分割线
            top = child.getTop();
            bottom = child.getBottom() + mDividerHeight;
            left = child.getRight() + layoutParams.rightMargin;
            right = left + mDividerHeight;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

            //画垂直Item左侧分割线
            top = child.getTop();
            bottom = child.getBottom() + mDividerHeight;
            left = child.getLeft() - layoutParams.leftMargin - mDividerHeight;
            right = left + mDividerHeight;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }


    /**
     * 获取RecyclerView 一行显示的列数
     *
     * @param parent   recyclerView
     * @param itemView 当前item对应的ItemView
     * @return
     */
    private ItemInfo getItemInfo(RecyclerView parent, View itemView, RecyclerView.State state) {
        ItemInfo itemInfo = new ItemInfo();

        int position = ((RecyclerView.LayoutParams) itemView.getLayoutParams()).getViewLayoutPosition();

        int itemCount = parent.getAdapter().getItemCount();
        itemInfo.setItemCount(itemCount);
        itemInfo.setPosition(position);

        /*保存每行列数信息*/
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();


        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.class.cast(layoutManager);
            GridLayoutManager.SpanSizeLookup sizeLookup = gridLayoutManager.getSpanSizeLookup();
            itemInfo.setSpanSizeLookup(sizeLookup.getSpanSize(position));
            itemInfo.setSpanCount(gridLayoutManager.getSpanCount());

            /*判断是否是第一行*/
            if (position < itemInfo.getSpanCount()) {
                int lookItemCount = 0;
                for (int i = 0; i <= position; i++) {
                    lookItemCount += sizeLookup.getSpanSize(i);
                }
                if (lookItemCount <= itemInfo.getSpanCount()) {
                    itemInfo.setFirstRow(true);
                }
            }

            /*判断是否是最后一行*/
//            if (position < )

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    StaggeredGridLayoutManager.class.cast(layoutManager);
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            itemInfo.setSpanSizeLookup(spanCount);
            itemInfo.setSpanCount(spanCount);

            /*判断是否是第一行*/
            if (position < itemInfo.getSpanCount()) {
                itemInfo.setFirstRow(true);
            }

        } else if (layoutManager instanceof LinearLayoutManager) {
            itemInfo.setSpanSizeLookup(1);
            itemInfo.setSpanCount(1);

            /*判断是否是第一行*/
            if (position < itemInfo.getSpanCount()) {
                itemInfo.setFirstRow(true);
            }
        }

        /*判断是否是一行中的第一个、一行中的最后一个*/
        if (layoutManager.getWidth() == 0) {
            itemInfo.setFirstInRow(true);
        }
//        if () {
//            itemInfo.setLastInRow(true);
//        }

        Log.i(TAG, "------l-----" + layoutManager.getLeftDecorationWidth(itemView));
        Log.i(TAG, "------r-----" + layoutManager.getRightDecorationWidth(itemView));
        return itemInfo;
    }


    /**
     * 描述：缓存RecyclerView属性
     *
     * @author yuanye
     * @date 2019/8/5 13:24
     */
    class ItemInfo {
        /**
         * 一行中可以显示item所占比例
         * 例如：一行最多3等分，其中一份值为1，当一行一份时，此时值为3
         */
        private int spanSizeLookup;

        /**
         * 一行最多显示的item的个数
         */
        private int spanCount;

        /**
         * recyclerView Item的数量
         */
        private int itemCount;

        /**
         * item当前的位置
         */
        private int position;

        /**
         * true: 是第一行，false:不是
         */
        private boolean firstRow = false;

        /**
         * true: 是最后一行  false:不是
         */
        private boolean lastRow = false;

        /**
         * true：是最后一列 false:不是
         */
        private boolean lastColumn = false;

        /**
         * 一行中的第一个
         */
        private boolean firstInRow = false;

        /**
         * 一行中的最后一个
         */
        private boolean lastInRow = false;

        public boolean isLastInRow() {
            return lastInRow;
        }

        public void setLastInRow(boolean lastInRow) {
            this.lastInRow = lastInRow;
        }

        public boolean isFirstInRow() {
            return firstInRow;
        }

        public void setFirstInRow(boolean firstInRow) {
            this.firstInRow = firstInRow;
        }

        public int getSpanSizeLookup() {
            return spanSizeLookup;
        }

        public void setSpanSizeLookup(int spanSizeLookup) {
            this.spanSizeLookup = spanSizeLookup;
        }

        public int getSpanCount() {
            return spanCount;
        }

        public void setSpanCount(int spanCount) {
            this.spanCount = spanCount;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public boolean isFirstRow() {
            return firstRow;
        }

        public void setFirstRow(boolean firstRow) {
            this.firstRow = firstRow;
        }

        public boolean isLastRow() {
            return lastRow;
        }

        public void setLastRow(boolean lastRow) {
            this.lastRow = lastRow;
        }

        public boolean isLastColumn() {
            return lastColumn;
        }

        public void setLastColumn(boolean lastColumn) {
            this.lastColumn = lastColumn;
        }
    }
}
