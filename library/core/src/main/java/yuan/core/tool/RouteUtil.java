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
package yuan.core.tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 描述：路由辅助跳转
 * <p>
 * 1.Activity跳转辅助类（跳转参数携带一律从getIntent（）获取 open
 * 2.系统设置调整辅助  openSetting
 * 3.系统播放文件跳转  openPlay
 * 4.权限检查和声情    requestPermission
 *
 * @author yuanye
 * @date 2019/4/4 9:13
 */
public class RouteUtil {

    private final static String TAG = "RouteUtil";
    /**
     * 系统设置
     */
    public static final String SETTING = "systemSetting";
    /**
     * 系统的辅助功能界面
     */
    public static final String ACCESSIBILITY = "accessibility";
    /**
     * 开发者模式界面
     */
    public static final String DEVELOPMENT = "development";
    /**
     * 添加帐户界面
     */
    public static final String ACCOUNT = "account";
    /**
     * 系统的包含飞行模式的界面
     */
    public static final String AIRPLANE = "airplane";
    /**
     * 系统的应用管理界面一(默认应用界面)
     */
    public static final String APPLICATION1 = "application1";
    /**
     * 系统的应用管理界面二(默认应用界面)
     */
    public static final String APPLICATION2 = "application2";
    /**
     * 系统的应用管理界面三(全部应用界面)
     */
    public static final String APPLICATION3 = "application3";
    /**
     * 系统的蓝牙管理界面
     */
    public static final String BLUETOOTH = "bluetooth";
    /**
     * 系统的SIM卡和网络管理界面
     */
    public static final String SIM = "sim";
    /**
     * 系统的NFC设置界面(需要手机支持NFC且API>=16)
     */
    public static final String NFC = "nfc";
    /**
     * 系统的语言选择界面
     */
    public static final String LANGUAGE = "Language";
    /**
     * 系统的声音设置界面
     */
    public static final String SOUND = "sound";
    /**
     * 系统的wifi界面
     */
    public static final String WIFI = "wifi";
    /**
     * 系统的定位服务界面
     */
    public static final String LOCATION = "location";
    /**
     * 文件类型后缀
     */
    private static final String[][] MIME_MapTable = {{".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"}, {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"}, {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"}, {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"}, {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, {".xls", "application/vnd.ms-excel"}, {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"}, {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"}, {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"}, {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"}, {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"}, {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"}, {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"}, {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"}, {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"}, {".ppt", "application/vnd.ms-powerpoint"}, {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, {".prop", "text/plain"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"}, {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/x-zip-compressed"}, {"", "*/*"}};
    /**
     * 提供的默认请求码
     */
    public static final int REQUEST_CODE = 10021;
    /**
     * 启动等待时间，200毫秒内只启动一次
     */
    private static final long SPACE_TIME = 50;
    /**
     * 上次启动时间，用于就判断重复启动
     */
    private static long lastStartTime = 0;

    /**
     * Activity跳转参数
     */
    private static RouteParam param;

    /**
     * 跳转Activity
     *
     * @param mContext 上下文
     * @param clazz    目标Activity
     */
    public static void open(Context mContext, Class clazz) {
        open(mContext, clazz, null, false);
    }

    /**
     * 跳转Activity
     *
     * @param mContext 上下文
     * @param clazz    目标Activity
     * @param param    参数传递，通过getIntent()获取参数
     */
    public static void open(Context mContext, Class clazz, RouteParam param) {
        open(mContext, clazz, param, false);
    }

    /**
     * 跳转Activity
     *
     * @param mContext   上下文
     * @param clazz      目标Activity
     * @param finishSelf 是否结束当前Activity
     */
    public static void open(Context mContext, Class clazz, boolean finishSelf) {
        open(mContext, clazz, null, finishSelf);
    }

    /**
     * 跳转Activity
     *
     * @param mContext   上下文
     * @param clazz      目标Activity
     * @param param      参数传递，通过getIntent()获取参数
     * @param finishSelf 是否结束当前Activity
     */
    public static void open(Context mContext, Class clazz, RouteParam param, boolean finishSelf) {
        /*取消重复跳转，200毫秒内只跳转一次*/
        if (System.currentTimeMillis() - lastStartTime > SPACE_TIME) {
            Intent intent = createIntent(mContext, clazz, param);
            if (intent == null) return;
            mContext.startActivity(intent);

            /*跳转前的Activity,是否结束*/
            AppCompatActivity fromActivity = AppCompatActivity.class.cast(mContext);
            if (fromActivity != null && finishSelf) {
                fromActivity.finish();
            }
            lastStartTime = System.currentTimeMillis();
        }
    }

    /**
     * 跳转Activity并获取返回结果
     *
     * @param mContext     上下文
     * @param clazz        目标Activity
     * @param REQUEST_CODE 请求码
     * @param listener     返回结果监听
     */
    public static void openResult(Context mContext, Class clazz, int REQUEST_CODE, OnActivityResultListener listener) {
        openResult(mContext, clazz, null, REQUEST_CODE, listener);
    }

    /**
     * 跳转Activity并获取返回结果
     *
     * @param mContext 上下文
     * @param clazz    目标Activity
     * @param listener 返回结果监听
     */
    public static void openResult(Context mContext, Class clazz, OnActivityResultListener listener) {
        openResult(mContext, clazz, REQUEST_CODE, listener);
    }

    /**
     * 跳转Activity并获取返回结果
     *
     * @param mContext 上下文
     * @param clazz    目标Activity
     * @param param    参数传递，通过getIntent()获取参数
     * @param listener 返回结果监听
     */
    public static void openResult(Context mContext, Class clazz, RouteParam param, OnActivityResultListener listener) {
        Intent intent = createIntent(mContext, clazz, param);
        if (intent == null) return;
        openResult(mContext, intent, REQUEST_CODE, listener);
    }

    /**
     * 跳转Activity并获取返回结果
     *
     * @param mContext     上下文
     * @param clazz        目标Activity
     * @param param        参数传递，通过getIntent()获取参数
     * @param REQUEST_CODE 请求码
     * @param listener     返回结果监听
     */
    public static void openResult(Context mContext, Class clazz, RouteParam param, int REQUEST_CODE, OnActivityResultListener listener) {
        Intent intent = createIntent(mContext, clazz, param);
        if (intent == null) return;
        openResult(mContext, intent, REQUEST_CODE, listener);
    }

    /**
     * 跳转Activity并获取返回结果
     *
     * @param mContext        上下文
     * @param clazz           目标Activity
     * @param param           参数传递，通过getIntent()获取参数
     * @param REQUEST_CODE    请求码
     * @param listener        返回结果监听
     * @param ignoreSpaceTime 是否忽略连续点击时间间隔
     */
    public static void openResult(Context mContext, Class clazz, RouteParam param, int REQUEST_CODE, OnActivityResultListener listener, boolean ignoreSpaceTime) {
        Intent intent = createIntent(mContext, clazz, param);
        if (intent == null) return;
        openResult(mContext, intent, REQUEST_CODE, listener, ignoreSpaceTime);
    }

    /**
     * 跳转Activity并获取返回结果
     *
     * @param mContext 上下文
     * @param intent   intent
     * @param listener 返回结果监听
     */
    public static void openResult(Context mContext, Intent intent, OnActivityResultListener listener) {
        openResult(mContext, intent, REQUEST_CODE, listener);
    }

    /**
     * 跳转Activity并获取返回结果
     *
     * @param mContext     上下文
     * @param intent       intent
     * @param REQUEST_CODE 请求码
     * @param listener     返回结果监听
     */
    public static void openResult(Context mContext, Intent intent, int REQUEST_CODE, OnActivityResultListener listener) {
        //取消重复跳转，200毫秒内只跳转一次
        openResult(mContext, intent, REQUEST_CODE, listener, false);
    }

    /**
     * 跳转Activity并获取返回结果
     *
     * @param mContext        上下文
     * @param intent          intent
     * @param REQUEST_CODE    请求码
     * @param listener        返回结果监听
     * @param ignoreSpaceTime 是否忽略连续点击时间间隔
     */
    public static void openResult(Context mContext, Intent intent, int REQUEST_CODE, OnActivityResultListener listener, boolean ignoreSpaceTime) {
        //取消重复跳转，200毫秒内只跳转一次
        if ((System.currentTimeMillis() - lastStartTime > SPACE_TIME) || ignoreSpaceTime) {
            ResultFragmentManager jumpResult = new ResultFragmentManager(mContext);
            jumpResult.getFragment().startForResult(intent, REQUEST_CODE, listener);
            lastStartTime = System.currentTimeMillis();
        }
    }


    /**
     * 跳转对应的设置界面
     *
     * @param mContext 上下文
     * @param action   目标页面对应的常量
     */
    public static void openSetting(@NonNull Context mContext, String action) {
        switch (action) {
            case SETTING:
                action = Settings.ACTION_SETTINGS;
                break;
            case ACCESSIBILITY:
                action = Settings.ACTION_ACCESSIBILITY_SETTINGS;
                break;
            case ACCOUNT:
                action = Settings.ACTION_ADD_ACCOUNT;
                break;
            case AIRPLANE:
                action = Settings.ACTION_AIRPLANE_MODE_SETTINGS;
                break;
            case DEVELOPMENT:
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS;
                break;
            case APPLICATION1:
                action = Settings.ACTION_APPLICATION_SETTINGS;
                break;
            case APPLICATION2:
                action = Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS;
                break;
            case APPLICATION3:
                action = Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS;
                break;
            case BLUETOOTH:
                action = Settings.ACTION_BLUETOOTH_SETTINGS;
                break;
            case SIM:
                action = Settings.ACTION_DATA_ROAMING_SETTINGS;
                break;
            case LANGUAGE:
                action = Settings.ACTION_LOCALE_SETTINGS;
                break;
            case NFC:
                action = Settings.ACTION_NFC_SETTINGS;
                break;
            case SOUND:
                action = Settings.ACTION_SOUND_SETTINGS;
                break;
            case WIFI:
                action = Settings.ACTION_WIFI_SETTINGS;
                break;
            case LOCATION:
                action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                break;
            default:
                break;
        }
        Intent intent = new Intent(action);
        mContext.startActivity(intent);
    }


    /**
     * 用系统默认的软件播放文件
     *
     * @param file 需要打开的文件
     */
    public static void openPlay(@NonNull AppCompatActivity activity, @NonNull File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = getMIMEType(file);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, type);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    /**
     * 查询是否有相应的权限
     *
     * @param mContext
     * @param permission 需要检查的权限
     * @return
     */
    public static boolean checkPermission(Context mContext, String permission) {
        // 相机的权限申请逻辑
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /**
     * 申请打开权限
     *
     * @param mContext
     * @param permissions  申请权限数组
     * @param REQUEST_CODE 请求码
     * @param listener     返回监听
     */
    public static void openPermission(Context mContext, String[] permissions, int REQUEST_CODE, OnPermissionListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ResultFragmentManager jumpResult = new ResultFragmentManager(mContext);
            jumpResult.getFragment().startPermission(permissions, REQUEST_CODE, listener);
        }
    }

    /**
     * 申请打开权限
     *
     * @param mContext
     * @param permissions 申请权限数组
     * @param listener    返回监听
     */
    public static void openPermission(Context mContext, String[] permissions, OnPermissionListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openPermission(mContext, permissions, REQUEST_CODE, listener);
        }
    }

