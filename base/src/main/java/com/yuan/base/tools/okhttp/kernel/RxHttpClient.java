package com.yuan.base.tools.okhttp.kernel;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by YuanYe on 2017/7/19.
 *
 * @author yuanye
 * @version 1.0
 * 本类设计说明：
 * 根据ClientBuilder构建Client初始化，
 * 根据CacheInterceptor实现OKHttp缓存,
 * 本类主要用于创建OKHttpClient对象,一次请求创建一个OKHttpClient对象
 */
public class RxHttpClient {

    private static final String TAG = "RxHttpClient";

    private OkHttpClient client; //主要创建的网络请求client
    private OKConfig config; //配置参数
    private Context mContext; //上下文

    public RxHttpClient(@NonNull Context context) {
        this.mContext = context;
        this.config = new OKConfig.Builder()
                .isReConnect(true)
                .build();
    }

    /**
     * 传入构建参数创建OkHttpClient
     *
     * @param context 上下文
     * @param _config 基本配置
     */
    public RxHttpClient(@NonNull Context context, @NonNull OKConfig _config) {
        this.mContext = context;
        this.config = _config;
    }

    /**
     * 创建OKHttpClient
     *
     * @return 返回创建的OKHttpClient实体
     */
    public OkHttpClient getClient() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //失败后是否重新连接
            builder.retryOnConnectionFailure(config.isReConnect());
            //连接超时
            builder.connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS);
            //读取超时
            builder.readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);
            //设置cookie，保存cookie,读取cookie
            CookieJar cookieJar = config.getCookie();
            if (cookieJar != null) {
                builder.cookieJar(cookieJar);
            }
            //设置缓存路径
            File cacheFile = new File(getCachePath(), config.getCacheFolder());
            if (cacheFile.getParentFile().exists())
                cacheFile.mkdirs();
            //设置缓存大小
            Cache cache = new Cache(cacheFile, config.getMaxCacheSize());
            //设置缓存拦截器，实现网络缓存(有网络的时候不缓存，没有网络的时候缓存)
            builder.addInterceptor(new CacheInterceptor(mContext))
                    .addNetworkInterceptor(new CacheInterceptor(mContext))
                    .cache(cache);
            //添加请求头
            client = builder.build();
        }
        return client;
    }


    public OKConfig getConfig() {
        return config;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 获取OKHttp缓存文件目录地址
     *
     * @return 地址
     */
    private @NonNull
    String getCachePath() {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return cachePath;
    }

}
