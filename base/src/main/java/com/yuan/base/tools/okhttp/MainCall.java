package com.yuan.base.tools.okhttp.callback;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ResponseBody;

/**
 * 接替OKHttp返回回调
 *
 * @author yuanye
 * @date 2018/11/28 11:56
 */
class MainCall implements Callback {

    private static final String TAG = "MainCall";
    /**
     * 回调
     */
    private BaseMainBack mainBack;
    /**
     * 主线程
     */
    private Handler mainHandler;

    public MainCall(BaseMainBack mainBack) {
        this.mainBack = mainBack;
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, IOException e) {
        runMainException(e);
    }

    @Override
    public void onResponse(Call call, okhttp3.Response response) throws IOException {
        Response baseResponse = new Response();
        baseResponse.code = response.code();
        baseResponse.mssage = response.message();
        baseResponse.requestUrl = response.request().url().url().getPath();

        ResponseBody body = response.body();
        baseResponse.contentLength = body.contentLength();

        baseResponse.body = body;

        try {
            mainBack.onResponse(baseResponse);
        } catch (Exception e) {
            runMainException(e);
        }
    }

    protected void runMainException(Exception exception) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "error:" + exception.getMessage());
                mainBack.onFail(exception);
            }
        });
    }
}
