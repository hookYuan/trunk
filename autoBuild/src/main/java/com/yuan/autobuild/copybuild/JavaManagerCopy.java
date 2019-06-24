package com.yuan.autobuild.copybuild;

/**
 * @author yuanye
 * @date 2019/6/24
 */
public class JavaManagerCopy implements ICopy {
    @Override
    public CopyInfo getAutoInfo() {
//        String inputPath =
//                System.getProperty("user.dir") +
//                        "\\autoBuild" + "\\java\\com\\yuan\\autobuild\\javabuild\\JavaManager.java";
        String inputPath = System.getProperty("user.dir") + "\\autoBuild\\src\\main\\java\\com\\yuan\\autobuild\\javabuild\\JavaManager.java";
        String outputPath = System.getProperty("user.dir") +"\\autoBuild\\src\\main\\java\\com\\yuan\\autobuild\\javabuild\\";
        CopyInfo info = new CopyInfo("JavaTest.java", inputPath, outputPath);
        info.getReplaceData()
                .put("JavaManager", "JavaTest");
        return info;
    }
}
