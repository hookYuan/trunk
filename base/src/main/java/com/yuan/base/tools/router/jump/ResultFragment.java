package com.yuan.base.tools.router.jump;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * 处理startActivityForResult返回结果
 *
 * @author yuanye
 * @date 2018/12/11
 */
public class ResultFragment extends Fragment {
    /**
     * onResult回调集合
     */
    private HashMap<Integer, OnResultListener> resultListeners = new HashMap<>();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callback方式的处理
        OnResultListener listener = resultListeners.remove(requestCode);
        if (listener != null) {
            listener.onResult(requestCode, resultCode, data);
        }
    }

}
