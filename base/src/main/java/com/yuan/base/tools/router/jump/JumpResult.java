package com.yuan.base.tools.router.jump;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;

/**
 * @author yuanye
 * @date 2018/12/11
 */
public class JumpResult {

    /**
     * 标识Fragment
     */
    private static final String TAG = "com.yuan.base.tools.router.jump.JumpResult";
    /**
     * 处理返回结果的Fragment
     */
    private ResultFragment resultFragment;


    protected JumpResult(Activity activity) {
        resultFragment = getFragment(activity);
    }

    protected JumpResult(Context context) {
        this((Activity)context);
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

    protected ResultFragment getFragment() {
        return resultFragment;
    }
}
