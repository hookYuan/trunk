package com.yuan.base.widget.title;

/**
 * Created by YuanYe on 2017/7/13.
 * 配置Title和statusbar显示的样式
 */
public interface TitleInterface {
//    int STATUS_BAR_H = 10001; //隐藏状态栏 -- 需要在setContentView之前调用
    int STATUS_BAR_FONT_BLACK = 10002;//状态栏黑色字体
    int STATUS_BAR_FONT_WHITE = 10003;//状态栏白色字体
    int STATUS_BAR_BG_TRANSPARENT_OVERLAY = 10004; //透明、覆盖内容状态栏
    int STATUS_BAR_BG_BLACK_OVERLAY = 10005;//黑色背景、覆盖内容状态栏
    int STATUS_BAR_BG_WHITE_OVERLAY = 10006;//白色背景、覆盖内容状态栏
    int STATUS_BAR_BG_TRANSPARENT_TOP = 10007; //透明、内容之上状态栏
    int STATUS_BAR_BG_BLACK_TOP = 10008;//黑色背景、内容之上状态栏
    int STATUS_BAR_BG_WHITE_TOP = 10009;//白色背景、内容之上状态栏


    int TITLE_H = 20001;//隐藏TitleBar
    int TITLE_FONT_BLACK = 20002;//标题栏黑色字体
    int TITLE_FONT_WHITE = 20003;//标题栏白色字体
    int TITLE_BG_TRANSPARENT = 20004;//透明标题栏
    int TITLE_BG_BLACK = 20005; //黑色背景色的标题栏
    int TITLE_BG_WHITE = 20006; //白色色背景色的标题栏
    int TITLE_BG_STATUE_HEIGHT = 20007;//显示状态栏高度
    int TITLE_CONTENT_TOP = 20008;//titleBar显示在内容之上
    int TITLE_CONTENT_OVERLAY = 20009;//titleBar覆盖在内容之上
}
