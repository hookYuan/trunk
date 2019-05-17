package com.yuan.kernel;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 新版RecyclerView适配器，简化使用
 *
 * @author yuanye
 * @date 2019/5/15
 */
public abstract class LightAdapter<T, K extends LightAdapter.ViewHolder> extends RecyclerView.Adapter<K> {

    public static final int HEADER_VIEW = 0x00000111;
    public static final int LOADING_VIEW = 0x00000222;
    public static final int ERROR_VIEW = 0x00000333;
    public static final int FOOTER_VIEW = 0x00000444;
    public static final int EMPTY_VIEW = 0x00000555;

    //header footer
    private View mHeaderLayout;
    private View mFooterLayout;
    //empty
    private View mEmptyLayout;
    private View mErrorLayout;
    private View mLoadingLayout;
    /**
     * 数据源<type,T>
     */
    private List<T> mData;

    private HashMap<Integer, T> classifyData;
    /**
     * 目录型数据源<position,type>
     */
    private HashMap<Integer, Integer> mCatalog;
    /**
     * 类型与布局资源 集合 <type,layoutId>
     */
    private SparseIntArray mTypeLayout;
    /**
     * 实际传递到RecyclerView中使用的int类型
     * Adapter实例后，mTypeInteger一直自增
     * <p>
     * mTypeInteger保存在mTypeData数据集中
     */
    private int mTypeInteger = 0;
    /**
     * 当前数据的位置,默认0
     */
    private int mPosition = 0;

    private LayoutInflater mLayoutInflater;
    /**
     * item点击事件监听
     * 支持多次设置点击事件
     */
    private List<OnItemClickListener> listeners;

    /**
     * context
     */
    protected Context mContext;

    public LightAdapter() {
        mData = new ArrayList<>();
        mCatalog = new HashMap<>();
        mTypeLayout = new SparseIntArray();

        //注册RecyclerView数据发生变化监听
        registerAdapterDataObserver(new DataObservable());
    }
//
//    /**
//     * 添加空布局
//     *
//     * @param emptyView
//     */
//    public void setEmptyView(View emptyView) {
//        this.mEmptyLayout = emptyView;
//        mData.put(EMPTY_VIEW, new Object());
//        saveTypePosition(EMPTY_VIEW);
//    }
//
//    /**
//     * 添加加载中布局
//     *
//     * @param loadingView
//     */
//    public void setLoadingView(View loadingView) {
//        this.mLoadingLayout = loadingView;
//        mData.put(LOADING_VIEW, new Object());
//        saveTypePosition(LOADING_VIEW);
//    }
//
//    /**
//     * 添加空布局
//     *
//     * @param errorView
//     */
//    public void setErrorView(View errorView) {
//        this.mErrorLayout = errorView;
//        mData.put(ERROR_VIEW, new Object());
//        saveTypePosition(ERROR_VIEW);
//    }
//
//    /**
//     * 添加头部布局
//     *
//     * @param mHeaderLayout
//     */
//    public void setHeaderLayout(View mHeaderLayout) {
//        this.mHeaderLayout = mHeaderLayout;
//        mData.put(HEADER_VIEW, new Object());
//        saveTypePosition(HEADER_VIEW);
//    }
//
//    /**
//     * 添加底部布局
//     *
//     * @param mFooterLayout
//     */
//    public void setFooterLayout(View mFooterLayout) {
//        this.mFooterLayout = mFooterLayout;
//        mData.put(FOOTER_VIEW, new Object());
//        saveTypePosition(FOOTER_VIEW);
//    }

    /**
     * 添加数据源
     *
     * @param type
     * @param section
     */
    public void addData(int type, List<T> section) {
        mData.addAll(section);
        saveTypePosition(type);
    }


    /**
     * 启动线程监听数据的变化
     */
    private void monitorData() {

    }

