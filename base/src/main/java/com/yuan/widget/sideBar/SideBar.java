package com.yuan.widget.sideBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yuan.R;
import com.yuan.tools_independ.common.Kits;
import com.yuan.tools_independ.sort.IPinyinSort;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author YuanYe
 * 一个自定义view 实现a-z的竖直绘制，和监听滑动事件
 */
public class SideBar extends View {

    private static final String TAG = "SideBar";
    private int maxRowHeight = -1; //最大行高

    private ArrayList<String> mData; //侧边栏需要显示的内容

    private OnTouchListener onTouchListener; //侧边栏滑动监听
    private OnTouchListener recyclerListener; //侧边栏滑动监听
    private int selectPosition = 0; //当前选中的position

    private Paint paint; //绘制侧边栏画笔

    private @ColorInt
    int selectColor = -1; //选中字体的颜色
    private @ColorInt
    int textColor = -1;//未选中时默认的颜色
    private int textSize = -1; //字体的大小
    private float rowHeight = -1;//行高

    private TextView mTextDialog;

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSideBar(context);
    }

    public SideBar(Context context) {
        super(context);
        initSideBar(context);
    }

    private void initSideBar(Context context) {
        String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z", "#"};
        mData = new ArrayList(Arrays.asList(data));
        paint = new Paint();
        selectColor = ContextCompat.getColor(context, R.color.teal500);
        textColor = ContextCompat.getColor(context, R.color.colorFont66);
        textSize = Kits.Dimens.dpToPxInt(context, 12);
        maxRowHeight = Kits.Dimens.dpToPxInt(context, 24);
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = getHeight() - getPaddingTop() - getPaddingBottom();
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        //计算行高
        if (mData.size() > 1) {
            float singleHeight = height / mData.size();
            rowHeight = singleHeight > maxRowHeight ? maxRowHeight : singleHeight;
        }

        //计算绘制起点
        float startY = (height - rowHeight * mData.size()) / 2 + getPaddingTop();

        //绘制侧边栏
        for (int i = 0; i < mData.size(); i++) {
            paint.setColor(textColor);
            paint.setAntiAlias(true); //设置抗锯齿
            paint.setTextSize(textSize);
            if (i == selectPosition) {
                paint.setColor(selectColor);
                paint.setFakeBoldText(true); //文字加粗
            }
            //获取文字宽高，计算绘制起点
            Rect rect = new Rect();
            paint.getTextBounds(mData.get(i), 0, mData.get(i).length(), rect);
            float textHeight = rect.height();
            float textWidth = paint.measureText(mData.get(i));

            float xPos = (width / 2 - textWidth / 2) + getPaddingLeft();
            float yPos = startY + (rowHeight - textHeight) / 2 + textHeight + rowHeight * i;

            canvas.drawText(mData.get(i), xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int oldChoose = selectPosition;
        float height = getHeight() - getPaddingTop() - getPaddingBottom();

        //计算选中的位置
        float startY = (height - rowHeight * mData.size()) / 2 + getPaddingTop();
        float endY = startY + rowHeight * mData.size();
        int c = y < startY ? 0 : (int) (y > endY ? mData.size() - 1 : (y - startY) / rowHeight);

        switch (action) {
            case MotionEvent.ACTION_UP:
                //TODO
                if (onTouchListener != null) {
                    onTouchListener.onActionUp(c);
                }
                if (recyclerListener != null) {
                    recyclerListener.onActionUp(c);
                }

                break;
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < mData.size()) {
                        if (onTouchListener != null) {
                            onTouchListener.onTouch(c);
                        }
                        if (recyclerListener != null) {
                            recyclerListener.onTouch(c);
                        }
                        selectPosition = c;
                        this.invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 刷新侧边栏数据
     * 只能在主线程中调用
     *
     * @param data
     */
    public void setData(ArrayList<String> data) {
        this.mData.clear();
        this.mData.addAll(data);
        this.invalidate();
    }

    /**
     * 设置排序后的数据
     *
     * @param data 排序的数据
     * @param <T>  实现排序的接口
     * @return
     */
    public <T extends IPinyinSort> void setSortData(ArrayList<T> data) {
        this.mData.clear();
        for (T t : data) {
            if (!mData.contains(t.getFirstLetters())) {
                mData.add(t.getFirstLetters());
            }
        }
        invalidate();
    }

    /**
     * 获取侧边栏列表数据
     *
     * @return
     */
    public ArrayList<String> getData() {
        return mData;
    }

    /**
     * 当有数据发生变化时调用
     */
    public void setDataChange() {
        this.invalidate();
    }

    /**
     * 设置选中的颜色
     *
     * @param selectColor
     */
    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    /**
     * 设置默认的字体颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置最大行高，当data数量较小时，控制最大高度
     *
     * @param maxRowHeight
     */
    public void setMaxRowHeight(int maxRowHeight) {
        this.maxRowHeight = maxRowHeight;
    }

    /**
     * 实现SideBar与RecyclerView 相互联动
     *
     * @param recyclerView
     */
    public <T extends IPinyinSort> void setRecyclerView(RecyclerView recyclerView, ArrayList<T> data) {
        if (recyclerView == null || data == null) return;
        //监听RecyclerView的滑动事件、更新SideBar
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int pastVisiblesItems = 0;
                if (manager instanceof LinearLayoutManager) {
                    pastVisiblesItems = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                } else if (manager instanceof GridLayoutManager) {
                    pastVisiblesItems = ((GridLayoutManager) manager).findFirstVisibleItemPosition();
                } else if (manager instanceof StaggeredGridLayoutManager) {
                    int[] mFirstVisibleItems = null;
                    mFirstVisibleItems = ((StaggeredGridLayoutManager) manager).findFirstVisibleItemPositions(mFirstVisibleItems);
                    if (mFirstVisibleItems.length > 0) pastVisiblesItems = mFirstVisibleItems[0];
                }
                String current = data.get(pastVisiblesItems).getFirstLetters();
                int index = 0;
                for (String str : mData) {
                    if (str.equals(current)) {
                        selectPosition = index;
                        SideBar.this.invalidate();
                        return;
                    }
                    index++;
                }
            }
        });
        //监听Sidebar的滑动事件，更新RecyclerView
        recyclerListener = new OnTouchListener() {
            @Override
            public void onTouch(int position) {
                int scrollPosition = 0;
                for (T t : data) {
                    if (t.getFirstLetters().equals(mData.get(position))) {
                        recyclerView.scrollToPosition(scrollPosition);
                        return;
                    }
                    scrollPosition++;
                }
            }

            @Override
            public void onActionUp(int position) {

            }
        };
    }

    /**
     * 设置侧边栏滑动监听
     *
     * @param onTouchListener
     */
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public interface OnTouchListener {
        //滑动时监听
        void onTouch(int position);

        //手指离开时的监听
        void onActionUp(int position);
    }

}