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
package com.yuan.simple.expand.module;

/**
 * @author YuanYe
 * @date 2019/7/19  12:56
 */
public class UserBean {

    /**
     * 用户名
     */
    private String name;

    /**
     * 年龄
     */
    private int age;

    /**
     * 详情
     */
    private DetailInfo detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public DetailInfo getDetail() {
        return detail;
    }

    public void setDetail(DetailInfo detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    /**
     * 关联子表，详情信息
     */
    public class DetailInfo {
        /**
         * 手机
         */
        private String phone;
        /**
         * 地址
         */
        private String address;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
