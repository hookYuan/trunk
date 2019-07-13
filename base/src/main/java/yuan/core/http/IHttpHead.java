package yuan.core.http;

import java.util.Map;

/**
 * @author yuanye
 * @date 2019/6/9
 */
public interface IHttpHead<T extends IHttpHead> {
    /**
     * 添加头部
     *
     * @param params
     */
    T addHead(Map<String, String> params);
}
