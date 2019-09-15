package yuan.core.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.AnimRes;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：对v7包下的AlertDialog进行简单封装，方便使用
 * 整体风格采用Material原生风格
 * <p>
 * Dialog根布局是一个PhoneWindow
 * <p>
 * DialogUtil采用单例模式，属性设置时需要注意
 *
 * @author yuanye
 * @modify 2019/6/27 解决Context容易引起的内存泄露,使用单例节约内存
 * @date 2019/4/4 16:29
 */
public class DialogUtils {

    private final static String TAG = "DialogUtils";
    private static final int DIALOG_TEXT = 1;
    private static final int DIALOG_LIST = 2;
    private static final int DIALOG_SINGLE = 3;
    private static final int DIALOG_MULTIPLE = 4;
    private static final int DIALOG_VIEW = 5;
    private static final int DIALOG_WAIT = 6;
    private static final int DIALOG_PROGRESS = 7;
    private static final int DIALOG_DATE = 8;
    private static final int DIALOG_TIME = 9;
    private final static String POSITIVE_TEXT = "确定";
    private final static String NEGATIVE_TEXT = "取消";
    private final static String TITLE_TEXT = "提示";
    private final static String EMPTY_TEXT = "";
    /**
     * Dialog
     */
    private static WeakReference<Dialog> mDialog;
    /**
     * 全局属性设置
     * Activity的生命周期有效
     */
    private static WeakReference<Params> mParams;

    /**
     * 全局设置Dialog属性，全局生效
     *
     * @param params
     */
    public static void setParams(Params params) {
        mParams = new WeakReference<>(params);
    }

    /**
     * 设置当前进度
     *
     * @param current
     */
    public static void setProgressCurrent(int current) {
        if (mDialog != null &&
                mDialog.get() != null &&
                mDialog.get() instanceof ProgressDialog) {
            ProgressDialog dialog = ProgressDialog.class.cast(mDialog.get());
            dialog.setProgress(current);
        } else {
            Log.e(TAG, "设置进度失败");
        }
    }

    /**
     * 显示弹窗
     * 显示最近一次创建的并且没有被销毁的Dialog
     *
     * @return 是否显示成功
     */
    public static boolean show() {
        if (mDialog != null && mDialog.get() != null) {
            mDialog.get().show();
            return true;
        }
        return false;
    }

    /**
     * 隐藏弹窗
     * 和show配合使用，不用频繁创建Dialog
     *
     * @return 是否隐藏成功
     */
    public static boolean dismiss() {
        if (mDialog != null && mDialog.get() != null) {
            mDialog.get().dismiss();
            return true;
        }
        return false;
    }

    /**
     * 销毁持有的对象,释放资源
     */
    public static void destroy() {
        //始放Dialog
        if (mDialog != null && mDialog.get() != null) {
            mDialog.get().dismiss();
            mDialog.clear();
            mDialog = null;
        }
        System.gc();
    }

    /**
     * 获取Dialog
     *
     * @param <T>
     * @return
     */
    public static <T extends Dialog> T getDialog() {
        if (mDialog == null) return null;
        return (T) mDialog.get();
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
    public static CreateDialog alertText(String title, String message
            , String positiveText, String neutralText, String negativeText
            , DialogInterface.OnClickListener positiveListener
            , DialogInterface.OnClickListener neutralListener
            , DialogInterface.OnClickListener negativeListener
            , boolean isCancel) {
        //可以通过R.style.MaterialDialog修改Dialog颜色等
        CreateDialog createDialog = new CreateDialog(DIALOG_TEXT);
        createDialog.title = title;
        createDialog.message = message;
        createDialog.positiveText = positiveText;
        createDialog.neutralText = neutralText;
        createDialog.negativeText = negativeText;
        createDialog.positiveListener = positiveListener;
        createDialog.neutralListener = neutralListener;
        createDialog.negativeListener = negativeListener;
        createDialog.isCancel = isCancel;
        return createDialog;
    }

    public static CreateDialog alertText(String title, String message, DialogInterface.OnClickListener positiveListener, boolean isCancel) {
        return alertText(title, message, POSITIVE_TEXT, "", "", positiveListener,
                null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                }, isCancel);
    }

    public static CreateDialog alertText(String message, DialogInterface.OnClickListener positiveListener, boolean isCancel) {
        return alertText(TITLE_TEXT, message, positiveListener, isCancel);
    }

    public static CreateDialog alertText(String message, DialogInterface.OnClickListener positiveListener) {
        return alertText(message, positiveListener, false);
    }


