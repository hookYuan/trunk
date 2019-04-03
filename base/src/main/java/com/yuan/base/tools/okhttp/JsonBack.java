package com.yuan.base.tools.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * Created by YuanYe on 2017/8/4.
 * RxCallBack---用于处理OKHttpUtil返回
 * Gson处理返回--使用RxJava切换处理方法到主线程
 * 支持的json说明：
 * 1、当setUseNetBean（）为空时，T代表完整Json的实体对象
 */
public abstract class JsonBack<T> implements BaseMainBack {

    private final static String TAG = "JsonBack";
    /**
     * 主线程
     */
    private Handler mainHandler;

    public JsonBack() {
        mainHandler = new Handler(Looper.getMainLooper());
    }

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
    public void onResponse(Response response) {
        try {
            String json = response.body.string();
            T entity = new Gson().fromJson(json, getClazz());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(entity, json);
                }
            });
        } catch (Exception e) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFail(e);
                }
            });
        }
    }

    @Override
    public void onFail(Exception e) {

    }

    /**
     * 反射泛型生成对象
     */
    private Class<T> getClazz() {
        try {
            return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()
            ).getActualTypeArguments()[0];
        } catch (ClassCastException e) {
            Log.i(TAG, e.getMessage());
        }
        return null;
    }

}
