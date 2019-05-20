package com.yuan.kernel;

import android.app.DatePickerDialog;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：对v7包下的AlertDialog进行简单封装，方便使用
 * 整体风格采用Material原生风格
 *
 * @author yuanye
 * @date 2019/4/4 16:29
 */
public class DialogUtil {

    private final static String TAG = "DialogUtil";
    /**
     * 默认文字
     */
    private final static String POSITIVETEXT = "确定";
    private final static String NEGATIVETEXT = "取消";
    private final static String TITLETEXT = "提示";
    /**
     * context
     */
    private static Context mContext;
    /**
     * 单例
     */
    private static DialogUtil util;
    /**
     * 全局配置参数
     */
    private static DialogParams allParams;
    /**
     * 当前配置参数
     */
    private static DialogParams diaLogParams;
    /**
     * app包下dialog
     */
    private static android.app.AlertDialog appAlertDialog;
    /**
     * v7包下dialog
     */
    private static AlertDialog v7AlertDialog;
    /**
     * 主题样式
     */
    private int themeResId = 0;

    /**
     * 单例
     *
     * @param context
     * @return
     */
    public static DialogUtil create(Context context) {
        return create(context, null);
    }

    /**
     * @param context
     * @param params  优先于全局生效
     * @return
     */
    public static DialogUtil create(Context context, DialogParams params) {
        if (util == null) {
            util = new DialogUtil();
        }
        if (params != null) {
            diaLogParams = params;
        } else if (allParams != null) {
            diaLogParams = allParams;
        } else {
            diaLogParams = new DialogParams.Builder().build();
        }
        mContext = context;
        return util;
    }

    /**
     * 全局设置Dialog属性，全局生效
     *
     * @param params
     */
    public static void setStyle(DialogParams params) {
        if (params != null) {
            allParams = params;
            diaLogParams = params;
        }
    }

    /**
     * 设置当前进度
     *
     * @param current
     */
    public static void setProgressCurrent(int current) {
        if (appAlertDialog != null && appAlertDialog instanceof ProgressDialog) {
            ((ProgressDialog) appAlertDialog).setProgress(current);
        }
    }

    private DialogUtil() {
    }

    /**
     * ************************文本Dialog*****************************************************************
     */

