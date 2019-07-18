package yuan.core.http;

import java.util.Map;

/**
 * 网络请求接口
 *
 * @author yuanye
 * @date 2019/6/9
 */
public interface IHttpRequest<T extends IRequestBody> {

    /**
     * 通常用于查询服务器支持的方法
     *
     * @param url
     */
    T options(String url);

    /**
     * post请求
     *
     * @param url
     * @param params
     */
    T post(String url, Map<String, String> params);

    /**
     * get请求
     *
     * @param url
     */
    T get(String url);

    /**
     * 通常使用put传输文件
     *
     * @param url
     */
    T put(String url);

    /**
     * 通常用于删除文件
     *
     * @param url
     */
    T delete(String url);

    /**
     * 回显服务器收到的请求，主要用于测试或诊断。
     *
     * @param url
     */
    T trace(String url);

}
