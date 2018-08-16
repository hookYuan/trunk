package com.yuan.base.widget.input;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

/**
 * Created by YuanYe on 2018/8/11.
 * 所有当键盘弹出或隐藏时需要滑动的布局，都可以放入InputScrollView之中
 * InputScrollView需要有效，需要在Manifest中的对应Activity设置以下参数:
 * android:windowSoftInputMode="adjustResize"
 */
public class InputScrollView extends ScrollView {

    private static final String TAG = "InputScrollView";
    private boolean isScroll = false; //是否有手势触摸

    public InputScrollView(Context context) {
        super(context);
    }

    public InputScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getEditext(this);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    private void getEditext(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof EditText) { // 若是Button记录下
                Log.i(TAG, "取到一个");
            } else if (view instanceof ViewGroup) {
                // 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
                this.getEditext((ViewGroup) view);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
