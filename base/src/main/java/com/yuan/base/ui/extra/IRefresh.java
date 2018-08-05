package com.yuan.base.ui.extra;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * Created by YuanYe on 2017/7/12.
 * <p>
 * 接口说明： 用于刷新的接口
 */
public interface IRefresh<T extends RefreshLayout> {

    void onRefresh(T refresh);

    void onLoadMore(T refresh);
}
