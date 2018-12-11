package com.yuan.simple.one.toolbar;


import com.yuan.base.tools.adapter.recycler.GridDivider;
import com.yuan.base.tools.adapter.recycler.RLVAdapter;
import com.yuan.base.tools.common.Kits;
import com.yuan.base.tools.log.ToastUtil;
import com.yuan.base.ui.fragment.RLVFragment;
import com.yuan.base.widget.title.statusbar.StatusUtil;
import com.yuan.base.widget.title.titlebar.TitleBar;
import com.yuan.simple.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * create by Yuan ye.
 * download truck before use this
 */
public class TitleBarFragment extends RLVFragment {

    private List<TitleBarBean> mData;

    @Override
    public void initRecyclerView(RecyclerView rlvList) {
        getTitleBar().setTitleText("TitleBar")
                .setTextColor(ContextCompat.getColor(mContext, R.color.white))
                .setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        //动态更改列数
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int code = mData.get(position).getCode();
                if (code == 1000 || code == 2000 || code == 3000
                        || code == 4000) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        rlvList.setLayoutManager(manager);
        //add divider.
        rlvList.addItemDecoration(new GridDivider((int) Kits.Dimens.dpToPx(mContext, 0.8f)
                , ContextCompat.getColor(mContext, R.color.colorDivider)));
    }

    @Override
    public void onBindHolder(RLVAdapter.ViewHolder holder, int position) {
        holder.setText(android.R.id.text1, mData.get(position).getName());
    }

    @Override
    public int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_2;
    }

    @Override
    public void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {
        switch (mData.get(position).getCode()) {
            case 1001:
                StatusUtil.darkMode(mContext, true);
                break;
            case 1002:
                StatusUtil.darkMode(mContext, false);
                break;
            case 1003:
                StatusUtil.setStatusBarColor(mContext, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case 1004:
                StatusUtil.hideBar(mContext);
                break;
            case 10041:
                StatusUtil.showBar(mContext);
                break;
            case 1005:
                StatusUtil.setStatusBarColor(mContext,
                        ContextCompat.getColor(mContext, android.R.color.transparent));
                break;
            case 1006:
                StatusUtil.setFloat(mContext);
                break;
            case 2001:
                getTitleBar().setBackgroundImg("http://img1.imgtn.bdimg.com/it/u=626186099,3046696268&fm=27&gp=0.jpg");
                break;
            case 2002:
                getTitleBar().setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case 2003:
                getTitleBar().setLeftText("取消");
                break;
            case 2004:
                getTitleBar().setLeftIcon(R.drawable.ic_base_back_black);
                break;
            case 2005:
                getTitleBar().setTitleText("Title");
                break;
            case 20051:
                getTitleBar().setSubtitleText("副标题");
                break;
            case 2006:
                getTitleBar().setRightText("菜单");
                break;
            case 2007:
                getTitleBar().setRightIcon(R.drawable.ic_base_menu_more_black);
                break;
            case 2008:
                ArrayList<String> menuData = new ArrayList<String>();
                menuData.add("选项一");
                menuData.add("选项二");
                menuData.add("选项三");
                getTitleBar().setRightMenu(menuData, new TitleBar.OnMenuItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        ToastUtil.showShort(mContext, menuData.get(position));
                    }
                });
                break;
            case 2009:
                getTitleBar().setAnimationIn();
                break;
            case 2010:
                getTitleBar().setAnimationOut();
                break;
            case 2011:
                getTitleBar().setLeftClickFinish();
                break;
            case 3001:
                getStatusBar().setVisibility(View.VISIBLE);
                break;
            case 3002:
                getStatusBar().setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                break;
            case 3003:
                getStatusBar().setBackgroundImg("http://img1.imgtn.bdimg.com/it/u=626186099,3046696268&fm=27&gp=0.jpg");
                break;
            case 3004:
                getStatusBar().setVisibility(View.GONE);
                break;
            case 4001:
                open(TitleDemo1.class);
                break;
        }
    }

    @Override
    public List<?> getData() {
        if (mData == null) {
            mData = new ArrayList<>();
            mData.add(new TitleBarBean("系统状态栏", 1000));
            mData.add(new TitleBarBean("黑色文字", 1001));
            mData.add(new TitleBarBean("白色文字", 1002));
            mData.add(new TitleBarBean("系统状态栏背景色", 1003));
            mData.add(new TitleBarBean("隐藏状态栏", 1004));
            mData.add(new TitleBarBean("显示状态栏", 10041));
            mData.add(new TitleBarBean("透明状态栏", 1005));
            mData.add(new TitleBarBean("悬浮状态栏", 1006));

            mData.add(new TitleBarBean("Toolbar", 2000));
            mData.add(new TitleBarBean("背景图片", 2001));
            mData.add(new TitleBarBean("颜色背景", 2002));
            mData.add(new TitleBarBean("左边文字", 2003));
            mData.add(new TitleBarBean("左边图标", 2004));
            mData.add(new TitleBarBean("主标题", 2005));
            mData.add(new TitleBarBean("副标题", 20051));
            mData.add(new TitleBarBean("右边文字", 2006));
            mData.add(new TitleBarBean("右边图标", 2007));
            mData.add(new TitleBarBean("右边弹窗菜单", 2008));
            mData.add(new TitleBarBean("进入动画", 2009));
            mData.add(new TitleBarBean("出场动画", 2010));
            mData.add(new TitleBarBean("左侧点击返回", 2011));

            mData.add(new TitleBarBean("自定义状态栏", 3000));
            mData.add(new TitleBarBean("显示状态栏", 3001));
            mData.add(new TitleBarBean("背景颜色", 3002));
            mData.add(new TitleBarBean("背景图", 3003));
            mData.add(new TitleBarBean("隐藏状态栏", 3004));

            mData.add(new TitleBarBean("精彩案例", 4000));
            mData.add(new TitleBarBean("全屏沉浸式", 4001));
        }
        return mData;
    }

    @Override
    public void findViews() {

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
