package com.yuan.simple;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import androidx.fragment.app.FragmentManager;
import yuan.core.dialog.DialogUtil;
import yuan.core.mvp.BaseActivity;
import yuan.core.tool.RouteUtil;
import yuan.core.tool.Views;
import com.yuan.simple.one.BaseFragment;
import com.yuan.simple.three.ui.ThreeFragment;

public class MainActivity extends BaseActivity {

    /**
     * 底部导航
     */
    private BottomNavigationView bottomView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void findViews() {
        bottomView = Views.find(mContext, R.id.bottomNavigationView);
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        DialogUtil.create(mContext);
    }

    @Override
    public void initData() {
//        addFragment(R.id.content, BaseFragment.class, TwoFragment.class, ThreeFragment.class);

        FragmentManager manager = getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
//        transaction.add(R.id.content,new BaseFragment());
//        transaction.add(R.id.content,new BaseFragment());
//        transaction.add(R.id.content,new BaseFragment());
//        transaction.add(R.id.content,new BaseFragment());
//        transaction.commit();
        addFragment(R.id.content, new BaseFragment(), new BaseFragment(), new ThreeFragment());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RouteUtil.openPermission(mContext, new String[]{
                    Manifest.permission.CAMERA
                    , Manifest.permission.CALL_PHONE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE
            }, new RouteUtil.OnPermissionListener() {
                @Override
                public void onResult(int requestCode, @NonNull String[] permissions, @NonNull boolean[] result) {
                    Log.i("yuanye", permissions.toString() + "--" + result.toString());
                }
            });
        }
    }

    @Override
    public void setListener() {
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
}
