package com.yuan.simple;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.yuan.base.ui.activity.FragActivity;
import com.yuan.base.tools.other.Views;
import com.yuan.simple.one.ui.OneFragment;
import com.yuan.simple.three.ui.ThreeFragment;
import com.yuan.simple.two.ui.TwoFragment;

public class MainActivity extends FragActivity {

    @Override
    public void initData(Bundle savedInstanceState) {
        addFragment(R.id.content, OneFragment.class, TwoFragment.class, ThreeFragment.class);
        BottomNavigationView bottomView = Views.find(mContext, R.id.bottomNavigationView);
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        showFragment(0);
                        return true;
                    case R.id.navigation_dashboard:
                        showFragment(1);
                        return true;
                    case R.id.navigation_notifications:
                        showFragment(2);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
