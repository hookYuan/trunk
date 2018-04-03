package com.yuan.base.mvp.extend;

import com.yuan.base.widget.state.StateController;

/**
 * Created by YuanYe on 2017/7/12.
 * 界面状态控制器
 */

public interface IStateController<T extends StateController> {
    T getStateView();
}
