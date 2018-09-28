package com.yuan.base.widget.title.statusbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import com.yuan.base.tools.glide.GlideHelper;

/**
 * Created by YuanYe on 2018/8/4.
 * 模拟的一条状态栏
 */
public class StatusBar extends AppCompatImageView {

    private final static String TAG = "StatueBar";

    private Context context;

    public StatusBar(Context context) {
        super(context);
        this.context = context;
        this.setScaleType(ScaleType.CENTER_CROP);
        init();
    }

    public StatusBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setScaleType(ScaleType.CENTER_CROP);
        init();
    }

    private void init() {
        this.setVisibility(GONE);
    }


    /*
     * *********************设置背景颜色、背景图片***********************************************************
     */
    /**
     * 设置Title的背景图片
     * 替换系统的设置背景图，采用Glide加载，不会使图片变形
     *
     * @return
     */
    public void setBackgroundImg(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl))
            GlideHelper.load(imgUrl, this);
    }

    @Override
    public void setBackgroundResource(int resid) {
        GlideHelper.load(resid, this);
    }

    @Override
    public void setBackgroundColor(int color) {
        Bitmap bitmap = Bitmap.createBitmap(10, 10,
                Bitmap.Config.ARGB_4444);
        bitmap.eraseColor(color);//填充颜色
        this.setImageBitmap(bitmap);
    }
}
