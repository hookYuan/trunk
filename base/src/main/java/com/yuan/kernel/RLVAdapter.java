package com.yuan.kernel;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView.Adapter 的基本封装
 * Created by YuanYe on 2017/12/18.
 * 简化RecyclerView的Adapter代码
 */
public abstract class RLVAdapter extends RecyclerView.Adapter<RLVAdapter.ViewHolder> {
    /**
     * context
     */
    protected Context mContext;
    /**
     * item点击事件监听
     * 支持多次设置点击事件
     */
    private List<OnItemClickListener> listeners;

    public RLVAdapter(Context context) {
        this.mContext = context;
        listeners = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(getItemLayout(parent, viewType)
                , parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    public abstract
    @LayoutRes
    int getItemLayout(ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (listeners != null ) {
            holder.itemView.setOnClickListener(new OnClickListener(holder, position));
        }
        onBindHolder(holder, position);
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    public abstract void onBindHolder(ViewHolder holder, int position);

    /**
     * item的点击事件
     */
    protected void onItemClick(ViewHolder holder, View view, int position) {

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;

        public ViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
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
     * 点击事件处理
     */
    public class OnClickListener implements View.OnClickListener {

        private ViewHolder holder;
        private int position;

        public OnClickListener(ViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            onItemClick(holder, view, position);
            if (listeners != null && listeners.size() > 0) {
                for (OnItemClickListener listener : listeners) {
                    listener.onItemClick(holder, view, position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);
    }
}
