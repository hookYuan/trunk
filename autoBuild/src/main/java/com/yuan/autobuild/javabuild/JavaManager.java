package com.yuan.autobuild.javabuild;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanye
 * @date 2019/6/24
 */
public class JavaManager {

    /**
     * 所有的构造任务
     */
    private List<IAutoBuild> mBuildTask;

    public JavaManager() {
        mBuildTask = new ArrayList<>();
    }
    
    /**
     * 添加构造任务
     *
     * @param iAutoBuild
     */
    public void addTask(IAutoBuild iAutoBuild) {
        mBuildTask.add(iAutoBuild);
    }

    /**
     * 自动构建
     */
    public void autoBuild() {
        for (IAutoBuild autoBuild : mBuildTask) {
            //基本构建信息
            AutoInfo autoInfo = autoBuild.getAutoInfo();
            List<MethodSpec> methods = autoBuild.getMethodSpecs();
            TypeSpec.Builder methodBuild = autoBuild.getTypeSpec(autoInfo.getClassName());
            for (MethodSpec method : methods) {
                methodBuild.addMethod(method);
            }
            methodBuild.addJavadoc(autoInfo.getClassDoc());

            TypeSpec methodSpec = methodBuild.build();
            JavaFile javaFile = JavaFile.builder("", methodSpec)
                    .build();
            try {
                javaFile.writeTo(new File(autoInfo.getOutputPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}