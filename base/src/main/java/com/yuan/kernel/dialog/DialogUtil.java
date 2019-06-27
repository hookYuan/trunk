package com.yuan.kernel.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：对v7包下的AlertDialog进行简单封装，方便使用
 * 整体风格采用Material原生风格
 *
 * @author yuanye
 * @modify 2019/6/27 解决Context容易引起的内存泄露,使用单例节约内存
 * @date 2019/4/4 16:29
 */
public class DialogUtil {

    private final static String TAG = "DialogUtil";
    /**
     * 默认文字
     */
    private final static String POSITIVE_TEXT = "确定";
    private final static String NEGATIVE_TEXT = "取消";
    private final static String TITLE_TEXT = "提示";
    /**
     * Dialog
     */
    private WeakReference<Dialog> mDialog;
    /**
     * 上下文
     */
    private WeakReference<Context> mContext;
    /**
     * 主题样式
     */
    private int themeResId = 0;
    /**
     * 配置参数
     */
    private Params mParams;

    private static class DialogUtilInstance {
        private static DialogUtil util = new DialogUtil();
    }

    public static DialogUtil create(Context context, Params params) {
        DialogUtilInstance.util.init(context, params);
        return DialogUtilInstance.util;
    }

    public static DialogUtil create(Context context) {
        DialogUtilInstance.util.init(context, null);
        return DialogUtilInstance.util;
    }

    private DialogUtil() {
        Log.i(TAG, "初始化");
    }

    /**
     * 初始化
     *
     * @param context
     * @param params
     */
    private void init(Context context, Params params) {
        //释放Context
        releaseContext();
        mContext = new WeakReference<>(context);
        if (params == null) mParams = new Params.Builder().build();
        else mParams = params;
    }

    /**
     * 全局设置Dialog属性，全局生效
     *
     * @param params
     */
    public static void setParams(Params params) {
        if (params != null) {
            DialogUtilInstance.util.mParams = params;
        }
    }

    /**
     * 设置当前进度
     *
     * @param current
     */
    public static void setProgressCurrent(int current) {
        if (DialogUtilInstance.util.mDialog != null &&
                DialogUtilInstance.util.mDialog.get() != null &&
                DialogUtilInstance.util.mDialog.get() instanceof ProgressDialog) {
            ProgressDialog dialog = (ProgressDialog) DialogUtilInstance.util.mDialog.get();
            dialog.setProgress(current);
        } else {
            Log.e(TAG, "设置进度失败");
        }
    }

    /**
     * 关闭弹窗,释放资源
     * <p>
     */
    public static void destroy() {
        releaseDialog();
        releaseContext();
    }

    /**
     * 隐藏弹窗
     * 和show配合使用，不用频繁创建Dialog
     *
     * @return 是否隐藏成功
     */
    public static boolean dismiss() {
        if (DialogUtilInstance.util.mDialog != null &&
                DialogUtilInstance.util.mDialog.get() != null) {
            DialogUtilInstance.util.mDialog.get().dismiss();
            return true;
        }
        return false;
    }

    /**
     * 显示弹窗
     * 显示最近一次创建的并且没有被销毁的Dialog
     *
     * @return 是否显示成功
     */
    public static boolean show() {
        if (DialogUtilInstance.util.mDialog != null && DialogUtilInstance.util.mDialog.get() != null) {
            DialogUtilInstance.util.mDialog.get().show();
            return true;
        }
        return false;
    }

    /**
     * ************************文本Dialog*****************************************************************
     */

