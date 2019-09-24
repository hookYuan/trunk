/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package yuan.core.mvp;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * 描述：Activity的基础封装
 *
 * <p>
 * 1.支持便捷添加Fragment
 * 2.支持快速实现Mvp风格代码
 * 3.支持快捷加载 String,View,Drawable,color等资源
 * 4.统一书写格式
 * 5.通过泛型自动绑定Presenter,可通过getP()获取Presenter实例
 * <p>
 * 注意：因为这里使用到泛型P易冲突，所有使用泛型返回的方法都需要强转
 *
 * @author yuanye
 * @date 2019/4/4 13:17
 */
public abstract class BaseActivity<presenter extends Presenter> extends AppCompatActivity implements BaseContract.View {

    private static final String TAG = "BaseActivity";

    /**
     * 保存上次显示的Fragment
     */
    private static final String SAVE_SHOW_FRAGMENT = "save_show_fragment";

    /**
     * 上下文对象
     */
    protected AppCompatActivity mContext;

    /**
     * 需要默认显示Fragment下标,默认显示第一页
     */
    private int mDefaultShowPosition = 0;

    /**
     * 用于缓存调用{@link #addFragment(int, Fragment, Fragment[])}添加的Fragment
     */
    private List<Fragment> mFragmentCache;

    /**
     * 最新的OnCreate中读取的状态
     */
    private Bundle savedInstanceState;

    /**
     * presenter
     */
    private presenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.mContext = this;
        this.savedInstanceState = savedInstanceState;
        this.mFragmentCache = new ArrayList<>();
        //反射获取Presenter
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        super.onCreate(savedInstanceState);

