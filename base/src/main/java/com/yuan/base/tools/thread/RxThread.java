package com.yuan.base.tools.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程操作工具类
 *
 * @author yuanye
 * @date 2018/12/24
 */
public class RxThread {

    /**
     * 标识主线程
     */
    public static final int mainThread = 0x1024256;
    /**
     * 新建子线程
     */
    public static final int newThread = 0x1024257;
    /**
     * 当前线程
     */
    public static final int currentThread = 0x1024258;

    /**
     * 单例
     */
    private static RxThread rxThread;

    /**
     * 主线程Handler
     */
    private Handler mainHandler;

    /**
     * 新建子线程Handler
     */
    private Handler newHandler;

    /**
     * 当前线程Handler
     */
    private Handler currentHandler;

    /**
     * 待调高性能，升级多线程
     */
    private ExecutorService cachedThread;

    /**
     * 标识开始线程
     */
    private ThreadId fromThreadId;

    /**
     * 标识结束线程
     */
    private ThreadId toThreadId;

    /**
     * 新建线程Id
     */
    private long newThreadId;

    private RxThread() {
        cachedThread = Executors.newCachedThreadPool();
        cachedThread.submit(new Runnable() {
            @Override
            public void run() {
                newThreadId = Thread.currentThread().getId();
            }
        });
    }

    public static RxThread onCreate() {
        if (rxThread == null) {
            rxThread = new RxThread();
        }
        return rxThread;
    }

    /**
     * 发送消息线程
     *
     * @param threadTag
     * @param callBack
     */
    private void putThread(ThreadId threadTag, FromBack callBack) {
        switch (threadTag) {
            case currentThread:  //切换到当前
                Message message = currentHandler.obtainMessage();
                callBack.run(message);
                currentHandler.sendMessage(message);
                break;
            case mainThread: //切换到主线程
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Message message = mainHandler.obtainMessage();
                        callBack.run(message);
                        mainHandler.sendMessage(message);
                    }
                });
                break;
            case newThread: //切换到新线程
                cachedThread.submit(new Runnable() {
                    @Override
                    public void run() {
                        Message message = newHandler.obtainMessage();
                        callBack.run(message);
                        newHandler.sendMessage(message);
                    }
                });
                break;
        }
    }

    /**
     * 工作流式数据流转
     *
     * @param threadTag
     */
    private RxThread flowThread(ThreadId threadTag) {
        return rxThread;
    }

    /**
     * 对应的处理线程中创建Handler
     *
     * @param threadTag 处理线程的标记
     */
    private void handlerThread(ThreadId threadTag, NextBack nextBack) {
        switch (threadTag) {
            case mainThread:
                //当前在主线程
                if (mainHandler == null) {
                    mainHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            nextBack.run(msg);
                        }
                    };
                }
                break;
            case newThread:
                //当前在新建子线程
                if (newHandler == null) {
                    cachedThread.submit(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();//创建与当前线程相关的Looper
                            newHandler = new Handler(Looper.myLooper()) {
                                @Override
                                public void handleMessage(Message msg) {
                                    nextBack.run(msg);
                                }
                            };
                            Looper.loop();//调用此方法，消息才会循环处理
                        }
                    });
                }
                break;
            case currentThread:
                //当前在其他子线程
                if (currentHandler == null) {
                    Looper.prepare();//创建与当前线程相关的Looper
                    currentHandler = new Handler(Looper.myLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            nextBack.run(msg);
                        }
                    };
                    Looper.loop();//调用此方法，消息才会循环处理
                }
                break;
        }
    }

    /**
     * 处理Message
     *
     * @param message
     */
    private void handlerMsg(Message message) {
        Log.i("yuanye", "----" + Thread.currentThread().getId()
                + "---" + Thread.currentThread().getName());
    }

    /**
     * 发送消息线程回调
     */
    public interface FromBack {
        void run(Message message);
    }

    /**
     * 接收消息线程回调
     */
    public interface NextBack {
        void run(Message message);
    }

    private enum ThreadId {
        mainThread, newThread, currentThread
    }
}
