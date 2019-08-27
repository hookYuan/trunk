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

import java.util.HashMap;
import java.util.Map;

/**
 * 描述： 重写Recycler通用分割线
 * 支持LinearLayoutManager
 * GridLayoutManager
 *
 * @author yuanye
 * @date 2019/8/27 17:29
 */
public class GridDivider extends RecyclerView.ItemDecoration {

    private final static String TAG = "GridDivider";
    /**
     * 默认分割线颜色,透明颜色，系统不会绘制
     */
    private final static int DEFAULT_SEPARATOR_COLOR = Color.parseColor("#dddddd");
    /**
     * 默认分割线高度 ，单位为dp
     */
    private final static float DEFAULT_SEPARATOR_HEIGHT = 0.8f;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 分割线宽度
     */
    private float mDividerHeight = 0;

    /**
     * 一行中的当前下标计数器
     */
    private int indexInRow;

    /**
     * 单次执行
     */
    private boolean singleTime = true;
    /**
     * 缓存Item信息
     */
    private Map<Integer, ItemInfo> mCacheInfo;

    public GridDivider() {
        this(-1, DEFAULT_SEPARATOR_COLOR);
    }

    /**
     * @param height
     */
    public GridDivider(float height) {
        this(height, DEFAULT_SEPARATOR_COLOR);
    }

