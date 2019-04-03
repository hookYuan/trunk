package com.yuan.simple.one.http;


import com.yuan.base.tools.okhttp.OKHttp;
import com.yuan.base.tools.log.ToastUtil;
import com.yuan.base.tools.okhttp.JsonBack;
import com.yuan.base.ui.MvpPresenter;

/**
 * Created by YuanYe on 2018/8/15.
 */
public class PNet extends MvpPresenter<NetActivity> {

    public void get() {
        new OKHttp(getV()).get("http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455")
                .execute(new JsonBack() {
                    @Override
                    public void onSuccess(Object o, String json) {
                        ToastUtil.showShort(getContext(), json);
                    }
                });
    }
}
