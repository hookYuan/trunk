package com.yuan.kernel.http;

/**
 * @author yuanye
 * @date 2019/6/9
 */
public class Response {

    /**
     * 网络请求返回字符串
     */
    private String mString;

    /**
     * 网络请求返回字节
     */
    private byte[] mByte;

    public String getString() {
        return mString;
    }

    public void setString(String mString) {
        this.mString = mString;
    }

    public byte[] getByte() {
        return mByte;
    }

    public void setByte(byte[] mByte) {
        this.mByte = mByte;
    }
}
