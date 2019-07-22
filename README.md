[![](https://jitpack.io/v/hookYuan/trunk.svg)](https://jitpack.io/#hookYuan/trunk)

## trunk简介

工程为原生Android项目，项目内容为我这些年在Android开发中使用到的一些常用基础工具类，以及自己根据对mvp的和模块化一些理解实现的编码以及分包结构。项目中所有的代码结构、包结构和实现方式均来自我现在的实际项目开发。本项目是一个长期维护并更新版本，当然由于项目涉及的知识点和代码较多，对于以前旧版本的支持可能不是那么好。本项目最初创建于2017年，由我的另一个项目truck（truck最早参考了XDroidMvp这个项目）分离而来。项目的目的是提供Android开发中开发效率，提升开发质量，减少bug率。

#### 1.base部分现已拆分，core 与 depend 两个模块，目的是为了减少模块之间的项目依赖。
其中core模块提供基本库（只依赖AndroidX包下lib）和常用方法.提高开发效率
depend模块依赖常用三方lib,例如依赖okhttp,glide等
#### 2.autobuild模块为单纯java模块，里面包含常用java脚本，项目可以不用直接依赖。作用例如：创建开发模板
#### 3.app模块 ，主要包含core和depend的使用案例

## 依赖/使用

### 第一步：由于所有模块均只上传maven,未同步到jcenter,所以需要在项目根目录下添加
maven { url 'https://dl.bintray.com/ydroid/maven' }

### 第二步：正常引用

#### depend 版本引用
compile 'yuan.depend:depend:1.0.1'

#### core 版本引用
compile 'yuan.core:core:1.0.4'
