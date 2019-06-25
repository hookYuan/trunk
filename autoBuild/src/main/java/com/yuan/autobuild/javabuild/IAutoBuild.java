package com.yuan.autobuild.javabuild;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

/**
 * 抽象java构建
 *
 * @author yuanye
 * @date 2019/6/24
 */
public interface IAutoBuild {

    /**
     * 获取需要生成的方法集合
     */
    List<MethodSpec> getMethodSpecs();

    /**
     * 获取需要构造完成时的类型
     *
     * @param className java类名
     * @return
     */
    TypeSpec.Builder getTypeSpec(String className);

    /**
     * 获取基本信息
     *
     * @return
     */
    AutoInfo getAutoInfo();
}
