package com.yuan.base.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.yuan.base.ui.fragment.ExtraFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuanYe on 2017/7/10.
 * 包含Fragment的Activity可直接继承该类
 * 使用方法：
 * 新建Activity继承FragmentActivity,
 * 在initData()中调用 addFragment()初始化
 * 调用showFragment()切换Fragment
 */
public abstract class FragActivity<T extends Fragment> extends ExtraActivity {

    private static final String TAG = "FragActivity";

    private int showIndex = 0; //需要默认显示Fragment下标,默认显示第一页
    private List<Fragment> fragments;  //用于放置Fragment

    /**
     * 根据下标，设置要展示的Fragment
     *
     * @param newIndex 要显示的Fragment的下标
     */
    public void showFragment(int newIndex) {
        if (showIndex == newIndex)
            return;
        if (fragments == null)
            throw new NullPointerException("请先调用addFragment()方法添加Fragment");
        if (newIndex >= fragments.size())
            throw new IndexOutOfBoundsException("下标越界，最大下标为:" + (fragments.size() - 1) + "当前为：" + newIndex);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragments.get(newIndex))
                .hide(fragments.get(showIndex))
                .commit();
        fragments.get(newIndex).setUserVisibleHint(true);
        showIndex = newIndex;
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param container   需要添加的布局ID
     * @param packageName Fragment包全名
     */
    protected void addFragment(@IdRes int container, Class<T>... packageName) {
        addFragment(container, 0, packageName);
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param showIndex   默认显示的页数
     * @param packageName
     */
    protected void addFragment(@IdRes int container, int showIndex, Class<T>... packageName) {
        showIndex = showIndex >= packageName.length ? 0 : showIndex;
        this.showIndex = showIndex;
        if (fragments == null) fragments = new ArrayList<>();

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        int index = 0;
        for (Class<T> t : packageName) {
            T fragment = newInstance(t, null);
            fragments.add(fragment);
            //添加Fragment并添加TAG,便于Activity销毁后查找
            ft.add(container, fragment, t.getName());
            if (showIndex == index) {
                ft.show(fragment);
                if (fragment instanceof ExtraFragment) fragment.setUserVisibleHint(true);
            } else {
                ft.hide(fragment);
                if (fragment instanceof ExtraFragment) fragment.setUserVisibleHint(false);
            }
            index++;
        }
        ft.commit();
    }


    protected void addFragment(@IdRes int container, T... frags) {
        addFragment(container, 0, frags);
    }

    protected void addFragment(@IdRes int container, int showIndex, T... frags) {
        showIndex = showIndex >= frags.length ? 0 : showIndex;
        this.showIndex = showIndex;
        if (fragments == null) fragments = new ArrayList<>();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        int index = 0;
        for (Fragment fragment : frags) {
            fragments.add(fragment);
            //添加Fragment并添加TAG,便于Activity销毁后查找
            ft.add(container, fragment, frags.getClass().getName());
            if (showIndex == index) {
                ft.show(fragment);
                if (fragment instanceof ExtraFragment) fragment.setUserVisibleHint(true);
            } else {
                ft.hide(fragment);
                if (fragment instanceof ExtraFragment) fragment.setUserVisibleHint(false);
            }
            index++;
        }
        ft.commit();
    }


    /**
     * 获取Fragment实例
     *
     * @param packageName 子类包名
     * @param bundle      需要传递给Fragment的参数
     * @return 返回子类对象实例
     */
    private T newInstance(Class<T> packageName, Bundle bundle) {
        T child = null;
        try {
            child = (T) Class.forName(packageName.getName()).newInstance();
            if (bundle != null) {
                Bundle bundle1 = new Bundle();
                child.setArguments(bundle1);
                //建议通过这样的方式给Fragment传值,内存重启前,系统可以帮你保存数据
                //界面恢复后,不会造成数据的丢失。
                child.setArguments(bundle);
            }
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "反射创建Fragment异常：" + e.getMessage());
        } catch (java.lang.InstantiationException e) {
            Log.e(TAG, "反射创建Fragment异常：" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, "反射创建Fragment异常：" + e.getMessage());
        }
        return child;
    }
}
