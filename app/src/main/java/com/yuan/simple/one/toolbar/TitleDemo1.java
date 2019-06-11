package com.yuan.simple.one.toolbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.yuan.kernel.mvp.BaseActivity;
import com.yuan.kernel.Views;
import com.yuan.simple.R;
import com.yuan.tools_extra.GlideUtil;
import com.yuan.widget.title.StatusUtil;

public class TitleDemo1 extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.act_title_demo1;
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
