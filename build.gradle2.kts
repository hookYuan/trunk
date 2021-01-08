buildscript {

    val kotlin_version by extra("1.4.10")

    repositories {
        google()
        jcenter()
    }
    dependencies {
        //gradle编译依赖
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")

        //github上传库依赖
        classpath("com.novoda:bintray-release:+")
        classpath("com.github.dcendents:android-maven-gradle-plugin:1.5")
    }
}

allprojects {
    repositories {
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://dl.bintray.com/ydroid/maven")
    }

//    //加上这些
//    tasks.withType(Javadoc) {
//        options {
//            encoding "UTF-8"
//            charSet 'UTF-8'
//            links "http://docs.oracle.com/javase/7/docs/api"
//        }
//    }
//
//    //最好加上全局编码设置
//    tasks.withType(JavaCompile) {
//        options.encoding = 'UTF-8'
//    }
}


