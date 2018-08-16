package com.yuan.base.tools.okhttp.params;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yuan.base.tools.other.Kits;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by YuanYe on 2017/9/26.
 * 用于构造OKUtil头部
 */
class HeadBuilder<T extends HeadBuilder> {

    protected Request.Builder requestBuilder;
    protected OkHttpClient client;
    protected Context mContext;

    public HeadBuilder(@NonNull Context context, @NonNull Request.Builder request
            , @NonNull OkHttpClient _client) {
        this.requestBuilder = request;
        this.client = _client;
        this.mContext = context;
    }

    /**
     * ****************************addHead请求封装****************************************
     */
    public T addHead(@NonNull Map<String, String> params) {
        if (Kits.Empty.check(params)) throw new NullPointerException("参数：params == null");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        return (T) this;
    }

    public T addHead(@NonNull String key, @NonNull String value) {
        if (Kits.Empty.check(key)) throw new NullPointerException("参数：params.key == null");
        requestBuilder.addHeader(key, value);
        return (T) this;
    }

}
