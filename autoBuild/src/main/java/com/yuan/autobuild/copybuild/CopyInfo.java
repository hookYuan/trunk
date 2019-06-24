package com.yuan.autobuild.copybuild;

import java.util.HashMap;

/**
 * @author yuanye
 * @date 2019/6/24
 */
public class CopyInfo {
    /**
     * 类名
     */
    private String className;
    /**
     * 文件输入路径
     */
    private String inputPath;
    /**
     * 输入路径
     */
    private String outputPath;
    /**
     * 替换数据<key,value> key:替换字符，value传入字符
     */
    private HashMap<String, String> replaceData;
    /**
     * 文件编码格式
     * The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     */
    private String charsetName;


    public CopyInfo(String className, String inputPath, String outputPath) {
        this.className = className;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        charsetName = "UTF-8";
        replaceData = new HashMap<>();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public HashMap<String, String> getReplaceData() {
        return replaceData;
    }

    public void setReplaceData(HashMap<String, String> replaceData) {
        this.replaceData = replaceData;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }
}
