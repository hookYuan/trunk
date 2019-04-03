package com.yuan.base.tools.okhttp.kernel;

import android.support.annotation.NonNull;

import okhttp3.CookieJar;

/**
 * Created by YuanYe on 2017/9/8.
 * 用于配置OkHttpClient，采用build模式进行配置
 * 用于配置单次OkHttp请求参数，在OKHttp创建时初始化使用
 */
public class OKConfig {

    private final static long CONNECTTIMEOUT = 10 * 1000l; //链接超时，单位：毫秒
    private final static long READTIMEOUT = 10 * 1000l;//读取超时， 单位：毫秒
    private final static String CACHEFOLDER = "okCache"; //默认网络缓存文件夹

    private long connectTimeout; //连接超时时间
    private long readTimeout;//读取超时时间
    private CookieJar cookie;//设置Cookie
    private boolean isReConnect; //是否重新连接
    private String commonHead;  //公共头部
    private String commonHeadKey;//公共头部key

    private String cacheFolder;//OKHttp缓存文件存放的文件夹位置
    private long maxCacheSize;//最大缓存的大小

    private OKConfig(Builder builder) {
        connectTimeout = builder.connectTimeout;
        readTimeout = builder.readTimeout;
        cookie = builder.cookie;
        isReConnect = builder.isReConnect;
        commonHead = builder.commonHead;
        commonHeadKey = builder.commonHeadKey;
        cacheFolder = builder.cacheFolder;
        maxCacheSize = builder.maxCacheSize;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public CookieJar getCookie() {
        return cookie;
    }

    public boolean isReConnect() {
        return isReConnect;
    }

    public String getCommonHead() {
        return commonHead;
    }

    public String getCommonHeadKey() {
        return commonHeadKey;
    }

    public String getCacheFolder() {
        return cacheFolder;
    }

    public long getMaxCacheSize() {
        return maxCacheSize;
    }

    public static final class Builder {
        private long connectTimeout;
        private long readTimeout;
        private CookieJar cookie;
        private boolean isReConnect;
        private String commonHead;
        private String commonHeadKey;
        private String cacheFolder;
        private long maxCacheSize;

        public Builder() {
            connectTimeout = CONNECTTIMEOUT;
            readTimeout = READTIMEOUT;
            cacheFolder = CACHEFOLDER;
            //默认缓存大小为当先线程的八分之一
            maxCacheSize = Runtime.getRuntime().maxMemory() / 8;
        }

        public Builder connectTimeout(long val) {
            connectTimeout = val;
            return this;
        }

        public Builder readTimeout(long val) {
            readTimeout = val;
            return this;
        }

        public Builder cookie(CookieJar val) {
            cookie = val;
            return this;
        }

        public Builder isReConnect(boolean val) {
            isReConnect = val;
            return this;
        }

        public Builder commonHead(@NonNull String val) {
            commonHead = val;
            return this;
        }

        public Builder commonHeadKey(@NonNull String val) {
            commonHeadKey = val;
            return this;
        }

        public Builder cacheFolder(@NonNull String val) {
            cacheFolder = val;
            return this;
        }

        public Builder maxCacheSize(long val) {
            maxCacheSize = val;
            return this;
        }

        public OKConfig build() {
            return new OKConfig(this);
        }
    }
}
