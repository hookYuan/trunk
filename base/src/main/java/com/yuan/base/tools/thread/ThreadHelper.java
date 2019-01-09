package com.yuan.base.tools.thread;

import android.os.Handler;
import android.os.Looper;

/**
 * 线程操作辅助类
 *
 * @author yuanye
 * @date 2018/12/24
 */
public class ThreadHelper {

    /**
     * 主线程 Handler
     */
    private static Handler mainHandler;

    /**
     * 运行在主线程
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        mainHandler.post(runnable);
    }

    /**
     * 主线程延迟执行
     *
     * @param runnable
     * @param delayMillis 延迟时间
     */
    public static void runOnUiThread(Runnable runnable, long delayMillis) {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        mainHandler.postDelayed(runnable, delayMillis);
    }


}
