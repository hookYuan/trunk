package com.yuan.base.ui.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuan.base.tools.common.ReflexUtil;
import com.yuan.base.ui.fragment.ExtraFragment;

/**
 * Created by YuanYe on 2017/9/19.
 */
public abstract class MvpFragment<T extends MvpPresenter> extends ExtraFragment {

    private T presenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter = ReflexUtil.getT(MvpFragment.this, 0);
        if (presenter != null) {
            presenter.attachView(this);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    protected T getP() {
        if (presenter == null) {
            try {
                throw new NullPointerException("使用presenter,MVPActivity泛型不能为空");
            } catch (NullPointerException e) {
                throw e;
            }
        }
        return presenter;
    }
}
