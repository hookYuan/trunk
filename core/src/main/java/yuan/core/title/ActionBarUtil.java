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

    /**
     * 全局主题样式
     */
    private DefaultTheme mDefaultTheme;

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
        if (ActionBarUtilsInstance.instance.mDefaultTheme != null) {
            ActionBarUtilsInstance.instance.mDefaultTheme
                    .defaultTheme(ActionBarUtilsInstance.instance.titleBar);
        }
        return ActionBarUtilsInstance.instance.titleBar;
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
     * 全局初始化
     *
     * @return
     */
    public interface DefaultTheme {
        void defaultTheme(TitleBar titleBar);
    }

}
