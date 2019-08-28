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
package com.yuan.simple.core.module;

/**
 * @author yuanye
 * @date 2019/7/19
 */
public class SubjectBean {
    /**
     * 标题名
     */
    private String name;
    /**
     * 标题对应的类型
     */
    private int type;
    /**
     * 对应的Activity
     */
    private Class clazz;


    public SubjectBean(String name) {
        this.name = name;
    }

    public SubjectBean(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public SubjectBean(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
