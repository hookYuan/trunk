package com.yuan.base.tools.common;

import android.util.Log;

import java.lang.reflect.ParameterizedType;

/**
 * Created by yuan
 * 根据泛型型反射对象
 */
public class ReflexUtil {
    //反射泛型生成对象
    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            Log.i("TUtil", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.i("TUtil", e.getMessage());
        } catch (ClassCastException e) {
            Log.i("TUtil", e.getMessage());
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
