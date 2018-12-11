package com.yuan.base.tools.okhttp.callback.construct;

import android.util.Log;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 接替OKHttp返回回调
 *
 * @author yuanye
 * @date 2018/11/28 11:56
 */
public class MainCall implements Callback {

    public static final String TAG = "MainCall";

    /**
     * 回调
     */
    private BaseMainBack mainBack;

    public MainCall(BaseMainBack mainBack) {
        this.mainBack = mainBack;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        runMainException(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        BaseResponse baseResponse = new BaseResponse();
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
        Observable.create(new ObservableOnSubscribe<Exception>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Exception> e) throws Exception {
                e.onNext(exception);
            }
        }).observeOn(AndroidSchedulers.mainThread()) //指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<Exception>() {

                    @Override
                    public void accept(@NonNull Exception response) throws Exception {
                        Log.d(TAG, "error:" + response.getMessage());
                        mainBack.onFail(response);
                    }
                });
    }
}
