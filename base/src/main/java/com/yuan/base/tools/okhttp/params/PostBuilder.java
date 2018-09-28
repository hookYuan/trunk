package com.yuan.base.tools.okhttp.params;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yuan.base.tools.common.Kits;
import com.yuan.base.tools.okhttp.Execute;
import com.yuan.base.tools.okhttp.callback.BaseBack;
import com.yuan.base.tools.okhttp.callback.FileBack;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by YuanYe on 2017/9/26.
 * 用于构造Post所需的所有参数
 */
public class PostBuilder extends HeadBuilder<PostBuilder> {

    private FormBody.Builder builder;

    public PostBuilder(@NonNull Context context, @NonNull Request.Builder request
            , @NonNull OkHttpClient _client, @NonNull FormBody.Builder builder) {
        super(context, request, _client);
        this.builder = builder;
    }

    /**
     * 添加参数
     *
     * @param params
     * @return
     */
    public PostBuilder post(@NonNull Map<String, String> params) {
        if (Kits.Empty.isEmpty(params)) throw new NullPointerException("参数：params == null");
        //设置参数
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public PostBuilder post(@NonNull String key, @NonNull String value) {
        if (Kits.Empty.isEmpty(key)) throw new NullPointerException("参数：params.key == null");
        builder.add(key, value);
        return this;
    }

    /**
     * ************************************执行部分****************************************
     *
     * @param fileBack
     */
    public void execute(@NonNull FileBack fileBack) {
        requestBuilder.post(builder.build());
        new Execute(mContext, requestBuilder, client).execute(fileBack);
    }

    public void execute(@NonNull BaseBack jsonBack) {
        requestBuilder.post(builder.build());
        new Execute(mContext, requestBuilder, client).execute(jsonBack);
    }

}
