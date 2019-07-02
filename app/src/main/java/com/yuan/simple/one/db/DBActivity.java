package com.yuan.simple.one.db;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import yuan.core.list.RLVAdapter;
import yuan.core.tool.ToastUtil;
import com.yuan.simple.R;
import com.yuan.simple.tool.ListActivity;
import yuan.tools_independ.database.DBUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作
 */
public class DBActivity extends ListActivity {

    private List<String> data;

    @Override
    public void initData() {
        super.initData();
        getTitleBar().setTitleText("DBActivity")
                .setLeftClickFinish()
                .setTitleTextColor(getColor2(R.color.white))
                .setLeftIcon(getDrawable2(R.drawable.ic_base_back_white))
                .setBackgroundColor(getColor2(R.color.colorPrimary));
    }

    @Override
    protected void onBindHolder(RLVAdapter.ViewHolder holder, int position) {
        holder.setText(android.R.id.text1, data.get(position));
    }

    @Override
    protected int getItemLayout(ViewGroup parent, int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public List<?> getData() {
        data = new ArrayList<>();

        data.add("初始化数据库");
        data.add("创建数据表");
        data.add("插入数据");
        data.add("更新数据");
        data.add("删除整张表");
        data.add("查询整张表");
        return data;
    }

    @Override
    protected void onItemClick(RLVAdapter.ViewHolder holder, View view, int position) {
        switch (position) {
            case 1:
                DBUtil.getDB(mContext).createTable(UserBean.class);
                break;
            case 2:
                ArrayList<UserBean> list = new ArrayList<>();
                UserBean bean = new UserBean();
                bean.setAge(27);
                bean.setName("袁冶");
                UserBean bea2 = new UserBean();
                bea2.setAge(23);
                List<String> data = new ArrayList<>();
                data.add("真的假的");
//                bea2.setData(data);
                bea2.setName("袁2冶");
                list.add(bean);
                list.add(bea2);
                DBUtil.getDB(mContext).insertList(list);
                break;
            case 3:
                UserBean bean2 = new UserBean();
                bean2.setAge(26);
                DBUtil.getDB(mContext).update(bean2, "age=?", new String[]{"27"});
                break;
            case 4:
                DBUtil.getDB(mContext).delete(UserBean.class.getName());
                break;
            case 5:
                List<UserBean> list2 = DBUtil.getDB(mContext).querySQL("SELECT * FROM UserBean", null, UserBean.class);
                ToastUtil.showShort(mContext, list2.toString());
                break;
        }
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }
}
