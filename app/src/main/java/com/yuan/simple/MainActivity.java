package com.yuan.simple;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yuan.base.mvp.activity.TitleActivity;
import com.yuan.base.tools.other.Views;
import com.yuan.base.widget.title.OnMenuItemClickListener;
import com.yuan.base.widget.title.TitleInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TitleActivity {
    boolean isO;

    @Override
    public void initData(Bundle savedInstanceState) {
//        addFragment(R.id.content,com.yuan.simple.one.ui.OneFragment.class);
        getTitleBar().setToolbar("袁冶");
        Button button = Views.find(mContext, R.id.btn_switch);
        isO = true;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isO) {
                    isO = false;
                    getTitleBar().setDefaultTheme(new int[]{
                            TitleInterface.TITLE_CONTENT_TOP
                    });
                } else {
                    isO = true;
                    getTitleBar().setDefaultTheme(new int[]{
                            TitleInterface.TITLE_CONTENT_OVERLAY
                    });
                    getTitleBar().setTitleStatusBgImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1130528128,1771363425&fm=27&gp=0.jpg");
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
