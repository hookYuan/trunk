package yuan.widget.title;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yuan.R;
import java.util.List;

/**
 * Created by YuanYe on 2017/8/17.
 * Title基本布局加载
 * 默认布局：左侧可放置TextView、ImageView
 * 中间支持主副标题、右侧可放置TextView、ImageView
 * 右侧支持菜单
 * 如果需要放置多个图标，可以动态添加图标
 */
public class TitleBar extends AbsTitle<TitleBar> {


    private static int DEFAULTCOLOR; //默认字体颜色

    private CharSequence leftText = ""; //左侧文字
    private CharSequence centerText = "";//中间文字
    private CharSequence rightText = "";//右边文字
    private Drawable leftIcon;//左侧图标
    private Drawable rightIcon;//右侧图标
    private Drawable background;//整体背景

    private int leftFontColor; //左侧文字颜色
    private int centerFontColor;//中间文字颜色
    private int rightFontColor;//右侧间文字颜色

    private float leftFontSize = 16 * context.getResources().getDisplayMetrics().scaledDensity; //左侧文字大小
    private float centerFontSize = 18 * context.getResources().getDisplayMetrics().scaledDensity; //中间文字大小
    private float rightFontSize = 16 * context.getResources().getDisplayMetrics().scaledDensity; //右侧文字大小

    private boolean leftClickFinish;//点击左侧图标返回

    protected TextView leftTextView;//左侧
    protected TextView titleTextView;//主标题
    protected TextView subtitleTextView;//副标题
    protected TextView rightTextView;//右侧

    private PopupWindow popupWindowMenu;//弹窗菜单

    public TitleBar(@NonNull Context _context, @Nullable AttributeSet attrs) {
        super(_context, attrs);
        this.context = _context;
        DEFAULTCOLOR = ContextCompat.getColor(getContext(), R.color.colorFont33);
        obtainAttributes(_context, attrs);
    }


    public TitleBar(Context _context) {
        super(_context);
        this.context = _context;
        DEFAULTCOLOR = ContextCompat.getColor(getContext(), R.color.colorFont33);
        drawTitle();
    }

    /**
     * 绑定自定义属性
     */
    public void obtainAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        int textColor = ta.getColor(R.styleable.TitleBar_android_textColor, 0);

        leftText = ta.getText(R.styleable.TitleBar_leftText);
        centerText = ta.getText(R.styleable.TitleBar_centerText);
        rightText = ta.getText(R.styleable.TitleBar_rightText);
        leftIcon = ta.getDrawable(R.styleable.TitleBar_leftDrawable);
        rightIcon = ta.getDrawable(R.styleable.TitleBar_rightDrawable);
        leftClickFinish = ta.getBoolean(R.styleable.TitleBar_leftClickFinish, false);

