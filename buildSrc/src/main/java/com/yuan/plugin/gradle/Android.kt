package com.yuan.plugin.gradle

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * gradle中的Android配置扩展函数
 *
 * @description
 * @author yuanye
 * @date 2021/1/8 17:10
 */
private typealias AndroidExtension = BaseExtension

fun Project.extAndroid() = this.extensions.getByType<AndroidExtension>().run {

    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0.0"
        vectorDrawables.useSupportLibrary = true //增加对vector文件的支持
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            isTestCoverageEnabled = true
            isDebuggable = true
            isMinifyEnabled = false //混淆
            isZipAlignEnabled = false //Zipalign优化
        }
    }

    /**
     * 修改SourceSets中的属性，可以指定哪些源文件（或文件夹下的源文件）要被编译
     */
    sourceSets {

    }

    packagingOptions {
        exclude("META-INF/NOTICE.txt")
        // ...
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}