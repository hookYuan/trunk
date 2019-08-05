/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package yuan.core.title;

import android.annotation.SuppressLint;
import android.view.Gravity;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import yuan.core.R;
import yuan.core.tool.Views;

/**
 * 描述：将TitleBar 添加到系统 ActionBar
 *
 * @author yuanye
 * @date 2019/7/18 13:36
 */
public class ActionBarUtil {

    private final static String TAG = "ActionBarUtil";
    /**
     * TitleBar
     * 避免内存泄漏
     */
    private WeakReference<TitleBar> mTitleBar;

    /**
     * 全局主题样式
     */
    private DefaultTheme mDefaultTheme;

    /**
     * ActionBar 的高度
     */
    private int mElevation = 0;

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
        TitleBar titleBar = Views.inflate(activity, R.layout.action_bar_title_bar_layout);
        mTitleBar = new WeakReference<>(titleBar);

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setElevation(mElevation);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //去掉actionBar默认留白，使自定义view以最大宽高显示
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL;
        actionBar.setCustomView(titleBar, layoutParams);
        titleBar.getParent();
        Toolbar parent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            parent = (Toolbar) titleBar.getParent();
            //设置左右间隔为0
            parent.setContentInsetsAbsolute(0, 0);
        }
    }

    /**
     * 初始化顶部ActionBar
     *
     * @param activity 当前Activity
     */
    @SuppressLint("ResourceType")
    public static TitleBar create(AppCompatActivity activity) {
        ActionBarUtilsInstance.instance.init(activity);
        if (ActionBarUtilsInstance.instance.mDefaultTheme != null) {
            ActionBarUtilsInstance.instance.mDefaultTheme
                    .defaultTheme(ActionBarUtilsInstance.instance.mTitleBar.get());
        }
        return ActionBarUtilsInstance.instance.mTitleBar.get();
    }

    /**
     * 设置全局样式
     * 局部样式优先于全局样式
     *
     * @param defaultTheme
     */
    public static void setDefaultTheme(DefaultTheme defaultTheme) {
        ActionBarUtilsInstance.instance.mDefaultTheme = defaultTheme;
    }

    /**
     * 设置ActionBar高度
     */
    public static void setElevation(int elevation) {
        ActionBarUtilsInstance.instance.mElevation = elevation;
    }

    /**
     * 隐藏ActionBar
     */
    public static void hideActionBar(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.hide();
    }

    /**
     * 显示ActionBar
     */
    public static void showActionBar(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.show();
    }

    /**
     * 全局初始化
     *
     * @return
     */
    public interface DefaultTheme {
        void defaultTheme(TitleBar titleBar);
    }

}
