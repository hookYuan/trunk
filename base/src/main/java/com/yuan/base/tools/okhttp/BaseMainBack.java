package com.yuan.base.tools.okhttp.callback;

import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;

/**
 * 执行与主线程
 *
 * @author yuanye
 * @date 2018/11/28 11:44
 */
public interface BaseMainBack {
    /**
     * 子线程调用方法
     */
    @AnyThread
    void onResponse(Response response) throws Exception;

    /**
     * 主线程处理异常方法
     */
    @MainThread
    void onFail(Exception e);

}
