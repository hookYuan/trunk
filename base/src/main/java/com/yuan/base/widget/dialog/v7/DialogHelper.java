package com.yuan.base.widget.dialog.v7;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuan.base.R;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YuanYe on 2017/9/13.
 * 对v7包下的AlertDialog进行简单封装，方便使用
 * 整体风格采用Material原生风格
 */
public class DialogHelper {

    private final static String TAG = "DialogHelper";

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;
    private Context mContext;

    private DialogHelperParams diaLogParams;
    private int themeResId = R.style.DialogHelperTheme; //Dialog默认theme

    public DialogHelper(Context context) {
        diaLogParams = new DialogHelperParams.Builder()
                .build();
        this.mContext = context;
    }

    public DialogHelper(Context context, DialogHelperParams diaLogParams) {
        this.mContext = context;
        this.diaLogParams = diaLogParams;
    }

    /**
     * @param context
     * @param themeResId 主题样式 参考style_extra文件中的DialogHelperTheme
     */
    public DialogHelper(Context context, @StyleRes int themeResId, DialogHelperParams diaLogParams) {
        this.mContext = context;
        this.diaLogParams = diaLogParams;
        this.themeResId = themeResId;
    }

    /**
     * 关闭弹窗
     */
    public void dismiss() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 全局统一设置显示，可以控制dialog显示位置
     */
    public void show() {
        if (dialog != null) {
            alertDialog = dialog.show();
            initWindow(alertDialog.getWindow(), alertDialog);
        } else if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
            initWindow(progressDialog.getWindow(), progressDialog);
        } else if (datePickerDialog != null && !datePickerDialog.isShowing()) {
            datePickerDialog.show();
            initWindow(datePickerDialog.getWindow(), datePickerDialog);
        } else if (timePickerDialog != null && !timePickerDialog.isShowing()) {
            timePickerDialog.show();
            initWindow(timePickerDialog.getWindow(), timePickerDialog);
        }
    }

    /**
     * 设置弹窗窗体界面
     */
    private void initWindow(Window window, Object object) {
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
            if (object instanceof AlertDialog) {
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
            } else if (object instanceof android.app.AlertDialog) {
                //TODO android.app.AlertDialog下的字体、颜色设置不会生效,暂无处理

            }
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "设置失败：" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, "设置失败：" + e.getMessage());
        }
    }


    /*
     * ************************文本Dialog*****************************************************************
     */

    /**
     * @param title            标题
     * @param message          正文
     * @param positveText      右侧按钮
     * @param neutralText      中间按钮
     * @param negativeText     左侧按钮
     * @param positiveListener
     * @param neutralListener
     * @param negativeListener
     * @param isCancel         是否点击外部可以取消  false--不可取消
     */
    public void alertText(String title, String message
            , String positveText, String neutralText, String negativeText
            , DialogInterface.OnClickListener positiveListener
            , DialogInterface.OnClickListener neutralListener
            , DialogInterface.OnClickListener negativeListener
            , boolean isCancel) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(mContext, themeResId);
        }
        //可以通过R.style.MaterialDialog修改Dialog颜色等
        if (!TextUtils.isEmpty(title)) dialog.setTitle(title);
        if (!TextUtils.isEmpty(message)) dialog.setMessage(message);
        if (!TextUtils.isEmpty(positveText))
            dialog.setPositiveButton(positveText, positiveListener);
        if (!TextUtils.isEmpty(neutralText))
            dialog.setNeutralButton(neutralText, neutralListener);
        if (!TextUtils.isEmpty(negativeText))
            dialog.setNegativeButton(negativeText, negativeListener);
        dialog.setCancelable(isCancel);
        // 显示
        show();
    }

    public void alertText(String message, boolean isCancel, DialogInterface.OnClickListener positiveListener) {
        alertText("提示", message, "确定", "", "取消", positiveListener, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, isCancel);
    }

    public void alertText(String message, DialogInterface.OnClickListener positiveListener) {
        alertText(message, false, positiveListener);
    }


    public void alertText(String title, String message, DialogInterface.OnClickListener positiveListener) {
        alertText(title, message, "确定", "", "", positiveListener, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, false);
    }

    public void alertText(String title, String message, DialogInterface.OnClickListener positiveListener,boolean isCancel) {
        alertText(title, message, "确定", "", "", positiveListener, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, isCancel);
    }

    public void alertText(String message, boolean isCancel) {
        alertText("提示", message, "确定", "", "", null, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, isCancel);
    }

    public void alertText(String message) {
        alertText("提示", message, "确定", "", "", null, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, false);
    }

    /**
     * ************************列表Dialog*****************************************************************
     */
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

    public void alertList(String title, String[] mData, boolean isCancel, DialogInterface.OnClickListener listener) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(mContext, themeResId);
        }
        if (!TextUtils.isEmpty(title)) dialog.setTitle(title);
        dialog.setItems(mData, listener);
        dialog.setCancelable(isCancel);
        // 显示
        show();
    }

    /**
     * ************************单选Dialog*****************************************************************
     */
    private int choicePosition = 0; //默认单选的位置

    public void alertSingle(String title, List<String> mData, final DialogInterface.OnClickListener listener) {
        alertSingle(title, mData, "确定", listener);
    }

    public void alertSingle(String title, List<String> mData, final String positiveText, final DialogInterface.OnClickListener listener) {
        int size = mData.size();
        String[] array = mData.toArray(new String[size]);
        alertSingle(title, array, positiveText, true, listener);
    }

    public void alertSingle(String title, String[] mData, final DialogInterface.OnClickListener listener) {
        alertSingle(title, mData, "确定", true, listener);
    }

    public void alertSingle(String title, String[] mData, final String positiveText, boolean isCancel, final DialogInterface.OnClickListener listener) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(mContext, themeResId);
        }

        if (!TextUtils.isEmpty(title)) dialog.setTitle(title);

        dialog.setSingleChoiceItems(mData, choicePosition,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        choicePosition = position;
                        if (TextUtils.isEmpty(positiveText)) {
                            listener.onClick(dialog, choicePosition);
                        }
                    }
                });

        if (!TextUtils.isEmpty(positiveText)) {
            dialog.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onClick(dialog, choicePosition);
                        }
                    });
        }
        dialog.setCancelable(isCancel);
        // 显示
        show();
    }

    /**
     * ************************多选Dialog*****************************************************************
     */
    private Map<Integer, String> selects = new HashMap<>();

    private boolean[] choices; //初始化item的选中状态

    public void alertMulti(String title, List<String> mData, boolean isCancel, final OnMultiListener listener) {
        int size = mData.size();
        String[] array = mData.toArray(new String[size]);
        alertMulti(title, array, null, isCancel, listener);
    }

    public void alertMulti(String title, List<String> mData, final OnMultiListener listener) {
        alertMulti(title, mData, true, listener);
    }

    public void alertMulti(String title, final String[] mData, final OnMultiListener listener) {
        alertMulti(title, mData, null, true, listener);
    }

    public void alertMulti(String title, final String[] mData, boolean isCancel, final OnMultiListener listener) {
        alertMulti(title, mData, null, isCancel, listener);
    }

    /**
     * @param title      标题
     * @param mData      数据项
     * @param choiceItem 定义数据每项初始化选中状态数组
     * @param isCancel   是否点击外部取消
     * @param listener   选中数据回调
     */
    public void alertMulti(String title, final String[] mData, final boolean[] choiceItem, boolean isCancel, final OnMultiListener listener) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(mContext, themeResId);
        }

        if (!TextUtils.isEmpty(title)) dialog.setTitle(title);
        if (choices == null && choiceItem != null) this.choices = choiceItem;
        if (choices == null) this.choices = new boolean[mData.length];

        dialog.setMultiChoiceItems(mData, choices,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        choices[which] = isChecked;
                        if (isChecked) {
                            selects.put(which, mData[which]);
                        } else {
                            selects.remove(which);
                        }
                    }
                });

        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClick(dialog, selects);
                    }
                });
        dialog.setCancelable(isCancel);
        // 显示
        show();
    }

    /**
     * ************************等待Dialog*****************************************************************
     */
    private ProgressDialog progressDialog;

    public void alertWait(String title, String message) {
        alertWait(title, message, true);
    }

    public void alertWait(String title, String message, boolean isCancel) {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext, themeResId);
        }
        if (!TextUtils.isEmpty(title)) progressDialog.setTitle(title);
        if (!TextUtils.isEmpty(message)) progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(isCancel);
        show();
    }


    /**
     * ************************进度条Dialog*****************************************************************
     */
    public void alertProgress(String title, int max, int current, boolean isCancel) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext, themeResId);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }
        progressDialog.setProgress(current);
        progressDialog.setTitle(title);
        progressDialog.setMax(max);
        progressDialog.setCancelable(isCancel);
        // 显示
        show();
    }

    public void alertProgress(String title, int max) {
        alertProgress(title, max, 0, true);
    }

    /**
     * 设置当前进度
     *
     * @param current
     */
    public void setProgressCurrent(int current) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setProgress(current);
    }

    /**
     * ************************自定义Dialog*****************************************************************
     */
    public void alertView(final String title, View view, boolean isCancel) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(mContext, themeResId);
        }
        if (!TextUtils.isEmpty(title)) dialog.setTitle(title);
        if (view.getParent() == null) {
            dialog.setView(view);
            dialog.setCancelable(isCancel);
            show();
        } else if (alertDialog.isShowing()) {
            dismiss();
        } else {
            show();
        }
    }

    public void alertView(View view) {
        alertView("", view, true);
    }

    /**
     * ************************日期选择Dialog*****************************************************************
     */
    private DatePickerDialog datePickerDialog;

    public void alertDate(DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth, boolean isCancel) {
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(mContext, R.style.DialogHelperTheme,
                    listener, year, monthOfYear, dayOfMonth);
        } else {
            datePickerDialog.updateDate(year, monthOfYear, dayOfMonth);
            datePickerDialog.setCancelable(isCancel);
        }
        show();
    }

    public void alertDate(DatePickerDialog.OnDateSetListener listener) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        alertDate(listener, year, month, day, false);
    }

    /**
     * ************************时间选择Dialog*****************************************************************
     */
    private TimePickerDialog timePickerDialog;

    public void alertTime(TimePickerDialog.OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView, boolean isCancel) {
        if (timePickerDialog == null) {
            timePickerDialog = new TimePickerDialog(mContext, themeResId, listener, hourOfDay, minute, is24HourView);
        } else {
            timePickerDialog.updateTime(hourOfDay, minute);
            timePickerDialog.setCancelable(isCancel);
        }
        show();
    }

    public void alertTime(TimePickerDialog.OnTimeSetListener listener) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);       //获取年月日时分秒
        int minute = cal.get(Calendar.MINUTE);   //获取到的月份是从0开始计数
        alertTime(listener, hour, minute, true, false);
    }
}
