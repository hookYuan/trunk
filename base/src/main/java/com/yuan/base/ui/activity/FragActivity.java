package com.yuan.base.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.yuan.base.ui.fragment.ExtraFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YuanYe on 2017/7/10.
 * 包含Fragment的Activity可直接继承该类
 * 使用方法：
 * 新建Activity继承FragmentActivity,
 * 在initData()中调用 addFragment()初始化
 * 调用showFragment()切换Fragment
 */
abstract class FragActivity<T extends Fragment> extends ExtraActivity {

    private static final String TAG = "FragActivity";

    /**
     * 保存上次显示的位置
     */
    private static final String SAVE_SHOWPOSITION = "showPosition";

    /**
     * 需要默认显示Fragment下标,默认显示第一页
     */
    private int showIndex = 0;

    /**
     * 用于放置Fragment
     */
    private List<Fragment> fragments;

    /**
     * 最新的OnCreate中读取的状态
     */
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        if (savedInstanceState != null) {
            showIndex = savedInstanceState.getInt(SAVE_SHOWPOSITION, 0);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存Fragment的状态，防止Fragment被销毁
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment.isAdded()) {
                    getSupportFragmentManager().putFragment(outState
                            , fragment.getClass().getName()
                            , fragment);
                }
            }
        }
        //保存上次Fragment的位置
        outState.putInt(SAVE_SHOWPOSITION, showIndex);
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param container   需要添加的布局ID
     * @param packageName Fragment包全名
     */
    protected void addFragment(@IdRes int container, Class<T>... packageName) {
        addFragment(container, showIndex, null, packageName);
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param container   需要添加的布局ID
     * @param packageName Fragment包全名
     */
    protected void addFragment(@IdRes int container, Bundle[] bundles, Class<T>... packageName) {
        addFragment(container, showIndex, bundles, packageName);
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param showIndex   默认显示的页数
     * @param packageName Fragment对应的类名
     */
    protected void addFragment(@IdRes int container, int showIndex, Bundle[] bundle, Class<T>... packageName) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < packageName.length; i++) {
            T fragment = null;
            if (bundle != null && bundle.length <= packageName.length) {
                fragment = newInstance(packageName[i], bundle[i]);
            } else {
                fragment = newInstance(packageName[i], null);
            }
            list.add(fragment);
        }
        addFragment(container, showIndex, list);
    }


    /**
     * Activity中添加Fragment
     *
     * @param container
     * @param frags
     */
    protected void addFragment(@IdRes int container, T... frags) {
        addFragment(container, 0, frags);
    }

    /**
     * Activity中添加Fragment
     *
     * @param container
     * @param showIndex
     * @param frags
     */
    protected void addFragment(@IdRes int container, int showIndex, T... frags) {
        List<T> lists = Arrays.asList(frags);
        addFragment(container, showIndex, lists);
    }

    /**
     * Activity中添加Fragment
     * 缺少Fragment状态保存
     *
     * @param container fragment显示的布局id
     * @param showIndex 显示Fragment的下标
     * @param frags     添加Fragment数组
     */
    protected void addFragment(@IdRes int container, int showIndex, List<T> frags) {
        this.showIndex = showIndex >= frags.size() ? 0 : showIndex;
        if (fragments == null) fragments = new ArrayList<>();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        int index = 0;
        for (T fragment : frags) {
            T oldFragment = null;
            if (savedInstanceState != null) {
                oldFragment = (T) getSupportFragmentManager()
                        .getFragment(savedInstanceState, fragment.getClass().getName());
            }
            //判断是否有上次保存的Fragments
            if (oldFragment == null) {
                oldFragment = fragment;
            }
            fragments.add(oldFragment);
            //添加Fragment并添加TAG,便于Activity销毁后查找
            if (!oldFragment.isAdded()) {
                ft.add(container, oldFragment, oldFragment.getClass().getName());
            }
            //控制Fragment显示隐藏
            if (showIndex == index) {
                ft.show(oldFragment);
                if (oldFragment instanceof ExtraFragment) oldFragment.setUserVisibleHint(true);
            } else {
                ft.hide(oldFragment);
                if (oldFragment instanceof ExtraFragment) oldFragment.setUserVisibleHint(false);
            }
            index++;
        }
        ft.commit();
    }


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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragments.get(newIndex))
                .hide(fragments.get(showIndex))
                .commit();
        fragments.get(newIndex).setUserVisibleHint(true);
        showIndex = newIndex;
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
