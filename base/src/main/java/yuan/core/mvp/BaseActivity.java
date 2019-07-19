package yuan.core.mvp;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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
public abstract class BaseActivity<P extends Presenter> extends AppCompatActivity implements Contract.View {

    protected static final String TAG = Thread.currentThread().getStackTrace()[1].getClassName();

    /**
     * 保存上次显示的位置
     */
    private static final String SAVE_SHOW_POSITION = "showPosition";

    /**
     * 上下文对象
     */
    protected Activity mContext;

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

    /**
     * presenter
     */
    private P presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.mContext = this;
        this.savedInstanceState = savedInstanceState;
        if (savedInstanceState != null) {
            showIndex = savedInstanceState.getInt(SAVE_SHOW_POSITION, 0);
        }
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
        outState.putInt(SAVE_SHOW_POSITION, showIndex);
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
     * 把Fragment添加到FragmentActivity中
     *
     * @param container   需要添加的布局ID
     * @param packageName Fragment包全名
     */
    protected <T extends Fragment> void addFragment(@IdRes int container, Class<T>... packageName) {
        addFragment(container, showIndex, null, packageName);
    }

    /**
     * Activity中添加Fragment
     *
     * @param container
     * @param frags
     */
    protected <T extends Fragment> void addFragment(@IdRes int container, T... frags) {
        addFragment(container, 0, frags);
    }

    /**
     * Activity中添加Fragment
     *
     * @param container
     * @param showIndex
     * @param frags
     */
    protected <T extends Fragment> void addFragment(@IdRes int container, int showIndex, T... frags) {
        List<T> lists = Arrays.asList(frags);
        addFragment(container, showIndex, lists);
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param container   需要添加的布局ID
     * @param packageName Fragment包全名
     */
    protected <T extends Fragment> void addFragment(@IdRes int container, Bundle[] bundles, Class<T>... packageName) {
        addFragment(container, showIndex, bundles, packageName);
    }

    /**
     * 把Fragment添加到FragmentActivity中
     *
     * @param showIndex   默认显示的页数
     * @param packageName Fragment对应的类名
     */
    protected <T extends Fragment> void addFragment(@IdRes int container, int showIndex, Bundle[] bundle, Class<T>... packageName) {
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
     * 缺少Fragment状态保存
     *
     * @param container fragment显示的布局id
     * @param showIndex 显示Fragment的下标
     * @param frags     添加Fragment数组
     */
    protected <T extends Fragment> void addFragment(@IdRes int container, int showIndex, List<T> frags) {
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
                oldFragment.setUserVisibleHint(true);
            } else {
                ft.hide(oldFragment);
                oldFragment.setUserVisibleHint(false);
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
     * 获取Presenter
     *
     * @return presenter实例
     */
    public P getP() {
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
    protected <T> T createPresenter() {
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
            while (((Class) currentType).getSuperclass() != Object.class) {
                if (((Class) currentType).getSuperclass() == Presenter.class) {
                    String presenterName = ((Class) currentType).getName();
                    try {
                        return (T) Class.forName(presenterName).newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
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
    protected final void showToast(String msg) {
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
     * @param packageName 子类包名
     * @param bundle      需要传递给Fragment的参数
     * @return 返回子类对象实例
     */
    private <T extends Fragment> T newInstance(Class<T> packageName, Bundle bundle) {
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
        } catch (InstantiationException e) {
            Log.e(TAG, "反射创建Fragment异常：" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, "反射创建Fragment异常：" + e.getMessage());
        }
        return child;
    }

}
