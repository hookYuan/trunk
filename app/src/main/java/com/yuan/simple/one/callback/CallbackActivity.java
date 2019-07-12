package com.yuan.simple.one.callback;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.yuan.simple.R;

import java.util.logging.Handler;

import yuan.core.function.CallbackManager;
import yuan.core.function.CallbackNoParamNoResult;
import yuan.core.mvp.BaseActivity;
import yuan.core.tool.ToastUtil;

/**
 * 描述：
 * <p>
 * 1.0 2019.7.1  测试CallbackManager 是否有内存泄漏
 *
 * @author yuanye
 * @date 2019/7/11 15:06
 */
public class CallbackActivity extends BaseActivity {

    private String Activity = "CallbackActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_callback;
    }

    @Override
    public void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("yuanye", Activity);
            }
        }).start();
    }

    @Override
    public void setListener() {
        findViewById(R.id.btn_test)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallbackManager.get().invoke(CallbackActivity.this);
                    }
                });

        CallbackManager.get().setCallback(this, new CallbackNoParamNoResult() {
            @Override
            public void callback() {
                ToastUtil.showShort(mContext, "测试回调点击事件");
            }
        });
    }

    @Override
    protected void onDestroy() {
        LoginManager.getInstance(mContext);
        super.onDestroy();
        CallbackManager.get().remove(this);
    }
}
