package com.yuan.base.tools.okhttp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * 拼接get地址
 *
 * @author yuanye
 * @date 2018/11/28 15:26
 */
class HttpUrlUtil {

    /**
     * 地址上添加参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String addParams(String url, @Nullable Map<String, String> params) {
        Map.Entry entry;
        if (params != null && !params.isEmpty()) {
            for (Iterator var2 = params.entrySet().iterator(); var2.hasNext(); url = addParam(url, (String) entry.getKey(), (String) entry.getValue())) {
                entry = (Map.Entry) var2.next();
            }
        }

        return url;
    }

    public static String addParam(String url, String key, String value) {
        if (TextUtils.isEmpty(url)) {
            return url;
        } else {
            String[] subUrlHolder = new String[1];
            url = removeSubUrl(url, subUrlHolder);
            key = key + '=';
            int index = url.indexOf(63);
            if (index < 0) {
                return addFirstParam(url, key, value) + subUrlHolder[0];
            } else {
                int keyIndex = url.indexOf('&' + key, index);
                if (keyIndex == -1) {
                    keyIndex = url.indexOf('?' + key, index);
                }

                return keyIndex != -1 ? replaceParam(url, value, keyIndex + 1 + key.length()) + subUrlHolder[0] : addFollowedParam(url, key, value) + subUrlHolder[0];
            }
        }
    }

    @NonNull
    private static String addFirstParam(String url, String key, String value) {
        return url + '?' + key + value;
    }

    @NonNull
    private static String removeSubUrl(@NonNull String url, @Nullable String[] subUrlHolder) {
        int indexHash = url.indexOf(35);
        if (indexHash < 0) {
            if (subUrlHolder != null) {
                subUrlHolder[0] = "";
            }

            return url;
        } else {
            if (subUrlHolder != null) {
                subUrlHolder[0] = url.substring(indexHash);
            }

            return url.substring(0, indexHash);
        }
    }

    private static String replaceParam(@NonNull String url, String value, int valueStart) {
        int valueEnd = url.indexOf(38, valueStart);
        if (valueEnd == -1) {
            valueEnd = url.length();
        }

        StringBuilder sb = new StringBuilder(url);
        sb.replace(valueStart, valueEnd, value);
        return sb.toString();
    }

    private static String addFollowedParam(@NonNull String url, String key, String value) {
        StringBuilder sb = new StringBuilder(url);
        if (!url.endsWith("&") && !url.endsWith("?")) {
            sb.append('&');
        }

        sb.append(key).append(value);
        return sb.toString();
    }
}
