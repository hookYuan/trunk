package com.yuan.base.widget.dialog.v7;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.view.Gravity;

import com.yuan.base.tools.common.Kits;

/**
 * Created by YuanYe on 2018/1/15.
 * AlertDialog 简单统一配置文件
 */

public class DialogHelperParams {

    private int gravity;
    private Drawable windowBackground;

    private float dialogBehindAlpha;
    private float dialogFrontAlpha;

    private boolean matchWidth;
    private boolean matchHeight;

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    private int width;
    private int height;
    private int posX;
    private int posY;

    private @ColorInt
    int titleColor;
    private int titleSize;

    private @ColorInt
    int contentColor;
    private int contentSize;

    private @ColorInt
    int positiveColor;
    private int positiveSize;

    private @ColorInt
    int negativeColor;
    private int negativeSize;

    private DialogHelperParams(Builder builder) {
        gravity = builder.gravity;

        windowBackground = builder.windowBackground;
        dialogBehindAlpha = builder.dialogBehindAlpha;
        dialogFrontAlpha = builder.dialogFrontAlpha;

        matchWidth = builder.matchWidth;
        matchHeight = builder.matchHeight;

        paddingLeft = builder.paddingLeft;
        paddingRight = builder.paddingRight;
        paddingTop = builder.paddingTop;
        paddingBottom = builder.paddingBottom;

        width = builder.width;
        height = builder.height;

        posX = builder.posX;
        posY = builder.posY;

        titleColor = builder.titleColor;
        titleSize = builder.titleSize;

        contentColor = builder.contentColor;
        contentSize = builder.contentSize;

        positiveColor = builder.positiveColor;
        positiveSize = builder.positiveSize;

        negativeColor = builder.negativeColor;
        negativeSize = builder.negativeSize;
    }

    public int getGravity() {
        return gravity;
    }

    public Drawable getWindowBackground() {
        return windowBackground;
    }

    public float getDialogBehindAlpha() {
        return dialogBehindAlpha;
    }

    public float getDialogFrontAlpha() {
        return dialogFrontAlpha;
    }

    public boolean isMatchWidth() {
        return matchWidth;
    }

    public boolean isMatchHeight() {
        return matchHeight;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public int getContentColor() {
        return contentColor;
    }

    public int getContentSize() {
        return contentSize;
    }

    public int getPositiveColor() {
        return positiveColor;
    }

    public int getPositiveSize() {
        return positiveSize;
    }

    public int getNegativeColor() {
        return negativeColor;
    }

    public int getNegativeSize() {
        return negativeSize;
    }

    public static final class Builder {

        private int gravity = Gravity.CENTER;//相对位置

        private GradientDrawable windowBackground;//弹窗白色部分背景

        private float dialogBehindAlpha = 0.5f;//弹窗整个界面黑色背景透明度，范围0.0-1：透明-不透明
        private float dialogFrontAlpha = 1f;//弹窗白色部分背景透明度，范围0.0-1：透明-不透明

        private boolean matchWidth = false; //最大宽度
        private boolean matchHeight = false;//最大高度

        private int paddingLeft = -1;
        private int paddingRight = -1;
        private int paddingTop = -1;
        private int paddingBottom = -1;

        private int width = 0; //指定宽度
        private int height = 0; //指定高度

        private int posX = 0;//Dialog左上角定点横坐标
        private int posY = 0;//Dialog左上角定点纵坐标(坐标位置与gravity有关)

        private @ColorInt
        int titleColor = 0; //DialogTitle的字体颜色
        private int titleSize = 0; //Title的字体大小

        private @ColorInt
        int contentColor = 0; //内容的字体颜色
        private int contentSize = 0; //内容的字体大小

        private @ColorInt
        int positiveColor = 0; //确定按钮颜色
        private int positiveSize = 0; //确定按钮字体大小

        private @ColorInt
        int negativeColor = 0; //取消按钮颜色
        private int negativeSize = 0; //取消按钮字体大小

        public Builder() {
        }

        public Builder gravity(int val) {
            gravity = val;
            return this;
        }

        public Builder windowBackground(GradientDrawable val) {
            windowBackground = val;
            return this;
        }

        public Builder windowBackground(@ColorInt int val) {
            windowBackground = new GradientDrawable();
            windowBackground.setColor(val);
            return this;
        }

        public Builder dialogBehindAlpha(float val) {
            dialogBehindAlpha = val;
            return this;
        }

        public Builder dialogFrontAlpha(float val) {
            dialogFrontAlpha = val;
            return this;
        }

        public Builder matchWidth(boolean val) {
            matchWidth = val;
            return this;
        }

        public Builder matchHeight(boolean val) {
            matchHeight = val;
            return this;
        }

        public Builder paddingTop(int val) {
            paddingTop = val;
            return this;
        }

        public Builder paddingRight(int val) {
            paddingRight = val;
            return this;
        }

        public Builder paddingLeft(int val) {
            paddingLeft = val;
            return this;
        }

        public Builder paddingBottom(int val) {
            paddingBottom = val;
            return this;
        }

        public Builder width(int val) {
            width = val;
            return this;
        }

        public Builder height(int val) {
            height = val;
            return this;
        }

        public Builder posX(int val) {
            posX = val;
            return this;
        }

        public Builder posY(int val) {
            posY = val;
            return this;
        }

        public Builder titleColor(int val) {
            titleColor = val;
            return this;
        }

        public Builder titleSize(int val) {
            titleSize = val;
            return this;
        }

        public Builder contentColor(int val) {
            contentColor = val;
            return this;
        }

        public Builder contentSize(int val) {
            contentSize = val;
            return this;
        }

        public Builder positiveColor(int val) {
            positiveColor = val;
            return this;
        }

        public Builder positiveSize(int val) {
            positiveSize = val;
            return this;
        }

        public Builder negativeColor(int val) {
            negativeColor = val;
            return this;
        }

        public Builder negativeSize(int val) {
            negativeSize = val;
            return this;
        }

        public DialogHelperParams build() {
            return new DialogHelperParams(this);
        }
    }
}
