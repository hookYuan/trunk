package yuan.ui_extend;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import yuan.core.list.DecorationDivider;
import yuan.core.list.RLVAdapter;
import yuan.core.mvp.BaseActivity;
import yuan.core.mvp.Presenter;

import java.util.List;

/**
 * 集成RecyclerView 布局自定义
 * <p>
 * Created by YuanYe on 2018/8/11.
 */
public abstract class RLVActivity<T extends Presenter> extends BaseActivity<T> {

    /**
     * recyclerView
     */
    private RecyclerView rlvList;

    /**
     * 适配器
     */
    private RLVAdapter adapter;

    @Override
    public int getLayoutId() {
        return 0;
    }

    /**
     * 绑定RecyclerView
     *
     * @return
     */
    protected RecyclerView bindRecyclerView() {
        return null;
    }

    @Override
    public View getLayoutView() {
        //如果有指定RecyclerView，使用指定布局，否则采用默认布局
        if (bindRecyclerView() != null) {
            rlvList = new RecyclerView(mContext);
            return rlvList;
        }
        return null;
    }

    @Override
    public void findViews() {
        if (rlvList == null) rlvList = bindRecyclerView();
    }

    @Override
    public void initData() {
        if (rlvList == null) {
            throw new NullPointerException("RecyclerView为null: 使用getLayoutId()重新绑定布局后，必须调用bindRecyclerView()绑定recyclerView");
        }
        initRecyclerView(rlvList);
        rlvList.setAdapter(getAdapter());
    }

    @Override
    public void setListener() {

    }

    /**
     * 初始化RecyclerView，可以设置RecyclerView的列数
     * 设置LayoutManager
     *
     * @param rlvList
     */
    protected void initRecyclerView(RecyclerView rlvList) {
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvList.addItemDecoration(new DecorationDivider(mContext));
    }

    /**
     * 初始化Adapter
     *
     * @return
     */
    protected RLVAdapter getAdapter() {
        if (adapter != null) {
            return adapter;
        }
        return adapter = new RLVAdapter(mContext) {
            @Override
            public int getItemLayout(ViewGroup parent, int viewType) {
                return RLVActivity.this.getItemLayout(parent, viewType);
            }

            @Override
            public void onBindHolder(ViewHolder holder, int position) {
                RLVActivity.this.onBindHolder(holder, position);
            }

            @Override
            public int getItemCount() {
                return getData().size();
            }

            @Override
            public void onItemClick(ViewHolder holder, View view, int position) {
                RLVActivity.this.onItemClick(holder, view, position);
            }
        };
    }


    /**
     * 绑定item数据
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindHolder(RLVAdapter.ViewHolder holder, int position);

    /**
     * 获取itemLayout布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract int getItemLayout(ViewGroup parent, int viewType);

    /**
     * item点击事件
     *
     * @param holder
     * @param view
     * @param position
     */
    protected void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {

    }

    /**
     * 获取数据源
     *
     * @return
     */
    public abstract List<?> getData();
}
