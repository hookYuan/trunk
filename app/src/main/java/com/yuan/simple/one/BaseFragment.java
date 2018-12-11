package com.yuan.simple.one;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.base.tools.adapter.recycler.GridDivider;
import com.yuan.base.tools.adapter.recycler.RLVAdapter;
import com.yuan.base.tools.common.Kits;
import com.yuan.base.tools.router.jump.Jump;
import com.yuan.base.ui.fragment.RLVFragment;
import com.yuan.simple.R;
import com.yuan.simple.one.dialog.AlertDialogActivity;
import com.yuan.simple.one.foldTextView.FoldActivity;
import com.yuan.simple.one.http.NetActivity;
import com.yuan.simple.one.input.InputActivity;
import com.yuan.simple.one.roundView.RoundTextActivity;
import com.yuan.simple.one.sort.SortActivity;
import com.yuan.simple.one.toolbar.TitleBarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuanYe on 2018/4/13.
 */
public class BaseFragment extends RLVFragment {

    private ArrayList<OneListBean> mData;

    @Override
    public void initRecyclerView(RecyclerView rlvList) {
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvList.addItemDecoration(new GridDivider((int) Kits.Dimens.dpToPx(mContext, 0.8f),
                ContextCompat.getColor(mContext, R.color.colorDivider)));

        getTitleBar().setTitleText("基础功能")
                .setTextColor(ContextCompat.getColor(mContext, R.color.white))
                .setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
    }

    @Override
    public void onBindHolder(RLVAdapter.ViewHolder holder, int position) {
        holder.setText(android.R.id.text1, mData.get(position).getToolbar());
    }

    @Override
    public int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {
        Jump.open(mContext, mData.get(position).getClazz());
    }

    @Override
    public List<?> getData() {
        mData = new ArrayList<>();
        mData.add(new OneListBean("titleBar", TitleBarActivity.class));
        mData.add(new OneListBean("roundView", RoundTextActivity.class));
        mData.add(new OneListBean("foldTextView", FoldActivity.class));
        mData.add(new OneListBean("sortChinese", SortActivity.class));
        mData.add(new OneListBean("input", InputActivity.class));
        mData.add(new OneListBean("okHttp", NetActivity.class));
        mData.add(new OneListBean("alertDialog", AlertDialogActivity.class));
        return mData;
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void setListener() {

    }
}
