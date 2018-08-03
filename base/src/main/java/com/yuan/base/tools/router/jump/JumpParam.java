package com.yuan.base.tools.router.jump;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by YuanYe on 2018/5/14.
 * 用于构建跳转参数
 */
public class JumpParam {

    private HashMap<String, Object> attr;
    private static JumpParam param;
    private String stringListKey;
    private ArrayList<String> stringList;
    private String parcelableKey;
    private ArrayList<Parcelable> ParcelableList;

    public static JumpParam getInstance() {
        if (param == null) {
            param = new JumpParam();
        }
        return param;
    }

    private JumpParam() {
        if (attr == null) attr = new HashMap<>();
    }

    public JumpParam put(String key, String value) {
        attr.put(key, value);
        return param;
    }

    public JumpParam put(String key, int value) {
        attr.put(key, value);
        return param;
    }

    public JumpParam put(String key, long value) {
        attr.put(key, value);
        return param;
    }

    public JumpParam put(String key, double value) {
        attr.put(key, value);
        return param;
    }

    public JumpParam put(String key, boolean value) {
        attr.put(key, value);
        return param;
    }

    public JumpParam put(String key, ArrayList value) {
        attr.put(key, value);
        return param;
    }

    public JumpParam putStringArrayList(String key, ArrayList<String> value) {
        this.stringListKey = key;
        this.stringList = value;
        return param;
    }

    public JumpParam putParcelableArrayList(String key, ArrayList<Parcelable> value) {
        this.ParcelableList = value;
        return param;
    }

    protected ArrayList<Parcelable> getParcelableList() {
        return ParcelableList;
    }

    protected String getParcelableKey() {
        return parcelableKey;
    }

    protected String getStringListKey() {
        return stringListKey;
    }

    protected ArrayList<String> getStringList() {
        return stringList;
    }

    protected HashMap<String, Object> getAttr() {
        return attr;
    }

}
