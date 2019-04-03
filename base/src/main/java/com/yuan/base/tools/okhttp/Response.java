package com.yuan.base.tools.okhttp.callback;

import okhttp3.ResponseBody;

/**
 * 接替OKHttp返回
 *
 * @author yuanye
 * @date 2018/11/28 11:47
 */
public class Response {

    public ResponseBody body;
    /**
     * 内容长度
     */
    public long contentLength;
    /**
     * 请求地址
     */
    public String requestUrl;
    /**
     * 消息
     */
    public String mssage;

    /**
     * 请求状态码
     */
    public int code;

}
