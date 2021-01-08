package yuan.core.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * DialogUtil采用静态方法，属性设置时需要注意
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
    private static final String POSITIVE_TEXT = "确定";
    private static final String NEGATIVE_TEXT = "取消";
    private static final String TITLE_TEXT = "提示";
    private static final String EMPTY_TEXT = "";
    /**
     * 全局属性设置
     * Activity的生命周期有效
     */
    private static WeakReference<Params> mParams;
    /**
     * 创建过的 Dialog 集合
     */
    private static List<WeakReference<Dialog>> mDialogs;

    /**
     * 全局初始化
     */
    static {
        mDialogs = new ArrayList<>();
    }

    /**
     * 全局设置Dialog属性，全局生效
     *
     * @param params
     */
    public static void setParams(Params params) {
        releaseEmptyDialog();
        mParams = new WeakReference<>(params);
    }

    /**
     * 设置最近创建Dialog 的进度
     *
     * @param current
     */
    public static void setProgressLastDialog(int current) {
        releaseEmptyDialog();
        for (int i = mDialogs.size() - 1; i >= 0; i--) {
            Dialog dialog = mDialogs.get(i).get();
            if (dialog instanceof ProgressDialog) {
                ProgressDialog progressDialog = ProgressDialog.class.cast(mDialogs.get(i).get());
                progressDialog.setProgress(current);
            }
        }
    }

    /**
     * 显示弹窗
     * 显示最近一次创建的并且没有被销毁的Dialog
     *
     * @return 是否显示成功
     */
    public static boolean showLastDialog() {
        releaseEmptyDialog();
        if (mDialogs.size() > 0) {
            mDialogs.get(mDialogs.size() - 1).get().show();
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
        releaseEmptyDialog();
        for (WeakReference<Dialog> mDialog : mDialogs) {
            if (mDialog != null && mDialog.get() != null) {
                mDialog.get().dismiss();
            }
        }
        return false;
    }

    /**
     * 销毁持有的对象,释放资源
     */
    public static void destroy() {
        for (WeakReference<Dialog> mDialog : mDialogs) {
            if (mDialog != null && mDialog.get() != null) {
                mDialog.get().dismiss();
                mDialog.clear();
            }
        }
        mDialogs.clear();
        System.gc();
    }

    /**
     * 移除已经 释放内存的 Dialog
     */
    private static void releaseEmptyDialog() {
        for (WeakReference<Dialog> dialog : mDialogs) {
            if (dialog.get() == null) {
                mDialogs.remove(dialog);
                releaseEmptyDialog();
                return;
            }
        }
    }

    /**
     * 获取Dialog
     *
     * @param <T>
     * @return 返回最近一次创建的Dialog
     */
    public static <T extends Dialog> T getLastDialog() {
        releaseEmptyDialog();
        if (mDialogs.size() > 1) {
            return (T) mDialogs.get(mDialogs.size() - 1).get();
        }
        return null;
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
        createDialog.positiveText = POSITIVE_TEXT;
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
        /**
         * Dialog
         */
        private Dialog dialog;

        public CreateDialog(int dialogType) {
            this.dialogType = dialogType;
            releaseEmptyDialog();
        }

        public <T extends Dialog> T create(Context context) {
            if (mParams == null || mParams.get() == null) {
                mParams = new WeakReference<>(new Params.Builder().build());
            }
            return create(context, new Params.Builder().build());
        }

        public <T extends Dialog> T create(Context context, Params params) {
            /*根布局，用于设置动画*/
            View rootView = null;
            switch (dialogType) {
                case DIALOG_TEXT:
                    AlertDialog.Builder textBuilder = new AlertDialog.Builder(context);
                    if (!TextUtils.isEmpty(title)) textBuilder.setTitle(title);
                    if (!TextUtils.isEmpty(message)) textBuilder.setMessage(message);
                    textBuilder.setPositiveButton(positiveText, positiveListener);
                    textBuilder.setNeutralButton(neutralText, neutralListener);
                    textBuilder.setNegativeButton(negativeText, negativeListener);
                    textBuilder.setCancelable(isCancel);
                    dialog = textBuilder.create();
                    rootView = AlertDialog.class.cast(dialog).getListView();
                    break;
                case DIALOG_LIST:
                    if (listData == null) return null;
                    AlertDialog.Builder listBuilder = new AlertDialog.Builder(context);
                    if (!TextUtils.isEmpty(title)) listBuilder.setTitle(title);
                    listBuilder.setItems(listData, listListener);
                    listBuilder.setCancelable(isCancel);
                    dialog = listBuilder.create();
                    rootView = AlertDialog.class.cast(dialog).getListView();
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
                    dialog = singleBuilder.create();
                    rootView = AlertDialog.class.cast(dialog).getListView();
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
                                    choiceData.clear();
                                    for (MultipleItem bean : multipleData) {
                                        if (bean.isSelect()) {
                                            choiceData.add(bean);
                                        }
                                    }
                                    multipleListener.onClick(dialog, choiceData);
                                }
                            });
                    dialog = multipleBuilder.create();
                    rootView = AlertDialog.class.cast(dialog).getListView();
                    break;
                case DIALOG_VIEW:
                    if (view == null || view.get() == null) return null;
                    dialog = new Dialog(context);
                    dialog.setContentView(view.get(), new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT
                            , RelativeLayout.LayoutParams.WRAP_CONTENT));
                    dialog.setCancelable(isCancel);
                    rootView = view.get();
                    break;
                case DIALOG_WAIT:
                    ProgressDialog waitDialog = new ProgressDialog(context);
                    dialog = waitDialog;
                    if (!TextUtils.isEmpty(title)) waitDialog.setTitle(title);
                    waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    waitDialog.setIndeterminate(true);
                    if (!TextUtils.isEmpty(message)) waitDialog.setMessage(message);
                    dialog.setCancelable(isCancel);
                    rootView = waitDialog.getListView();
                    break;
                case DIALOG_PROGRESS:
                    ProgressDialog progressDialog = new ProgressDialog(context);
                    dialog = progressDialog;
                    progressDialog.setTitle(title);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgress(current);
                    progressDialog.setMax(max);
                    progressDialog.setCancelable(isCancel);
                    rootView = progressDialog.getListView();
                    break;
                case DIALOG_DATE:
                    DatePickerDialog dateDialog = new DatePickerDialog(context,
                            dateListener, year, month, dayOfMonth);
                    dialog = dateDialog;
                    dateDialog.updateDate(year, month, dayOfMonth);
                    dateDialog.setCancelable(isCancel);
                    rootView = dateDialog.getListView();
                    break;
                case DIALOG_TIME:
                    TimePickerDialog timeDialog = new TimePickerDialog(context, timeListener,
                            hourOfDay, minute, is24HourView);
                    dialog = timeDialog;
                    timeDialog.updateTime(hourOfDay, minute);
                    timeDialog.setCancelable(isCancel);
                    rootView = timeDialog.getListView();
                    break;
            }