    /**
     * 保存一种类型的起始位置和结束位置
     * 当有数据增删时都应该调用，以保证mCatalog的有效性
     *
     * @param type 类型
     */
    private final void saveTypePosition(int type) {
        mData.get(type);
        mCatalog.put(mPosition, type);
        mPosition = mData.size();
    }

    /**
     * 设置自定义类型及布局
     *
     * @param type
     * @param layoutResId
     */
    public void setItemTypeLayoutId(int type, @LayoutRes int layoutResId) {
        mTypeLayout.put(type, layoutResId);
    }

    @Override
    public final int getItemViewType(int position) {
        //获取Integer类型type数据
        return mCatalog.get(position);
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        K baseViewHolder = null;
        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        //初始化ViewHolder
        switch (viewType) {
            case LOADING_VIEW:
                baseViewHolder = createBaseViewHolder(mLoadingLayout);
                break;
            case ERROR_VIEW:
                baseViewHolder = createBaseViewHolder(mErrorLayout);
                break;
            case HEADER_VIEW:
                baseViewHolder = createBaseViewHolder(mHeaderLayout);
                break;
            case EMPTY_VIEW:
                baseViewHolder = createBaseViewHolder(mEmptyLayout);
                break;
            case FOOTER_VIEW:
                baseViewHolder = createBaseViewHolder(mFooterLayout);
                break;
            default:
                baseViewHolder = createBaseViewHolder(parent, viewType);
                bindViewClickListener(baseViewHolder);
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        int type = mCatalog.get(position);
//        onBindHolder(holder, mData.get(type));
    }

    /**
     * 绑定点击事件
     *
     * @param baseViewHolder
     */
    protected void bindViewClickListener(K baseViewHolder) {

    }

    /**
     * @param layoutResId ID for an XML layout resource to load
     * @param parent      Optional view to be the parent of the generated hierarchy or else simply an object that
     *                    provides a set of LayoutParams values for root of the returned
     *                    hierarchy
     * @return view will be return
     */
    private View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    private K createBaseViewHolder(ViewGroup parent, int type) {
        return createBaseViewHolder(getItemView(mTypeLayout.get(type), parent));
    }

    @SuppressWarnings("unchecked")
    private K createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (K) new ViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (K) new ViewHolder(view);
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param section
     */
    public abstract void onBindHolder(K holder, int type, T section);

    /**
     * item的点击事件
     */
    protected void onItemClick(K holder, View view, int position) {

    }

    /**
     * 提供外部设置点击事件
     *
     * @param listener 事件监听
     */
    public void setOnItemClick(OnItemClickListener listener) {
        if (listeners != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }


    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (ViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && ViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private K createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;

        /**
         * 最近一次调用ViewHolder赋值的position
         * 界面有刷新时刷新Position
         */
        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
        }

        public void setPosition(int position) {
            this.position = position;
        }

        /**
         * 获取View
         */
        public <k extends View> k getView(@IdRes int resId) {
            k k = (k) mViews.get(resId);
            if (k == null) {
                k = (k) itemView.findViewById(resId);
                mViews.put(resId, k);
            }
            return k;
        }

        /**
         * TextView设置文字
         */
        public void setText(@IdRes int resId, CharSequence text) {
            View view = getView(resId);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
        }

        /**
         * 添加事件监听
         */
        public void setOnclick(@IdRes int resId, View.OnClickListener listener) {
            getView(resId).setOnClickListener(listener);
        }

        /**
         * 控制自定义View的显示与隐藏
         */
        public void setVisibility(@IdRes int resId, int visibility) {
            getView(resId).setVisibility(visibility);
        }
    }

    /**
     * 监听RecyclerView数据发生变化
     */
    public class DataObservable extends RecyclerView.AdapterDataObserver {

        public void onChanged() {
            // Do nothing

        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            // do nothing

        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            // fallback to onItemRangeChanged(positionStart, itemCount) if app
            // does not override this method.
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            // do nothing

        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            // do nothing

        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            // do nothing

        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);
    }
}
