package yuan.core.title;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import yuan.core.R;
import yuan.core.tool.Views;

/**
 * 描述：将TitleBar 添加到系统 ActionBar
 *
 * @author yuanye
 * @date 2019/7/18 13:36
 */
public class ActionBarUtil {
    /**
     * 标题
     */
    private TitleBar titleBar;

    private static class ActionBarUtilsInstance {
        private static ActionBarUtil instance = new ActionBarUtil();
    }

    private ActionBarUtil() {
    }

    /**
     * 初始化
     *
     * @param activity
     */
    private void init(AppCompatActivity activity) {
        titleBar = Views.inflate(activity, R.layout.action_bar_title_layout);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(titleBar);
    }

    /**
     * 初始化顶部ActionBar
     *
     * @param activity 当前Activity
     */
    @SuppressLint("ResourceType")
    public static TitleBar create(AppCompatActivity activity) {
        ActionBarUtilsInstance.instance.init(activity);
        return ActionBarUtilsInstance.instance.titleBar;
    }

}
