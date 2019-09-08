package yuan.core.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

/**
 * 描述：新版RecyclerView适配器，简化使用
 * BaseViewHolder 采用 BRVAH 扩展实用行
 *
 * @author yuanye
 * @date 2019/7/18 10:55
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

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
    /*以上内容待完成*/

    /**
     * context
     */
    protected Context mContext;

    /**
     * item click listener
     */
    private OnItemClickListener mItemClickListener;

    /**
     * item long click listener
     */
    private OnItemLongClickListener mItemLongClickListener;
    /**
     * Item布局文件缓存<viewType,LayoutRes></>
     * viewType :从0开始递增，由系统维护
     * LayoutRes： 不同ViewType对应的布局文件
     */
    private HashMap<Integer, Integer> mLayoutCache;
    /**
     * 多类型设置器
     */
    private OnMultiType mMultiType;

    /**
     * 数据源
     */
    protected List<T> mData;


    /**
     * 无参构造方法
     * 必须通过{@link #setData(List)}设置数据源
     * 必须通过{@link #registerMultiType(OnMultiType)}设置布局
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
        init();
        this.mData = data;
        registerMultiType(new OnMultiType() {
            @Override
            public int getLayoutResId(int position) {
                return layoutResId;
            }
        });
    }

    /**
     * 初始化
     */
    private void init() {
        mLayoutCache = new HashMap<>();
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
     * @param multiType
     */
    public void registerMultiType(OnMultiType multiType) {
        this.mMultiType = multiType;
    }

    @Override
    public final int getItemViewType(int position) {
        int resLayout = mMultiType.getLayoutResId(position);
        if (!mLayoutCache.containsValue(resLayout)) {
            mLayoutCache.put(mLayoutCache.size(), resLayout);
        }
        //缓存布局类型，同时也是缓存的布局类型编号，递增，从0开始,type是无序的
        int viewType = 0;
        for (Integer key : mLayoutCache.keySet()) {
            if (mLayoutCache.get(key) == resLayout) {
                viewType = key;
            }
        }
        return viewType;
    }

//    /**
//     * @param position
//     * @return
//     */
//    public int getItemViewId(int position) {
//
//    }

    /**
     * 根据 position 获取对应 layoutId
     *
     * @param position
     * @return
     */
    public int getLayoutResIdType(int position) {
        return mMultiType.getLayoutResId(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(mLayoutCache.get(viewType), parent, false);
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
        T item = null;//绑定数据，可能为空
        if (mData != null && mData.size() > position) item = mData.get(position);
        onBindHolder(holder, item, position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    public abstract void onBindHolder(BaseViewHolder holder, T item, int position);

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
    public interface OnMultiType {
        @LayoutRes
        int getLayoutResId(int position);
    }
}
