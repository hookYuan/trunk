//package yuan.expand.adapter;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Rect;
//import androidx.annotation.LayoutRes;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import yuan.core.list.RecyclerAdapter;
//
//
///**
// * RecyclerView 多段标签适配器
// * 1.支持item分段添加标签
// * 2.支持标签悬浮控制
// * 3.支持多种item布局和多种section布局自定义
// *
// * @author yuanye
// * @date 2018/12/12
// */
//public abstract class MultiAdapter<S, D> extends RecyclerAdapter {
//    /**
//     * 默认标记普通Item类型
//     */
//    public static final int TYPE_ITEM = 0x1024256;
//    /**
//     * 默认标记标题Item类型
//     */
//    public static final int TYPE_SECTION = 0x1024257;
//    /**
//     * 标记spanSize独占一行
//     */
//    private static final int SPAN_SIZE_SECTION = -0x1024258;
//    /**
//     * 标记spanSize只占一行中的一份
//     */
//    private static final int SPAN_SIZE_ITEM = -0x1024259;
//    /**
//     * 传入的数据集合
//     */
//    private ArrayList<MultiBean> multiData;
//    /**
//     * 对原始数据有移除的数据收集
//     */
//    private HashMap<Integer, List<MultiBean>> expandableData;
//
//
//    public MultiAdapter() {
//        super();
//        multiData = new ArrayList<>();
//        expandableData = new HashMap<>();
//    }
//
//    /**
//     * 绑定FloatView内容
//     *
//     * @param sectionView
//     * @param position    经过Section的下标
//     */
//    public abstract void onBindSectionView(View sectionView, S section, int position);
//
//    /**
//     * 获取悬浮标题的自定义样式，默认选择Section列表中的样式
//     *
//     * @return
//     */
//    public @LayoutRes
//    int getSectionLayoutId() {
//        return 0;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (multiData != null) {
//            return multiData.get(position).getLayoutType();
//        }
//        return super.getItemViewType(position);
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        initRecycler(recyclerView);
//        super.onAttachedToRecyclerView(recyclerView);
//    }
//
//    @Override
//    public int getItemCount() {
//        return multiData.size();
//    }
//
//    /**
//     * 初始化设置RecyclerView的样式
//     *
//     * @param parent
//     */
//    private void initRecycler(RecyclerView parent) {
//        if (parent instanceof RecyclerView && parent.getLayoutManager()
//                instanceof GridLayoutManager) {
//            parent.addItemDecoration(new FloatingDecoration());
//            GridLayoutManager manager = ((GridLayoutManager) ((RecyclerView) parent).getLayoutManager());
//            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    if (multiData.get(position).getSpanSize() == SPAN_SIZE_ITEM) {
//                        return 1;
//                    } else if (multiData.get(position).getSpanSize() == SPAN_SIZE_SECTION) {
//                        return manager.getSpanCount();
//                    } else {
//                        return multiData.get(position).getSpanSize() <= 0 ? 1
//                                : (multiData.get(position).getSpanSize() > manager.getSpanCount() ? manager.getSpanCount()
//                                : multiData.get(position).getSpanSize());
//                    }
//                }
//            });
//        }
//    }
//
//
//    /**
//     * 处理Section的点击事件
//     *
//     * @param holder
//     * @param view
//     * @param position
//     */
//    @Override
//    public void onItemClick(ViewHolder holder, View view, int position) {
//        if (getMultiData().get(position).isSection
//                && getMultiData().get(position).getSectionType() == MultiBean.TYPE_EXPANDABLE
//                && getMultiData().get(position).isExpandable()) {
//            removeExpandableSectionData(position);
//            getMultiData().get(position).setExpandable(false);
//        } else if (getMultiData().get(position).isSection
//                && getMultiData().get(position).getSectionType() == MultiBean.TYPE_EXPANDABLE
//                && !getMultiData().get(position).isExpandable()) {
//            addExpandableSectionData(position);
//            getMultiData().get(position).setExpandable(true);
//        }
//    }
//
//    /**
//     * 关闭折叠Section对应的子项
//     *
//     * @param sectionPosition section的位置
//     */
//    private void removeExpandableSectionData(int sectionPosition) {
//        //计算当前插入的位置
//        int itemCount = 0;
//        List<MultiBean> expandable = new ArrayList<>();
//        for (int i = sectionPosition + 1; i < multiData.size(); i++) {
//            if (multiData.get(i).isSection) {
//                break;
//            }
//            expandable.add(multiData.get(i));
//        }
//        itemCount = expandable.size();
//        expandableData.put(sectionPosition, expandable);
//        multiData.removeAll(expandable);
//
//        notifyItemRangeRemoved(sectionPosition + 1, itemCount);
//    }
//
//
//    /**
//     * 展开折叠Section对应的子项
//     *
//     * @param sectionPosition section的位置
//     */
//    private void addExpandableSectionData(int sectionPosition) {
//        if (expandableData.containsKey(sectionPosition)) {
//            multiData.addAll(sectionPosition + 1, expandableData.get(sectionPosition));
//            List<MultiBean> removeList = expandableData.remove(sectionPosition);
//            notifyItemRangeInserted(sectionPosition + 1, removeList.size());
//        }
//    }
//
//
//    /**
//     * 获取Section和Data的集合
//     *
//     * @return
//     */
//    public ArrayList<MultiBean> getMultiData() {
//        if (multiData == null) {
//            multiData = new ArrayList<>();
//        }
//        return multiData;
//    }
//
//    /**
//     * 根据position获取标题
//     *
//     * @param position
//     * @return
//     */
//    public S getSection(int position) {
//        if (multiData != null) {
//            for (int i = position; i >= 0; i--) {
//                if (multiData.get(i).isSection) {
//                    return multiData.get(i).getSection();
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 获取所有的Section数据
//     *
//     * @return
//     */
//    public List<S> getSection() {
//        List<S> list = new ArrayList<>();
//        for (MultiBean multi : multiData) {
//            if (multi.isSection) {
//                list.add(multi.getSection());
//            }
//        }
//        return list;
//    }
//
//    /**
//     * 根据下标获取Section的下标
//     *
//     * @param position
//     * @return
//     */
//    private int getLastSectionPosition(int position) {
//        if (multiData != null) {
//            for (int i = position; i >= 0; i--) {
//                if (multiData.get(i).isSection) {
//                    return i;
//                }
//            }
//        }
//        return 0;
//    }
//
//    /**
//     * 获取第一组非Section的数据
//     *
//     * @return
//     */
//    public List<D> getData() {
//        return getData(-1);
//    }
//
//    /**
//     * 根据Section的位置获取对应Data数据
//     * 当不存在Section的情况，传-1即可
//     *
//     * @param sectionPosition
//     */
//    public List<D> getData(int sectionPosition) {
//        List<D> lists = new ArrayList<>();
//        if (sectionPosition >= multiData.size() || sectionPosition < -1) {
//            return lists;
//        }
//        for (int i = sectionPosition + 1; i < multiData.size(); i++) {
//            if (!multiData.get(i).isSection()) {
//                lists.add(multiData.get(i).getItem());
//            } else {
//                return lists;
//            }
//        }
//        return lists;
//    }
//
//    /**
//     * 添加Item
//     *
//     * @param itemData
//     */
//    public void addData(List<D> itemData, int spanSize) {
//        for (D item : itemData) {
//            addData(item, spanSize);
//        }
//    }
//
//    public void addData(List<D> itemData) {
//        for (D item : itemData) {
//            addData(item, SPAN_SIZE_ITEM);
//        }
//    }
//
//    /**
//     * 添加Item
//     */
//    public void addData(D itemData) {
//        addData(itemData, TYPE_ITEM, SPAN_SIZE_ITEM);
//    }
//
//    /**
//     * 添加Item
//     */
//    public void addData(D itemData, int spanSize) {
//        addData(itemData, TYPE_ITEM, spanSize);
//    }
//
//    /**
//     * 添加内容Item数据
//     *
//     * @param itemData 数据源
//     * @param type     布局类型，每种不同布局指定不同int型数字即可
//     * @param spanSize 一行中n份中的spanSize份
//     */
//    public void addData(D itemData, int type, int spanSize) {
//        if (multiData == null || itemData == null) return;
//        MultiBean dataBean = new MultiBean();
//        dataBean.setSection(false);
//        dataBean.setLayoutType(type != 0 ? type : TYPE_ITEM);
//        dataBean.setItem(itemData);
//        dataBean.setSpanSize(spanSize);
//        multiData.add(dataBean);
//    }
//
//
//    /**
//     * 添加标题，默认悬浮
//     *
//     * @param section
//     */
//    public void addSection(S section) {
//        addSection(section, MultiBean.TYPE_FLOAT);
//    }
//
//    /**
//     * 添加标题，默认标题占一行
//     *
//     * @param section 标题实体
//     */
//    public void addSection(S section, int sectionType) {
//        addSection(section, sectionType, TYPE_SECTION, SPAN_SIZE_SECTION);
//    }
//
//    /**
//     * 添加标题
//     *
//     * @param itemSection 标题实体
//     * @param sectionType 标题布局类型
//     * @param type        标题的布局类型
//     * @param spanSize    一行中n份中的spanSize份
//     */
//    public void addSection(S itemSection, int sectionType, int type, int spanSize) {
//        if (multiData == null || itemSection == null) return;
//        MultiBean dataBean = new MultiBean();
//        dataBean.setSection(true);
//        dataBean.setSectionType(sectionType);
//        dataBean.setLayoutType(type != 0 ? type : TYPE_SECTION);
//        dataBean.setSection(itemSection);
//        dataBean.setSpanSize(spanSize);
//        multiData.add(dataBean);
//    }
//
//    /**
//     * RecyclerView 悬浮Decoration
//     *
//     * @author yuanye
//     * @date 2018/12/12
//     */
//    protected class FloatingDecoration extends RecyclerView.ItemDecoration {
//        /**
//         * 缓存当前的View
//         */
//        private View floatingView;
//        /**
//         * 缓存当前悬浮View的位置
//         */
//        private int oldPosition = -1;
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            super.getItemOffsets(outRect, view, parent, state);
//            //设置所有悬浮标题之下的内容都向下偏移section的高度
//        }
//
//        /**
//         * 就是先执行ItemDecoration的onDraw()、再执行ItemView的onDraw()、再执行ItemDecoration的onDrawOver()
//         */
//        @Override
//        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//            super.onDrawOver(c, parent, state);
//            //确保是MultiSectionAdapter,确保有View
//            if (parent.getAdapter() instanceof MultiAdapter && parent.getChildCount() > 0) {
//                //获取第一个可见item的position
//                int firstVisiblePosition = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
//                int itemHeight = parent.getLayoutManager().findViewByPosition(firstVisiblePosition).getMeasuredHeight();
//                int itemWith = parent.getMeasuredWidth();
//
//                int left = parent.getPaddingLeft();
//                int right = parent.getMeasuredWidth() - parent.getPaddingRight();
//
//                //当检测到悬浮标题时加载悬浮标题布局
//                if (multiData.get(firstVisiblePosition).getSectionType() == MultiBean.TYPE_FLOAT
//                        && multiData.get(firstVisiblePosition).isSection() && floatingView == null) {
//
//                    //加载FloatingView
//                    int layoutId = 0;
//                    if (getSectionLayoutId() == 0) {
//                        layoutId = ((MultiAdapter) parent.getAdapter()).getItemLayout(parent, parent.getAdapter()
//                                .getItemViewType(firstVisiblePosition));
//                    } else {
//                        layoutId = getSectionLayoutId();
//                    }
//                    //添加一层包裹View,解决悬浮调layout失效的bug
//                    LinearLayout linearLayout = new LinearLayout(parent.getContext());
//                    linearLayout.setGravity(Gravity.CENTER);
//                    linearLayout.setOrientation(LinearLayout.VERTICAL);
//                    linearLayout.addView(LayoutInflater.from(mContext).inflate(layoutId, parent, false));
//                    floatingView = linearLayout;
//                    //指定View的宽/高
//                    RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(itemWith, itemHeight);
//                    floatingView.setLayoutParams(params);
//                    floatingView.measure(View.MeasureSpec.makeMeasureSpec(itemWith, View.MeasureSpec.EXACTLY),
//                            View.MeasureSpec.makeMeasureSpec(itemHeight, View.MeasureSpec.EXACTLY));
//                }
//
//                if (floatingView != null && multiData.get(getLastSectionPosition(firstVisiblePosition))
//                        .getSectionType() == MultiBean.TYPE_FLOAT) {
//                    Log.i("yuanye", "---" + multiData.get(getLastSectionPosition(firstVisiblePosition))
//                            .getSectionType());
//
//                    c.save();
//                    c.restore();
//                    View topView = parent.getChildAt(0);
//
//                    int floatViewHeight = floatingView.getMeasuredHeight();
//                    int topViewHeight = Math.abs(topView.getTop());
//
//                    if (firstVisiblePosition < state.getItemCount() - 1
//                            && multiData.get(firstVisiblePosition + 1).isSection()
//                            && (itemHeight - topViewHeight) <= floatViewHeight) {
//                        //当两个Section交汇时，逐渐刷新FloatingView的高度
//                        floatingView.layout(left, floatViewHeight - (itemHeight - topViewHeight)
//                                , right, floatViewHeight - (floatViewHeight - (itemHeight - topViewHeight)));
//                    } else {
//                        floatingView.layout(left, 0, right, floatViewHeight);
//                    }
//                    //Canvas 绘制悬浮标题布局
//                    floatingView.draw(c);
//                    //过滤多余的数据
//                    if (oldPosition != getLastSectionPosition(firstVisiblePosition)) {
//                        oldPosition = getLastSectionPosition(firstVisiblePosition);
//                        onBindSectionView(floatingView, getSection(oldPosition), oldPosition);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 描述传入的数据类型
//     */
//    public class MultiBean {
//        /**
//         * Section悬浮
//         */
//        public static final int TYPE_FLOAT = 0x2014128;
//        /**
//         * Section折叠
//         */
//        public static final int TYPE_EXPANDABLE = 0x2014129;
//
//        /**
//         * Section无特殊效果
//         */
//        public static final int TYPE_NORMAL = 0x2014127;
//        /**
//         * 标题的数据类型
//         */
//        private S section;
//
//        /**
//         * 内容的数据类型
//         */
//        private D item;
//
//        /**
//         * 是否是Section标题
//         */
//        private boolean isSection;
//
//        /**
//         * 用于区分显示布局的type
//         */
//        private int layoutType;
//
//        /**
//         * 每行显示的数量
//         */
//        private int spanSize = 0;
//        /**
//         * Section的样式:
//         * 1.  悬浮标题  TYPE_FLOAT
//         * 2.  折叠标题  TYPE_EXPANDABLE
//         * 3.  普通标题  TYPE_NORMAL
//         */
//        private int sectionType = TYPE_NORMAL;
//        /**
//         * 是否展开: true展开，false折叠
//         */
//        private boolean expandable = false;
//
//        public S getSection() {
//            return section;
//        }
//
//        public void setSection(S section) {
//            this.section = section;
//        }
//
//        public D getItem() {
//            return item;
//        }
//
//        public void setItem(D item) {
//            this.item = item;
//        }
//
//        public boolean isSection() {
//            return isSection;
//        }
//
//        public void setSection(boolean section) {
//            isSection = section;
//        }
//
//        public int getLayoutType() {
//            return layoutType;
//        }
//
//        public void setLayoutType(int layoutType) {
//            this.layoutType = layoutType;
//        }
//
//        public int getSpanSize() {
//            return spanSize;
//        }
//
//        public void setSpanSize(int spanSize) {
//            this.spanSize = spanSize;
//        }
//
//        public int getSectionType() {
//            return sectionType;
//        }
//
//        public void setSectionType(int sectionType) {
//            this.sectionType = sectionType;
//        }
//
//        public boolean isExpandable() {
//            return expandable;
//        }
//
//        public void setExpandable(boolean expandable) {
//            this.expandable = expandable;
//        }
//    }
//}
