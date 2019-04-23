package com.yuan.simple.one.multi;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yuan.base.tools.adapter.recycler.GridDivider;
import com.yuan.base.tools.adapter.recycler.MultiAdapter;
import com.yuan.base.ui.kernel.BaseActivity;
import com.yuan.base.widget.title.TitleBar;
import com.yuan.simple.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanye
 * @date 2018-12-12 12:20:59
 */
public class MultiActivity extends BaseActivity {


    private TitleBar titleBar;

    private RecyclerView rlvList;


    @Override
    public int getLayoutId() {
        return R.layout.activity_multi;
    }

    @Override
    public void findViews() {
        rlvList = findViewById(R.id.rlv_list);
        titleBar = findViewById(R.id.title_bar);

        rlvList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rlvList.setAdapter(getAdapter(MultiAdapter.MultiBean.TYPE_NORMAL, 1));
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void initData() {

    }

    private boolean switchFlag = false;

    @Override
    public void setListener() {
        titleBar.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchFlag) {
                    switchFlag = false;
                    //清除样式
                    for (int i = 0; i < rlvList.getItemDecorationCount(); i++) {
                        rlvList.removeItemDecorationAt(i);
                    }

                    rlvList.setLayoutManager(new GridLayoutManager(mContext, 2));
                    rlvList.setAdapter(getAdapter(MultiAdapter.MultiBean.TYPE_NORMAL, 1));
                    rlvList.addItemDecoration(new GridDivider(mContext));
                    titleBar.setRightText("样式一");
                } else {
                    for (int i = 0; i < rlvList.getItemDecorationCount(); i++) {
                        rlvList.removeItemDecorationAt(i);
                    }
                    rlvList.addItemDecoration(new GridDivider(mContext));

                    rlvList.setLayoutManager(new GridLayoutManager(mContext, 1));
                    rlvList.setAdapter(getAdapter(MultiAdapter.MultiBean.TYPE_FLOAT, 1));
                    titleBar.setRightText("样式二");
                    switchFlag = true;
                }
            }
        });
    }


    private MultiDemoAdapter getAdapter(int sectionType, int count) {
        MultiDemoAdapter multiAdapter = new MultiDemoAdapter(mContext);
        List<ItemBean> list = new ArrayList<>();
        list.add(new ItemBean("1"));
        list.add(new ItemBean("2"));
        list.add(new ItemBean("3"));
        list.add(new ItemBean("4"));
        multiAdapter.addSection(new SectionBean("1"), sectionType);
        multiAdapter.addData(list, count);
        multiAdapter.addSection(new SectionBean("2"), sectionType);
        multiAdapter.addData(list, count);
        multiAdapter.addSection(new SectionBean("3"), sectionType);
        multiAdapter.addData(list, count);
        multiAdapter.addSection(new SectionBean("4"), sectionType);
        multiAdapter.addData(list, count);
        multiAdapter.addSection(new SectionBean("5"), sectionType);
        multiAdapter.addData(list, count);
        multiAdapter.addSection(new SectionBean("6"), sectionType);
        multiAdapter.addData(list, count);
        multiAdapter.addSection(new SectionBean("7"), sectionType);
        multiAdapter.addData(list, count);
        multiAdapter.addSection(new SectionBean("8"), sectionType);
        multiAdapter.addData(list, count);
        multiAdapter.addData(list, count);
        return multiAdapter;
    }
}
