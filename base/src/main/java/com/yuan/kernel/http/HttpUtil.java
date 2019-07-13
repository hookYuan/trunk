//package com.yuan.kernel.http;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @author yuanye
// * @date 2019/6/9
// */
//public class HttpUtil implements IHttpRequest {
//
//    /**
//     * 网络请求体
//     */
//    private IHttpRequest mRequest;
//
//    /**
//     * 上传参数集合:<key,value></key,value>
//     */
//    private Map<String, String> params;
//    /**
//     * 上传文件集合 : <key,path></key,path>
//     */
//    private Map<String, String> files;
//    /**
//     * 上传字节集合
//     */
//    private List<byte[]> bytes;
//
//    public void init(IHttpRequest request) {
//        mRequest = request;
//    }
//
//    @Override
//    public IHttpRequest options(String url) {
//        return mRequest.options(url);
//    }
//
//    @Override
//    public IHttpRequest get(String url) {
//        return mRequest.get(url);
//    }
//
//    @Override
//    public IHttpRequest put(String url) {
//        return mRequest.put(url);
//    }
//
//    @Override
//    public IHttpRequest delete(String url) {
//        return mRequest.delete(url);
//    }
//
//    @Override
//    public IHttpRequest trace(String url) {
//        return mRequest.trace(url);
//    }
//
//    @Override
//    public IHttpRequest post(String url, Map params) {
//        return mRequest.post(url, params);
//    }
//}
