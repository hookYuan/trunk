package com.yuan.simple.one.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import yuan.core.dialog.DialogUtil;
import yuan.core.list.DecorationDivider;
import yuan.core.list.RLVAdapter;
import yuan.core.tool.ToastUtil;
import yuan.core.tool.Views;
import com.yuan.simple.tool.ListActivity;
import com.yuan.simple.R;
import yuan.core.tool.Kits;

import java.util.ArrayList;
import java.util.List;

/**
 * AlertDialog使用示例
 */
public class AlertDialogActivity extends ListActivity {

    private ArrayList<DialogBean> mData;
    int i = 0;

    private DialogUtil helper;

    @Override
    public void initRecyclerView(RecyclerView rlvList) {
        getTitleBar().setTitleText("AlertDialog")
                .setLeftClickFinish()
                .setTitleTextColor(getColor2(R.color.white))
                .setLeftIcon(getDrawable2(R.drawable.ic_base_back_white))
                .setBackgroundColor(getColor2(R.color.colorPrimary));
        rlvList.setLayoutManager(new LinearLayoutManager(this));
        rlvList.addItemDecoration(new DecorationDivider(this));
        helper = DialogUtil.create(mContext);
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
                helper.alertText("这是一个简单提示");
                break;
            case 2:
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
                DialogUtil.create(mContext).alertText("最大宽度的Dialog");
                break;
            case 3:
                DialogUtil.Params params2 = new DialogUtil.Params.Builder()
                        .gravity(Gravity.BOTTOM)
                        .build();
                helper.alertText("Dialog居下显示");
                break;
            case 4:
                DialogUtil.Params params3 = new DialogUtil.Params.Builder()
                        .windowBackground(getColor2(R.color.colorPrimary))
                        .build();
                DialogUtil.create(this).alertText("Dialog背景透明");
                break;
            case 5:
                DialogUtil.Params params4 = new DialogUtil.Params.Builder()
                        .dialogBehindAlpha(0.8f)
                        .build();
                DialogUtil.create(this).alertText("灰色背景透明度");
                break;
            case 6:
                DialogUtil.Params params5 = new DialogUtil.Params.Builder()
                        .dialogFrontAlpha(0.5f)
                        .build();
                DialogUtil.create(this).alertText("前景背景透明度");
                break;
            case 7:
                DialogUtil.Params params6 = new DialogUtil.Params.Builder()
                        .titleColor(getColor2(R.color.colorPrimary))
                        .titleSize(20)
                        .contentSize(16)
                        .positiveColor(getColor2(R.color.colorPrimary))
                        .build();
                DialogUtil.create(this).alertText("字体颜色大小");
                break;
            case 8:
                View dialogView = Views.inflate(this, R.layout.my_dialog_view);

                DialogUtil.Params params7 = new DialogUtil.Params.Builder()
                        .height(Kits.Dimens.dpToPxInt(this, 200))
                        .width(Kits.Dimens.dpToPxInt(this, 250))
                        .windowBackground(getColor2(R.color.transparent))
                        .build();
                DialogUtil.create(this).alertView(dialogView);
                break;
            case 9:
                helper.alertDate(2019, 4, 23, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ToastUtil.showShort(AlertDialogActivity.this, (year + "--" + month + "--" + dayOfMonth));
                    }
                });
                break;
            case 10:
                helper.alertTime(13, 23, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ToastUtil.showShort(AlertDialogActivity.this, (hourOfDay + "--" + minute));
                    }
                });
                break;
            case 11:
                helper.alertWait("提示", "加载中...");
                break;
            case 12:
                if (i >= 100) {
                    helper.alertText("已经下载完成，是否重新下载？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int pos) {
                            i = 0;
                        }
                    });
                    return;
                }
                helper.alertProgress("下载", 100);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (i >= 100) {
                                helper.dismiss();
                                return;
                            }
                            helper.setProgressCurrent(i);
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
                helper.alertSingle("城市", singleData, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtil.showShort(mContext, "您选择的是" + singleData[i]);
                    }
                });
                break;
            case 14:
                List<MultiBean> mData = new ArrayList<>();
                MultiBean bean = new MultiBean("长春");
                bean.setSelect(true);
                mData.add(bean);
                mData.add(new MultiBean("重庆"));
                mData.add(new MultiBean("北京"));
                mData.add(new MultiBean("上海"));
                mData.add(new MultiBean("成都"));

                helper.alertMulti("城市", mData, new DialogUtil.OnMultiListener() {
                    @Override
                    public <T extends DialogUtil.MultiItem> void onClick(DialogInterface dialog, List<T> selects) {
                        ToastUtil.showShort(mContext, "选中的有" + selects.toString());
                    }
                });
                break;
            case 15:
                final String[] listData = {"长春", "重庆", "北京", "上海", "成都", "开封", "广东",
                        "长春", "重庆", "北京", "上海", "成都", "开封"};
                helper.alertList("城市", listData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtil.showShort(mContext, "您选择了" + listData[i]);
                    }
                });
                break;
            case 16:
                DialogUtil.dismiss();
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
        mData.add(new DialogBean("取消弹窗", 16));
        return mData;
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    public class MultiBean extends DialogUtil.MultiItem {

        private String name;

        public MultiBean(String name) {
            this.name = name;
        }

        @Override
        public String getText() {
            return name;
        }

        @Override
        public String toString() {
            return "MultiBean{" +
                    "name='" + name + '\'' +
                    '}';
        }
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
