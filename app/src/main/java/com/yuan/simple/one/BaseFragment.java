package com.yuan.simple.one;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yuan.kernel.GridDivider;
import com.yuan.kernel.RLVAdapter;
import com.yuan.kernel.RouteUtil;
import com.yuan.kernel.ToastUtil;
import com.yuan.kernel.function.CallbackManager;
import com.yuan.kernel.function.CallbackNoParamNoResult;
import com.yuan.simple.R;
import com.yuan.simple.one.db.DBActivity;
import com.yuan.simple.one.dialog.AlertDialogActivity;
import com.yuan.simple.one.foldTextView.FoldActivity;
import com.yuan.simple.one.http.NetActivity;
import com.yuan.simple.one.input.InputActivity;
import com.yuan.simple.one.multi.MultiActivity;
import com.yuan.simple.one.roundView.RoundTextActivity;
import com.yuan.simple.one.select.SelectActivity;
import com.yuan.simple.one.sort.SortActivity;
import com.yuan.simple.one.toolbar.TitleBarActivity;
import com.yuan.simple.tool.ListFragment;
import com.yuan.tools_independ.common.Kits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuanYe on 2018/4/13.
 */
public class BaseFragment extends ListFragment {

    private ArrayList<OneListBean> mData;

    @Override
    public void initRecyclerView(RecyclerView rlvList) {
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvList.addItemDecoration(new GridDivider((int) Kits.Dimens.dpToPx(mContext, 0.8f),
                ContextCompat.getColor(mContext, R.color.colorDivider)));

//        getTitleBar().setTitleText("基础功能")
//                .setTextColor(ContextCompat.getColor(mContext, R.color.colorFont33))
//                .setRightText("线程")
//                .setRightOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
        CallbackManager.get().addCallback("123",
                new CallbackNoParamNoResult() {
                    @Override
                    public void callback() {
                        ToastUtil.showShort(mContext, "返回了");
                    }
                });
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
        RouteUtil.open(mContext, mData.get(position).getClazz());
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
        mData.add(new OneListBean("multiAdapter", MultiActivity.class));
        mData.add(new OneListBean("DBUtil", DBActivity.class));
        mData.add(new OneListBean("SelectUtil", SelectActivity.class));
        return mData;
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }
}