    /**
     * 获取文件类型后缀
     *
     * @param paramFile
     * @return
     */
    private static String getMIMEType(File paramFile) {
        String str1 = "*/*";
        String str2 = paramFile.getName();
        int i = str2.lastIndexOf(".");
        if (i < 0)
            return str1;
        String str3 = str2.substring(i, str2.length()).toLowerCase();
        if (str3 == "")
            return str1;
        for (int j = 0; ; j++) {
            if (j >= MIME_MapTable.length)
                return str1;
            if (str3.equals(MIME_MapTable[j][0]))
                str1 = MIME_MapTable[j][1];
        }
    }

    /**
     * 构造Intent
     *
     * @param mContext
     * @param activityClazz
     * @param param
     * @return
     */
    private static Intent createIntent(Context mContext, Class activityClazz, @Nullable RouteParam param) {
        return createIntent(mContext, activityClazz.getName(), param);
    }

    /**
     * 构造Intent
     *
     * @param mContext            上下文
     * @param activityPackageName 目标调整Activity类名
     * @param param               参数
     * @return intent
     */
    private static Intent createIntent(Context mContext, String activityPackageName, @Nullable RouteParam param) {

        /*创建Intent对象*/
        Intent intent = new Intent();
        intent.setClassName(mContext, activityPackageName);

        /*向Intent中添加参数*/
        if (param != null) {
            HashMap<String, Object> map = param.getAttr();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                Object val = entry.getValue();
                if (val instanceof String) {
                    intent.putExtra(key, (String) val);
                } else if (val instanceof String[]) {
                    intent.putExtra(key, (String[]) val);
                } else if (val instanceof Boolean) {
                    intent.putExtra(key, (boolean) val);
                } else if (val instanceof Boolean[]) {
                    intent.putExtra(key, (boolean[]) val);
                } else if (val instanceof Integer) {
                    intent.putExtra(key, (int) val);
                } else if (val instanceof Integer[]) {
                    intent.putExtra(key, (int[]) val);
                } else if (val instanceof Long) {
                    intent.putExtra(key, (long) val);
                } else if (val instanceof Long[]) {
                    intent.putExtra(key, (long[]) val);
                } else if (val instanceof Float) {
                    intent.putExtra(key, (float) val);
                } else if (val instanceof Float[]) {
                    intent.putExtra(key, (float[]) val);
                } else if (val instanceof Double) {
                    intent.putExtra(key, (double) val);
                } else if (val instanceof Double[]) {
                    intent.putExtra(key, (double[]) val);
                } else if (val instanceof Serializable) {
                    intent.putExtra(key, (Serializable) val);
                } else if (val instanceof Bundle) {
                    intent.putExtra(key, (Bundle) val);
                } else if (val instanceof Parcelable) {
                    intent.putExtra(key, (Parcelable) val);
                }
            }
            if (param.getStringList() != null) {
                intent.putStringArrayListExtra(param.getStringListKey(), param.getStringList());
            }
            if (param.getParcelableList() != null) {
                intent.putParcelableArrayListExtra(param.getParcelableKey(), param.getParcelableList());
            }
        }

