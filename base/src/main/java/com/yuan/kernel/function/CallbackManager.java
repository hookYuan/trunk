package com.yuan.kernel.function;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理Callback回调，避免大量的写Callback代码
 * 使用方式：
 * 在调用invoke方法前添加调用addCallback方法
 * 在需要执行回调的地方调用invoke方法即可
 * <p>
 * 采用单例实现线程安全
 * <p>
 * 可以管理4种回调形式：
 * 1.无返回值、无参数类型
 * 2.有返回值、无参数类型
 * 3.无返回值、有参数类型
 * 4.有返回值、有参数类型
 *
 * @author yuanye
 * @date 2019/6/7
 */
public class CallbackManager {

    private Map<String, CallbackNoParamNoResult> mCallbackNoParamNoResult;
    private Map<String, CallbackParamNoResult> mCallbackParamNoResult;
    private Map<String, CallbackNoParamResult> mCallbackNoParamResult;
    private Map<String, CallbackParamResult> mCallbackParamResult;

    private static class CallbackManagerSingle {
        private final static CallbackManager manager = new CallbackManager();
    }

    public static CallbackManager get() {
        return CallbackManagerSingle.manager;
    }

    private CallbackManager() {
        mCallbackNoParamNoResult = new HashMap<>();
        mCallbackParamNoResult = new HashMap<>();
        mCallbackNoParamResult = new HashMap<>();
        mCallbackParamResult = new HashMap<>();
    }

    /**
     * 添加无返回/无参回调
     *
     * @param tag
     * @param callback
     */
    public final CallbackManager addCallback(String tag, CallbackNoParamNoResult callback) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("添加callback必须指定tag名称不能为空");
        }

        if (callback == null) {
            throw new NullPointerException("添加callback不能为空");
        }
        mCallbackNoParamNoResult.put(tag, callback);
        return CallbackManagerSingle.manager;
    }

    /**
     * 添加无返回/有参回调
     *
     * @param tag
     * @param callback
     */
    public final CallbackManager addCallback(String tag, CallbackParamNoResult callback) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("添加callback必须指定tag名称不能为空");
        }

        if (callback == null) {
            throw new NullPointerException("添加callback不能为空");
        }
        mCallbackParamNoResult.put(tag, callback);
        return CallbackManagerSingle.manager;
    }

    /**
     * 添加有返回/有参回调
     *
     * @param tag
     * @param callback
     */
    public final CallbackManager addCallback(String tag, CallbackParamResult callback) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("添加callback必须指定tag名称不能为空");
        }

        if (callback == null) {
            throw new NullPointerException("添加callback不能为空");
        }
        mCallbackParamResult.put(tag, callback);
        return CallbackManagerSingle.manager;
    }

    /**
     * 添加有返回/无参回调
     *
     * @param tag
     * @param callback
     */
    public final CallbackManager addCallback(String tag, CallbackNoParamResult callback) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("添加callback必须指定tag名称不能为空");
        }

        if (callback == null) {
            throw new NullPointerException("添加callback不能为空");
        }
        mCallbackNoParamResult.put(tag, callback);
        return CallbackManagerSingle.manager;
    }

    /**
     * 移除指定tag callback
     *
     * @param tag
     */
    public void remove(String tag) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("添加callback必须指定tag名称不能为空");
        }
        mCallbackNoParamNoResult.remove(tag);
        mCallbackParamNoResult.remove(tag);
        mCallbackNoParamResult.remove(tag);
        mCallbackParamResult.remove(tag);
    }


    /**
     * 执行无返回值且没有参数
     *
     * @param tag
     */
    public final <Result> Result invoke(String tag, Class<Result> result) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("tag名称不能为空");
        }
        if (mCallbackNoParamResult.get(tag) == null) {
            throw new NullPointerException("未找到指定tag=" + tag + "的callback回调");
        }
        CallbackNoParamResult callback = mCallbackNoParamResult.get(tag);

        if (result != null) {
            return result.cast(CallbackNoParamResult.class.cast(callback).callback());
        } else {
            return (Result) CallbackNoParamResult.class.cast(callback).callback();
        }
    }

    /**
     * 执行有返回值且有参数
     *
     * @param tag
     */
    public final <Param, Result> Result invoke(String tag, Param param, Class<Result> result) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("tag名称不能为空");
        }
        if (mCallbackParamResult.get(tag) == null) {
            throw new NullPointerException("未找到指定tag=" + tag + "的callback回调");
        }
        CallbackParamResult callback = mCallbackParamResult.get(tag);

        if (result != null) {
            return result.cast(callback.callback(param));
        } else {
            return (Result) callback.callback(param);
        }
    }

    /**
     * 执行无返回值且有参数
     *
     * @param tag
     */
    public final <Param> void invoke(String tag, Param param) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("tag名称不能为空");
        }
        if (mCallbackParamNoResult.get(tag) == null) {
            throw new NullPointerException("未找到指定tag=" + tag + "的callback回调");
        }
        CallbackParamNoResult callback = mCallbackParamNoResult.get(tag);
        callback.callback(param);
    }

    /**
     * 执行无返回值且没有参数
     *
     * @param tag
     */
    public final void invoke(String tag) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("tag名称不能为空");
        }
        if (mCallbackNoParamNoResult.get(tag) == null) {
            throw new NullPointerException("未找到指定tag=" + tag + "的callback回调");
        }
        CallbackNoParamNoResult callback = mCallbackNoParamNoResult.get(tag);
        callback.callback();
    }
}
