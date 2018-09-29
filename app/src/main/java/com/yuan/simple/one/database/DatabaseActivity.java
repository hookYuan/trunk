package com.yuan.simple.one.database;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.base.tools.adapter.recycler.GridDivider;
import com.yuan.base.tools.adapter.recycler.RLVAdapter;
import com.yuan.base.ui.activity.RLVActivity;
import com.yuan.simple.R;

import java.util.ArrayList;
import java.util.List;

public class DatabaseActivity extends RLVActivity {

    private ArrayList<DataBean> mData;

    @Override
    public void initRecyclerView(RecyclerView rlvList) {
        getTitleBar().setTitleText("数据库操作")
                .setLeftClickFinish()
                .setTitleTextColor(getColor2(R.color.white))
                .setLeftIcon(getDrawable2(R.drawable.ic_base_back_white))
                .setBackgroundColor(getColor2(R.color.colorPrimary));
        rlvList.setLayoutManager(new LinearLayoutManager(this));
        rlvList.addItemDecoration(new GridDivider(this));
    }

    @Override
    public void onBindHolder(RLVAdapter.ViewHolder holder, int position) {
        holder.setText(android.R.id.text1, mData.get(position).getName());
    }

    @Override
    public int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {
        switch (mData.get(position).getType()) {
            case 1:

                break;
        }
    }

    @Override
    public List<?> getData() {
        mData = new ArrayList<>();
        mData.add(new DataBean("创建数据库", 1));
        mData.add(new DataBean("创建表", 2));
        mData.add(new DataBean("添加数据", 3));
        mData.add(new DataBean("查询数据", 4));
        mData.add(new DataBean("删除数据", 5));
        mData.add(new DataBean("sql执行", 6));
        return mData;
    }

    public class DataBean {
        private String name;
        private int type;

        public DataBean(String name, int type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
