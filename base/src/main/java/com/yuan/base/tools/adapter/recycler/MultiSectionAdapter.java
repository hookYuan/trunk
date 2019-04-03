package com.yuan.base.tools.adapter.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 多段标签适配器
 * 1.支持item分段添加标签
 * 2.支持标签悬浮控制
 * 3.支持多种item布局和多种section布局自定义
 *
 * @author yuanye
 * @date 2018/12/12
 */
public abstract class MultiSectionAdapter<S, D> extends RLVAdapter {

    /**
     * 默认标记普通Item类型
     */
    public static final int TYPE_ITEM = 0x1024256;
    /**
     * 默认标记标题Item类型
     */
    public static final int TYPE_SECTION = 0x1024257;
    /**
     * 标记spanSize独占一行
     */
    private static final int SPAN_SIZE_SECTION = -0x1024258;
    /**
     * 标记spanSize只占一行中的一份
     */
    private static final int SPAN_SIZE_ITEM = -0x1024259;
    /**
     * 传入的数据集合
     */
    private List<ItemInfo> multiData;

    public MultiSectionAdapter(Context context) {
        super(context);
        multiData = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        if (multiData != null) {
            return multiData.get(position).getType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        initRecycler(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return multiData.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    /**
     * 初始化设置RecyclerView的样式
     *
     * @param parent
     */
    private void initRecycler(RecyclerView parent) {
        if (parent instanceof RecyclerView && ((RecyclerView) parent).getLayoutManager()
                instanceof GridLayoutManager) {
            parent.addItemDecoration(new FloatingDecoration());
            GridLayoutManager manager = ((GridLayoutManager) ((RecyclerView) parent).getLayoutManager());
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (multiData.get(position).getSpanSize() == SPAN_SIZE_ITEM) {
                        return 1;
                    } else if (multiData.get(position).getSpanSize() == SPAN_SIZE_SECTION) {
                        return manager.getSpanCount();
                    } else {
                        return multiData.get(position).getSpanSize() <= 0 ? 1
                                : (multiData.get(position).getSpanSize() > manager.getSpanCount() ? manager.getSpanCount()
                                : multiData.get(position).getSpanSize());
                    }
                }
            });
        }
    }

    /**
     * 包含 Section 和 Item的数据集合
     *
     * @return
     */
    public List<ItemInfo> getData() {
        if (multiData == null) {
            multiData = new ArrayList<>();
        }
        return multiData;
    }

    /**
     * 根据Position获取
     *
     * @param position
     * @return
     */
    protected ItemInfo getData(int position) {
        if (multiData != null) return multiData.get(position);
        else return null;
    }

    /**
     * 添加 Item集合
     *
     * @param itemData
     */
    public void addItemList(List<D> itemData) {
        for (D item : itemData) {
            addItem(item);
        }
    }

    /**
     * 添加Item
     */
    public void addItem(D itemData) {
        addItem(itemData, TYPE_ITEM, SPAN_SIZE_ITEM);
    }

    /**
     * 添加Item
     */
    public void addItem(D itemData, int spanSize) {
        addItem(itemData, TYPE_ITEM, spanSize);
    }

    /**
     * 添加内容Item数据
     *
     * @param itemData 数据源
     * @param type     布局类型，每种不同布局指定不同int型数字即可
     * @param spanSize 一行中n份中的spanSize份
     */
    public void addItem(D itemData, int type, int spanSize) {
        if (multiData == null || itemData == null) return;
        ItemInfo dataBean = new ItemInfo();
        dataBean.setIsSection(false);
        dataBean.setType(type != 0 ? type : TYPE_ITEM);
        dataBean.setItem(itemData);
        dataBean.setSpanSize(spanSize);
        multiData.add(dataBean);
    }

    /**
     * 添加标题，默认标题占一行
     *
     * @param itemSection 标题实体
     */
    public void addSection(S itemSection) {
        addSection(itemSection, TYPE_SECTION, SPAN_SIZE_SECTION);
    }

