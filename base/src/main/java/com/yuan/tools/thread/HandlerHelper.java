package com.yuan.tools.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @author yuanye
 * @date 2018/12/24
 */
public class HandlerHelper {

    private Handler handler;

    public Handler createHandler(HandleBack handleBack) {
        Looper.prepare();//创建与当前线程相关的Looper
        if (handler == null) {
            handler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    handleBack.handleMessage(msg);
                }
            };
        }
        Looper.loop();//调用此方法，消息才会循环处理
        return handler;
    }

    public void sendMessage() {

    }

    /**
     * 接收消息线程回调
     */
    public interface HandleBack {
        void handleMessage(Message message);
    }
}
