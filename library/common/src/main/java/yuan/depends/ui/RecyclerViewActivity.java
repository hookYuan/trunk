package yuan.depends.ui;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.GridDivider;
import yuan.core.list.RecyclerAdapter;
import yuan.core.mvp.BaseActivity;
import yuan.core.mvp.Presenter;
import yuan.core.title.TitleBar;
import yuan.core.widget.StateLayout;
import yuan.depends.R;

/**
 * 集成RecyclerView 布局自定义
 * <p>
 * Created by YuanYe on 2018/8/11.
 */
public abstract class RecyclerViewActivity<T extends Presenter, D> extends BaseActivity<T> {

    /**
     * recyclerView
     */
    protected RecyclerView mRecyclerView;

    /**
     * 上拉加载，下拉刷新
     */
    protected SmartRefreshLayout mSmartRefreshLayout;

    /**
     * 状态切换Layout
     */
    protected StateLayout mStateLayout;

    /**
     * 适配器
     */
    protected RecyclerAdapter mAdapter;

    /**
     * 标题
     */
    protected TitleBar mTitleBar;

    /**
     * 数据源
     */
    protected List<D> mData;

    /**
     * 获取ItemLayoutId布局文件
     *
     * @return
     */
    protected abstract int getItemLayoutId(int position);

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    public abstract void onBindHolder(BaseViewHolder holder, D item, int position);

    @Override
    public int getLayoutId() {
        return R.layout.base_recycler_refresh_layout;
    }

    @Override
    public final void initData() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mStateLayout = findViewById(R.id.stateLayout);
        mData = new ArrayList<>();

        createAdapter();
        init(mRecyclerView, mSmartRefreshLayout, mStateLayout);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化RecyclerView，可以设置RecyclerView的列数
     * 设置LayoutManager
     *
     * @param recyclerView
     */
    protected void init(RecyclerView recyclerView,
                        SmartRefreshLayout smartRefreshLayout,
                        StateLayout mStateLayout) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new GridDivider());
    }

    /**
     * 创建Adapter
     */
    protected void createAdapter() {
//        mAdapter = new RecyclerAdapter<D>(mData, new RecyclerAdapter.OnMultiType() {
//            @Override
//            public int getLayoutResId(int position) {
//                return getItemLayoutId(position);
//            }
//        }) {
//            @Override
//            public void onBindHolder(BaseViewHolder holder, D item, int position) {
//                RecyclerViewActivity.this.onBindHolder(holder, item, position);
//            }
//        };
    }
}
