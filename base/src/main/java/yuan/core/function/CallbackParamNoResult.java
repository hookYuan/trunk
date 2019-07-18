package yuan.core.function;

/**
 * @author yuanye
 * @date 2019/6/7
 */
public interface CallbackParamNoResult<Param> {
    /**
     * 无参数，无返回值
     */
    void callback(Param param);
}
