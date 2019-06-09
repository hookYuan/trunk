package com.yuan.kernel.function;

/**
 * @author yuanye
 * @date 2019/6/7
 */
public interface CallbackParamResult<Param, Result> {
    /**
     * 无参数，无返回值
     */
    Result callback(Param param);
}