//            rootView = dialog.getWindow().findViewById(com.android.internal.R.id.parentPanel);
            rootView = dialog.getWindow().findViewById(Resources.getSystem().getIdentifier("parentPanel", "id", "com.android.internal.R"));
            initWindow(params, dialog.getWindow(), rootView);
            dialog.show();
            mDialogs.add(new WeakReference<>(dialog));
            return (T) dialog;
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
        private void initWindow(Params params, Window window, View rootView) {
            Animation translateAnimation = new TranslateAnimation(0, 0, 0, 1000);//设置平移的起点和终点
            translateAnimation.setDuration(10000);//动画持续的时间为10s
            translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
            translateAnimation.setFillAfter(true);//不回到起始位置
            //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
            rootView.setAnimation(translateAnimation);//给imageView添加的动画效果
            translateAnimation.startNow();//动画开始执行 放在最后即可


            /*设置背景颜色，通常为透明*/
            if (params.foregroundColor != null)
                window.setBackgroundDrawable(params.foregroundColor);
            /*设置Dialog相对于屏幕的位置*/
            window.setGravity(params.foregroundGravity);
            /*设置padding*/
            int paddingLeft = params.paddingLeft != -1 ? params.paddingLeft : window.getDecorView().getPaddingLeft();
            int paddingTop = params.paddingTop != -1 ? params.paddingTop : window.getDecorView().getPaddingTop();
            int paddingRight = params.paddingRight != -1 ? params.paddingRight : window.getDecorView().getPaddingRight();
            int paddingBottom = params.paddingBottom != -1 ? params.paddingBottom : window.getDecorView().getPaddingBottom();
            window.getDecorView().setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            WindowManager.LayoutParams windowParams = window.getAttributes();
            /*设置Window显示的位置*/
            if (params.targetView != null) {
                int[] location = new int[2];
                params.targetView.getLocationInWindow(location);
                switch (params.targetViewGravity) {
                    case Gravity.TOP:
                        windowParams.x = location[0];
                        windowParams.y = location[1] - getStatusBarHeight(dialog.getContext())
                                - windowParams.height;
                        break;
                    case Gravity.BOTTOM:
                        windowParams.x = location[0];
                        windowParams.y = location[1] - getStatusBarHeight(dialog.getContext())
                                + params.targetView.getHeight();
                        break;
                    case Gravity.LEFT:
                        windowParams.x = location[0] - windowParams.width;
                        windowParams.y = location[1] - getStatusBarHeight(dialog.getContext());
                        break;
                    case Gravity.RIGHT:
                        windowParams.x = location[0] + params.targetView.getWidth();
                        windowParams.y = location[1] - getStatusBarHeight(dialog.getContext());
                        break;
                }
            } else {
                windowParams.x = params.x;
                windowParams.y = params.y;
            }
            /*设置前景Dialog高度*/
            windowParams.height = params.matchHeight ? WindowManager.LayoutParams.MATCH_PARENT : windowParams.height;
            windowParams.height = params.height > 0 ? params.height : windowParams.height;
            /*设置前景Dialog宽度*/
            windowParams.width = params.matchWidth ? WindowManager.LayoutParams.MATCH_PARENT : windowParams.width;
            windowParams.width = params.width > 0 ? params.width : windowParams.width;
            /*弹窗布局的alpha值  1.0表示完全不透明，0.0表示没有变暗*/
            windowParams.alpha = params.foregroundAlpha;
            /*当FLAG_DIM_BEHIND设置后生效。该变量指示后面的窗口变暗的程度。1.0表示完全不透明，0.0表示没有变暗*/
            windowParams.dimAmount = params.backgroundAlpha;
            /*设置Window的进出场动画*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                window.setWindowAnimations(params.windowAnimations);
            }
            window.setAttributes(windowParams);
            window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //反射设置AlertDialog属性
            if (dialog instanceof AlertDialog) {
                reflexAlert((AlertDialog) dialog, params, true);
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

                /*设置title*/
                if (mTitle != null) {
                    TextView titleView = (TextView) mTitle.get(mAlertController);
                    if (titleView != null) {
                        if (params.titleColor != 0) titleView.setTextColor(params.titleColor);
                        if (params.titleSize != 0) titleView.setTextSize(params.titleSize);
                    }
                }

                /*设置content内容*/
                if (mMessage != null) {
                    TextView tvMessage = (TextView) mMessage.get(mAlertController);
                    if (tvMessage != null) {
                        if (params.contentColor != 0) tvMessage.setTextColor(params.contentColor);
                        if (params.contentSize != 0) tvMessage.setTextSize(params.contentSize);
                    }
                }

                /*设置 positive(左侧)按钮*/
                if (mPositive != null) {
                    Button btnPositive = (Button) mPositive.get(mAlertController);
                    if (btnPositive != null) {
                        if (params.positiveColor != 0)
                            btnPositive.setTextColor(params.positiveColor);
                        if (params.positiveSize != 0) btnPositive.setTextSize(params.positiveSize);
                    }
                }

                /*设置 negative(右侧)按钮*/
                if (mNegative != null) {
                    Button btnNegative = (Button) mNegative.get(mAlertController);
                    if (btnNegative != null) {
                        if (params.negativeColor != 0)
                            btnNegative.setTextColor(params.negativeColor);
                        if (params.negativeSize != 0) btnNegative.setTextSize(params.negativeSize);
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
     * 获取状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        int result = 24;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    result, Resources.getSystem().getDisplayMetrics());
        }
        return result;
    }

    /**
     * Created by YuanYe on 2018/1/15.
     * AlertDialog 简单统一配置文件
     */
    public static class Params {
        /**
         * 前景相对位置（一般为内容相对位置）
         * 设置为： Gravity.TOP 等
         */
        private int foregroundGravity;
        /**
         * 前景颜色，及弹窗白色部分背景颜色
         */
        private Drawable foregroundColor;
        /**
         * 背景景颜色，及弹窗灰色部分背景颜色
         */
        private Drawable backgroundColor;
        /**
         * 背景透明度，弹窗整个界面黑色背景透明度，范围0.0-1：透明-不透明
         */
        private float backgroundAlpha;
        /**
         * 前景透明度，弹窗整个界面黑色背景透明度，范围0.0-1：透明-不透明
         */
        private float foregroundAlpha;
        /**
         * 前景最大宽度，true-最大，false-默认
         */
        private boolean matchWidth = false;
        /**
         * 前景最大高度，true-最大，false-默认
         */
        private boolean matchHeight = false;
        /**
         * 前景Padding左间隔
         */
        private int paddingLeft;
        /**
         * 前景Padding右间隔
         */
        private int paddingRight;
        /**
         * 前景Padding上间隔
         */
        private int paddingTop;
        /**
         * 前景Padding下间隔
         */
        private int paddingBottom;
        /**
         * 前景显示宽度
         */
        private int width;
        /**
         * 前景显示高度
         */
        private int height;
        /**
         * 前景左上点的x坐标
         */
        private int x;
        /**
         * 前景左上点的y坐标
         */
        private int y;
        /**
         * 标题字体颜色
         */
        private int titleColor;
        /**
         * 标题字体大小
         */
        private int titleSize;
        /**
         * 内容区域文字颜色
         */
        private int contentColor;
        /**
         * 内容区域文字大小
         */
        private int contentSize;
        /**
         * 最左侧按钮字体颜色
         */
        private int positiveColor;
        /**
         * 最左侧按钮字体大小
         */
        private int positiveSize;
        /**
         * 最右侧按钮字体颜色
         */
        private int negativeColor;
        /**
         * 最右侧按钮字体大小
         */
        private int negativeSize;

        private int rotationAnimation;
        /**
         * 前景动画
         */
        private int windowAnimations;
        /**
         * 指定view,dialog 显示在View的周围，配合targetViewGravity使用
         */
        private View targetView;
        /**
         * 控制现在targetView的方向，取值 Gravity.TOP
         */
        private int targetViewGravity;

        private Params(Builder builder) {
            foregroundGravity = builder.foregroundGravity;
            foregroundColor = builder.foregroundColor;
            backgroundColor = builder.backgroundColor;
            backgroundAlpha = builder.backgroundAlpha;
            foregroundAlpha = builder.foregroundAlpha;
            matchWidth = builder.matchWidth;
            matchHeight = builder.matchHeight;
            paddingLeft = builder.paddingLeft;
            paddingRight = builder.paddingRight;
            paddingTop = builder.paddingTop;
            paddingBottom = builder.paddingBottom;
            width = builder.width;
            height = builder.height;
            x = builder.x;
            y = builder.y;
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
            targetView = builder.targetView;
            targetViewGravity = builder.targetViewGravity;
        }

        public static final class Builder {
            /**
             * 前景相对位置（一般为内容相对位置）
             * 设置为： Gravity.TOP 等
             */
            private int foregroundGravity = Gravity.CENTER;
            /**
             * 前景颜色，及弹窗白色部分背景颜色
             */
            private GradientDrawable foregroundColor;
            /**
             * 背景景颜色，及弹窗灰色部分背景颜色
             */
            private GradientDrawable backgroundColor;
            /**
             * 背景透明度，弹窗整个界面黑色背景透明度，范围0.0-1：透明-不透明
             */
            private float backgroundAlpha = 0.5f;
            /**
             * 前景透明度，弹窗整个界面黑色背景透明度，范围0.0-1：透明-不透明
             */
            private float foregroundAlpha = 1f;
            /**
             * 前景最大宽度，true-最大，false-默认
             */
            private boolean matchWidth = false;
            /**
             * 前景最大高度，true-最大，false-默认
             */
            private boolean matchHeight = false;
            /**
             * 前景Padding左间隔
             */
            private int paddingLeft = -1;
            /**
             * 前景Padding右间隔
             */
            private int paddingRight = -1;
            /**
             * 前景Padding上间隔
             */
            private int paddingTop = -1;
            /**
             * 前景Padding下间隔
             */
            private int paddingBottom = -1;
            /**
             * 前景显示宽度
             */
            private int width = 0;
            /**
             * 前景显示高度
             */
            private int height = 0;
            /**
             * 前景左上点的x坐标
             */
            private int x = 0;
            /**
             * 前景左上点的y坐标
             */
            private int y = 0;
            /**
             * 标题字体颜色
             */
            private int titleColor = 0;
            /**
             * 标题字体大小
             */
            private int titleSize = 0;
            /**
             * 内容区域文字颜色
             */
            private int contentColor = 0;
            /**
             * 内容区域文字大小
             */
            private int contentSize = 0;
            /**
             * 最左侧按钮字体颜色
             */
            private int positiveColor = 0;
            /**
             * 最左侧按钮字体大小
             */
            private int positiveSize = 0;
            /**
             * 最右侧按钮字体颜色
             */
            private int negativeColor = 0;
            /**
             * 最右侧按钮字体大小
             */
            private int negativeSize = 0;

            private int rotationAnimation;
            /**
             * 前景动画
             */
            private int windowAnimations;
            /**
             * 指定view,dialog 显示在View的周围，配合targetViewGravity使用
             */
            private View targetView;
            /**
             * 控制现在targetView的方向，取值 Gravity.TOP
             */
            private int targetViewGravity = Gravity.BOTTOM;

            public Builder() {

            }

            public Builder boregroundGravity(int foregroundGravity) {
                this.foregroundGravity = foregroundGravity;
                return this;
            }

            public Builder foregroundColor(int foregroundColor) {
                this.foregroundColor = new GradientDrawable();
                this.foregroundColor.setColor(foregroundColor);
                return this;
            }

            public Builder backgroundColor(int backgroundColor) {
                this.backgroundColor = new GradientDrawable();
                this.backgroundColor.setColor(backgroundColor);
                return this;
            }

            public Builder backgroundAlpha(float backgroundAlpha) {
                this.backgroundAlpha = backgroundAlpha;
                return this;
            }

            public Builder foregroundAlpha(float foregroundAlpha) {
                this.foregroundAlpha = foregroundAlpha;
                return this;
            }

            public Builder matchWidth() {
                this.matchWidth = true;
                return this;
            }

            public Builder matchHeight() {
                this.matchHeight = true;
                return this;
            }

            public Builder paddingLeft(int paddingLeft) {
                this.paddingLeft = paddingLeft;
                return this;
            }

            public Builder paddingRight(int paddingRight) {
                this.paddingRight = paddingRight;
                return this;
            }

            public Builder paddingTop(int paddingTop) {
                this.paddingTop = paddingTop;
                return this;
            }

            public Builder paddingBottom(int paddingBottom) {
                this.paddingBottom = paddingBottom;
                return this;
            }

            public Builder width(int width) {
                this.width = width;
                return this;
            }

            public Builder height(int height) {
                this.height = height;
                return this;
            }

            public Builder x(int x) {
                this.x = x;
                return this;
            }

            public Builder y(int y) {
                this.y = y;
                return this;
            }

            public Builder titleColor(int titleColor) {
                this.titleColor = titleColor;
                return this;
            }

            public Builder titleSize(int titleSize) {
                this.titleSize = titleSize;
                return this;
            }

            public Builder contentColor(int contentColor) {
                this.contentColor = contentColor;
                return this;
            }

            public Builder contentSize(int contentSize) {
                this.contentSize = contentSize;
                return this;
            }

            public Builder positiveColor(int positiveColor) {
                this.positiveColor = positiveColor;
                return this;
            }

            public Builder positiveSize(int positiveSize) {
                this.positiveSize = positiveSize;
                return this;
            }

            public Builder negativeColor(int negativeColor) {
                this.negativeColor = negativeColor;
                return this;
            }

            public Builder negativeSize(int negativeSize) {
                this.negativeSize = negativeSize;
                return this;
            }

            public Builder rotationAnimation(int rotationAnimation) {
                this.rotationAnimation = rotationAnimation;
                return this;
            }

            public Builder windowAnimations(int windowAnimations) {
                this.windowAnimations = windowAnimations;
                return this;
            }

            public Builder relativePosition(View targetView, int targetViewGravity) {
                this.targetView = targetView;
                this.targetViewGravity = targetViewGravity;
                this.foregroundGravity = Gravity.TOP;
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

    public static class MultipleItem {
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
