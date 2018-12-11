package com.yuan.simple.one.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.yuan.base.tools.adapter.recycler.GridDivider;
import com.yuan.base.tools.adapter.recycler.RLVAdapter;
import com.yuan.base.tools.common.Kits;
import com.yuan.base.tools.layout.Views;
import com.yuan.base.tools.log.ToastUtil;
import com.yuan.base.tools.router.jump.JumpHelper;
import com.yuan.base.ui.activity.RLVActivity;
import com.yuan.base.widget.dialog.v7.DialogHelper;
import com.yuan.base.widget.dialog.v7.DialogHelperParams;
import com.yuan.base.widget.dialog.v7.OnMultiListener;
import com.yuan.simple.R;
import com.yuan.simple.one.OneListBean;
import com.yuan.simple.one.foldTextView.FoldActivity;
import com.yuan.simple.one.http.NetActivity;
import com.yuan.simple.one.input.InputActivity;
import com.yuan.simple.one.roundView.RoundTextActivity;
import com.yuan.simple.one.sort.SortActivity;
import com.yuan.simple.one.toolbar.TitleBarActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AlertDialog使用示例
 */
public class AlertDialogActivity extends RLVActivity {

    private ArrayList<DialogBean> mData;
    int i = 0;

    @Override
    public void initRecyclerView(RecyclerView rlvList) {
        getTitleBar().setTitleText("AlertDialog")
                .setLeftClickFinish()
                .setTitleTextColor(getColor2(R.color.white))
                .setLeftIcon(getDrawable2(R.drawable.ic_base_back_white))
                .setBackgroundColor(getColor2(R.color.colorPrimary));
        rlvList.setLayoutManager(new LinearLayoutManager(this));
        rlvList.addItemDecoration(new GridDivider(this));
    }

    @Override
    public void onBindHolder(RLVAdapter.ViewHolder holder, int position) {
        holder.setText(android.R.id.text1, mData.get(position).getName());
    }

    @Override
    public int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {
        switch (mData.get(position).getType()) {
            case 1:
                new DialogHelper(this).alertText("这是一个简单提示");
                break;
            case 2:
                DialogHelperParams params1 = new DialogHelperParams.Builder()
                        .matchHeight(false)
                        .matchWidth(true)
                        .paddingTop(0)
                        .paddingBottom(0)
                        .paddingLeft(0)
                        .paddingRight(0)
                        .gravity(Gravity.BOTTOM)
                        .windowBackground(getColor2(R.color.white))
                        .build();
                DialogHelper helper = new DialogHelper(this, params1);
                helper.alertText("最大宽度的Dialog");
                break;
            case 3:
                DialogHelperParams params2 = new DialogHelperParams.Builder()
                        .gravity(Gravity.BOTTOM)
                        .build();
                new DialogHelper(this, params2).alertText("Dialog居下显示");
                break;
            case 4:
                DialogHelperParams params3 = new DialogHelperParams.Builder()
                        .windowBackground(getColor2(R.color.colorPrimary))
                        .build();
                new DialogHelper(this, params3).alertText("Dialog背景透明");
                break;
            case 5:
                DialogHelperParams params4 = new DialogHelperParams.Builder()
                        .dialogBehindAlpha(0.8f)
                        .build();
                new DialogHelper(this, params4).alertText("灰色背景透明度");
                break;
            case 6:
                DialogHelperParams params5 = new DialogHelperParams.Builder()
                        .dialogFrontAlpha(0.5f)
                        .build();
                new DialogHelper(this, params5).alertText("前景背景透明度");
                break;
            case 7:
                DialogHelperParams params6 = new DialogHelperParams.Builder()
                        .titleColor(getColor2(R.color.colorPrimary))
                        .titleSize(20)
                        .contentSize(16)
                        .positiveColor(getColor2(R.color.colorPrimary))
                        .build();
                new DialogHelper(this, params6).alertText("字体颜色大小");
                break;
            case 8:
                View dialogView = Views.inflate(this, R.layout.my_dialog_view);

                DialogHelperParams params7 = new DialogHelperParams.Builder()
                        .height(Kits.Dimens.dpToPxInt(this, 200))
                        .width(Kits.Dimens.dpToPxInt(this, 250))
                        .windowBackground(getColor2(R.color.transparent))
                        .build();
                new DialogHelper(this, params7)
                        .alertView(dialogView);
                break;
            case 9:
                new DialogHelper(this)
                        .alertDate(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                ToastUtil.showShort(AlertDialogActivity.this, (year + "--" + month + "--" + dayOfMonth));
                            }
                        });
                break;
            case 10:
                new DialogHelper(this)
                        .alertTime(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                ToastUtil.showShort(AlertDialogActivity.this, (hourOfDay + "--" + minute));
                            }
                        });
                break;
            case 11:
                new DialogHelper(this)
                        .alertWait("提示", "加载中...");
                break;
            case 12:
                if (i >= 100) {
                    new DialogHelper(mContext).alertText("已经下载完成，是否重新下载？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int pos) {
                            i = 0;
                        }
                    });
                    return;
                }
                final DialogHelper dialog = new DialogHelper(mContext);
                dialog.alertProgress("下载", 100);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (i >= 100) {
                                dialog.dismiss();
                                return;
                            }
                            dialog.setProgressCurrent(i);
                            i = i + 1;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                break;
            case 13:
                final String[] singleData = {"长春", "重庆", "北京", "上海", "成都"};
                new DialogHelper(mContext).alertSingle("城市", singleData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtil.showShort(mContext, "您选择的是" + singleData[i]);
                    }
                });
                break;
            case 14:
                final String[] mData = {"长春", "重庆", "北京", "上海", "成都"};
                new DialogHelper(mContext).alertMulti("城市", mData, new OnMultiListener() {
                    @Override
                    public void onClick(DialogInterface dialog, Map<Integer, String> selects) {
                        ToastUtil.showShort(mContext, "选中的有" + selects.toString());
                    }
                });
                break;
            case 15:
                final String[] listData = {"长春", "重庆", "北京", "上海", "成都", "开封", "广东",
                        "长春", "重庆", "北京", "上海", "成都", "开封"};
                new DialogHelper(mContext).alertList("城市", listData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtil.showShort(mContext, "您选择了" + listData[i]);
                    }
                });
                break;

        }
    }

    @Override
    public List<?> getData() {
        mData = new ArrayList<>();
        mData.add(new DialogBean("默认弹窗", 1));
        mData.add(new DialogBean("最大宽度", 2));
        mData.add(new DialogBean("相对位置", 3));
        mData.add(new DialogBean("背景颜色", 4));
        mData.add(new DialogBean("灰色背景透明度", 5));
        mData.add(new DialogBean("前景透明度", 6));
        mData.add(new DialogBean("字体颜色大小", 7));
        mData.add(new DialogBean("自定义弹窗", 8));
        mData.add(new DialogBean("日期选择弹窗", 9));
        mData.add(new DialogBean("时间选择弹窗", 10));
        mData.add(new DialogBean("加载中弹窗", 11));
        mData.add(new DialogBean("进度条弹窗", 12));
        mData.add(new DialogBean("单选弹窗", 13));
        mData.add(new DialogBean("多选弹窗", 14));
        mData.add(new DialogBean("列表弹窗", 15));
        return mData;
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void setListener() {

    }

    public class DialogBean {
        private String name;
        private int type;

        public DialogBean(String name, int type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
