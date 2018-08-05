package com.yuan.simple.one.bean;

/**
 * Created by YuanYe on 2018/8/4.
 */
public class OneListBean {
    private String toolbar;
    private Class clazz;

    public OneListBean(String toolbar, Class clazz) {
        this.toolbar = toolbar;
        this.clazz = clazz;
    }

    public String getToolbar() {
        return toolbar;
    }

    public void setToolbar(String toolbar) {
        this.toolbar = toolbar;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "OneListBean{" +
                "toolbar='" + toolbar + '\'' +
                ", clazz=" + clazz +
                '}';
    }
}
