package com.yuan.simple.one.sort;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import yuan.core.mvp.BaseActivity;
import yuan.core.list.DecorationDivider;
import yuan.core.list.RLVAdapter;
import yuan.core.tool.Views;
import com.yuan.simple.R;
import yuan.core.sort.ChineseSortUtil;
import yuan.widget.sideBar.SideBar;

import java.util.ArrayList;


/**
 * 按照汉字排序
 */
public class SortActivity extends BaseActivity {

    private ArrayList<ChineseBean> data = new ArrayList<>();

    private RecyclerView recyclerView;

    private SideBar sideBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sort;
    }

    @Override
    public void findViews() {
        ChineseSortUtil.sortData(createData());
        recyclerView = Views.find(this, R.id.rlv_list);
        sideBar = findViewById(R.id.sideBar);
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DecorationDivider(mContext));
        recyclerView.setAdapter(createAdapter());
        sideBar.setRecyclerView(recyclerView, data);
        sideBar.setSortData(data);
    }

    @Override
    public void setListener() {

    }

    private RLVAdapter createAdapter() {
        RLVAdapter adapter = new RLVAdapter(mContext) {
            @Override
            public int getItemLayout(ViewGroup parent, int viewType) {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void onBindHolder(ViewHolder holder, int position) {
                holder.setText(android.R.id.text1, data.get(position).getName());
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        };
        return adapter;
    }


    private ArrayList<ChineseBean> createData() {
        data.add(new ChineseBean("马云"));
        data.add(new ChineseBean("刘强东"));
        data.add(new ChineseBean("柳传志"));
        data.add(new ChineseBean("彭万里"));
        data.add(new ChineseBean("高大山"));
        data.add(new ChineseBean("谢大海"));
        data.add(new ChineseBean("林莽"));
        data.add(new ChineseBean("黄强辉"));
        data.add(new ChineseBean("章汉夫"));
        data.add(new ChineseBean("范长江"));
        data.add(new ChineseBean("林君雄"));
        data.add(new ChineseBean("朱希亮"));
        data.add(new ChineseBean("李四光"));
        data.add(new ChineseBean("甘铁生"));
        data.add(new ChineseBean("张伍绍祖"));
        data.add(new ChineseBean("马继祖"));
        data.add(new ChineseBean("赵进喜"));
        data.add(new ChineseBean("程孝先"));
        data.add(new ChineseBean("宗敬先"));
        data.add(new ChineseBean("赵大华"));
        data.add(new ChineseBean("年广嗣"));
        data.add(new ChineseBean("赵德荣"));
        return data;
    }
}
