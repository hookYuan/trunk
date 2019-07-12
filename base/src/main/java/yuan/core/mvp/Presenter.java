package yuan.core.mvp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 * 描述：Presenter
 * <p>
 * <p>
 * <p>
 * 接管Activity或Fragment中的逻辑操作，优化View中的代码逻辑
 * <p>
 * 特性：
 * 1.支持快捷加载 String,View,Drawable,color等资源
 * 2.支持主线程切换runOnUiThread
 * 3.接管Activity生命周期： onCreate,onResume,onDestroy(执行顺序低于Activity生命周期)
 * 4.通过泛型，自动绑定对应的Activity或Fragment,可通过getV()获取View实例
 *
 * @author yuanye
 * @date 2019/4/4 13:21
 */
public class Presenter<V extends Contract.View> implements Contract.IPresenter {

    /**
     * 弱引用持有Activity引用，防止内存泄漏
     * 当Activity结束时因该调用Presenter的结束destroy
     * 释放对Activity的引用
     */
    private WeakReference<V> mView;

    /**
     * 切换到主线程
     */
    private Handler mainHandler;

    /**
     * BaseActivity采用反射无参构造函数生产Presenter,
     * 所以一定确保所有继承Presenter中必须有一个无参构造函数
     */
    public Presenter() {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 传递加载View
     * 此方法一定要在
     *
     * @param view 需要绑定的View对象
     */
    public final void attachView(V view) {
        this.mView = new WeakReference<V>(view);
    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        if (mView != null) mView.clear();
    }


    @Override
    public final Context getContext() {
        if (mView == null) return null;

        if (mView.get() instanceof Activity) {
            return (Activity) mView.get();
        }

        if (mView.get() instanceof Fragment) {
            return ((Fragment) mView.get()).getActivity();
        }

        if (mView.get() instanceof androidx.fragment.app.Fragment) {
            return ((androidx.fragment.app.Fragment) mView.get()).getContext();
        }
        return null;
    }

    /**
     * 获取 view实例
     *
     * @return
     */
    protected final V getView() {
        return mView.get();
    }

    /**
     * 运行在主线程
     *
     * @param runnable
     */
    protected final void runOnUiThread(Runnable runnable) {
        if (mainHandler != null) mainHandler.post(runnable);
    }

}