    /**
     * @param title            标题
     * @param message          正文
     * @param POSITIVE_TEXT    右侧按钮
     * @param neutralText      中间按钮
     * @param NEGATIVE_TEXT    左侧按钮
     * @param positiveListener
     * @param neutralListener
     * @param negativeListener
     * @param isCancel         是否点击外部可以取消  false--不可取消
     */
    public void alertText(String title, String message
            , String POSITIVE_TEXT, String neutralText, String NEGATIVE_TEXT
            , DialogInterface.OnClickListener positiveListener
            , DialogInterface.OnClickListener neutralListener
            , DialogInterface.OnClickListener negativeListener
            , boolean isCancel) {
        checkContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext.get(),
                themeResId);
        //可以通过R.style.MaterialDialog修改Dialog颜色等
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        if (!TextUtils.isEmpty(message)) builder.setMessage(message);
        if (!TextUtils.isEmpty(POSITIVE_TEXT))
            builder.setPositiveButton(POSITIVE_TEXT, positiveListener);
        if (!TextUtils.isEmpty(neutralText))
            builder.setNeutralButton(neutralText, neutralListener);
        if (!TextUtils.isEmpty(NEGATIVE_TEXT))
            builder.setNegativeButton(NEGATIVE_TEXT, negativeListener);
        builder.setCancelable(isCancel);
        // 显示
        mDialog = new WeakReference(builder.create());
        initWindow(mParams, mDialog.get().getWindow());
        show();
    }

    public void alertText(String message, boolean isCancel, DialogInterface.OnClickListener positiveListener) {
        alertText(TITLE_TEXT, message, POSITIVE_TEXT, "", NEGATIVE_TEXT, positiveListener, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, isCancel);
    }

    public void alertText(String message, DialogInterface.OnClickListener positiveListener) {
        alertText(message, false, positiveListener);
    }


    public void alertText(String title, String message, DialogInterface.OnClickListener positiveListener) {
        alertText(title, message, POSITIVE_TEXT, "", "", positiveListener, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, false);
    }

    public void alertText(String title, String message, DialogInterface.OnClickListener positiveListener, boolean isCancel) {
        alertText(title, message, POSITIVE_TEXT, "", "", positiveListener, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, isCancel);
    }

    public void alertText(String message, boolean isCancel) {
        alertText(TITLE_TEXT, message, POSITIVE_TEXT, "", "", null, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, isCancel);
    }

    public void alertText(String message) {
        alertText(TITLE_TEXT, message, POSITIVE_TEXT, "", "", null, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, false);
    }

    /**
     * ************************列表Dialog*****************************************************************
     */
    public void alertList(String title, String[] mData, boolean isCancel, DialogInterface.OnClickListener listener) {
        checkContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext.get(), themeResId);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        builder.setItems(mData, listener);
        builder.setCancelable(isCancel);
        // 显示
        mDialog = new WeakReference(builder.create());
        initWindow(mParams, mDialog.get().getWindow());
        show();
    }

    public void alertList(String title, List<String> mData, boolean isCancel, DialogInterface.OnClickListener listener) {
        int size = mData.size();
        String[] array = mData.toArray(new String[size]);
        alertList(title, array, isCancel, listener);
    }

    public void alertList(String title, List<String> mData, DialogInterface.OnClickListener listener) {
        alertList(title, mData, true, listener);
    }

    public void alertList(String title, String[] mData, DialogInterface.OnClickListener listener) {
        alertList(title, mData, true, listener);
    }

    public void alertList(String[] mData, DialogInterface.OnClickListener listener) {
        alertList("", mData, true, listener);
    }

    /**
     * ************************单选Dialog*****************************************************************
     */

    /**
     * 默认位置
     */
    private int defPos = 0;

    /**
     * @param title         标题
     * @param mData         数据源
     * @param POSITIVE_TEXT 确定按钮
     * @param defPosition   默认选中位置
     * @param isCancel      是否点击外部取消
     * @param listener      监听
     */
    public void alertSingle(String title, String[] mData, final String POSITIVE_TEXT, int defPosition, boolean isCancel, final DialogInterface.OnClickListener listener) {
        checkContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext.get(), themeResId);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        defPos = defPosition;
        //点击Item按钮
        builder.setSingleChoiceItems(mData, defPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                defPos = which;
            }
        });
        //点击确定按钮
        if (!TextUtils.isEmpty(POSITIVE_TEXT)) {
            builder.setPositiveButton(POSITIVE_TEXT, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onClick(dialog, defPos);
                }
            });
        }
        builder.setCancelable(isCancel);
        // 显示
        mDialog = new WeakReference(builder.create());
        initWindow(mParams, mDialog.get().getWindow());
        show();
    }

    public void alertSingle(String title, List<String> mData, int defPosition, final DialogInterface.OnClickListener listener) {
        alertSingle(title, mData, POSITIVE_TEXT, defPosition, listener);
    }

    public void alertSingle(String title, List<String> mData, final String POSITIVE_TEXT, int defPosition, final DialogInterface.OnClickListener listener) {
        int size = mData.size();
        String[] array = mData.toArray(new String[size]);
        alertSingle(title, array, POSITIVE_TEXT, defPosition, true, listener);
    }

    public void alertSingle(String title, String[] mData, int defPosition, final DialogInterface.OnClickListener listener) {
        alertSingle(title, mData, POSITIVE_TEXT, defPosition, true, listener);
    }

    /**
     * ************************多选Dialog*****************************************************************
     */

    /**
     * @param title            标题
     * @param data             数据项
     * @param isCancel         是否点击外部取消
     * @param positiveListener 选中数据回调
     */
    public <T extends MultiItem> void alertMulti(String title, final List<T> data,
                                                 boolean isCancel,
                                                 final OnMultiListener positiveListener) {
        checkContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext.get(), themeResId);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        //解析数据源
        String[] strData = new String[data.size()];
        boolean[] choiceItem = new boolean[data.size()];
        int i = 0;
        for (T bean : data) {
            strData[i] = bean.getText();
            choiceItem[i] = bean.isSelect();
            bean.setPosition(i);
            i++;
        }
        builder.setMultiChoiceItems(strData, choiceItem,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            data.get(which).setSelect(true);
                        } else {
                            data.get(which).setSelect(false);
                        }
                    }
                });
        builder.setPositiveButton(POSITIVE_TEXT,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<T> list = new ArrayList<>();
                        for (T bean : data) {
                            if (bean.isSelect()) {
                                list.add(bean);
                            }
                        }
                        positiveListener.onClick(dialog, list);
                    }
                });
        builder.setCancelable(isCancel);
        // 显示
        mDialog = new WeakReference(builder.create());
        initWindow(mParams, mDialog.get().getWindow());
        show();
    }

    public <T extends MultiItem> void alertMulti(String title, T[] mData, OnMultiListener listener) {
        List<T> list = Arrays.asList(mData);
        alertMulti(title, list, true, listener);
    }

    public <T extends MultiItem> void alertMulti(String title, List<T> mData, OnMultiListener listener) {
        alertMulti(title, mData, true, listener);
    }

    /**
     * ************************等待Dialog*****************************************************************
     */
    /**
     * 未知进度Dialog
     *
     * @param title    标题
     * @param message  提示文字
     * @param isCancel 是否取消
     */
    public void alertWait(String title, String message, boolean isCancel) {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
        checkContext();
        mDialog = new WeakReference(new ProgressDialog(mContext.get(), themeResId));
        if (!TextUtils.isEmpty(title)) mDialog.get().setTitle(title);
        if (mDialog.get() instanceof ProgressDialog) {
            ProgressDialog dialog = (ProgressDialog) mDialog.get();
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            if (!TextUtils.isEmpty(message)) dialog.setMessage(message);
        }
        mDialog.get().setCancelable(isCancel);
        // 显示
        initWindow(mParams, mDialog.get().getWindow());
        show();
    }

    /**
     * @param title   标题
     * @param message 提示文字
     */
    public void alertWait(String title, String message) {
        alertWait(title, message, true);
    }

    /**
     * @param message 提示文字
     */
    public void alertWait(String message) {
        alertWait(TITLE_TEXT, message, true);
    }

    /**
     * ************************进度条Dialog*****************************************************************
     */
    public void alertProgress(String title, int max) {
        alertProgress(title, max, 0, true);
    }

    public void alertProgress(String title, int max, int current, boolean isCancel) {
        checkContext();
        mDialog = new WeakReference(new ProgressDialog(mContext.get(), themeResId));
        mDialog.get().setTitle(title);
        if (mDialog.get() instanceof ProgressDialog) {
            ProgressDialog dialog = (ProgressDialog) mDialog.get();
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(current);
            dialog.setMax(max);
        }
        mDialog.get().setCancelable(isCancel);
        // 显示
        initWindow(mParams, mDialog.get().getWindow());
        show();
    }

    /**
     * ************************自定义Dialog*****************************************************************
     */

    /**
     * 自定布局Dialog
     *
     * @param title
     * @param view
     * @param isCancel
     */
    public void alertView(final String title, View view, boolean isCancel) {
        checkContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext.get(), themeResId);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        builder.setView(view);
        builder.setCancelable(isCancel);
        mDialog = new WeakReference(builder.create());
        initWindow(mParams, mDialog.get().getWindow());
        show();
    }

    public void alertView(View view) {
        alertView("", view, true);
    }

    /**
     * ************************日期选择Dialog*****************************************************************
     */
    /**
     * @param year       年
     * @param month      月
     * @param dayOfMonth 日
     * @param isCancel   是否取消
     * @param listener   回调
     */
    public void alertDate(int year, int month, int dayOfMonth, boolean isCancel, DatePickerDialog.OnDateSetListener listener) {
        checkContext();
        mDialog = new WeakReference(new DatePickerDialog(mContext.get(), themeResId,
                listener, year, month, dayOfMonth));
        if (mDialog.get() instanceof DatePickerDialog) {
            DatePickerDialog dialog = (DatePickerDialog) mDialog.get();
            dialog.updateDate(year, month, dayOfMonth);
            dialog.setCancelable(isCancel);
        }
        initWindow(mParams, mDialog.get().getWindow());
        show();
    }


    public void alertDate(int year, int month, int dayOfMonth, DatePickerDialog.OnDateSetListener listener) {
        alertDate(year, month, dayOfMonth, true, listener);
    }

    /**
     * ************************时间选择Dialog*****************************************************************
     */

    /**
     * @param hourOfDay    小时
     * @param minute       分钟
     * @param is24HourView 是否24小时制
     * @param isCancel     是否取消
     * @param listener     回调
     */
    public void alertTime(int hourOfDay, int minute, boolean is24HourView,
                          boolean isCancel, TimePickerDialog.OnTimeSetListener listener) {
        checkContext();
        mDialog = new WeakReference(new TimePickerDialog(mContext.get(), themeResId, listener,
                hourOfDay, minute, is24HourView));
        if (mDialog.get() instanceof TimePickerDialog) {
            TimePickerDialog dialog = (TimePickerDialog) mDialog.get();
            dialog.updateTime(hourOfDay, minute);
            dialog.setCancelable(isCancel);
        }
        initWindow(mParams, mDialog.get().getWindow());
        show();
    }

    public void alertTime(int hourOfDay, int minute, TimePickerDialog.OnTimeSetListener listener) {
        alertTime(hourOfDay, minute, true, true, listener);
    }

    /**
     * 释放上下文
     */
    private static void releaseContext() {
        if (DialogUtilInstance.util.mContext != null &&
                DialogUtilInstance.util.mContext.get() != null) {
            DialogUtilInstance.util.mContext.clear();
            DialogUtilInstance.util.mContext = null;
        }
    }

    /**
     * 释放Dialog
     */
    private static void releaseDialog() {
        if (DialogUtilInstance.util.mDialog != null &&
                DialogUtilInstance.util.mDialog.get() != null) {
            DialogUtilInstance.util.mDialog.get().dismiss();
            DialogUtilInstance.util.mDialog.clear();
            DialogUtilInstance.util.mDialog = null;
        }
    }

    /**
     * 创建Dialog时，校验上下文
     */
    private void checkContext() {
        if (mContext.get() == null) {
            Log.e(TAG, "请检查Context是否已经被回收");
            return;
        }
        //释放资源
        releaseDialog();
    }

    /**
     * 设置Dialog窗口属性
     * #background
     * #gravity
     * #padding
     * #height
     * #width
     * #x,y
     * #alpha
     *
     * @param params
     * @param window
     */
    private void initWindow(Params params, Window window) {
        //设置背景颜色，通常为透明
        if (params.getWindowBackground() != null) {
            window.setBackgroundDrawable(params.getWindowBackground());
        }
        //设置Dialog相对于屏幕的位置
        window.setGravity(params.getGravity());
        //设置padding
        int paddingLeft = params.getPaddingLeft() != -1 ? params.getPaddingLeft()
                : window.getDecorView().getPaddingLeft();
        int paddingTop = params.getPaddingTop() != -1 ? params.getPaddingTop()
                : window.getDecorView().getPaddingTop();
        int paddingRight = params.getPaddingRight() != -1 ? params.getPaddingRight()
                : window.getDecorView().getPaddingRight();
        int paddingBottom = params.getPaddingBottom() != -1 ? params.getPaddingBottom()
                : window.getDecorView().getPaddingBottom();
        window.getDecorView().setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        WindowManager.LayoutParams windowParams = window.getAttributes();
        //最大高度
        if (params.isMatchHeight()) {
            windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else if (params.getHeight() > 0) {
            windowParams.height = params.getHeight();
        }
        //最大宽度
        if (params.isMatchWidth()) {
            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        } else if (params.getWidth() > 0) {
            windowParams.width = params.getWidth();
        }
        /*实例化Window*/
        windowParams.x = params.getPosX();
        windowParams.y = params.getPosY();
        //弹窗布局的alpha值  1.0表示完全不透明，0.0表示没有变暗。
        windowParams.alpha = params.getDialogFrontAlpha();
        // 当FLAG_DIM_BEHIND设置后生效。该变量指示后面的窗口变暗的程度。1.0表示完全不透明，0.0表示没有变暗。
        windowParams.dimAmount = params.getDialogBehindAlpha();
        //设置Window的进出场动画
//        windowParams.windowAnimations =
        window.setAttributes(windowParams);
        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //反射设置AlertDialog属性
        if (mDialog.get() instanceof AlertDialog) {
            reflexAlert((AlertDialog) mDialog.get(), params, true);
        }
    }


    /**
     * 通过反射更改v7包下AlertDialog按钮/字体颜色大小
     * #不建议使用#
     *
     * @param alertDialog
     * @param params
     * @param isAvailable true-使用改方法，false-不使用
     */
    private void reflexAlert(AlertDialog alertDialog, Params params, boolean isAvailable) {
        if (alertDialog == null || params == null || !isAvailable) return;
        //通过反射设置字体颜色及大小
        Field mAlert = null;
        try {
            mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = null;
            mAlertController = mAlert.get(alertDialog);
            Field mTitle = mAlertController.getClass().getDeclaredField("mTitleView");
            Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
            Field mPositive = mAlertController.getClass().getDeclaredField("mButtonPositive");
            Field mNegative = mAlertController.getClass().getDeclaredField("mButtonNegative");
            mTitle.setAccessible(true);
            mMessage.setAccessible(true);
            mPositive.setAccessible(true);
            mNegative.setAccessible(true);
            if (mTitle != null) {
                TextView titleView = (TextView) mTitle.get(mAlertController);
                if (params.getTitleColor() != 0)
                    titleView.setTextColor(params.getTitleColor());
                if (params.getTitleSize() != 0)
                    titleView.setTextSize(params.getTitleSize());
            }

            if (mMessage != null) {
                TextView tvMessage = (TextView) mMessage.get(mAlertController);
                if (params.getContentColor() != 0)
                    tvMessage.setTextColor(params.getContentColor());
                if (params.getContentSize() != 0)
                    tvMessage.setTextSize(params.getContentSize());
            }

            if (mPositive != null) {
                Button btnPositive = (Button) mPositive.get(mAlertController);
                if (params.getPositiveColor() != 0)
                    btnPositive.setTextColor(params.getPositiveColor());
                if (params.getPositiveSize() != 0)
                    btnPositive.setTextSize(params.getPositiveSize());
            }
            if (mNegative != null) {
                Button btnNegative = (Button) mNegative.get(mAlertController);
                if (params.getNegativeColor() != 0)
                    btnNegative.setTextColor(params.getNegativeColor());
                if (params.getNegativeSize() != 0)
                    btnNegative.setTextSize(params.getNegativeSize());
            }
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "设置失败：" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, "设置失败：" + e.getMessage());
        }
    }

    /**
     * Created by YuanYe on 2018/1/15.
     * AlertDialog 简单统一配置文件
     */

    public static class Params {

        private int gravity;
        private Drawable windowBackground;

        private float dialogBehindAlpha;
        private float dialogFrontAlpha;

        private boolean matchWidth;
        private boolean matchHeight;

        private int paddingLeft;
        private int paddingRight;
        private int paddingTop;
        private int paddingBottom;

        private int width;
        private int height;
        private int posX;
        private int posY;

        private @ColorInt
        int titleColor;
        private int titleSize;

        private @ColorInt
        int contentColor;
        private int contentSize;

        private @ColorInt
        int positiveColor;
        private int positiveSize;

        private @ColorInt
        int negativeColor;
        private int negativeSize;

        private Params(Params.Builder builder) {
            gravity = builder.gravity;

            windowBackground = builder.windowBackground;
            dialogBehindAlpha = builder.dialogBehindAlpha;
            dialogFrontAlpha = builder.dialogFrontAlpha;

            matchWidth = builder.matchWidth;
            matchHeight = builder.matchHeight;

            paddingLeft = builder.paddingLeft;
            paddingRight = builder.paddingRight;
            paddingTop = builder.paddingTop;
            paddingBottom = builder.paddingBottom;

            width = builder.width;
            height = builder.height;

            posX = builder.posX;
            posY = builder.posY;

            titleColor = builder.titleColor;
            titleSize = builder.titleSize;

            contentColor = builder.contentColor;
            contentSize = builder.contentSize;

            positiveColor = builder.positiveColor;
            positiveSize = builder.positiveSize;

            negativeColor = builder.negativeColor;
            negativeSize = builder.negativeSize;
        }

        public int getGravity() {
            return gravity;
        }

        public Drawable getWindowBackground() {
            return windowBackground;
        }

        public float getDialogBehindAlpha() {
            return dialogBehindAlpha;
        }

        public float getDialogFrontAlpha() {
            return dialogFrontAlpha;
        }

        public boolean isMatchWidth() {
            return matchWidth;
        }

        public boolean isMatchHeight() {
            return matchHeight;
        }

        public int getPaddingLeft() {
            return paddingLeft;
        }

        public int getPaddingRight() {
            return paddingRight;
        }

        public int getPaddingTop() {
            return paddingTop;
        }

        public int getPaddingBottom() {
            return paddingBottom;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getPosX() {
            return posX;
        }

        public int getPosY() {
            return posY;
        }

        public int getTitleColor() {
            return titleColor;
        }

        public int getTitleSize() {
            return titleSize;
        }

        public int getContentColor() {
            return contentColor;
        }

        public int getContentSize() {
            return contentSize;
        }

        public int getPositiveColor() {
            return positiveColor;
        }

        public int getPositiveSize() {
            return positiveSize;
        }

        public int getNegativeColor() {
            return negativeColor;
        }

        public int getNegativeSize() {
            return negativeSize;
        }

        public static final class Builder {

            private int gravity = Gravity.CENTER;//相对位置

            private GradientDrawable windowBackground;//弹窗白色部分背景

            private float dialogBehindAlpha = 0.5f;//弹窗整个界面黑色背景透明度，范围0.0-1：透明-不透明
            private float dialogFrontAlpha = 1f;//弹窗白色部分背景透明度，范围0.0-1：透明-不透明

            private boolean matchWidth = false; //最大宽度
            private boolean matchHeight = false;//最大高度

            private int paddingLeft = -1;
            private int paddingRight = -1;
            private int paddingTop = -1;
            private int paddingBottom = -1;

            private int width = 0; //指定宽度
            private int height = 0; //指定高度

            private int posX = 0;//Dialog左上角定点横坐标
            private int posY = 0;//Dialog左上角定点纵坐标(坐标位置与gravity有关)

            private @ColorInt
            int titleColor = 0; //DialogTitle的字体颜色
            private int titleSize = 0; //Title的字体大小

            private @ColorInt
            int contentColor = 0; //内容的字体颜色
            private int contentSize = 0; //内容的字体大小

            private @ColorInt
            int positiveColor = 0; //确定按钮颜色
            private int positiveSize = 0; //确定按钮字体大小

            private @ColorInt
            int negativeColor = 0; //取消按钮颜色
            private int negativeSize = 0; //取消按钮字体大小

            public Builder() {
            }

            public Params.Builder gravity(int val) {
                gravity = val;
                return this;
            }

            public Params.Builder windowBackground(GradientDrawable val) {
                windowBackground = val;
                return this;
            }

            public Params.Builder windowBackground(@ColorInt int val) {
                windowBackground = new GradientDrawable();
                windowBackground.setColor(val);
                return this;
            }

            public Params.Builder dialogBehindAlpha(float val) {
                dialogBehindAlpha = val;
                return this;
            }

            public Params.Builder dialogFrontAlpha(float val) {
                dialogFrontAlpha = val;
                return this;
            }

            public Params.Builder matchWidth(boolean val) {
                matchWidth = val;
                return this;
            }

            public Params.Builder matchHeight(boolean val) {
                matchHeight = val;
                return this;
            }

            public Params.Builder paddingTop(int val) {
                paddingTop = val;
                return this;
            }

            public Params.Builder paddingRight(int val) {
                paddingRight = val;
                return this;
            }

            public Params.Builder paddingLeft(int val) {
                paddingLeft = val;
                return this;
            }

            public Params.Builder paddingBottom(int val) {
                paddingBottom = val;
                return this;
            }

            public Params.Builder width(int val) {
                width = val;
                return this;
            }

            public Params.Builder height(int val) {
                height = val;
                return this;
            }

            public Params.Builder posX(int val) {
                posX = val;
                return this;
            }

            public Params.Builder posY(int val) {
                posY = val;
                return this;
            }

            public Params.Builder titleColor(int val) {
                titleColor = val;
                return this;
            }

            public Params.Builder titleSize(int val) {
                titleSize = val;
                return this;
            }

            public Params.Builder contentColor(int val) {
                contentColor = val;
                return this;
            }

            public Params.Builder contentSize(int val) {
                contentSize = val;
                return this;
            }

            public Params.Builder positiveColor(int val) {
                positiveColor = val;
                return this;
            }

            public Params.Builder positiveSize(int val) {
                positiveSize = val;
                return this;
            }

            public Params.Builder negativeColor(int val) {
                negativeColor = val;
                return this;
            }

            public Params.Builder negativeSize(int val) {
                negativeSize = val;
                return this;
            }

            public Params build() {
                return new Params(this);
            }
        }
    }

    public static abstract class MultiItem {

        private boolean isSelect = false;

        /**
         * 赋值时的位置
         */
        private int position;

        /**
         * 获取需要展示的文字
         */
        public abstract String getText();

        /**
         * 是否被选中
         */
        public final boolean isSelect() {
            return isSelect;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        /**
         * 设置选中状态
         *
         * @param isSelect
         * @return
         */
        public final void setSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }
    }

    public interface OnMultiListener {
        <T extends MultiItem> void onClick(DialogInterface dialog, List<T> selects);
    }

}
