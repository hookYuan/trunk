package com.yuan.autobuild.javabuild;

/**
 * @author yuanye
 * @date 2019/6/24
 */
public class AutoInfo {
    /**
     * 类名
     */
    private String className;

    /**
     * 输入路径
     */
    private String outputPath;

    /**
     * 类描述
     */
    private String classDoc;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getClassDoc() {
        return classDoc;
    }

    public void setClassDoc(String classDoc) {
        this.classDoc = classDoc;
    }
}
