package com.yuan.simple.one.multi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuan.base.tools.adapter.recycler.MultiSectionAdapter;
import com.yuan.simple.R;

/**
 * @author yuanye
 * @date 2018/12/12
 */
public class MultiAdapter extends MultiSectionAdapter<String, String> {

    public MultiAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindFloatView(View floatView, int position) {
        TextView textView = floatView.findViewById(R.id.tv_title);
        textView.setText("标题" + position);
    }

    @Override
    public int getItemLayout(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MultiSectionAdapter.TYPE_ITEM:
                return R.layout.multi_item;
            case MultiSectionAdapter.TYPE_SECTION:
                return R.layout.multi_section;
        }
        return 0;
    }

    @Override
    public void onBindHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MultiSectionAdapter.TYPE_ITEM:
                holder.setText(R.id.tv_content, "内容" + position);
                break;
            case MultiSectionAdapter.TYPE_SECTION:
                holder.setText(R.id.tv_title, "标题" + position);
                break;
        }
    }

}
