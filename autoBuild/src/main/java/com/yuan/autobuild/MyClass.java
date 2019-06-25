package com.yuan.autobuild;

import com.yuan.autobuild.copybuild.CopyManager;
import com.yuan.autobuild.copybuild.JavaManagerCopy;
import com.yuan.autobuild.javabuild.JavaManager;

public class MyClass {

    public static void main(String[] args) {
        //方法体
        CopyManager manager = new CopyManager();
        manager.addTask(new JavaManagerCopy());
        manager.autoBuild();
//        System.out.print(JavaManager.class.getResource("").getPath());
//        System.out.print(System.getProperty("user.dir") +
//                "\\autoBuild" + "\\java\\main\\com\\yuan\\autobuild\\javabuild\\JavaManager.java");
    }
}
