package com.yuan.simple.two.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public class MyManager extends ViewGroup {

    public MyManager(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

        }
    }
}