    /**
     * @param title            标题
     * @param message          正文
     * @param positiveText     右侧按钮
     * @param neutralText      中间按钮
     * @param negativeText     左侧按钮
     * @param positiveListener
     * @param neutralListener
     * @param negativeListener
     * @param isCancel         是否点击外部可以取消  false--不可取消
     */
    public void alertText(String title, String message
            , String positiveText, String neutralText, String negativeText
            , DialogInterface.OnClickListener positiveListener
            , DialogInterface.OnClickListener neutralListener
            , DialogInterface.OnClickListener negativeListener
            , boolean isCancel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, themeResId);
        //可以通过R.style.MaterialDialog修改Dialog颜色等
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        if (!TextUtils.isEmpty(message)) builder.setMessage(message);
        if (!TextUtils.isEmpty(positiveText))
            builder.setPositiveButton(positiveText, positiveListener);
        if (!TextUtils.isEmpty(neutralText))
            builder.setNeutralButton(neutralText, neutralListener);
        if (!TextUtils.isEmpty(negativeText))
            builder.setNegativeButton(negativeText, negativeListener);
        builder.setCancelable(isCancel);
        // 显示
        initCreate(diaLogParams, builder);
    }

    public void alertText(String message, boolean isCancel, DialogInterface.OnClickListener positiveListener) {
        alertText(TITLETEXT, message, POSITIVETEXT, "", NEGATIVETEXT, positiveListener, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, isCancel);
    }

    public void alertText(String message, DialogInterface.OnClickListener positiveListener) {
        alertText(message, false, positiveListener);
    }


    public void alertText(String title, String message, DialogInterface.OnClickListener positiveListener) {
        alertText(title, message, POSITIVETEXT, "", "", positiveListener, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, false);
    }

    public void alertText(String title, String message, DialogInterface.OnClickListener positiveListener, boolean isCancel) {
        alertText(title, message, POSITIVETEXT, "", "", positiveListener, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, isCancel);
    }

    public void alertText(String message, boolean isCancel) {
        alertText(TITLETEXT, message, POSITIVETEXT, "", "", null, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, isCancel);
    }

    public void alertText(String message) {
        alertText(TITLETEXT, message, POSITIVETEXT, "", "", null, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, false);
    }

    /**
     * ************************列表Dialog*****************************************************************
     */
    public void alertList(String title, String[] mData, boolean isCancel, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, themeResId);

        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        builder.setItems(mData, listener);
        builder.setCancelable(isCancel);
        // 显示
        initCreate(diaLogParams, builder);
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

    public void alertSingle(String title, List<String> mData, int defPosition, final DialogInterface.OnClickListener listener) {
        alertSingle(title, mData, POSITIVETEXT, defPosition, listener);
    }

    public void alertSingle(String title, List<String> mData, final String positiveText, int defPosition, final DialogInterface.OnClickListener listener) {
        int size = mData.size();
        String[] array = mData.toArray(new String[size]);
        alertSingle(title, array, positiveText, defPosition, true, listener);
    }

    public void alertSingle(String title, String[] mData, int defPosition, final DialogInterface.OnClickListener listener) {
        alertSingle(title, mData, POSITIVETEXT, defPosition, true, listener);
    }

    /**
     * @param title        标题
     * @param mData        数据源
     * @param positiveText 确定按钮
     * @param defPosition  默认选中位置
     * @param isCancel     是否点击外部取消
     * @param listener     监听
     */
    public void alertSingle(String title, String[] mData, final String positiveText, int defPosition, boolean isCancel, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, themeResId);
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
        if (!TextUtils.isEmpty(positiveText)) {
            builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onClick(dialog, defPos);
                }
            });
        }
        builder.setCancelable(isCancel);
        // 显示
        initCreate(diaLogParams, builder);
    }

    /**
     * ************************多选Dialog*****************************************************************
     */

    public <T extends MultiItem> void alertMulti(String title, T[] mData, OnMultiListener listener) {
        List<T> list = Arrays.asList(mData);
        alertMulti(title, list, true, listener);
    }


    public <T extends MultiItem> void alertMulti(String title, List<T> mData, OnMultiListener listener) {
        alertMulti(title, mData, true, listener);
    }

    /**
     * @param title            标题
     * @param data             数据项
     * @param isCancel         是否点击外部取消
     * @param positiveListener 选中数据回调
     */
    public <T extends MultiItem> void alertMulti(String title, final List<T> data,
                                                 boolean isCancel,
                                                 final OnMultiListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, themeResId);

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
        builder.setPositiveButton(POSITIVETEXT,
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
        initCreate(diaLogParams, builder);
    }

    /**
     * ************************等待Dialog*****************************************************************
     */

    /**
     * @param title   标题
     * @param message 提示文字
     */
    public void alertWait(String title, String message) {
        alertWait(title, message, true);
    }

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
        appAlertDialog = new ProgressDialog(mContext, themeResId);
        if (!TextUtils.isEmpty(title)) appAlertDialog.setTitle(title);
        if (!TextUtils.isEmpty(message)) appAlertDialog.setMessage(message);
        if (appAlertDialog instanceof ProgressDialog) {
            ((ProgressDialog) appAlertDialog).setProgressStyle(ProgressDialog.STYLE_SPINNER);
            ((ProgressDialog) appAlertDialog).setIndeterminate(true);
        }
        appAlertDialog.setCancelable(isCancel);
        // 显示
        initCreate(diaLogParams, appAlertDialog);
    }

    /**
     * ************************进度条Dialog*****************************************************************
     */
    public void alertProgress(String title, int max) {
        alertProgress(title, max, 0, true);
    }

    public void alertProgress(String title, int max, int current, boolean isCancel) {
        appAlertDialog = new ProgressDialog(mContext, themeResId);
        if (appAlertDialog instanceof ProgressDialog) {
            ((ProgressDialog) appAlertDialog).setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            ((ProgressDialog) appAlertDialog).setProgress(current);
            ((ProgressDialog) appAlertDialog).setMax(max);
        }
        appAlertDialog.setTitle(title);
        appAlertDialog.setCancelable(isCancel);
        // 显示
        initCreate(diaLogParams, appAlertDialog);
    }

    /**
     * ************************自定义Dialog*****************************************************************
     */
    public void alertView(final String title, View view, boolean isCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, themeResId);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        builder.setView(view);
        builder.setCancelable(isCancel);
        initCreate(diaLogParams, builder);
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
        if (appAlertDialog == null || !(appAlertDialog instanceof DatePickerDialog)) {
            appAlertDialog = new DatePickerDialog(mContext, themeResId,
                    listener, year, month, dayOfMonth);
        }

        if (appAlertDialog instanceof DatePickerDialog) {
            ((DatePickerDialog) appAlertDialog).updateDate(year, month, dayOfMonth);
            appAlertDialog.setCancelable(isCancel);
        }
        initCreate(diaLogParams, appAlertDialog);
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
        if (appAlertDialog == null || !(appAlertDialog instanceof TimePickerDialog)) {
            appAlertDialog = new TimePickerDialog(mContext, themeResId, listener,
                    hourOfDay, minute, is24HourView);
        }

        if (appAlertDialog instanceof TimePickerDialog) {
            ((TimePickerDialog) appAlertDialog).updateTime(hourOfDay, minute);
            appAlertDialog.setCancelable(isCancel);
        }
        initCreate(diaLogParams, appAlertDialog);
    }

    public void alertTime(int hourOfDay, int minute, TimePickerDialog.OnTimeSetListener listener) {
        alertTime(hourOfDay, minute, true, true, listener);
    }


    /**
     * 关闭弹窗
     */
    public static void dismiss() {
        if (v7AlertDialog != null) {
            v7AlertDialog.dismiss();
        }
        if (appAlertDialog != null) {
            appAlertDialog.dismiss();
        }
    }

    /**
     * 全局统一设置显示，可以控制dialog显示位置
     */
    private void initCreate(DialogParams params, Object dialog) {
        if (dialog instanceof AlertDialog.Builder) {
            v7AlertDialog = ((AlertDialog.Builder) dialog).show();
            initParams(params, v7AlertDialog.getWindow(), v7AlertDialog);
        } else if (dialog instanceof android.app.AlertDialog) {
            ((android.app.AlertDialog) dialog).show();
            initParams(params, ((android.app.AlertDialog) dialog).getWindow(), null);
        }
    }

    /**
     * 设置弹窗窗体界面
     *
     * @param diaLogParams
     * @param window
     * @param object
     */
    private void initParams(DialogParams diaLogParams, Window window, AlertDialog object) {
        //设置背景颜色，通常为透明
        if (diaLogParams.getWindowBackground() != null) {
            window.setBackgroundDrawable(diaLogParams.getWindowBackground());
        }
        //设置Dialog相对于屏幕的位置
        window.setGravity(diaLogParams.getGravity());
        //设置padding
        int paddingLeft = diaLogParams.getPaddingLeft() != -1 ? diaLogParams.getPaddingLeft()
                : window.getDecorView().getPaddingLeft();
        int paddingTop = diaLogParams.getPaddingTop() != -1 ? diaLogParams.getPaddingTop()
                : window.getDecorView().getPaddingTop();
        int paddingRight = diaLogParams.getPaddingRight() != -1 ? diaLogParams.getPaddingRight()
                : window.getDecorView().getPaddingRight();
        int paddingBottom = diaLogParams.getPaddingBottom() != -1 ? diaLogParams.getPaddingBottom()
                : window.getDecorView().getPaddingBottom();
        window.getDecorView().setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        WindowManager.LayoutParams windowParams = window.getAttributes();
        //最大高度
        if (diaLogParams.isMatchHeight()) {
            windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else if (diaLogParams.getHeight() > 0) {
            windowParams.height = diaLogParams.getHeight();
        }

        //最大宽度
        if (diaLogParams.isMatchWidth()) {
            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        } else if (diaLogParams.getWidth() > 0) {
            windowParams.width = diaLogParams.getWidth();
        }

        /*实例化Window*/
        windowParams.x = diaLogParams.getPosX();
        windowParams.y = diaLogParams.getPosY();

        //弹窗布局的alpha值  1.0表示完全不透明，0.0表示没有变暗。
        windowParams.alpha = diaLogParams.getDialogFrontAlpha();
        // 当FLAG_DIM_BEHIND设置后生效。该变量指示后面的窗口变暗的程度。1.0表示完全不透明，0.0表示没有变暗。
        windowParams.dimAmount = diaLogParams.getDialogBehindAlpha();
        //屏幕亮度 用来覆盖用户设置的屏幕亮度。表示应用用户设置的屏幕亮度。从0到1调整亮度从暗到最亮发生变化。
        //layoutParams.screenBrightness = 0.7f;
        window.setAttributes(windowParams);
        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //通过反射设置字体颜色及大小
        Field mAlert = null;
        try {
            if (object != null && object instanceof AlertDialog) {
                mAlert = AlertDialog.class.getDeclaredField("mAlert");
                mAlert.setAccessible(true);
                Object mAlertController = null;
                mAlertController = mAlert.get(object);
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
                    if (diaLogParams.getTitleColor() != 0)
                        titleView.setTextColor(diaLogParams.getTitleColor());
                    if (diaLogParams.getTitleSize() != 0)
                        titleView.setTextSize(diaLogParams.getTitleSize());
                }

                if (mMessage != null) {
                    TextView tvMessage = (TextView) mMessage.get(mAlertController);
                    if (diaLogParams.getContentColor() != 0)
                        tvMessage.setTextColor(diaLogParams.getContentColor());
                    if (diaLogParams.getContentSize() != 0)
                        tvMessage.setTextSize(diaLogParams.getContentSize());
                }

                if (mPositive != null) {
                    Button btnPositive = (Button) mPositive.get(mAlertController);
                    if (diaLogParams.getPositiveColor() != 0)
                        btnPositive.setTextColor(diaLogParams.getPositiveColor());
                    if (diaLogParams.getPositiveSize() != 0)
                        btnPositive.setTextSize(diaLogParams.getPositiveSize());
                }
                if (mNegative != null) {
                    Button btnNegative = (Button) mNegative.get(mAlertController);
                    if (diaLogParams.getNegativeColor() != 0)
                        btnNegative.setTextColor(diaLogParams.getNegativeColor());
                    if (diaLogParams.getNegativeSize() != 0)
                        btnNegative.setTextSize(diaLogParams.getNegativeSize());
                }
            }
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "设置失败：" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, "设置失败：" + e.getMessage());
        }

        //设置Window的进出场动画
