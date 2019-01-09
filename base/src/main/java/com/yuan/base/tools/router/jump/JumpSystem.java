package com.yuan.base.tools.router.jump;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import java.io.File;

import android.support.annotation.NonNull;

/**
 * 跳转到系统
 *
 * @author yuanye
 * @date 2018/12/12
 */
public class JumpSystem {

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
     * 跳转对应的设置界面
     *
     * @param mContext
     * @param action
     */
    public static void startSetting(@NonNull Context mContext, String action) {
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
     * @param file
     */
    public static void startPlay(@NonNull File file, @NonNull Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = getMIMEType(file);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, type);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
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
}
