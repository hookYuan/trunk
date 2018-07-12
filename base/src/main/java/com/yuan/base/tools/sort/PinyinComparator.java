package com.yuan.base.tools.sort;

import java.util.Comparator;

/**
 * 作者：yuanYe创建于2016/9/26
 * QQ：962851730
 */
class PinyinComparator implements Comparator<IPinyinSort> {

    public int compare(IPinyinSort o1, IPinyinSort o2) {
        if (o1.getFirstLetters().equals("@") || o2.getFirstLetters().equals("#")) {
            return -1;
        } else if (o1.getFirstLetters().equals("#")
                || o2.getFirstLetters().equals("@")) {
            return 1;
        } else {
            //去对比两个拼音最短的部分
            int length = o1.getPinyin().length() > o2.getPinyin().length() ? o2.getPinyin().length() : o1.getPinyin().length();
            for (int i = 0; i < length; i++) {
                String o1Letters = o1.getPinyin().substring(i, i + 1);
                String o2Letters = o2.getPinyin().substring(i, i + 1);
                //如果对比的两个字母不相同，返回对比结果，否则继续下次比较
                if (0 != o1Letters.compareTo(o2Letters)) {
                    return o1Letters.compareTo(o2Letters);
                }
            }
            //如果拼音前面部分全部相同的处理方式，按照拼音长度返回
            if (o1.getPinyin().length() > o2.getPinyin().length()) return 1;
            else if (o1.getPinyin().length() == o2.getPinyin().length()) return 0;
            else return -1;
        }
    }
}
