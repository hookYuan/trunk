package com.yuan.base.widget.title;

import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;

/**
 * Created by YuanYe on 2017/9/21.
 *
 */
public interface OnMenuItemClickListener {
    void onItemClick(PopupWindow popupWindow, AdapterView<?> adapterView, View view, int i);
}