    public static CreateDialog alertText(String title, String message, DialogInterface.OnClickListener positiveListener) {
        return alertText(title, message, positiveListener, false);
    }

    public static CreateDialog alertText(String message, boolean isCancel) {
        return alertText(message, null, isCancel);
    }

    public static CreateDialog alertText(String message) {
        return alertText(TITLE_TEXT, message, POSITIVE_TEXT, EMPTY_TEXT, EMPTY_TEXT, null, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, false);
    }

    /**
     * ************************列表Dialog*****************************************************************
     */

    public static CreateDialog alertList(String title, String[] listData, boolean isCancel, DialogInterface.OnClickListener listener) {
        CreateDialog createDialog = new CreateDialog(DIALOG_LIST);
        createDialog.title = title;
        createDialog.listData = listData;
        createDialog.isCancel = isCancel;
        createDialog.listListener = listener;
        return createDialog;
    }

    public static CreateDialog alertList(String title, List<String> mData, boolean isCancel, DialogInterface.OnClickListener listener) {
        int size = mData.size();
        String[] array = mData.toArray(new String[size]);
        return alertList(title, array, isCancel, listener);
    }

    public static CreateDialog alertList(String title, List<String> mData, DialogInterface.OnClickListener listener) {
        return alertList(title, mData, true, listener);
    }

    public static CreateDialog alertList(String title, String[] mData, DialogInterface.OnClickListener listener) {
        return alertList(title, mData, true, listener);
    }

    public static CreateDialog alertList(String[] mData, DialogInterface.OnClickListener listener) {
        return alertList("", mData, true, listener);
    }

    /**
     * ************************单选Dialog*****************************************************************
     */

    /**
     * @param title        标题
     * @param listData     数据源
     * @param positiveText 确定按钮
     * @param defPosition  默认选中位置
     * @param isCancel     是否点击外部取消
     * @param listener     监听
     */
    public static CreateDialog alertSingle(String title, String[] listData, final String positiveText, int defPosition, boolean isCancel, final DialogInterface.OnClickListener listener) {
        CreateDialog createDialog = new CreateDialog(DIALOG_SINGLE);
        createDialog.title = title;
        createDialog.listData = listData;
        createDialog.isCancel = isCancel;
        createDialog.positiveText = positiveText;
        createDialog.listListener = listener;
        createDialog.defaultPosition = defPosition;
        return createDialog;
    }

    public static CreateDialog alertSingle(String title, List<String> mData, int defPosition, final DialogInterface.OnClickListener listener) {
        return alertSingle(title, mData, POSITIVE_TEXT, defPosition, listener);
    }

    public static CreateDialog alertSingle(String title, List<String> mData, final String POSITIVE_TEXT, int defPosition, final DialogInterface.OnClickListener listener) {
        int size = mData.size();
        String[] array = mData.toArray(new String[size]);
        return alertSingle(title, array, POSITIVE_TEXT, defPosition, true, listener);
    }

    public static CreateDialog alertSingle(String title, String[] mData, int defPosition, final DialogInterface.OnClickListener listener) {
        return alertSingle(title, mData, POSITIVE_TEXT, defPosition, true, listener);
    }

    /**
     * ************************多选Dialog*****************************************************************
     */

    /**
     * @param title            标题
     * @param multipleData     数据项
     * @param isCancel         是否点击外部取消
     * @param positiveListener 选中数据回调
     */
    public static <T extends MultipleItem> CreateDialog alertMulti(String title, final List<T> multipleData,
                                                                   boolean isCancel,
                                                                   final OnMultipleListener positiveListener) {
        CreateDialog createDialog = new CreateDialog(DIALOG_MULTIPLE);
        createDialog.title = title;
        createDialog.multipleData = (List<MultipleItem>) multipleData;
        createDialog.isCancel = isCancel;
        createDialog.multipleListener = positiveListener;
        return createDialog;
    }

    public static <T extends MultipleItem> CreateDialog alertMulti(String title, T[] mData, OnMultipleListener listener) {
        List<T> list = Arrays.asList(mData);
        return alertMulti(title, list, true, listener);
    }

