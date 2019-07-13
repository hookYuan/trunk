package com.yuan.kernel.http;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * @author yuanye
 * @date 2019/6/27
 */
public class RequestBody implements IRequestBody<RequestBody> {

    /**
     * 上传参数集合:<key,value></key,value>
     */
    private Map<String, String> params;
    /**
     * 上传文件集合 : <key,path></key,path>
     */
    private Map<String, String> files;

    @Override
    public RequestBody addParams(String key, String value) {
        return null;
    }

    @Override
    public RequestBody addHead(String key, String value) {
        return null;
    }

    @Override
    public RequestBody addJson(@NonNull String json) {
        return null;
    }

    @Override
    public RequestBody addByte(@NonNull byte[] bytes1) {
        return null;
    }

    @Override
    public RequestBody addFile(@NonNull String key, @NonNull String filePath) {
        return null;
    }

    @Override
    public void execute() {

    }
}