    /**
     * 添加标题
     *
     * @param itemSection 标题实体
     * @param type        标题的布局类型
     * @param spanSize    一行中n份中的spanSize份
     */
    public void addSection(S itemSection, int type, int spanSize) {
        if (multiData == null || itemSection == null) return;
        ItemInfo dataBean = new ItemInfo();
        dataBean.setIsSection(true);
        dataBean.setFloating(true);
        dataBean.setType(type != 0 ? type : TYPE_SECTION);
        dataBean.setSection(itemSection);
        dataBean.setSpanSize(spanSize);
        multiData.add(dataBean);
    }

    /**
     * 绑定FloatView内容
     *
     * @param floatView
     */
    public abstract void onBindFloatView(View floatView, int position);

    /**
     * 描述传入的数据类型
     */
    public class ItemInfo {
        /**
         * 标题的数据类型
         */
        private S section;

        /**
         * 内容的数据类型
         */
        private D item;

        /**
         * 是否是Section标题
         */
        private boolean isSection;

        /**
         * 用于区分显示布局的type
         */
        private int type;

        /**
         * 每行显示的数量
         */
        private int spanSize = 0;
        /**
         * 是否悬浮，只针对Section有效
         */
        private boolean isFloating;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public S getSection() {
            return section;
        }

        public void setSection(S section) {
            this.section = section;
        }

        public D getItem() {
            return item;
        }

        public void setItem(D item) {
            this.item = item;
        }

        public boolean isSection() {
            return isSection;
        }

        public void setIsSection(boolean section) {
            isSection = section;
        }

        public int getSpanSize() {
            return spanSize;
        }

        public void setSpanSize(int spanSize) {
            this.spanSize = spanSize;
        }

        public boolean isFloating() {
            return isFloating;
        }

        public void setFloating(boolean floating) {
            isFloating = floating;
        }
    }

    /**
     * RecyclerView 悬浮Decoration
     *
     * @author yuanye
     * @date 2018/12/12
     */
    protected class FloatingDecoration extends RecyclerView.ItemDecoration {

        /**
         * 缓存当前的View
         */
        private View currentFloatingView;

        /**
         * 缓存当前悬浮View的位置
         */
        private int currentFloatingPosition;


        protected FloatingDecoration() {
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
        }


        //就是先执行ItemDecoration的onDraw()、再执行ItemView的onDraw()、再执行ItemDecoration的onDrawOver()
        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            //确保是PinnedHeaderAdapter的adapter,确保有View
            if (parent.getAdapter() instanceof MultiSectionAdapter && parent.getChildCount() > 0) {
                //其实就是获取到每一个可见的位置的item时，执行画顶层悬浮栏
                int firstVisiblePosition = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
                if (((MultiSectionAdapter) parent.getAdapter())
                        .getData(firstVisiblePosition).isFloating()
                        && ((MultiSectionAdapter) parent.getAdapter())
                        .getData(firstVisiblePosition).isSection()) {
                    //加载FloatingView
                    int layoutId = ((MultiSectionAdapter) parent.getAdapter()).getItemLayout(parent, parent.getAdapter()
                            .getItemViewType(firstVisiblePosition));
                    View firstView = View.inflate(parent.getContext(), layoutId, parent);

//                    View firstView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent);
                    currentFloatingView = firstView;
                    currentFloatingPosition = firstVisiblePosition;
                    c.save();
                    c.clipRect(0, 0, parent.getWidth(), firstView.getMeasuredHeight());
                    //Canvas默认在视图顶部，无需平移，直接绘制
                    currentFloatingView.draw(c);
                    c.restore();
                } else if (currentFloatingView != null) {
                    onBindFloatView(currentFloatingView, currentFloatingPosition);
                    currentFloatingView.draw(c);
                }
            }
        }
    }
}
