## 简介trunk 开发基础库

[![](https://jitpack.io/v/hookYuan/trunk.svg)](https://jitpack.io/#hookYuan/trunk)

### 项目主要分为3部分
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
