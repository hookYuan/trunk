package yuan.core.list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import yuan.core.R;

import java.util.List;

/**
 * 描述：新版RecyclerView适配器，简化使用
 * BaseViewHolder 采用 BRVAH 扩展实用行
 *
 * @author yuanye
 * @date 2019/7/18 10:55
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    public static final String TAG = "RecyclerAdapter";
    public static final int HEADER_VIEW = 0x1001;
    public static final int FOOTER_VIEW = 0x1002;
    public static final int LOADING_VIEW = 0x1003;
    public static final int ERROR_VIEW = 0x1004;
    public static final int EMPTY_VIEW = 0x1005;

    //header footer
    private View mHeaderLayout;
    private View mFooterLayout;
    /*以上内容待完成*/

    /**
     * context
     */
    protected Context mContext;

    /**
     * recyclerView 多个RecyclerView可能使用同一个Adapter
     */
    protected RecyclerView mRecyclerView;
    /**
     * item click listener
     */
    private OnItemClickListener mItemClickListener;

    /**
     * item long click listener
     */
    private OnItemLongClickListener mItemLongClickListener;
    /**
     * 多类型设置器
     */
    private MultipleType mMultipleType;
    /**
     * 数据源
     */
    protected List<T> mData;
    /**
     * 是否启用自定义View：emptyView,errorView,loadingView
     */
    protected boolean enableFullView;
    /**
     * 全屏View
     */
    private View fullView;
    /**
     * 全屏View类型
     */
    private int fullType;
    /**
     * 空布局
     */
    private View mEmptyLayout;
    /**
     * 失败布局
     */
    private View mErrorLayout;
    /**
     * 加载中布局
     */
    private View mLoadingLayout;

    /**
     * 无参构造方法
     * 必须通过{@link #setData(List)}设置数据源
     * 必须通过{@link #setMultipleType(MultipleType)}}设置布局
     */
    public RecyclerAdapter(List<T> data) {
        this(data, android.R.layout.simple_list_item_1);
    }

    /**
     * 绑定绑定数据/布局文件
     *
     * @param layoutResId
     */
    public RecyclerAdapter(List<T> data, @LayoutRes final int layoutResId) {
        init(layoutResId);
        this.mData = data;
    }

    /**
     * 初始化
     */
    private void init(@LayoutRes final int layoutResId) {

        /*默认类型设置器*/
        setMultipleType(new MultipleType() {
            @Override
            public int getItemLayoutId(int position) {
                return layoutResId;
            }
        });

        /*数据发生改变时监听*/
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {

            }
        });
    }

    /**
     * 设置绑定空布局
     *
     * @param mEmptyLayout
     */
    public void setEmptyLayout(View mEmptyLayout) {
        this.mEmptyLayout = mEmptyLayout;
    }

    /**
     * 设置加载失败布局
     *
     * @param mErrorLayout
     */
    public void setErrorLayout(View mErrorLayout) {
        this.mErrorLayout = mErrorLayout;
    }

    /**
     * 设置加载中布局
     *
     * @param mLoadingLayout
     */
    public void setLoadingLayout(View mLoadingLayout) {
        this.mLoadingLayout = mLoadingLayout;
    }


    /**
     * 显示空布局
     */
    public void showEmptyView() {
        if (mEmptyLayout == null) {
            Log.e(TAG, "请先指定Empty布局");
            return;
        }
        this.fullType = EMPTY_VIEW;
        this.enableFullView = true;
        this.fullView = mEmptyLayout;
        notifyDataSetChanged();
    }

    /**
     * 显示加载失败布局
     */
    public void showErrorLayout() {
        if (mErrorLayout == null) {
            Log.e(TAG, "请先指定Empty布局");
            return;
        }
        this.fullType = ERROR_VIEW;
        this.enableFullView = true;
        this.fullView = mErrorLayout;
        notifyDataSetChanged();
    }

    /**
     * 显示加载中布局
     */
    public void showLoadingLayout() {
        if (mLoadingLayout == null) {
            Log.e(TAG, "请先指定Empty布局");
            return;
        }
        this.fullType = LOADING_VIEW;
        this.enableFullView = true;
        this.fullView = mLoadingLayout;
        notifyDataSetChanged();
    }

    /**
     * 显示数据
     */
    public void showContent() {
        this.enableFullView = false;
        notifyDataSetChanged();
    }

    /**
     * 设置数据源
     *
     * @param mData
     */
    public void setData(List<T> mData) {
        this.mData = mData;
    }

    /**
     * 注册多类型布局，必须在Adapter初始化之前注册才能生效
     *
     * @param multipleType
     */
    public void setMultipleType(MultipleType multipleType) {
        this.mMultipleType = multipleType;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mContext = recyclerView.getContext();
        mRecyclerView = recyclerView;
        /* 设置默认 emptyView、errorView、loadingView */
        if (mEmptyLayout == null) {
            View emptyView = LayoutInflater.from(mContext).inflate(R.layout.empty_view_layout, recyclerView, false);
            setEmptyLayout(emptyView);
        }
        if (mErrorLayout == null) {
            View errorView = LayoutInflater.from(mContext).inflate(R.layout.error_view_layout, recyclerView, false);
            setErrorLayout(errorView);
        }
        if (mLoadingLayout == null) {
            View loadingView = LayoutInflater.from(mContext).inflate(R.layout.loading_view_layout, recyclerView, false);
            setLoadingLayout(loadingView);
        }

        /* 当 emptyView、errorView、loadingView布局时设置显示一行  */
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            final GridLayoutManager.SpanSizeLookup oldSpanSizeLookup = gridManager.getSpanSizeLookup();
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (enableFullView) {
                        return gridManager.getSpanCount();
                    }
                    return oldSpanSizeLookup.getSpanSize(position);
                }
            });
        }
    }

    @Override
    public final int getItemViewType(int position) {
        //针对全屏自定义View,控制类型
        if (enableFullView) {
            return fullType;
        }
        //缓存布局类型，同时也是缓存的布局类型编号，递增，从0开始,type是无序的
        Integer itemLayoutId = mMultipleType.getItemLayoutId(position);
        return itemLayoutId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (enableFullView) {
            /* 全屏布局  emptyView、loadingView、errorView*/
            itemView = fullView;
        } else {
            /* 加载item布局 */
            itemView = LayoutInflater.from(mContext).inflate(viewType, parent, false);
        }
        final BaseViewHolder viewHolder = new BaseViewHolder(itemView);

        /* 统一处理Item点击事件 */
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ItemClick 处理
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(viewHolder, v, viewHolder.getAdapterPosition());
            }
        });

        /*统一处理长按事件*/
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemLongClickListener != null) {
                    return mItemLongClickListener.onItemLongClick(viewHolder, v, viewHolder.getAdapterPosition());
                }
                return false;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (enableFullView) {
            onBindFullViewHolder(holder, position);
            return;
        }
        T item = null;//绑定数据，可能为空
        if (mData != null && mData.size() > position) item = mData.get(position);
        onBindHolder(holder, item, position);
    }

    @Override
    public int getItemCount() {
        //启用全屏自定义View，返回数据集合为1
        if (enableFullView) return 1;
        return mData != null ? mData.size() : 0;
    }

    /**
     * 设置全屏
     *
     * @param holder
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    public abstract void onBindHolder(BaseViewHolder holder, T item, int position);

    /**
     * 绑定全屏数据项
     *
     * @param holder
     */
    public void onBindFullViewHolder(BaseViewHolder holder, int position) {
    }

    /**
     * 提供外部设置点击事件
     *
     * @param listener 事件监听
     */
    public void setOnItemClick(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    /**
     * 提供外部设置长按点击事件
     *
     * @param listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    /**
     * Item点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(BaseViewHolder holder, View view, int position);
    }

    /**
     * Item长按事件
     */
    public interface OnItemLongClickListener {
        boolean onItemLongClick(BaseViewHolder adapter, View view, int position);
    }

    /**
     * 根据position注册多类型
     */
    public interface MultipleType {
        @LayoutRes
        int getItemLayoutId(int position);
    }
}
