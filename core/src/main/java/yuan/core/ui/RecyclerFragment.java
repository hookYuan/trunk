package yuan.core.ui;

import android.os.Bundle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import yuan.core.R;
import yuan.core.list.RecyclerAdapter;
import yuan.core.mvp.BaseFragment;
import yuan.core.mvp.Presenter;

/**
 * 描述：自带recyclerView的Activity
 * 使用RecyclerActivity时，注入的Adapter必须包含无参构造方法
 *
 * @author yuanye
 * @date 2019/8/28 10:41
 */
public abstract class RecyclerFragment<presenter extends Presenter, Model extends Object> extends BaseFragment<presenter> {
    //RecyclerView
    protected RecyclerView mRecyclerView;

    //适配器
    protected RecyclerAdapter mAdapter;

    //数据源
    protected ArrayList<Model> mData;

    @Override
    public int getLayoutId() {
        return R.layout.base_recycler_layout;
    }

    @Override
    public void findViews() {
        //设置RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {
        super.parseBundle(bundle);
        Adapter annotation = this.getClass().getAnnotation(Adapter.class);
        mData = new ArrayList<Model>();
        mAdapter = createAdapter(annotation);
        initRecyclerView();
    }


    @Override
    public void initComplete() {
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化RecyclerView
     */
    protected void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,1));
    }

    /**
     * 创建Adapter,默认反射创建
     *
     * @return
     */
    protected <Adapter extends RecyclerAdapter> Adapter createAdapter(yuan.core.ui.Adapter annotation) {
        if (annotation==null){
            throw new NullPointerException("请指定Recycler Adapter");
        }
        Class adapterClass = annotation.adapter();
        if (adapterClass == null) {
            throw new NullPointerException("请指定Recycler Adapter");
        }
        try {
            //默认List<数据源> 构造方法
            Constructor constructor = null;
            Adapter adapter = null;
            if (annotation.layoutId() == -1) {
                constructor = adapterClass.getConstructor(List.class);
                adapter = (Adapter) constructor.newInstance(mData);
            } else {
                constructor = adapterClass.getConstructor(List.class, Integer.class);
                adapter = (Adapter) constructor.newInstance(mData, annotation.layoutId());
            }
            return adapter;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
