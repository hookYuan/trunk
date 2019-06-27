package com.yuan.kernel.dialog;

import android.app.Dialog;
import android.view.Window;

public interface IDialog {

    /**
     * 创建Dialog
     *
     * @return
     */
    Dialog createDialog();

    /**
     * 设置Dialog背景
     *
     * @return
     */
    Window getWindow();

}
