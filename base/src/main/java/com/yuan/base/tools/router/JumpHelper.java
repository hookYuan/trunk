package com.yuan.base.tools.router;

import android.content.Context;
import android.content.Intent;

/**
 * Created by YuanYe on 2018/4/13.
 * 跳转辅助类
 */

public class JumpHelper {

    public static void open(Context mContext, Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        mContext.startActivity(intent);
    }
}
