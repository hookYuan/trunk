package com.yuan.simple.one.input;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yuan.base.tools.adapter.recycler.RLVAdapter;
import com.yuan.base.ui.activity.MvpActivity;
import com.yuan.simple.R;

/**
 * 解决键盘弹出引起的布局上窜
 */
public class InputActivity extends MvpActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_input;
    }

    @Override
    public void findViews() {
        RecyclerView recyclerView = find(R.id.rlv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(new RLVAdapter(mContext) {
            @Override
            public int getItemLayout(ViewGroup parent, int viewType) {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void onBindHolder(ViewHolder holder, int position) {
                holder.setText(android.R.id.text1, "测试" + position);
            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

    }

}
