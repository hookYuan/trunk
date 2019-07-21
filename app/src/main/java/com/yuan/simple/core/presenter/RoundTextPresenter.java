package com.yuan.simple.core.presenter;

import com.yuan.simple.core.ui.roundview.RoundTextActivity;

import java.util.ArrayList;
import java.util.List;

import yuan.core.mvp.Presenter;

/**
 * @author yuanye
 * @date 2019/7/21
 */
public class RoundTextPresenter extends Presenter<RoundTextActivity> {


    /**
     * 加载数据源
     *
     * @return
     */
    public List<String> loadData() {
        List<String> data = new ArrayList<>();
        data.add("28");
        data.add("68");
        data.add("128");
        data.add("198");
        data.add("298");
        data.add("自定义");
        return data;
    }
}
