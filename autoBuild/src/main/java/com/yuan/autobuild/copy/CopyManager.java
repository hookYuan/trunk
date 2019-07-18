package com.yuan.autobuild.copy;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文件创建任务
 *
 * @author yuanye
 * @date 2019/6/24
 */
public class CopyManager {

    /**
     * 所有的构造任务
     */
    private List<ICopy> mBuildTask;

    private static class CopyManagerInstance {
        private static CopyManager manager = new CopyManager();
    }

    public static CopyManager getInstance() {
        return CopyManagerInstance.manager;
    }

    private CopyManager() {
        mBuildTask = new ArrayList<>();
    }

    /**
     * 添加构造任务
     *
     * @param iAutoBuild
     */
    public CopyManager addTask(ICopy iAutoBuild) {
        mBuildTask.add(iAutoBuild);
        return CopyManagerInstance.manager;
    }

    /**
     * 自动构建
     */
    public void autoBuild() {
        for (ICopy autoBuild : mBuildTask) {
            //基本构建信息
            CopyInfo autoInfo = autoBuild.getAutoInfo();
            HashMap<Integer, String> readData = readFileToList(autoInfo);
            //替换数据
            for (String key : autoInfo.getReplaceData().keySet()) {
                int i = 0;
                String value = autoInfo.getReplaceData().get(key);
                for (String readDatum : readData.values()) {
                    String old = readDatum.replaceAll(key, value);
                    readData.put(i, old);
                    i++;
                }
            }
            //输出文件
            writeFile(autoInfo, readData, false);
        }
    }


    /**
     * read file to string list, a element of list is a line
     *
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    private HashMap<Integer, String> readFileToList(CopyInfo autoInfo) {
        File file = new File(autoInfo.getInputPath());
        HashMap<Integer, String> fileContent = new HashMap<Integer, String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), autoInfo.getCharsetName());
            reader = new BufferedReader(is);
            String line = null;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                fileContent.put(i, line);
                i++;
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * write file
     *
     * @param contentList 文本集合
     * @param append      is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    private boolean writeFile(CopyInfo autoInfo, HashMap<Integer, String> contentList, boolean append) {
        if (contentList == null || contentList.isEmpty()) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            mkDirs(autoInfo.getOutputPath());
            fileWriter = new FileWriter(autoInfo.getOutputPath() +
                    autoInfo.getClassName(), append);
            int i = 0;
            for (String line : contentList.values()) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件是否创建成功
     *
     * @return
     */
    private boolean mkDirs(String folderName) {
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }
}
