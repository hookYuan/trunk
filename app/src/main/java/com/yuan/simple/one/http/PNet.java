package com.yuan.simple.one.http;


import android.os.Environment;

import com.yuan.kernel.mvp.Presenter;
import com.yuan.kernel.ToastUtil;
import com.yuan.tools_extra.OKUtil;

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
        OKUtil.with(getContext(), new OKUtil.Config.Builder()
                .setCache(true)
                .build())
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

    public void showCacheSize() {
        long size = OKUtil.getCacheSize();
        ToastUtil.showShort(getContext(), size + "B");
    }

    public void delCache() {
        OKUtil.delCache();
        ToastUtil.showShort(getContext(), "删除成功");
    }

    public void downloadFile() {
        OKUtil.with(getContext())
                .get("http://cp-dev.yqcx.faw.cn/ftp/file/signDownlaodUrl?fileKey=FgpjStJHE2bL7_gWao5Umfpe6RLs")
                .execute(new OKUtil.FileBack(Environment.getExternalStorageDirectory() + "/Download/", "232.pptx") {
                    @Override
                    public void onSuccess(String fileDir, byte[] bytes) {
//                        ToastUtil.showShort(getV(), "文件大小：" + bytes.length);
                    }

                    @Override
                    public void onFail(Exception e) {
//                        ToastUtil.showShort(getV(), "下载异常：" + e.getMessage());
                    }
                });
    }
}
