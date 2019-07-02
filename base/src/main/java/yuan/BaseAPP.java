package yuan;

import android.app.Application;

/**
 * Created by YuanYe on 2017/7/19.
 * BaseApplication,所有直接或间接引用base做为的Module的主项目
 * ，如果需要使用ARouter,并且使用模块化开发，都应该继承自BaseApp,
 * 并在Manifest文件中注册
 */
public class BaseAPP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
