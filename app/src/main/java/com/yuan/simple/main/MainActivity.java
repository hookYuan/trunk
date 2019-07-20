/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuan.simple.main;

import android.Manifest;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;

import yuan.core.dialog.DialogUtil;
import yuan.core.mvp.BaseActivity;
import yuan.core.title.ActionBarUtil;
import yuan.core.tool.RouteUtil;
import yuan.core.tool.Views;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yuan.simple.R;
import com.yuan.simple.core.ui.CoreFragment;
import com.yuan.simple.depends.DependsFragment;
import com.yuan.simple.expand.ui.ExpandFragment;

/**
 * 启动Activity
 *
 * @author YuanYe
 * @date 2019/7/19  11:54
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
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
    public void initData() {
        addFragmentReflex(R.id.content, CoreFragment.class, DependsFragment.class, ExpandFragment.class);
        requestPermission();
    }

    @Override
    public void setListener() {
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        showFragmentForPosition(0);
                        ActionBarUtil.create(MainActivity.this)
                                .setTitleText("core");
                        return true;
                    case R.id.navigation_dashboard:
                        showFragmentForPosition(1);
                        ActionBarUtil.create(MainActivity.this)
                                .setTitleText("depends");
                        return true;
                    case R.id.navigation_notifications:
                        showFragmentForPosition(2);
                        ActionBarUtil.create(MainActivity.this)
                                .setTitleText("expand");
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 权限申请
     */
    private void requestPermission() {
        RouteUtil.openPermission(mContext, new String[]{
                Manifest.permission.CAMERA
                , Manifest.permission.CALL_PHONE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
        }, new RouteUtil.OnPermissionListener() {
            @Override
            public void onResult(int requestCode, @NonNull String[] permissions, @NonNull boolean[] result) {
                for (boolean isSuccess : result) {
                    if (!isSuccess) {
                        DialogUtil.create(mContext)
                                .alertText(getString2(R.string.main_permission_dialog), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RouteUtil.openSetting(mContext, RouteUtil.APPLICATION1);
                                    }
                                });
                        return;
                    }
                }
                Log.i(TAG, permissions.toString() + "--" + result.toString());
            }
        });
    }
}