    /**
     * @param height         分割线高度
     * @param separatorColor 分割线颜色
     */
    public GridDivider(float height, @ColorInt int separatorColor) {
        mDividerHeight = height;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(separatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mCacheInfo = new HashMap<>();
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
        /*获取item的顺序位置信息*/
        ItemInfo itemInfo = getItemInfo(parent, view, state);
        int top = 0;
        int left = 0;
        int right = 0;
        int bottom = (int) mDividerHeight;
        /*设置分割线偏移量*/
        if (itemInfo.isFirstInRow()) {
            left = 0;
            right = (int) (mDividerHeight / 2);
        } else if (itemInfo.isLastInRow()) {
            left = (int) (mDividerHeight / 2);
            right = 0;
            if (itemInfo.getPosition() + 1 == itemInfo.getItemCount()) {
                left = (int) (mDividerHeight / 2);
                if (indexInRow < itemInfo.getSpanCount()) {
                    right = (int) (mDividerHeight / 2);
                }
            }
        } else {
            left = (int) (mDividerHeight / 2);
            right = (int) (mDividerHeight / 2);
        }

        /*独立一行取消分割线*/
        if (itemInfo.getSpanSizeLookup() == itemInfo.getSpanCount()) {
            left = 0;
            right = 0;
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
        if (singleTime) {
            //设置默认分割线高度度
            if (mDividerHeight < 0) {
                mDividerHeight = (int) (parent.getResources().getDisplayMetrics().density * DEFAULT_SEPARATOR_HEIGHT);
            } else {
                mDividerHeight = (int) (parent.getResources().getDisplayMetrics().density * mDividerHeight);
            }
            singleTime = false;
        }
        drawSeparator(c, parent);
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
     * 绘制分割线,只有设置偏移量之后，分割线才会显示
     * 此分割线可能被item背景覆盖，可以设置相应的分割线偏移量
     *
     * @param canvas
     * @param parent
     */
    private void drawSeparator(Canvas canvas, RecyclerView parent) {
        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            ItemInfo itemInfo = getItemInfo(parent, child, null);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            //画水平Item底部分隔线
            int left = (int) (child.getLeft() - mDividerHeight);
            int right = (int) (child.getRight() + mDividerHeight);
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = (int) (top + mDividerHeight);
            Log.i(TAG, "底部分割线：" + top + "," + bottom + "," + left + "," + right);

            canvas.drawRect(left, top, right, bottom, mPaint);

            //画水平Item顶部分隔线
            left = (int) (child.getLeft() - mDividerHeight);
            right = (int) (child.getRight() + mDividerHeight);
            top = (int) (child.getTop() - layoutParams.bottomMargin - mDividerHeight);
            bottom = (int) (top + mDividerHeight);
            canvas.drawRect(left, top, right, bottom, mPaint);
            Log.i(TAG, "顶部分割线：" + top + "," + bottom + "," + left + "," + right);

            //画垂直Item右侧分割线
            if (itemInfo.isFirstInRow()) {
                left = child.getRight();
                right = (int) (left + mDividerHeight / 2);
                if (itemInfo.isLastInRow()) {
                    right = (int) (left + mDividerHeight);
                }
            } else if (itemInfo.isLastInRow()) {
                left = child.getRight();
                right = left;
            } else {
                left = child.getRight();
                right = (int) (left + mDividerHeight / 2);
            }

            if (itemInfo.isLastInRow()
                    && itemInfo.getPosition() + 1 == itemInfo.getItemCount()) {
                right = (int) (left + mDividerHeight);
            }
            top = child.getTop() - layoutParams.topMargin;
            bottom = child.getBottom() + layoutParams.bottomMargin;
            canvas.drawRect(left, top, right, bottom, mPaint);
            Log.i(TAG, "右侧分割线：" + top + "," + bottom + "," + left + "," + right);

            //画垂直Item左侧分割线
            if (itemInfo.isFirstInRow()) {
                right = child.getLeft();
                left = child.getLeft();
            } else if (itemInfo.isLastInRow()) {
                right = child.getLeft();
                left = (int) (right - mDividerHeight / 2);
            } else {
                right = child.getLeft();
                left = (int) (right - mDividerHeight / 2);
            }
            top = child.getTop() - layoutParams.topMargin;
            bottom = child.getBottom() + layoutParams.bottomMargin;
            canvas.drawRect(left, top, right, bottom, mPaint);
            Log.i(TAG, "左侧分割线：" + top + "," + bottom + "," + left + "," + right);
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
        int position = ((RecyclerView.LayoutParams) itemView.getLayoutParams()).getViewLayoutPosition();

        //从缓存中读取
        if (mCacheInfo.get(position) != null)
            return mCacheInfo.get(position);

        ItemInfo itemInfo = new ItemInfo();
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

            /*判断是否是一行中的第一个*/
            if (indexInRow + itemInfo.getSpanSizeLookup() <= itemInfo.getSpanCount()) {
                indexInRow += itemInfo.getSpanSizeLookup();
            } else { //换行
                indexInRow = itemInfo.getSpanSizeLookup();
            }
            if (indexInRow == itemInfo.getSpanSizeLookup()) {
                itemInfo.setFirstInRow(true);
            }

            /*判断是否是最后一个*/
            if (itemCount <= position + 1) {
                itemInfo.setLastInRow(true);
            } else if (itemCount > position + 1 && indexInRow + sizeLookup.getSpanSize(position + 1)
                    > itemInfo.getSpanCount()) {
                itemInfo.setLastInRow(true);
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    StaggeredGridLayoutManager.class.cast(layoutManager);
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            itemInfo.setSpanSizeLookup(spanCount);
            itemInfo.setSpanCount(spanCount);

            /*判断是否是一行中的第一个*/
            if (position % spanCount == 1) {
                itemInfo.setFirstInRow(true);
            } else if (position % spanCount == 0) {
                itemInfo.setLastInRow(true);
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            itemInfo.setSpanSizeLookup(1);
            itemInfo.setSpanCount(1);
            itemInfo.setFirstInRow(true);
            itemInfo.setLastInRow(true);
        }
        //添加缓存集合
        mCacheInfo.put(position, itemInfo);
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
         * 一行中的第一个
         */
        private boolean firstInRow = false;

        /**
         * 一行中的最后一个
         */
        private boolean lastInRow = false;

        /**
         * 是否是单行
         */
        private boolean singleRow = false;

        public boolean isSingleRow() {
            return isFirstInRow() && isLastInRow();
        }

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

        @Override
        public String toString() {
            return "ItemInfo{" +
                    "spanSizeLookup=" + spanSizeLookup +
                    ", spanCount=" + spanCount +
                    ", itemCount=" + itemCount +
                    ", position=" + position +
                    ", firstInRow=" + firstInRow +
                    ", lastInRow=" + lastInRow +
                    ", singleRow=" + singleRow +
                    '}';
        }
    }
}