        View layoutView = getLayoutView();
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
        } else if (layoutView != null) {
            setContentView(layoutView);
        } else {
            throw new NullPointerException("没有给Activity设置显示视图");
        }

        findViews();
        parseBundle(savedInstanceState);
        initData();
        initComplete();
        setListener();
        if (presenter != null) presenter.onCreate(savedInstanceState);
    }

    @Override
    public View getLayoutView() {
        return null;
    }

    @Override
    public void findViews() {

    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void initComplete() {

    }

    @Override
    protected void onResume() {
        //防止当onActivityResult后mContext为空
        if (mContext == null) mContext = this;
        super.onResume();
        if (presenter != null) presenter.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存Fragment的状态，防止Fragment被销毁
        if (mFragmentCache != null) {
            int index = 0;
            for (Fragment fragment : mFragmentCache) {
                if (fragment.isAdded()) {
                    //保存异常退出前显示的Fragment
                    if (mDefaultShowPosition == index) {
                        getSupportFragmentManager().putFragment(outState
                                , SAVE_SHOW_FRAGMENT
                                , fragment);
                    } else {
                        getSupportFragmentManager().putFragment(outState
                                , fragment.getClass().getName()
                                , fragment);
                    }

                }
                index++;
            }
        }
    }

    @Override
    protected void onDestroy() {
        //关闭键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View view = getCurrentFocus();
            if (view == null) view = new View(this);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }

    /**
     * 根据下标，设置要展示的Fragment
     *
     * @param position 要显示的Fragment的下标
     */
    public void showFragmentForPosition(int position) {
        if (mDefaultShowPosition == position || position < 0 ||
                position >= mFragmentCache.size())
            return;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(mFragmentCache.get(position))
                .hide(mFragmentCache.get(mDefaultShowPosition))
                .commit();

        mFragmentCache.get(position).setUserVisibleHint(true);
        mDefaultShowPosition = position;
    }

    /**
     * 显示指定Fragment
     *
     * @param fragment
     * @param <T>
     */
    public <T extends Fragment> void showFragment(T fragment) {
        if (!fragment.isAdded()) {
            Log.e(TAG, "fragment 未添加到Activity中:" + fragment.getClass().getName());
        }
        if (fragment.isHidden()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.show(fragment)
                    .show(fragment)
                    .hide(mFragmentCache.get(mDefaultShowPosition))
                    .commit();

            //更新Fragment 显示下标
            for (int i = 0; i < mFragmentCache.size(); i++) {
                if (mFragmentCache.get(i) == fragment) {
                    mDefaultShowPosition = i;
                }
            }
        }
    }

    /**
     * 根据添加fragment的位置获取Fragment
     *
     * @param position
     * @param <T>
     * @return
     */
    public <T extends Fragment> T getFragmentForPosition(int position) {
        if (position < 0 ||
                position >= mFragmentCache.size())
            return null;
        return (T) mFragmentCache.get(position);
    }

    /**
     * 获取Presenter
     *
     * @return presenter实例
     */
    public presenter getPresenter() {
        if (presenter == null) {
            try {
                throw new NullPointerException("使用presenter,MVPActivity泛型不能为空");
            } catch (NullPointerException e) {
                throw e;
            }
        }
        return presenter;
    }

    /**
     * 获取Presenter实例
     * <p>
     * 默认反射第一个泛型创建Presenter
     * 如果未指定泛型，请重写该方法
     *
     * @return Presenter的实例化对象
     */
    protected presenter createPresenter() {
        //只获取当前类的泛型参数
        Type type = this.getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        //当前class有泛型参数
        for (Type currentType : types) {
            /*遍历所有继承父类，判断是否包含Presenter类型*/
            Class superClass = ((Class) currentType).getSuperclass();
            while (superClass != Object.class) {
                if (superClass == Presenter.class) {
                    String presenterName = ((Class) currentType).getName();
                    try {
                        return (presenter) Class.forName(presenterName).newInstance();
                    } catch (InstantiationException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    } catch (IllegalAccessException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                }
                superClass = superClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param container   需要添加的布局ID
     * @param packageName Fragment包全名
     */
    protected <T extends Fragment> void addFragmentReflex(@IdRes int container, Class<T>... packageName) {
        addFragmentReflex(container, mDefaultShowPosition, packageName);
    }

    /**
     * Activity中添加Fragment
     *
     * @param container
     * @param fragmentClazz
     */
    protected <T extends Fragment> void addFragmentReflex(@IdRes int container, int showIndex, Class<T>... fragmentClazz) {
        ArrayList<FragmentBundle> list = new ArrayList<>();
        for (Class<T> fragment : fragmentClazz) {
            FragmentBundle fragmentBundle = new FragmentBundle();
            fragmentBundle.setClazz(fragment);
            list.add(fragmentBundle);
        }
        addFragmentReflex(container, showIndex, list);
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param container 需要添加的布局ID
     * @param fragments Fragment类型和Fragment参数
     */
    protected <T extends Fragment> void addFragmentReflex(@IdRes int container, List<FragmentBundle> fragments) {
        addFragmentReflex(container, mDefaultShowPosition, fragments);
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param showIndex 需要显示showFragment
     * @param fragments Fragment对应的类名
     */
    protected <T extends Fragment> void addFragmentReflex(@IdRes int container,
                                                          int showIndex,
                                                          List<FragmentBundle> fragments) {
        List<T> list = new ArrayList<>();
        for (FragmentBundle fragmentBundle : fragments) {
            //通过反射创建Fragment,默认反射无参构造方法
            T fragment = reflexCreateFragment((Class<T>) fragmentBundle.getClazz(), fragmentBundle.getBundle());
            //只添加创建成功的Fragment
            if (fragment != null) list.add(fragment);
        }
        Fragment[] availableFragment = new Fragment[list.size()];

        if (showIndex >= list.size()) showIndex = list.size() - 1;
        if (showIndex < 0) showIndex = 0;
        addFragment(container, list.get(showIndex), list.toArray(availableFragment));
    }

    /**
     * Activity中添加Fragment
     * 默认显示第一个添加Fragment
     *
     * @param container
     */
    protected <T extends Fragment> void addFragment(@IdRes int container, T... fragments) {
        addFragment(container, null, fragments);
    }

    /**
     * Activity中添加Fragment
     * 缺少Fragment状态保存
     *
     * @param container    fragment显示的布局id
     * @param showFragment 需要显示showFragment
     * @param fragments    添加Fragment数组
     */
    protected <T extends Fragment> void addFragment(@IdRes int container, T showFragment, T... fragments) {
        if (mFragmentCache == null) mFragmentCache = new ArrayList<>();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (T fragment : fragments) {
            /*根据标记TAG寻找Fragment上次是否异常退出,如果有则优先显示该Fragment*/
            if (savedInstanceState != null) {
                //异常中断Fragment
                T interruptFragment = (T) getSupportFragmentManager()
                        .getFragment(savedInstanceState, fragment.getClass().getName());

                if (interruptFragment != null) {
                    fragment = interruptFragment;
                    mFragmentCache.add(fragment);
                    Log.e(TAG, "异常Fragment--" + interruptFragment);
                } else {
                    //异常中断显示Fragment
                    T interruptShowFragment = (T) getSupportFragmentManager()
                            .getFragment(savedInstanceState, SAVE_SHOW_FRAGMENT);
                    if (interruptShowFragment != null) {
                        fragment = interruptShowFragment;
                        showFragment = fragment;
                        mFragmentCache.add(fragment);
                    }
                }
            }
            /*判断Fragment是否已经添加过，只添加未添加过的Fragment*/
            if (!fragment.isAdded()) {
                ft.add(container, fragment, fragment.getClass().getName());
                mFragmentCache.add(fragment);
            }
            /*控制Fragment显示隐藏*/
            if (showFragment == fragment) {
                ft.show(showFragment);
                showFragment.setUserVisibleHint(true);
                mDefaultShowPosition = mFragmentCache.size() - 1;
            } else {
                ft.hide(fragment);
                fragment.setUserVisibleHint(false);
            }
        }
        /*如果没有需要显示的Fragment,默认显示第一个Fragment*/
        if (showFragment == null && fragments.length > 0) {
            Fragment firstFragment = fragments[0];
            ft.show(firstFragment);
            firstFragment.setUserVisibleHint(true);
            mDefaultShowPosition = mFragmentCache.size() - 1;
        }
        ft.commit();
    }

    /**
     * 获取颜色
     *
     * @param colorId
     * @return
     */
    @ColorInt
    protected final int getColor2(@ColorRes int colorId) {
        return ContextCompat.getColor(this, colorId);
    }

    /**
     * 获取Drawable
     *
     * @param drawableId
     * @return
     */
    @Nullable
    protected final Drawable getDrawable2(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(this, drawableId);
    }

    /**
     * 获取String
     *
     * @param id
     * @return
     */
    @NonNull
    protected final String getString2(@StringRes int id) {
        return getResources().getString(id);
    }

    /**
     * Toast,系统默认样式
     *
     * @param msg 提示内容
     */
    protected final void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 反射 Fragment
     *
     * @param clazz  Fragment或者Fragment继承子类
     * @param bundle 需要传递给Fragment的参数
     * @return 返回子类对象实例
     */
    private <T extends Fragment> T reflexCreateFragment(Class<T> clazz, Bundle bundle) {
        T child = null;
        boolean isAbstract = Modifier.isAbstract(clazz.getModifiers());
        if (isAbstract) {
            Log.e(TAG, "抽象类型的Fragment不能被创建" + clazz.getName());
            return null;
        }
        try {
            child = (T) Class.forName(clazz.getName()).newInstance();
            if (bundle != null) {
                Bundle bundle1 = new Bundle();
                child.setArguments(bundle1);
                //建议通过这样的方式给Fragment传值,内存重启前,系统可以帮你保存数据
                //界面恢复后,不会造成数据的丢失。
                child.setArguments(bundle);
            }
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "反射创建Fragment异常：" + e.getMessage());
        } catch (InstantiationException e) {
            Log.e(TAG, "反射创建Fragment异常：" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, "反射创建Fragment异常：" + e.getMessage());
        }
        return child;
    }

    /**
     * 确保每个Fragment有一个对应的Bundle
     *
     * @param <T>
     */
    class FragmentBundle<T extends Fragment> {
        /**
         * Fragment 对应 class对象
         */
        private Class<T> clazz;
        /**
         * Fragment 对应Bundle对象
         */
        private Bundle bundle;

        public Class<T> getClazz() {
            return clazz;
        }

        public void setClazz(Class<T> clazz) {
            this.clazz = clazz;
        }

        public Bundle getBundle() {
            return bundle;
        }

        public void setBundle(Bundle bundle) {
            this.bundle = bundle;
        }
    }
}
