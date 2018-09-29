package com.yuan.simple.one.expandable;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yuan.base.tools.adapter.expandable.ExpandableAdapter;
import com.yuan.base.tools.adapter.recycler.GridDivider;
import com.yuan.base.tools.layout.Views;
import com.yuan.base.ui.extra.HRefresh;
import com.yuan.base.ui.extra.IRefresh;
import com.yuan.base.ui.mvp.MvpActivity;
import com.yuan.base.widget.title.Title;
import com.yuan.simple.R;

import java.util.ArrayList;
import java.util.List;

public class ExpandableActivity extends MvpActivity {

    @Override
    public int getLayoutId() {
        return R.layout.frag_simple_list;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Title titleBar = find(R.id.title_bar);
        titleBar.getTitleBar().setTitleText("折叠TextView")
                .setLeftClickFinish()
                .setTextColor(ContextCompat.getColor(mContext, R.color.white))
                .setLeftIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_base_back_white))
                .setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

        SmartRefreshLayout refreshView = Views.find(this, com.yuan.base.R.id.srl_refresh);
        if (this instanceof IRefresh) HRefresh.init(refreshView, (IRefresh) this);
        else refreshView.setEnableRefresh(false);

        RecyclerView rlvList = find(R.id.rlv_list);
        rlvList.setLayoutManager(new LinearLayoutManager(this));
        rlvList.addItemDecoration(new GridDivider(this));
        rlvList.setAdapter(new ExpandableAdapter(this) {
            @Override
            public void onGroupItemClick(GroupHolder holder, int groupPosition, boolean isExpandable) {

            }

            @Override
            public void onChildItemClick(ChildHolder holder, int groupPosition, int childPosition) {

            }

            @Override
            public int getGroupLayout(ViewGroup parent, int viewType) {
                return android.R.layout.simple_list_item_2;
            }

            @Override
            public int getChildLayout(ViewGroup parent, int viewType) {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public List getGroupData() {
                return getData();
            }

            @Override
            public List getChildData(int groupPosition) {
                return getData().get(groupPosition).getList();
            }

            @Override
            public void onBindGroupHolder(GroupHolder holder, int groupPosition) {
                holder.setText(android.R.id.text1, getData().get(groupPosition).getGroup());
            }

            @Override
            public void onBindChildHolder(ChildHolder holder, int groupPosition, int childPosition) {
                holder.setText(android.R.id.text1, getData().get(groupPosition).getList().get(childPosition).getItem());
            }
        });
    }

    ArrayList<ExpandableBean> groupData;

    private ArrayList<ExpandableBean> getData() {
        if (groupData == null) {
            groupData = new ArrayList<>();

            if (true) {
                ExpandableBean bean1 = new ExpandableBean();

                ArrayList<ExpandableBean.ChildItem> child1 = new ArrayList<>();
                ExpandableBean.ChildItem childItem1 = new ExpandableBean.ChildItem();
                childItem1.setItem("子目录一");
                child1.add(childItem1);

                ExpandableBean.ChildItem childItem2 = new ExpandableBean.ChildItem();
                childItem2.setItem("子目录二");
                child1.add(childItem2);

                bean1.setGroup("父目录一");
                bean1.setList(child1);
                groupData.add(bean1);
            }

            if (true) {
                ExpandableBean bean1 = new ExpandableBean();
                ArrayList<ExpandableBean.ChildItem> child1 = new ArrayList<>();
                ExpandableBean.ChildItem childItem1 = new ExpandableBean.ChildItem();
                childItem1.setItem("子目录三");
                ExpandableBean.ChildItem childItem2 = new ExpandableBean.ChildItem();
                childItem2.setItem("子目录四");
                ExpandableBean.ChildItem childItem3 = new ExpandableBean.ChildItem();
                childItem3.setItem("子目录五");

                child1.add(childItem1);
                child1.add(childItem2);
                child1.add(childItem3);

                bean1.setGroup("父目录二");
                bean1.setList(child1);
                groupData.add(bean1);
            }
        }
        return groupData;
    }

}
