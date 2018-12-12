package com.yuan.base.tools.okhttp.callback;

import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.yuan.base.tools.common.ReflexUtil;
import com.yuan.base.tools.okhttp.callback.construct.BaseMainBack;
import com.yuan.base.tools.okhttp.callback.construct.BaseResponse;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by YuanYe on 2017/8/4.
 * RxCallBack---用于处理OKHttpUtil返回
 * Gson处理返回--使用RxJava切换处理方法到主线程
 * 支持的json说明：
 * 1、当setUseNetBean（）为空时，T代表完整Json的实体对象
 */
public abstract class JsonBack<T> implements BaseMainBack {

    private final String TAG = "JsonBack";
    /**
     * 根据泛型反射生成对象
     */
    protected T t;

    /**
     * 主线程成功方法
     */
    @MainThread
    public abstract void onSuccess(T t, String json);

    /**
     * 子线程成功方法
     *
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(BaseResponse response) {
        Observable.create(new ObservableOnSubscribe<TempBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<TempBean> e) throws Exception {
                /*处理
                 */
                t = ReflexUtil.getT(JsonBack.this, 0);
                TempBean tempBean = new TempBean();
                String json = response.body.string();
                Log.d(TAG, "-->" + json);
                if (t == null) {
                    tempBean.json = json;
                    //不存在泛型的情况,直接返回json
                    e.onNext(tempBean);
                } else {
                    tempBean.json = json;
                    try {
                        T entity = (T) new Gson().fromJson(json, ReflexUtil.getT(JsonBack.this, 0).getClass());
                        tempBean.t = entity;
                        e.onNext(tempBean);
                    } catch (Exception exception) {
                        runMainException(exception);
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()) //指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<TempBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull TempBean response) {
                        if (TextUtils.isEmpty(response.json)) {
                            runMainException(new NullPointerException("服务返回数据为空,请检查接口"));
                        } else {
                            onSuccess(response.t, response.json);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof Exception) {
                            onFail((Exception) e);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void runMainException(Exception exception) {
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
                        onFail(response);
                    }
                });
    }

    @Override
    public void onFail(Exception e) {

    }

    private class TempBean {
        /**
         * json
         */
        public String json;
        /**
         * T
         */
        public T t;
    }

}
