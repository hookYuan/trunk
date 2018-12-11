package com.yuan.base.tools.router.permission;

import android.support.annotation.NonNull;

/**
 * @author yuanye
 * @date 2018/12/11
 */
public interface OnPermissionListener {
    /**
     * 权限请求结果
     *
     * @param requestCode 请求码
     * @param result      对应的权限请求结果，true--有权限，false--没有权限
     */
    void onResult(int requestCode, @NonNull String[] permissions, @NonNull boolean[] result);
}
