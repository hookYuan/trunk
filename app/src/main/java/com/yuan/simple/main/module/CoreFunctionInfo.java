/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuan.simple.main.module;

/**
 * core 所有功能总览
 *
 * @author YuanYe
 * @date 2019/7/19  12:34
 */
public class CoreFunctionInfo {
    /**
     * 功能名称
     */
    private String functionName;
    /**
     * 对应的Activity
     */
    private Class clazz;

    public CoreFunctionInfo(String toolbar, Class clazz) {
        this.functionName = toolbar;
        this.clazz = clazz;
    }

    public String getName() {
        return functionName;
    }

    public void setName(String functionName) {
        this.functionName = functionName;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "CoreFunctionInfo{" +
                "functionName='" + functionName + '\'' +
                ", clazz=" + clazz +
                '}';
    }
}
