[![](https://jitpack.io/v/hookYuan/trunk.svg)](https://jitpack.io/#hookYuan/trunk)

## trun简介

工程为原生Android项目，是一个长期维护并更新版本，当然由于项目涉及的知识点和代码较多，对于以前旧版本的支持可能不是那么好。本项目最初创建于2017年，由我的另一个项目truck（truck最早参考了XDroidMvp这个项目）分离而来。 

* 收集Android开发中使用到的一些常用基础工具类
* 实际开发中我对mvp的理解和实现方式
* 实际开发中的编码规范以及项目分包结构
* 提供Android开发中开发效率，提升开发质量，减少bug率

#### 项目虽然使用java写的，但完全试用于kotlin开发，具体使用可参考[KotlinDemo](https://github.com/hookYuan/KotlinDemo)

## 工程结构

工程结构主要分为3大模块core、depends、expand。依赖关系为core只依赖AndroidX;depands依赖core和三方常用lib;expand依赖core.

## 依赖方式 
  * 下载源码，修改需要依赖模块下的build.gradle文件。优点：可以修改源码，实现高度自定义；缺点：版本库升级项目不能平滑升级，编译时间长
```
    dependencies{
      implementation project(':base') //base 基础模块
      implementation project(':depends') //depends以及包含对应版本的base模块不用同时依赖
      implementation project(':expand') //expand以及包含对应版本的base模块不用同时依赖
    }
```
  * jitpack在线依赖，修改需要依赖模块和工程下的build.gradle文件。优点：依赖简单；缺点：发现基础库bug会增大修改难度
```
    //项目根目录下build.gradle文件
    repositories{
       maven { url 'https://jitpack.io' }
    }
```
  
```
    //需要依赖模块下的build.gradle文件
    dependencies{
       implementation 'com.github.hookYuan.trunk:core:1.0.3' //base 基础模块
       implementation 'com.github.hookYuan.trunk:depends:1.0.3' //depends以及包含对应版本的base模块不用同时依赖
       implementation 'com.github.hookYuan.trunk:expand:1.0.3' //expand以及包含对应版本的base模块不用同时依赖
    }
```
   * bintray在线依赖(可以上传同步jcenter,省略repositories修改，我的审核没有通过⊙﹏⊙)，修改需要依赖模块和工程下的build.gradle文件。优点：依赖简单；缺点：发现基础库bug会增大修改难度。由于bintray上传网络不稳定，后期可能会放弃此方式
```
    //项目根目录下build.gradle文件
    repositories{
       maven { url 'https://dl.bintray.com/ydroid/maven' }
    }
```
  
```
    //需要依赖模块下的build.gradle文件
    dependencies{
       implementation 'yuan.core:core:1.0.6'     //base 基础模块
       implementation 'yuan.depend:depend:1.0.3' //depends以及包含对应版本的base模块不用同时依赖
       implementation 'yuan.expand:expand:1.0.2' //expand以及包含对应版本的base模块不用同时依赖
    }
```
## core模块

  * core模块的设计之初是为了提供常用开发工具类和常用效果实现方式并且不依赖任何非官方的第三方类库。这样做的目的显而易见的的就是能增强Android程序的适配机型能力
  * 对于一些常用的非常优秀的开源库，core模块采用接口的方式将实现依赖传递到外部。这样的好处是可以自由实现接口，动态替换各个三方类库。降低项目与第三方项目的直接依赖
  * 为了增强开发的可复用性，模块下的代码只依赖相同包下文件，大部分为单一文件无依赖。

 
 #### core模块包含以下基础包（后期可能会有修改和扩展）：
 * cache : 实现文件缓存/内存缓存/LRU缓存策略工具类
 * dialog : 基于系统AlerDialog封装简化各种基础配置，实现AlerDialog的高度扩展性
 * function : 不用定义接口，不用写模板代码，随时随地轻松实现Callback回调函数
 * http : 采用装饰者模式轻松替换三方http网络请求框架
 * list : ListView和RecyclerView的Adapter的增强实现，RecyclerView常见ItemDecoration，轻松实现悬浮/折叠等
 * mvp : Activity和Fragment实现Mvp解藕，扩展Activity和Fragment方法，如：fragment状态报错、代码结构统一、延迟加载等
 * roundView : 轻松实现各种圆角、描边、点按View，告别繁杂的shape/selector
 * sort : 轻松实现中文排序，一个接口，告别各种对比
 * title : 自定Title,功能强大，可以配合ActionBar使用，可控制StatueBar，自带动画
 * tool : 常用工具类，例如路由跳转（支持系统Activity,支持权限申请告别OnActivityResult)。这里的知识点太多了，可以看此项目的案例simple或者后期我会单独为这个工程做一个博客专题
 * widget : 常用自定义View，例如手势PinchImageView等

## depend模块
  * depend模块设计是为了简化一些常用第三方库的用法，例如glide、okhttp等
  * depend模块不会依赖大量三方模块，因为它也是常用基础模块，应该控制该模块的大小和版本
  * 所有的工具库不提供直接依赖，优先提供工具类，使用接口，当出现有需要淘汰的第三方工具库时，便于替换
  * 为了增强开发的可复用性，模块下的代码只依赖相同包下文件，大部分为单一文件无依赖。
  
  #### 对于三方库，我的态度一直是能自己实现，尽量少依赖，能简单就不要复杂化
  
   #### depend模块包含以下基础包（后期可能会有修改和扩展）：
  * glide:
  * imagepicker:
  * okhttp:
  * refresh:
  * tablayout:
  * ui:

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
