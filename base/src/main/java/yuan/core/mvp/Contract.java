package yuan.core.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

/**
 * 该接口主要用于约束Activity和Fragment的基本方法
 * 必须保证此接口中的方法只执行一次
 *
 * @author yuanye
 * @date 2018/11/26
 */
class Contract {

    /**
     * 约束 Activity/Fragment 中的方法
     */
    public interface View {
        /**
         * 加载的布局文件
         * 优先于getLayoutView，只有当返回值为0时触发getLayoutView
         */
        @LayoutRes
        int getLayoutId();

        /**
         * 加载的布局View
         */
        android.view.View getLayoutView();

        /**
         * 加载控件
         */
        void findViews();

        /**
         * 解析传递参数
         */
        void parseBundle(@Nullable Bundle bundle);

        /**
         * 初始化
         */
        void initData();

        /**
         * 设置监听
         */
        void setListener();

    }

    /**
     * 约束Presenter中的方法
     * TODO 绑定生命周期
     */
    public interface IPresenter {
        /**
         * create
         */
        void onCreate(Bundle bundle);

        /**
         * resume
         */
        void onResume();

        /**
         * destroy
         */
        void onDestroy();

        /**
         * 获取上下文对象
         *
         * @return context
         */
        Context getContext();
    }
}
