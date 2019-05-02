package com.yuan.simple.one.db;

import java.util.List;

public class UserBean {

    private String name;

    private int age;


    private Yuan yuan;

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

    public Yuan getYuan() {
        return yuan;
    }

    public void setYuan(Yuan yuan) {
        this.yuan = yuan;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public class Yuan {
        private String phone;
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
