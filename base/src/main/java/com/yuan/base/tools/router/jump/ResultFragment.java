package com.yuan.base.tools.router.jump;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.yuan.base.tools.router.permission.OnPermissionListener;

import java.util.HashMap;

/**
 * 处理startActivityForResult返回结果
 * 处理onRequestPermissionsResult返回结果
 *
 * @author yuanye
 * @date 2018/12/11
 */
public class ResultFragment extends Fragment {
    /**
     * onResult回调集合
     */
    private HashMap<Integer, OnResultListener> resultListeners = new HashMap<>();

    /**
     * onPermission回调集合
     */
    private HashMap<Integer, OnPermissionListener> permissionListeners = new HashMap<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 执行启动
     *
     * @param intent
     * @param requestCode
     * @param listener
     */
    public void startForResult(Intent intent, int requestCode, OnResultListener listener) {
        resultListeners.put(requestCode, listener);
        startActivityForResult(intent, requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startPermission(String[] permissions, int requestCode, OnPermissionListener listener) {
        permissionListeners.put(requestCode, listener);
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callback方式的处理
        OnResultListener listener = resultListeners.remove(requestCode);
        if (listener != null) {
            listener.onResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermissionListener listener = permissionListeners.remove(requestCode);
        if (listener != null) {
            boolean[] results = new boolean[permissions.length];
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    results[i] = true;
                } else {
                    results[i] = false;
                }
            }
            listener.onResult(requestCode, permissions, results);
        }
    }
}
