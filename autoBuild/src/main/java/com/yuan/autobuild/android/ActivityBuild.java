package com.yuan.autobuild.android;

import com.yuan.autobuild.copy.CopyInfo;
import com.yuan.autobuild.copy.ICopy;
import com.yuan.autobuild.copy.PathUtil;


/**
 * 复制Android Activity文件
 */
public class ActivityBuild implements ICopy {


    @Override
    public CopyInfo getAutoInfo() {
        String outputPath = System.getProperty("user.dir") + "\\autoBuild\\src\\main\\java\\com\\yuan\\autobuild\\javabuild\\";
        CopyInfo info = new CopyInfo("JavaTest.java", Config.TEMP_ACTIVITY_PATH, outputPath);
        info.getReplaceData()
                .put("JavaManager", "JavaTest");
        return info;
    }
}
