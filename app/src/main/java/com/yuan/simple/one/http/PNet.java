package com.yuan.simple.one.http;


import com.yuan.base.tools.okhttp.OKUtil;
import com.yuan.base.tools.log.ToastUtil;
import com.yuan.base.ui.MvpPresenter;

/**
 * Created by YuanYe on 2018/8/15.
 */
public class PNet extends MvpPresenter<NetActivity> {

    public void get() {
<<<<<<< HEAD
        new OKHttp(getV())
                .get("http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455")
                .execute(new JsonBack() {
=======
        new OKUtil(getV()).get("http://mobile.weather.com.cn/data/sk/101010100.html?_=1381891661455")
                .execute(new OKUtil.JsonBack() {
>>>>>>> develop
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
