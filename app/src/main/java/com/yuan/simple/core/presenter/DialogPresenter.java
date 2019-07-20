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
package com.yuan.simple.core.presenter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.yuan.simple.R;
import com.yuan.simple.core.module.SubjectBean;
import com.yuan.simple.core.module.MultiBean;
import com.yuan.simple.main.contract.MainContract;

import java.util.ArrayList;
import java.util.List;

import yuan.core.dialog.DialogUtil;
import yuan.core.mvp.Presenter;
import yuan.core.tool.Kits;
import yuan.core.tool.ToastUtil;
import yuan.core.tool.Views;

/**
 * 这里Presenter一定要使用BaseContract,不要直接时使用
 * Fragment和Activity，避免Presenter与View耦合度高，不能复用
 *
 * @author yuanye
 * @date 2019/7/19
 */
public class DialogPresenter extends Presenter<MainContract> {

    /**
     * 当前进度
     */
    private int mCurrentPercent;

    /**
     * 获取数据
     *
     * @param mData Recycler集合
     */
    public void loadData(List<SubjectBean> mData) {
        new Handler(Looper.myLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.add(new SubjectBean("默认弹窗", 1));
                        mData.add(new SubjectBean("最大宽度", 2));
                        mData.add(new SubjectBean("相对位置", 3));
                        mData.add(new SubjectBean("背景颜色", 4));
                        mData.add(new SubjectBean("灰色背景透明度", 5));
                        mData.add(new SubjectBean("前景透明度", 6));
                        mData.add(new SubjectBean("字体颜色大小", 7));
                        mData.add(new SubjectBean("自定义弹窗", 8));
                        mData.add(new SubjectBean("日期选择弹窗", 9));
                        mData.add(new SubjectBean("时间选择弹窗", 10));
                        mData.add(new SubjectBean("加载中弹窗", 11));
                        mData.add(new SubjectBean("进度条弹窗", 12));
                        mData.add(new SubjectBean("单选弹窗", 13));
                        mData.add(new SubjectBean("多选弹窗", 14));
                        mData.add(new SubjectBean("列表弹窗", 15));
                        mData.add(new SubjectBean("取消弹窗", 16));
                        mData.add(new SubjectBean("平移动画", 17));
                        //更新列表
                        getView().notifyDataChange(true);
                    }
                }, 200);
    }

    /**
     * 默认弹窗,可以全局配置
     */
    public void showDialog1() {
        DialogUtil.create(getContext()).alertText("这是一个简单提示");
    }

    /**
     * 最大宽度
     */
    public void showDialog2() {
        DialogUtil.Params params1 = new DialogUtil.Params.Builder()
                .matchHeight(false)
                .matchWidth(true)
                .paddingTop(0)
                .paddingBottom(0)
                .paddingLeft(0)
                .paddingRight(0)
                .gravity(Gravity.BOTTOM)
                .windowBackground(getColor2(R.color.white))
                .build();
        DialogUtil.create(getContext(), params1).alertText("最大宽度的Dialog");
    }

    /**
     * 相对位置
     */
    public void showDialog3() {
        DialogUtil.Params params2 = new DialogUtil.Params.Builder()
                .gravity(Gravity.BOTTOM)
                .build();
        DialogUtil.create(getContext(), params2).alertText("Dialog居下显示");
    }

    /**
     * 背景颜色
     */
    public void showDialog4() {
        DialogUtil.Params params3 = new DialogUtil.Params.Builder()
                .windowBackground(getColor2(R.color.colorPrimary))
                .build();
        DialogUtil.create(getContext(), params3)
                .alertText("Dialog背景透明");
    }

    /**
     * 灰色背景透明度
     */
    public void showDialog5() {
        DialogUtil.Params params4 = new DialogUtil.Params.Builder()
                .dialogBehindAlpha(0.8f)
                .build();
        DialogUtil.create(getContext(), params4).alertText("灰色背景透明度");
    }

    /**
     * 前景透明度
     */
    public void showDialog6() {
        DialogUtil.Params params5 = new DialogUtil.Params.Builder()
                .dialogFrontAlpha(0.5f)
                .build();
        DialogUtil.create(getContext(), params5).alertText("前景背景透明度");
    }

    /**
     * 字体颜色大小
     */
    public void showDialog7() {
        DialogUtil.Params params6 = new DialogUtil.Params.Builder()
                .titleColor(getColor2(R.color.colorPrimary))
                .titleSize(20)
                .contentSize(16)
                .positiveColor(getColor2(R.color.colorPrimary))
                .build();
        DialogUtil.create(getContext(), params6).alertText("字体颜色大小");
    }

    /**
     * 自定义弹窗
     */
    public void showDialog8() {
        View dialogView = Views.inflate(getContext(), R.layout.my_dialog_view);
        DialogUtil.Params params7 = new DialogUtil.Params.Builder()
                .height(Kits.Dimens.dpToPxInt(getContext(), 200))
                .width(Kits.Dimens.dpToPxInt(getContext(), 250))
                .windowBackground(getColor2(R.color.transparent))
                .build();
        DialogUtil.create(getContext(), params7).alertView(dialogView);
    }

    /**
     * 日期选择弹窗
     */
    public void showDialog9() {
        DialogUtil.create(getContext()).alertDate(2019, 4, 23, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                showToast(year + "--" + month + "--" + dayOfMonth);
            }
        });
    }

    /**
     * 时间选择弹窗
     */
    public void showDialog10() {
        DialogUtil.create(getContext()).alertTime(13, 23, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                showToast(hourOfDay + "--" + minute);
            }
        });
    }

    /**
     * 加载中弹窗
     */
    public void showDialog11() {
        DialogUtil.create(getContext()).alertWait("提示", "加载中...");
    }

    /**
     * 进度条弹窗
     */
    public void showDialog12() {
        if (mCurrentPercent >= 100) {
            DialogUtil.create(getContext()).alertText("已经下载完成，是否重新下载？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int pos) {
                    mCurrentPercent = 0;
                }
            });
            return;
        }
        DialogUtil.create(getContext()).alertProgress("下载", 100);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mCurrentPercent >= 100) {
                        DialogUtil.dismiss();
                        return;
                    }
                    DialogUtil.setProgressCurrent(mCurrentPercent);
                    mCurrentPercent = mCurrentPercent + 1;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 单选弹窗
     */
    public void showDialog13() {
        final String[] singleData = {"长春", "重庆", "北京", "上海", "成都"};
        DialogUtil.create(getContext()).
                alertSingle("城市", singleData, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtil.showShort(getContext(), "您选择的是" + singleData[i]);
                    }
                });
    }

    /**
     * 多选弹窗
     */
    public void showDialog14() {
        List<MultiBean> mData = new ArrayList<>();
        MultiBean bean = new MultiBean("长春");
        bean.setSelect(true);
        mData.add(bean);
        mData.add(new MultiBean("重庆"));
        mData.add(new MultiBean("北京"));
        mData.add(new MultiBean("上海"));
        mData.add(new MultiBean("成都"));

        DialogUtil.create(getContext()).alertMulti("城市", mData, new DialogUtil.OnMultiListener() {
            @Override
            public <T extends DialogUtil.MultiItem> void onClick(DialogInterface dialog, List<T> selects) {
                showToast("选中的有" + selects.toString());
            }
        });
    }

    /**
     * 列表弹窗
     */
    public void showDialog15() {
        final String[] listData = {"长春", "重庆", "北京", "上海", "成都", "开封", "广东",
                "长春", "重庆", "北京", "上海", "成都", "开封"};
        DialogUtil.create(getContext()).
                alertList("城市", listData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToast("您选择了" + listData[i]);
                    }
                });
    }

    /**
     * 取消弹窗
     */
    public void showDialog16() {
        DialogUtil.dismiss();
    }

    /**
     * 平移动画
     */
    public void showDialog17() {
        DialogUtil.Params params17 = new DialogUtil.Params.Builder()
                .windowAnimations(R.anim.dialog_in)
                .build();
        DialogUtil.create(getContext(), params17).alertText("动画弹窗");
    }
}