    public static <T extends MultipleItem> CreateDialog alertMulti(String title, List<T> mData, OnMultipleListener listener) {
        return alertMulti(title, mData, true, listener);
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
    public static CreateDialog alertWait(String title, String message, boolean isCancel) {
        CreateDialog createDialog = new CreateDialog(DIALOG_WAIT);
        createDialog.title = title;
        createDialog.message = message;
        createDialog.isCancel = isCancel;
        return createDialog;
    }

    /**
     * @param title   标题
     * @param message 提示文字
     */
    public static CreateDialog alertWait(String title, String message) {
        return alertWait(title, message, true);
    }

    /**
     * @param message 提示文字
     */
    public static CreateDialog alertWait(String message) {
        return alertWait(TITLE_TEXT, message, true);
    }

    /**
     * ************************进度条Dialog*****************************************************************
     */

    public static CreateDialog alertProgress(String title, int max, int current, boolean isCancel) {
        CreateDialog createDialog = new CreateDialog(DIALOG_PROGRESS);
        createDialog.title = title;
        createDialog.max = max;
        createDialog.current = current;
        createDialog.isCancel = isCancel;
        return createDialog;
    }

    public static CreateDialog alertProgress(String title, int max) {
        return alertProgress(title, max, 0, true);
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
    public static CreateDialog alertView(final String title, View view, boolean isCancel) {
        CreateDialog createDialog = new CreateDialog(DIALOG_VIEW);

        createDialog.title = title;
        if (createDialog.view != null) {
            createDialog.view.clear();
        }
        createDialog.view = new WeakReference<>(view);
        createDialog.isCancel = isCancel;
        return createDialog;
    }

    public static CreateDialog alertView(View view) {
        return alertView("", view, true);
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
    public static CreateDialog alertDate(int year, int month, int dayOfMonth, boolean isCancel, DatePickerDialog.OnDateSetListener listener) {
        CreateDialog createDialog = new CreateDialog(DIALOG_DATE);
        createDialog.year = year;
        createDialog.month = month;
        createDialog.dayOfMonth = dayOfMonth;
        createDialog.isCancel = isCancel;
        createDialog.dateListener = listener;
        return createDialog;
    }


    public static CreateDialog alertDate(int year, int month, int dayOfMonth, DatePickerDialog.OnDateSetListener listener) {
        return alertDate(year, month, dayOfMonth, true, listener);
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
    public static CreateDialog alertTime(int hourOfDay, int minute, boolean is24HourView,
                                         boolean isCancel, TimePickerDialog.OnTimeSetListener listener) {
        CreateDialog createDialog = new CreateDialog(DIALOG_TIME);
        createDialog.hourOfDay = hourOfDay;
        createDialog.minute = minute;
        createDialog.is24HourView = is24HourView;
        createDialog.isCancel = isCancel;
        createDialog.timeListener = listener;
        return createDialog;
    }

    public static CreateDialog alertTime(int hourOfDay, int minute, TimePickerDialog.OnTimeSetListener listener) {
        return alertTime(hourOfDay, minute, true, true, listener);
    }

    public static class CreateDialog {
        /**
         * 弹窗类型
         */
        private int dialogType;

        private String title;
        private String message;
        private String positiveText;
        private String neutralText;
        private String negativeText;
        private DialogInterface.OnClickListener positiveListener;
        private DialogInterface.OnClickListener neutralListener;
        private DialogInterface.OnClickListener negativeListener;
        /**
         * 是否可以点击外部取消
         */
        private boolean isCancel;
        /**
         * 文本数据源  对应单选/列表数据源
         */
        private String[] listData;
        /**
         * 多选数据源
         */
        private List<MultipleItem> multipleData;
        /**
         * list Item点击事件
         */
        private DialogInterface.OnClickListener listListener;

        /**
         * Multiple 确定点击事件
         */
        private OnMultipleListener multipleListener;
        /**
         * singleDialog 默认选中的position
         */
        private int defaultPosition;
        /**
         * 自定弹窗View
         */
        private WeakReference<View> view;

        /**
         * Progress Dialog 显示最大值
         */
        private int max;
        /**
         * Progress Dialog 显示当前值
         */
        private int current;
        /**
         * Date Dialog 默认年份
         */
        private int year;
        /**
         * Date Dialog 默认月份
         */
        private int month;
        /**
         * Date Dialog 默认一月中的天数
         */
        private int dayOfMonth;
        /**
         * Date Dialog 日期选中监听
         */
        private DatePickerDialog.OnDateSetListener dateListener;
        /**
         * Time Dialog 小时
         */
        private int hourOfDay;
        /**
         * Time Dialog 分钟
         */
        private int minute;
        /**
         * Time Dialog 是否24小时制
         */
        private boolean is24HourView;
        /**
         * Time Dialog 时间点击监听
         */
        private TimePickerDialog.OnTimeSetListener timeListener;

        public CreateDialog(int dialogType) {
            this.dialogType = dialogType;

            if (mDialog != null && mDialog.get() != null) {
                mDialog.get().dismiss();
                mDialog.clear();
                mDialog = null;
            }
        }

        public <T extends Dialog> T create(Context context) {
            if (mParams == null || mParams.get() == null) {
                mParams = new WeakReference<>(new Params.Builder().build());
            }
            return create(context, mParams.get());
        }

        public <T extends Dialog> T create(Context context, Params params) {
            switch (dialogType) {
                case DIALOG_TEXT:
                    AlertDialog.Builder textBuilder = new AlertDialog.Builder(context);
                    if (!TextUtils.isEmpty(title)) textBuilder.setTitle(title);
                    if (!TextUtils.isEmpty(message)) textBuilder.setMessage(message);
                    textBuilder.setPositiveButton(positiveText, positiveListener);
                    textBuilder.setNeutralButton(neutralText, neutralListener);
                    textBuilder.setNegativeButton(negativeText, negativeListener);
                    textBuilder.setCancelable(isCancel);
                    mDialog = new WeakReference<Dialog>(textBuilder.create());
                    break;
                case DIALOG_LIST:
                    if (listData == null) return null;
                    AlertDialog.Builder listBuilder = new AlertDialog.Builder(context);
                    if (!TextUtils.isEmpty(title)) listBuilder.setTitle(title);
                    listBuilder.setItems(listData, listListener);
                    listBuilder.setCancelable(isCancel);
                    mDialog = new WeakReference<Dialog>(listBuilder.create());
                    break;
                case DIALOG_SINGLE:
                    AlertDialog.Builder singleBuilder = new AlertDialog.Builder(context);
                    if (!TextUtils.isEmpty(title)) singleBuilder.setTitle(title);
                    singleBuilder.setCancelable(isCancel);
                    singleBuilder.setSingleChoiceItems(listData, defaultPosition, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            defaultPosition = which;
                        }
                    });
                    //点击确定按钮
                    if (!TextUtils.isEmpty(positiveText)) {
                        singleBuilder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listListener.onClick(dialog, defaultPosition);
                            }
                        });
                    }
                    mDialog = new WeakReference<Dialog>(singleBuilder.create());
                    break;
                case DIALOG_MULTIPLE:
                    AlertDialog.Builder multipleBuilder = new AlertDialog.Builder(context);
                    if (!TextUtils.isEmpty(title)) multipleBuilder.setTitle(title);
                    multipleBuilder.setCancelable(isCancel);
                    //解析数据源
                    boolean[] choiceItem = new boolean[multipleData.size()];
                    String[] displayData = new String[multipleData.size()];
                    for (int i = 0; i < multipleData.size(); i++) {
                        displayData[i] = multipleData.get(i).displayText;
                        choiceItem[i] = multipleData.get(i).select;
                    }
                    final ArrayList<MultipleItem> choiceData = new ArrayList<>();

                    multipleBuilder.setMultiChoiceItems(displayData, choiceItem,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        multipleData.get(which).select = true;
                                    } else {
                                        multipleData.get(which).select = false;
                                    }
                                }
                            });

                    multipleBuilder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (MultipleItem bean : multipleData) {
                                        if (bean.isSelect()) {
                                            choiceData.add(bean);
                                        }
                                    }
                                    multipleListener.onClick(dialog, choiceData);
                                }
                            });
                    mDialog = new WeakReference<Dialog>(multipleBuilder.create());
                    break;
                case DIALOG_VIEW:
                    if (view == null || view.get() == null) return null;
                    AlertDialog.Builder viewBuilder = new AlertDialog.Builder(context);
                    viewBuilder.setView(view.get());
                    viewBuilder.setCancelable(isCancel);
                    mDialog = new WeakReference<Dialog>(viewBuilder.create());
                    break;
                case DIALOG_WAIT:
                    ProgressDialog waitDialog = new ProgressDialog(context);
                    mDialog = new WeakReference<Dialog>(waitDialog);
                    if (!TextUtils.isEmpty(title)) waitDialog.setTitle(title);
                    waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    waitDialog.setIndeterminate(true);
                    if (!TextUtils.isEmpty(message)) waitDialog.setMessage(message);
                    mDialog.get().setCancelable(isCancel);
                    break;
                case DIALOG_PROGRESS:
                    ProgressDialog progressDialog = new ProgressDialog(context);
                    mDialog = new WeakReference(progressDialog);
                    progressDialog.setTitle(title);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgress(current);
                    progressDialog.setMax(max);
                    progressDialog.setCancelable(isCancel);
                    break;
                case DIALOG_DATE:
                    DatePickerDialog dateDialog = new DatePickerDialog(context,
                            dateListener, year, month, dayOfMonth);
                    mDialog = new WeakReference(dateDialog);
                    dateDialog.updateDate(year, month, dayOfMonth);
                    dateDialog.setCancelable(isCancel);
                    break;
                case DIALOG_TIME:
                    TimePickerDialog timeDialog = new TimePickerDialog(context, timeListener,
                            hourOfDay, minute, is24HourView);
                    mDialog = new WeakReference(timeDialog);
                    timeDialog.updateTime(hourOfDay, minute);
                    timeDialog.setCancelable(isCancel);
                    break;
            }
            initWindow(params, mDialog.get().getWindow());
            show();
            return (T) mDialog.get();
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
            windowParams.windowAnimations = params.getWindowAnimations();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                windowParams.rotationAnimation = params.getRotationAnimation();
                window.setWindowAnimations(params.getWindowAnimations());
            }
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

                //设置title
                if (mTitle != null) {
                    TextView titleView = (TextView) mTitle.get(mAlertController);
                    if (titleView != null) {
                        if (params.getTitleColor() != 0)
                            titleView.setTextColor(params.getTitleColor());
                        if (params.getTitleSize() != 0)
                            titleView.setTextSize(params.getTitleSize());
                    }
                }

                //设置Message
                if (mMessage != null) {
                    TextView tvMessage = (TextView) mMessage.get(mAlertController);
                    if (tvMessage != null) {
                        if (params.getContentColor() != 0)
                            tvMessage.setTextColor(params.getContentColor());
                        if (params.getContentSize() != 0)
                            tvMessage.setTextSize(params.getContentSize());
                    }
                }

                //设置 positive(左侧)按钮
                if (mPositive != null) {
                    Button btnPositive = (Button) mPositive.get(mAlertController);
                    if (btnPositive != null) {
                        if (params.getPositiveColor() != 0)
                            btnPositive.setTextColor(params.getPositiveColor());
                        if (params.getPositiveSize() != 0)
                            btnPositive.setTextSize(params.getPositiveSize());
                    }
                }

                //设置 negative(右侧)按钮
                if (mNegative != null) {
                    Button btnNegative = (Button) mNegative.get(mAlertController);
                    if (btnNegative != null) {
                        if (params.getNegativeColor() != 0)
                            btnNegative.setTextColor(params.getNegativeColor());
                        if (params.getNegativeSize() != 0)
                            btnNegative.setTextSize(params.getNegativeSize());
                    }
                }
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "设置失败：" + e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e(TAG, "设置失败：" + e.getMessage());
            }
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

        private @AnimRes
        int rotationAnimation;
        private @AnimRes
        int windowAnimations;

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

            rotationAnimation = builder.rotationAnimation;
            windowAnimations = builder.windowAnimations;
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

        public int getRotationAnimation() {
            return rotationAnimation;
        }

        public int getWindowAnimations() {
            return windowAnimations;
        }

        public static final class Builder {

            //相对位置
            private int gravity = Gravity.CENTER;
            //弹窗白色部分背景
            private GradientDrawable windowBackground;
            //弹窗整个界面黑色背景透明度，范围0.0-1：透明-不透明
            private float dialogBehindAlpha = 0.5f;
            //弹窗白色部分背景透明度，范围0.0-1：透明-不透明
            private float dialogFrontAlpha = 1f;
            //最大宽度
            private boolean matchWidth = false;
            //最大高度
            private boolean matchHeight = false;

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

            private @AnimRes
            int rotationAnimation; //旋转动画
            private @AnimRes
            int windowAnimations; //window动画

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

            public Params.Builder rotationAnimation(int val) {
                rotationAnimation = val;
                return this;
            }

            public Params.Builder windowAnimations(int val) {
                windowAnimations = val;
                return this;
            }

            public Params build() {
                return new Params(this);
            }
        }
    }

    /**
     * 多选点击事件
     */
    public interface OnMultipleListener {
        <T extends MultipleItem> void onClick(DialogInterface dialog, List<T> selects);
    }

    public static abstract class MultipleItem {
        /**
         * 是否选中
         */
        private boolean select = false;
        /**
         * 展示文本
         */
        private String displayText;

        public MultipleItem(String displayText) {
            this.displayText = displayText;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        /**
         * 是否被选中
         */
        public final boolean isSelect() {
            return select;
        }
    }
}
