package yuan.core.sort;


/**
 * Created by YuanYe on 2018/7/9.
 * 需要排序的实体
 * default关键字使用，需要在jDK 8以上，Android N以上
 */
public abstract class ChineseSort {

   public abstract String getSortText(); //需要排序的文字

    //获取汉字全拼
    public final String getPinyin() {
        return ChineseSortUtil.CharacterParser.getInstance().getSelling(getSortText());
    }

    //获取首字母
    public final String getFirstLetters() {
        String pinyin = getPinyin();
        String firstLetters = "";
        if (pinyin.length() > 0) {
            firstLetters = (pinyin.charAt(0) + "").toUpperCase();
        }
        return firstLetters;
    }
}
