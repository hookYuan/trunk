package com.yuan.simple.one.http;


import com.yuan.base.tools.okhttp.OKUtil;
import com.yuan.base.tools.log.ToastUtil;
import com.yuan.base.ui.kernel.Presenter;

/**
 * Created by YuanYe on 2018/8/15.
 */
public class PNet extends Presenter<NetActivity> {

    public void get() {
        new OKUtil(getContext()).get("http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455")
                .execute(new OKUtil.JsonBack() {
                    @Override
                    public void onSuccess(Object o, String json) {
                        ToastUtil.showShort(getContext(), json);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        ToastUtil.showShort(getContext(), e.getMessage());
                    }
                });
    }
}
