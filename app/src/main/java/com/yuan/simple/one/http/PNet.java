package com.yuan.simple.one.http;


import com.yuan.base.tools.okhttp.OKUtil;
import com.yuan.base.tools.log.ToastUtil;
import com.yuan.base.ui.kernel.Presenter;

/**
 * Created by YuanYe on 2018/8/15.
 */
public class PNet extends Presenter<NetActivity> {

    public void get() {
        OKUtil.init(new OKUtil.Config.Builder()
                .setCache(false)
                .setCommonHead("yuanye", "987654")
                .setReConnect(true)
                .build());

        OKUtil.with(getContext())
                .get("http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455")
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

    public void get2() {
        OKUtil.with(getContext())
                .get("http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455")
                .put("yuanye", "123123")
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
