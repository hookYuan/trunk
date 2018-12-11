package com.yuan.base.tools.router.jump;

import android.content.Intent;

/**
 * startActivity 返回结果处理
 *
 * @author yuanye
 * @date 2018/12/11
 */
public interface OnResultListener {
    void onResult(int requestCode, int resultCode, Intent data);
}
