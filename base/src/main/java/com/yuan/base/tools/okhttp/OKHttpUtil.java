package com.yuan.base.tools.okhttp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yuan.base.tools.okhttp.kernel.OKConfig;
import com.yuan.base.tools.okhttp.kernel.RxHttpClient;
import com.yuan.base.tools.okhttp.params.ParamsBuild;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by YuanYe on 2017/12/27.
 * 全局调用OkHttpUtil,并且全局配置OkHttp
 * <p>
 * 注意：使用使用全局设置，必须在Application中做全局初始化操作
 * 如果不需要做全局配置，可以使用OKHttp分别对每次联网做单独配置
 */
public class OKHttpUtil {

    private static OkHttpClient client;
    private static Request.Builder requestBuilder;
    private static Context mContext;

    private static OKHttpUtil okHttp;
    private static OKConfig okConfig;

    /**
     * 如果需要统一修改
     * @param okConfig
     */
    public static void init(@NonNull OKConfig okConfig) {
        OKHttpUtil.okConfig = okConfig;
    }

    private OKHttpUtil(@NonNull Context context, @NonNull OKConfig config) {
        //获取Client
        client = new RxHttpClient(context, config).getClient();
        requestBuilder = new Request.Builder();
        this.mContext = context;
        if (!TextUtils.isEmpty(config.getCommonHead()) && !TextUtils.isEmpty(config.getCommonHeadKey())) {
            //TODO 公共head,可以统一添加
            requestBuilder.addHeader(config.getCommonHeadKey(), config.getCommonHead());
        }
    }

    public static ParamsBuild url(@NonNull String httpUrl) {
        if (okConfig == null) {
            throw new NullPointerException("全局OkHttp配置参数为空,请确认是否需要全局配置");
        }
        if (okHttp == null) {
            okHttp = new OKHttpUtil(mContext, okConfig);
        }

        if (TextUtils.isEmpty(httpUrl)) {
            throw new NullPointerException("地址：url == null");
        }
        requestBuilder.url(httpUrl);
        return new ParamsBuild(mContext, requestBuilder, client);
    }

    public static ParamsBuild url(@NonNull HttpUrl _httpUrl) {
        if (okConfig == null) {
            throw new NullPointerException("全局OkHttp配置参数为空,请确认是否需要全局配置");
        }
        if (okHttp == null) {
            okHttp = new OKHttpUtil(mContext, okConfig);
        }
        if (_httpUrl == null) {
            throw new NullPointerException("地址：url == null");
        }
        requestBuilder.url(_httpUrl);
        return new ParamsBuild(mContext, requestBuilder, client);
    }
}
