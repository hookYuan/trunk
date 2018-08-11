package com.yuan.simple.one.ui.sort;

import com.yuan.base.tools.sort.IPinyinSort;

/**
 * Created by YuanYe on 2018/8/11.
 */
public class ChineseBean implements IPinyinSort {

    private String name;

    public ChineseBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSortText() {
        return name; //设置需要排序的文字
    }
}
