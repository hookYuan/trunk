package com.yuan.autobuild.copy;

import java.io.File;

/**
 * 描述：
 *
 * @author yuanye
 * @date 2019/6/25 10:53
 */
public class PathUtil {
    /**
     * 工程路径
     */
    private static final String PROJECTPATH = System.getProperty("user.dir");

    /**
     * 默认模块名称
     */
    private static String MODELNAME = "app";


    /**
     * 获取包名全路径
     * 默认app模块下
     *
     * @param packageName
     * @return
     */
    public static String getPackagePath(String packageName) {
        return getPackagePath(MODELNAME, packageName);
    }

    /**
     * 获取包名全路径
     *
     * @param modelName
     * @param packageName
     * @return
     */
    public static String getPackagePath(String modelName, String packageName) {
        return PROJECTPATH + File.separator + modelName +
                File.separator + "src" + File.separator + "main" + File.separator + "java"
                + getPathForPackageName(packageName);
    }

    /**
     * 根据包名获取路径
     *
     * @param packageName
     * @return
     */
    public static String getPathForPackageName(String packageName) {
        String[] path = packageName.split("\\.");
        String filePath = "";
        for (String s : path) {
            filePath = filePath + File.separator + s;
        }
        return filePath;
    }
}
