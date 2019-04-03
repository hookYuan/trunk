package com.yuan.base.tools.okhttp;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by YuanYe on 2017/9/26.
 */
public class Execute {

    protected Request.Builder requestBuilder;
    protected OkHttpClient client;
    protected Context mContext;

    public Execute(Context context, Request.Builder request, OkHttpClient _client) {
        this.requestBuilder = request;
        this.client = _client;
        this.mContext = context;
    }

    /**
     * ****************************callBack请求封装****************************************
     */
    //统一对requestBuild处理，
    private Request getRequestBuild() {
        return requestBuilder.build();
    }

    //统一返回
    public void execute(BaseMainBack mainBack) {
        if (mainBack == null) throw new NullPointerException("回调：RxCall == null");
        MainCall baseFileBack = new MainCall(mainBack);
        client.newCall(getRequestBuild())
                .enqueue(baseFileBack);
    }
}
