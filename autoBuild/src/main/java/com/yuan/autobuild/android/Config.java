package com.yuan.autobuild.android;

import com.yuan.autobuild.copy.PathUtil;

/**
 * 描述：基本配置管理
 *
 * @author yuanye
 * @date 2019/6/25 11:10
 */
class Config {
    /**
     * 模块名
     */
    protected static String MODELNAME = "app";
    /**
     * 包名
     */
    protected static String PACKAGENAME = "com.yuan.autobuild";

    /**
     * 生成文件名
     */
    protected static String FILENAME = "文件名";

    /**
     * 模板Activity路径
     */
    protected static String TEMP_ACTIVITY_PATH = PathUtil.getPackagePath(Config.MODELNAME, Config.PACKAGENAME)
            + "\\javabuild\\JavaManager.java";

    /**
     * 模板Activity路径
     */
    protected static String OUT_PATH = PathUtil.getPackagePath(Config.MODELNAME, Config.PACKAGENAME)
            + FILENAME;


}
