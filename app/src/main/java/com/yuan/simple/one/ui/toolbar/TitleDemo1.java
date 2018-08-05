package com.yuan.simple.one.ui.toolbar;

import android.os.Bundle;
import android.widget.ImageView;

import com.yuan.base.tools.glide.GlideHelper;
import com.yuan.base.tools.other.Views;
import com.yuan.base.ui.extra.ISwipeBack;
import com.yuan.base.ui.mvp.MvpActivity;
import com.yuan.base.widget.title.statusbar.StatusUtil;
import com.yuan.simple.R;

public class TitleDemo1 extends MvpActivity implements ISwipeBack {

    @Override
    public int getLayoutId() {
        return R.layout.act_ttile_demo1;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusUtil.setFloat(mContext);
        StatusUtil.setTransparent(mContext);
        ImageView imageView = Views.find(mContext, R.id.iv_background);
        GlideHelper.load("http://static.oneplus.cn/data/attachment/forum/201701/05/165732nmebemxb1my1e08e.jpg", imageView);
    }
}
