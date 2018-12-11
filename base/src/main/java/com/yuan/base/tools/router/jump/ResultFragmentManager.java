package com.yuan.base.tools.router.jump;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;

/**
 * 处理ResultFragment添加到Activity
 *
 * @author yuanye
 * @date 2018/12/11
 */
public class ResultFragmentManager {

    /**
     * 标识Fragment
     */
    private static final String TAG = "com.yuan.base.tools.router.jump.ResultFragmentManager";
    /**
     * 处理返回结果的Fragment
     */
    private ResultFragment resultFragment;


    public ResultFragmentManager(Activity activity) {
        resultFragment = getFragment(activity);
    }

    public ResultFragmentManager(Context context) {
        this((Activity) context);
    }


    private ResultFragment getFragment(Activity activity) {
        ResultFragment fragment = (ResultFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new ResultFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    public ResultFragment getFragment() {
        return resultFragment;
    }
}
