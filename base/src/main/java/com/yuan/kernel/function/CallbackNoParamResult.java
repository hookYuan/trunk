package com.yuan.kernel.function;

/**
 * @author yuanye
 * @date 2019/6/7
 */
public interface CallbackNoParamResult<Result> {
    /**
     * 无参数，有返回值
     */
    Result callback();
}
