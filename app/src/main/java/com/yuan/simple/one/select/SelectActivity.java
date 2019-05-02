package com.yuan.simple.one.select;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.kernel.RLVAdapter;
import com.yuan.kernel.ToastUtil;
import com.yuan.simple.R;
import com.yuan.simple.tool.ListActivity;
import com.yuan.tools_extend.sys.SelectUtil;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends ListActivity {

    private List<String> data;

    @Override
    public void initData() {
        super.initData();
        getTitleBar().setTitleText("SelectUtil")
                .setLeftClickFinish()
                .setTitleTextColor(getColor2(R.color.white))
                .setLeftIcon(getDrawable2(R.drawable.ic_base_back_white))
                .setBackgroundColor(getColor2(R.color.colorPrimary));
    }

    @Override
    protected void onBindHolder(RLVAdapter.ViewHolder holder, int position) {
        holder.setText(android.R.id.text1, data.get(position));
    }

    @Override
    protected int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public List<?> getData() {
        data = new ArrayList<>();

        data.add("启动相机");
        data.add("启动图库");
        data.add("启动相机和图库");
        data.add("启动通讯录");
        return data;
    }

    @Override
    protected void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {
        switch (position) {
            case 0:
                SelectUtil.startCamera(mContext, new SelectUtil.SelectBack() {
                    @Override
                    public void onBack(String path) {
                        ToastUtil.showShort(mContext, path);
                    }
                });
                break;
            case 1:
                SelectUtil.startAlbum(mContext, new SelectUtil.SelectBack() {
                    @Override
                    public void onBack(String path) {
                        ToastUtil.showShort(mContext, path);
                    }
                });
                break;
            case 2:
                SelectUtil.startCameraAlbum(mContext, new SelectUtil.SelectBack() {
                    @Override
                    public void onBack(String path) {
                        ToastUtil.showShort(mContext, path);
                    }
                });
                break;
            case 3:
                SelectUtil.startAddressBook(mContext, new SelectUtil.ContactBack() {
                    @Override
                    public void onBack(String name, String phone) {
                        ToastUtil.showShort(mContext, "姓名：" + name + "  电话：" + phone);
                    }
                });
                break;
        }
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }
}
