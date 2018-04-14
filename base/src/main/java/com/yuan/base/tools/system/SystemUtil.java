package com.yuan.base.tools.system;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.FloatRange;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by YuanYe on 2017/7/10.
 * <p>
 * 与Android 系统有关的工具类
 */

public class SystemUtil {

    /**
     * 和输入有关的工具类
     */
    public static class Input {
        /**
         * 收起键盘
         */
        public static void hideSoftInput(Activity context) {
            try {
                if (context == null) return;
                InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                Log.i("TAG", "----" + e.toString());
            }
        }
    }

    public static class SDcard {
        /**
         * 获取手机外部存储路径
         */
        public static String getCachPath(Context context) {
            String cachePath;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalCacheDir().getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
            return cachePath;
        }
    }

}
