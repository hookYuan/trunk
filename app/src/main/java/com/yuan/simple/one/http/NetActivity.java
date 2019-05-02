package com.yuan.simple.one.http;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.kernel.GridDivider;
import com.yuan.kernel.RLVAdapter;
import com.yuan.kernel.ToastUtil;
import com.yuan.simple.tool.ListActivity;
import com.yuan.simple.R;
import com.yuan.tools_extend.sys.SelectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用OKHttpUtil的一个事例
 */
public class NetActivity extends ListActivity<PNet> {

    private ArrayList<String> mData;

    @Override
    public void initRecyclerView(RecyclerView rlvList) {
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvList.addItemDecoration(new GridDivider(mContext));
        getTitleBar().setTitleText("OKUtil")
                .setLeftClickFinish()
                .setTextColor(getResources().getColor(R.color.white))
                .setLeftIcon(getResources().getDrawable(R.drawable.ic_base_back_white))
                .setBackgroundColor(
                        getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onBindHolder(RLVAdapter.ViewHolder holder, int position) {
        holder.setText(android.R.id.text1, mData.get(position));
    }

    @Override
    public int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {
        switch (position) {
            case 0:
                getP().get();
                break;
            case 1:
                getP().get2();
                break;
            case 3:
                getP().downloadFile();
                break;
            case 4:
                getP().showCacheSize();
                break;
            case 5:
                getP().delCache();
                break;
            case 6:
                SelectUtil.startAddressBook(mContext, new SelectUtil.ContactBack() {
                    @Override
                    public void onBack(String name, String phone) {
                        ToastUtil.showShort(mContext, name + phone);
                    }
                });
                break;

        }
    }

    @Override
    public List<?> getData() {
        if (mData == null) {
            mData = new ArrayList<>();
            mData.add("Get请求");
            mData.add("Post请求");
            mData.add("上传文件");
            mData.add("下载文件");
            mData.add("获取缓存文件大小");
            mData.add("删除缓存文件");
            mData.add("选择图片");
        }
        return mData;
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

}
