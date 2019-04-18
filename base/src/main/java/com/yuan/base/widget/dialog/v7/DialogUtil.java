package com.yuan.base.widget.dialog.v7;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yuan.base.R;

import java.lang.reflect.Field;

/**
 * 描述：对v7包下的AlertDialog进行简单封装，方便使用
 * 整体风格采用Material原生风格
 *
 * @author yuanye
 * @date 2019/4/4 16:29
 */
public class DialogUtil {

//    private final static String TAG = "DialogUtil";
//
//    /**
//     * v7包下dialog 构造器
//     */
//    private AlertDialog.Builder builder;
//    /**
//     * app包下dialog
//     */
//    private android.app.AlertDialog appAlertDialog;
//
//    private Context mContext;
//
//    private DialogHelperParams diaLogParams;
//
//    private static DialogUtil util;
//
//    public static DialogUtil create() {
//        if (util == null) {
//            util = new DialogUtil();
//        }
//        return util;
//    }
//
//    public DialogUtil(Context context) {
//        diaLogParams = new DialogHelperParams.Builder()
//                .build();
//        this.mContext = context;
//    }
//
//    public DialogUtil(Context context, DialogHelperParams diaLogParams) {
//        this.mContext = context;
//        this.diaLogParams = diaLogParams;
//    }
//
//    /*
//     * ************************文本Dialog*****************************************************************
//     */
//
//    /**
//     * @param title            标题
//     * @param message          正文
//     * @param positveText      右侧按钮
//     * @param neutralText      中间按钮
//     * @param negativeText     左侧按钮
//     * @param positiveListener
//     * @param neutralListener
//     * @param negativeListener
//     * @param isCancel         是否点击外部可以取消  false--不可取消
//     */
//    public void alertText(String title, String message
//            , String positveText, String neutralText, String negativeText
//            , DialogInterface.OnClickListener positiveListener
//            , DialogInterface.OnClickListener neutralListener
//            , DialogInterface.OnClickListener negativeListener
//            , boolean isCancel) {
//        builder = new AlertDialog.Builder(mContext, themeResId);
//        //可以通过R.style.MaterialDialog修改Dialog颜色等
//        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
//        if (!TextUtils.isEmpty(message)) builder.setMessage(message);
//        if (!TextUtils.isEmpty(positveText))
//            builder.setPositiveButton(positveText, positiveListener);
//        if (!TextUtils.isEmpty(neutralText))
//            builder.setNeutralButton(neutralText, neutralListener);
//        if (!TextUtils.isEmpty(negativeText))
//            builder.setNegativeButton(negativeText, negativeListener);
//        builder.setCancelable(isCancel);
//        // 显示
//        show();
//    }
//
//
//    /**
//     * 关闭弹窗
//     */
//    public void dismiss() {
//        if (alertDialog != null) {
//            alertDialog.dismiss();
//        }
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }
//
//
//    /**
//     * 全局统一设置显示，可以控制dialog显示位置
//     */
//    private void show(DialogHelperParams params, Object dialog) {
//        if (dialog instanceof AlertDialog.Builder) {
//            AlertDialog alertDialog = ((AlertDialog.Builder) dialog).show();
//            initParams(params, alertDialog.getWindow(), alertDialog);
//        } else if (dialog instanceof android.app.AlertDialog) {
//            ((android.app.AlertDialog) dialog).show();
//            initParams(params, (Window) dialog);
//        }
//    }
//
//    /**
//     * 设置弹窗窗体界面
//     *
//     * @param diaLogParams
//     * @param window
//     */
//    private void initParams(DialogHelperParams diaLogParams, Window window) {
//        initParams(diaLogParams, window);
//    }
//
//    /**
//     * 设置弹窗窗体界面
//     *
//     * @param diaLogParams
//     * @param window
//     * @param object
//     */
//    private void initParams(DialogHelperParams diaLogParams, Window window, AlertDialog object) {
//        //设置背景颜色，通常为透明
//        if (diaLogParams.getWindowBackground() != null) {
//            window.setBackgroundDrawable(diaLogParams.getWindowBackground());
//        }
//        //设置Dialog相对于屏幕的位置
//        window.setGravity(diaLogParams.getGravity());
//        //设置padding
//        int paddingLeft = diaLogParams.getPaddingLeft() != -1 ? diaLogParams.getPaddingLeft()
//                : window.getDecorView().getPaddingLeft();
//        int paddingTop = diaLogParams.getPaddingTop() != -1 ? diaLogParams.getPaddingTop()
//                : window.getDecorView().getPaddingTop();
//        int paddingRight = diaLogParams.getPaddingRight() != -1 ? diaLogParams.getPaddingRight()
//                : window.getDecorView().getPaddingRight();
//        int paddingBottom = diaLogParams.getPaddingBottom() != -1 ? diaLogParams.getPaddingBottom()
//                : window.getDecorView().getPaddingBottom();
//        window.getDecorView().setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
//
//        WindowManager.LayoutParams windowParams = window.getAttributes();
//        //最大高度
//        if (diaLogParams.isMatchHeight()) {
//            windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//        } else if (diaLogParams.getHeight() > 0) {
//            windowParams.height = diaLogParams.getHeight();
//        }
//
//        //最大宽度
//        if (diaLogParams.isMatchWidth()) {
//            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        } else if (diaLogParams.getWidth() > 0) {
//            windowParams.width = diaLogParams.getWidth();
//        }
//
//        /*实例化Window*/
//        windowParams.x = diaLogParams.getPosX();
//        windowParams.y = diaLogParams.getPosY();
//
//        //弹窗布局的alpha值  1.0表示完全不透明，0.0表示没有变暗。
//        windowParams.alpha = diaLogParams.getDialogFrontAlpha();
//        // 当FLAG_DIM_BEHIND设置后生效。该变量指示后面的窗口变暗的程度。1.0表示完全不透明，0.0表示没有变暗。
//        windowParams.dimAmount = diaLogParams.getDialogBehindAlpha();
//        //屏幕亮度 用来覆盖用户设置的屏幕亮度。表示应用用户设置的屏幕亮度。从0到1调整亮度从暗到最亮发生变化。
//        //layoutParams.screenBrightness = 0.7f;
//        window.setAttributes(windowParams);
//        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//
//        //通过反射设置字体颜色及大小
//        Field mAlert = null;
//        try {
//            if (object != null && object instanceof AlertDialog) {
//                mAlert = AlertDialog.class.getDeclaredField("mAlert");
//                mAlert.setAccessible(true);
//                Object mAlertController = null;
//                mAlertController = mAlert.get(object);
//                Field mTitle = mAlertController.getClass().getDeclaredField("mTitleView");
//                Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
//                Field mPositive = mAlertController.getClass().getDeclaredField("mButtonPositive");
//                Field mNegative = mAlertController.getClass().getDeclaredField("mButtonNegative");
//                mTitle.setAccessible(true);
//                mMessage.setAccessible(true);
//                mPositive.setAccessible(true);
//                mNegative.setAccessible(true);
//
//                if (mTitle != null) {
//                    TextView titleView = (TextView) mTitle.get(mAlertController);
//                    if (diaLogParams.getTitleColor() != 0)
//                        titleView.setTextColor(diaLogParams.getTitleColor());
//                    if (diaLogParams.getTitleSize() != 0)
//                        titleView.setTextSize(diaLogParams.getTitleSize());
//                }
//
//                if (mMessage != null) {
//                    TextView tvMessage = (TextView) mMessage.get(mAlertController);
//                    if (diaLogParams.getContentColor() != 0)
//                        tvMessage.setTextColor(diaLogParams.getContentColor());
//                    if (diaLogParams.getContentSize() != 0)
//                        tvMessage.setTextSize(diaLogParams.getContentSize());
//                }
//
//                if (mPositive != null) {
//                    Button btnPositive = (Button) mPositive.get(mAlertController);
//                    if (diaLogParams.getPositiveColor() != 0)
//                        btnPositive.setTextColor(diaLogParams.getPositiveColor());
//                    if (diaLogParams.getPositiveSize() != 0)
//                        btnPositive.setTextSize(diaLogParams.getPositiveSize());
//                }
//                if (mNegative != null) {
//                    Button btnNegative = (Button) mNegative.get(mAlertController);
//                    if (diaLogParams.getNegativeColor() != 0)
//                        btnNegative.setTextColor(diaLogParams.getNegativeColor());
//                    if (diaLogParams.getNegativeSize() != 0)
//                        btnNegative.setTextSize(diaLogParams.getNegativeSize());
//                }
//            }
//        } catch (NoSuchFieldException e) {
//            Log.e(TAG, "设置失败：" + e.getMessage());
//        } catch (IllegalAccessException e) {
//            Log.e(TAG, "设置失败：" + e.getMessage());
//        }
//    }
}