//        windowParams.windowAnimations =
    }


    /**
     * Created by YuanYe on 2018/1/15.
     * AlertDialog 简单统一配置文件
     */

    public static class DialogParams {

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

        private DialogParams(DialogParams.Builder builder) {
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

            public DialogParams.Builder gravity(int val) {
                gravity = val;
                return this;
            }

            public DialogParams.Builder windowBackground(GradientDrawable val) {
                windowBackground = val;
                return this;
            }

            public DialogParams.Builder windowBackground(@ColorInt int val) {
                windowBackground = new GradientDrawable();
                windowBackground.setColor(val);
                return this;
            }

            public DialogParams.Builder dialogBehindAlpha(float val) {
                dialogBehindAlpha = val;
                return this;
            }

            public DialogParams.Builder dialogFrontAlpha(float val) {
                dialogFrontAlpha = val;
                return this;
            }

            public DialogParams.Builder matchWidth(boolean val) {
                matchWidth = val;
                return this;
            }

            public DialogParams.Builder matchHeight(boolean val) {
                matchHeight = val;
                return this;
            }

            public DialogParams.Builder paddingTop(int val) {
                paddingTop = val;
                return this;
            }

            public DialogParams.Builder paddingRight(int val) {
                paddingRight = val;
                return this;
            }

            public DialogParams.Builder paddingLeft(int val) {
                paddingLeft = val;
                return this;
            }

            public DialogParams.Builder paddingBottom(int val) {
                paddingBottom = val;
                return this;
            }

            public DialogParams.Builder width(int val) {
                width = val;
                return this;
            }

            public DialogParams.Builder height(int val) {
                height = val;
                return this;
            }

            public DialogParams.Builder posX(int val) {
                posX = val;
                return this;
            }

            public DialogParams.Builder posY(int val) {
                posY = val;
                return this;
            }

            public DialogParams.Builder titleColor(int val) {
                titleColor = val;
                return this;
            }

            public DialogParams.Builder titleSize(int val) {
                titleSize = val;
                return this;
            }

            public DialogParams.Builder contentColor(int val) {
                contentColor = val;
                return this;
            }

            public DialogParams.Builder contentSize(int val) {
                contentSize = val;
                return this;
            }

            public DialogParams.Builder positiveColor(int val) {
                positiveColor = val;
                return this;
            }

            public DialogParams.Builder positiveSize(int val) {
                positiveSize = val;
                return this;
            }

            public DialogParams.Builder negativeColor(int val) {
                negativeColor = val;
                return this;
            }

            public DialogParams.Builder negativeSize(int val) {
                negativeSize = val;
                return this;
            }

            public DialogParams build() {
                return new DialogParams(this);
            }
        }
    }


    public interface OnMultiListener {
        <T extends MultiItem> void onClick(DialogInterface dialog, List<T> selects);
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

}