        leftFontColor = ta.getColor(R.styleable.TitleBar_leftTextColor, textColor == 0 ? DEFAULTCOLOR : textColor);
        centerFontColor = ta.getColor(R.styleable.TitleBar_centerTextColor, textColor == 0 ? DEFAULTCOLOR : textColor);
        rightFontColor = ta.getColor(R.styleable.TitleBar_rightTextColor, textColor == 0 ? DEFAULTCOLOR : textColor);
        leftFontSize = ta.getDimension(R.styleable.TitleBar_leftTextSize, dp2Dx(16));
        centerFontSize = ta.getDimension(R.styleable.TitleBar_centerTextSize, dp2Dx(18));
        rightFontSize = ta.getDimension(R.styleable.TitleBar_rightTextSize, dp2Dx(16));
        rightFontSize = ta.getDimension(R.styleable.TitleBar_rightTextSize, dp2Dx(16));
        background = ta.getDrawable(R.styleable.TitleBar_android_background);
        ta.recycle();
        drawTitle();
    }


    /**
     * 在Abs布局添加默认需要添加的内容
     */
    private void drawTitle() {
        setBackground(background);

        if (leftTextView == null) {
            leftTextView = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            leftTextView.setLayoutParams(params);
            leftTextView.setPadding(dp2Dx(15), 0, dp2Dx(15), 0);
            leftTextView.setGravity(Gravity.CENTER_VERTICAL);
            setClickStyle(leftTextView);
            addLeftView(leftTextView);
        }
        setLeftTextColor(leftFontColor);
        setLeftTextSize(leftFontSize);
        setLeftText(leftText);
        setLeftIcon(leftIcon);

        if (titleTextView == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.title_center, this, false);
            titleTextView = view.findViewById(R.id.tv_title);
            subtitleTextView = view.findViewById(R.id.tv_subtitle);
            subtitleTextView.setVisibility(GONE);
            addCenterView(view);
        }
        setTitleTextColor(centerFontColor);
        setTitleTextSize(centerFontSize);
        setTitleText(centerText);

        if (rightTextView == null) {
            rightTextView = new TextView(context);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            rightTextView.setLayoutParams(params);
            rightTextView.setPadding(dp2Dx(15), 0, dp2Dx(15), 0);
            rightTextView.setGravity(Gravity.CENTER_VERTICAL);
            addRightView(rightTextView);
            setClickStyle(rightTextView);
        }
        setRightTextColor(rightFontColor);
        setRightTextSize(rightFontSize);
        setRightIcon(rightIcon);
        setRightText(rightText);

        if (leftClickFinish) setLeftClickFinish();

    }


    /**
     * ------------------------------------左侧内容设置----------------------------------------
     **/
    public TitleBar setLeftText(@NonNull CharSequence text) {
        leftTextView.setText(text);
        return this;
    }

    public TitleBar setLeftIcon(@NonNull Drawable icon) {
        if (icon != null) {
            icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight()); //设置边界
            leftTextView.setCompoundDrawables(icon, null, null, null);//画在左边
        }
        return this;
    }

    public TitleBar setLeftIcon(@DrawableRes int drawableId) {
        Drawable icon = getResources().getDrawable(drawableId);
        setLeftIcon(icon);
        return this;
    }

    public TitleBar setLeftOnClickListener(OnClickListener listener) {
        if (leftTextView != null && listener != null) leftTextView.setOnClickListener(listener);
        return this;
    }

    public TitleBar setLeftClickFinish() {
        setLeftOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context != null && context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
        return this;
    }

    /**
     * ------------------------------------中间toolbar按钮设置----------------------------------------
     **/
    public TitleBar setTitleText(@NonNull CharSequence text) {
        if (titleTextView != null && text != null) {
            titleTextView.setText(text);
        }
        return this;
    }

    //设置二级标题
    public TitleBar setSubtitleText(@NonNull CharSequence text) {
        if (subtitleTextView != null && text != null) {
            subtitleTextView.setText(text);
            subtitleTextView.setVisibility(VISIBLE);
        }
        return this;
    }

    /**
     * ------------------------------------右侧toolbar按钮设置----------------------------------------
     **/
    public TitleBar setRightIcon(@NonNull Drawable icon) {
        if (rightTextView != null && icon != null) {
            icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight()); //设置边界
            rightTextView.setCompoundDrawables(icon, null, null, null);//画在左边
        }
        return this;
    }

    public TitleBar setRightIcon(@DrawableRes int drawableId) {
        Drawable icon = getResources().getDrawable(drawableId);
        return setRightIcon(icon);
    }

    public TitleBar setRightText(@NonNull CharSequence text) {
        rightTextView.setText(text);
        return this;
    }

    public TitleBar setRightOnClickListener(@NonNull OnClickListener listener) {
        if (listener != null) {
            rightTextView.setOnClickListener(listener);
        }
        return this;
    }

    public TitleBar setRightClickEnable(@NonNull boolean clickAble) {
        rightTextView.setEnabled(clickAble);
        return this;
    }

    public TitleBar setRightAsButton(@DrawableRes int res) {
        rightTextView.setBackgroundResource(res);
        int left = (int) (8 * context.getResources().getDisplayMetrics().density);
        int top = (int) (4 * context.getResources().getDisplayMetrics().density);
        int right = (int) (8 * context.getResources().getDisplayMetrics().density);
        int bottom = (int) (4 * context.getResources().getDisplayMetrics().density);
        rightTextView.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * ------------------------------------右侧toolbar的menu菜单按钮设置----------------------------------------
     **/
    public TitleBar setRightMenu(@NonNull final List<String> popupData, @NonNull final OnMenuItemClickListener listener) {
        setRightOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(rightTextView, popupData, listener);
            }
        });
        return this;
    }

    /**
     * @param view      做为参考坐标的View
     * @param popupData 数据源
     * @param listener  点击监听
     */
    private void showPopMenu(@NonNull View view, @NonNull List<String> popupData, @NonNull final OnMenuItemClickListener listener) {
        if (popupWindowMenu != null && popupWindowMenu.isShowing()) {
            //关闭popupWindow
            popupWindowMenu.dismiss();
        } else {
            final View popupView = LayoutInflater.from(context).inflate(R.layout.title_menu, null);
            popupWindowMenu = new PopupWindow(popupView, ListPopupWindow.WRAP_CONTENT, ListPopupWindow.WRAP_CONTENT, true);

            //设置弹出的popupWindow不遮挡软键盘
            popupWindowMenu.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindowMenu.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            //设置点击外部让popupWindow消失
            popupWindowMenu.setFocusable(true);//可以试试设为false的结果
            popupWindowMenu.setOutsideTouchable(true); //点击外部消失

            //必须设置的选项
            popupWindowMenu.setBackgroundDrawable(ContextCompat.getDrawable(context, android.R.color.transparent));
            popupWindowMenu.setTouchInterceptor(new OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                }
            });
            //将window视图显示在点击按钮下面(向上偏移20像素)
            popupWindowMenu.showAsDropDown(view, 0, 0);
            ListView listView = (ListView) popupView.findViewById(R.id.pop_listView);
            listView.setAdapter(new MenuAdapter(popupData));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    listener.onItemClick(position);
                    popupWindowMenu.dismiss();
                }
            });
        }
    }

    /**
     * -------------------------------------设置字体颜色---------------------------------------------
     **/
    public TitleBar setLeftTextColor(@ColorInt int textColor) {
        if (leftTextView != null && textColor != 0)
            leftTextView.setTextColor(textColor);
        return this;
    }

    public TitleBar setTitleTextColor(@ColorInt int textColor) {
        if (titleTextView != null && textColor != 0)
            titleTextView.setTextColor(textColor);
        return this;
    }

    public TitleBar setRightTextColor(@ColorInt int textColor) {
        if (rightTextView != null && textColor != 0)
            rightTextView.setTextColor(textColor);
        return this;
    }

    public TitleBar setTextColor(@ColorInt int textColor) {
        setLeftTextColor(textColor);
        setTitleTextColor(textColor);
        setRightTextColor(textColor);
        return this;
    }

    /**
     * -------------------------------------设置字体的大小---------------------------------------------------
     */
    public TitleBar setLeftTextSize(float size) {
        if (leftTextView != null && size != 0)
            leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setTitleTextSize(float size) {
        if (titleTextView != null && size != 0)
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setRightTextSize(float size) {
        if (rightTextView != null && size != 0)
            rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public interface OnMenuItemClickListener {
        void onItemClick(int position);
    }

    /**
     * menu菜单适配器
     */
    public class MenuAdapter extends BaseAdapter {

        private List<String> popupData;

        public MenuAdapter(List<String> popupData) {
            this.popupData = popupData;
        }

        @Override
        public int getCount() {
            return popupData.size();
        }

        @Override
        public Object getItem(int i) {
            return popupData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.title_menu_item, viewGroup, false);
                holder.content = view.findViewById(R.id.tv_item_content);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.content.setText(popupData.get(i));
            return view;
        }

        public class ViewHolder {
            private TextView content;
        }
    }

    private int dp2Dx(int dp) {
        return (int) (dp * context.getResources()
                .getDisplayMetrics().scaledDensity);
    }
}
