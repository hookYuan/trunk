package com.yuan.base.tools.router.permission;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.yuan.base.tools.router.jump.ResultFragmentManager;

/**
 * 请求权限
 *
 * @author yuanye
 * @date 2018/12/11
 */
public class Permission {

    /**
     * 提供的默认请求码
     */
    public final static int PERMISSIONREQUESTCODE = 10011;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void request(Context mContext, String[] permissions, int requestCode, OnPermissionListener listener) {
        ResultFragmentManager jumpResult = new ResultFragmentManager(mContext);
        jumpResult.getFragment().startPermission(permissions, requestCode, listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void request(Context mContext, String[] permissions, OnPermissionListener listener) {
        request(mContext, permissions, PERMISSIONREQUESTCODE, listener);
    }
}
