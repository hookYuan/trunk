package com.yuan.simple.one.roundView;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.GridView;

import yuan.core.mvp.BaseActivity;
import yuan.core.list.ListAdapter;
import com.yuan.simple.R;

import java.util.ArrayList;
import java.util.List;

public class RoundTextActivity extends BaseActivity {


    private GridView gridView;
    int selecte = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_round_text;
    }

    @Override
    public void findViews() {
        gridView = (GridView) findViewById(R.id.gv_simple_demo);
        gridView.setAdapter(getAdapter());
    }

    @Override
    public void parseBundle(@Nullable Bundle bundle) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

    }


    private ListAdapter getAdapter() {
        return new ListAdapter(getData(), R.layout.shape_gv_item) {
            @Override
            public void bindView(final ViewHolder holder, Object obj) {
                holder.setText(R.id.rtv_text, (String) obj);
                if (selecte == holder.getItemPosition()) {
                    holder.getView(R.id.rtv_text).setSelected(true);
                } else {
                    holder.getView(R.id.rtv_text).setSelected(false);
                }
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selecte = holder.getItemPosition();
                        notifyDataSetChanged();
                    }
                });
            }
        };
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("28");
        data.add("68");
        data.add("128");
        data.add("198");
        data.add("298");
        data.add("自定义");
        return data;
    }

}
