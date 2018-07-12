package com.yuan.base.tools.sort;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by YuanYe on 2018/7/12.
 * 拼音排序工具类,需要排序中文需要实现IPinyingSort这个接口
 */
public class PinyinSortUtil {

    /**
     * 给汉字按照字母排序顺序排序
     *
     * @param data 传入的数据需要集成SortBean
     * @return
     */
    public static <T extends IPinyinSort> ArrayList<T> sortData(ArrayList<T> data) {
        if (data != null) {
            //对数据重新排序
            PinyinComparator pinyinComparator = new PinyinComparator();
            Collections.sort(data, pinyinComparator);
        }
        return data;
    }
}
