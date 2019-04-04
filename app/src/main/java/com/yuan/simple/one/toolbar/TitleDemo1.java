package com.yuan.simple.one.toolbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.yuan.base.tools.glide.GlideUtil;
import com.yuan.base.tools.layout.Views;
import com.yuan.base.ui.activity.MvpActivity;
import com.yuan.base.ui.extra.ISwipeBack;
import com.yuan.base.widget.title.statusbar.StatusUtil;
import com.yuan.simple.R;

public class TitleDemo1 extends MvpActivity implements ISwipeBack {

    @Override
    public int getLayoutId() {
        return R.layout.act_ttile_demo1;
    }

    @Override
    public void findViews() {

    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void initData() {
        StatusUtil.setFloat(mContext);
        StatusUtil.setTransparent(mContext);
        ImageView imageView = Views.find(mContext, R.id.iv_background);
        GlideUtil.load("http://static.oneplus.cn/data/attachment/forum/201701/05/165732nmebemxb1my1e08e.jpg", imageView);
    }

    @Override
    public void setListener() {

    }
}