        /*判断Intent是否有效，即是否在Manifest种注册*/
        ComponentName componentName = intent.resolveActivity(mContext.getPackageManager());
        if (componentName == null) {//如果为null，表示匹配失败,异常提示避免崩溃
            Log.e(TAG, "请检查目标Activity是否存在，如果存在请检查Manifest文件是否注册");
            return null;
        }
        return intent;
    }

    /**
     * startActivity 返回结果处理
     *
     * @author yuanye
     * @date 2018/12/11
     */
    public interface OnActivityResultListener {
        void onActivityResult(int REQUEST_CODE, int resultCode, @Nullable Intent data);
    }

    /**
     * @author yuanye
     * @date 2018/12/11
     */
    public interface OnPermissionListener {
        /**
         * 权限请求结果
         *
         * @param REQUEST_CODE 请求码
         * @param result       对应的权限请求结果，true--有权限，false--没有权限
         */
        void onResult(int REQUEST_CODE, @NonNull String[] permissions, @NonNull boolean[] result);
    }

    /**
     * 构建Intent参数
     *
     * @return
     */
    public static RouteParam buildParam() {
        param = new RouteParam();
        return param;
    }

    /**
     * Created by YuanYe on 2018/5/14.
     * 用于构建跳转参数
     */
    public static class RouteParam {

        private HashMap<String, Object> attr;

        private String stringListKey;
        private ArrayList<String> stringList;
        private String parcelableKey;
        private ArrayList<Parcelable> ParcelableList;

        private RouteParam() {
            if (attr == null) attr = new HashMap<>();
        }

        public RouteParam put(String key, String value) {
            attr.put(key, value);
            return param;
        }

        public RouteParam put(String key, int value) {
            attr.put(key, value);
            return param;
        }

        public RouteParam put(String key, long value) {
            attr.put(key, value);
            return param;
        }

        public RouteParam put(String key, double value) {
            attr.put(key, value);
            return param;
        }

        public RouteParam put(String key, boolean value) {
            attr.put(key, value);
            return param;
        }

        public RouteParam put(String key, ArrayList value) {
            attr.put(key, value);
            return param;
        }

        public RouteParam put(String key, Serializable value) {
            attr.put(key, value);
            return param;
        }

        public RouteParam putStringArrayList(String key, ArrayList<String> value) {
            this.stringListKey = key;
            this.stringList = value;
            return param;
        }

        public RouteParam putParcelableArrayList(String key, ArrayList<Parcelable> value) {
            this.ParcelableList = value;
            return param;
        }

        protected ArrayList<Parcelable> getParcelableList() {
            return ParcelableList;
        }

        protected String getParcelableKey() {
            return parcelableKey;
        }

        protected String getStringListKey() {
            return stringListKey;
        }

        protected ArrayList<String> getStringList() {
            return stringList;
        }

        protected HashMap<String, Object> getAttr() {
            return attr;
        }

    }

    /**
     * 处理ResultFragment添加到Activity
     *
     * @author yuanye
     * @date 2018/12/11
     */
    private static class ResultFragmentManager {
        /**
         * 标识Fragment
         */
        private static final String TAG = "com.yuan.base.tools.router.jump.ResultFragmentManager";
        /**
         * 处理返回结果的Fragment
         */
        private ResultFragment resultFragment;


        public ResultFragmentManager(AppCompatActivity activity) {
            resultFragment = getFragment(activity);
        }

        public ResultFragmentManager(Context context) {
            this((AppCompatActivity) context);
        }


        private ResultFragment getFragment(AppCompatActivity activity) {
            ResultFragment fragment = (ResultFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
            if (fragment == null) {
                fragment = new ResultFragment();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .add(fragment, TAG)
                        .commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
            return fragment;
        }

        public ResultFragment getFragment() {
            return resultFragment;
        }
    }

    /**
     * 处理startActivityForResult返回结果
     * 处理onRequestPermissionsResult返回结果
     *
     * @author yuanye
     * @date 2018/12/11
     */
    public static class ResultFragment extends Fragment {
        /**
         * onResult回调集合
         */
        private HashMap<Integer, OnActivityResultListener> resultListeners = new HashMap<>();

        /**
         * onPermission回调集合
         */
        private HashMap<Integer, OnPermissionListener> permissionListeners = new HashMap<>();


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        /**
         * 执行启动
         *
         * @param intent
         * @param REQUEST_CODE
         * @param listener
         */
        public void startForResult(Intent intent, int REQUEST_CODE, OnActivityResultListener listener) {
            if (listener != null) resultListeners.put(REQUEST_CODE, listener);
            startActivityForResult(intent, REQUEST_CODE);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void startPermission(String[] permissions, int REQUEST_CODE, OnPermissionListener listener) {
            if (listener != null) permissionListeners.put(REQUEST_CODE, listener);
            requestPermissions(permissions, REQUEST_CODE);
        }

        @Override
        public void onActivityResult(int REQUEST_CODE, int resultCode, Intent data) {
            super.onActivityResult(REQUEST_CODE, resultCode, data);
            //callback方式的处理
            OnActivityResultListener listener = resultListeners.remove(REQUEST_CODE);
            if (listener != null) {
                listener.onActivityResult(REQUEST_CODE, resultCode, data);
            }
        }

        @Override
        public void onRequestPermissionsResult(int REQUEST_CODE, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(REQUEST_CODE, permissions, grantResults);
            OnPermissionListener listener = permissionListeners.remove(REQUEST_CODE);
            if (listener != null) {
                boolean[] results = new boolean[permissions.length];
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        results[i] = true;
                    } else {
                        results[i] = false;
                    }
                }
                listener.onResult(REQUEST_CODE, permissions, results);
            }
        }
    }
}