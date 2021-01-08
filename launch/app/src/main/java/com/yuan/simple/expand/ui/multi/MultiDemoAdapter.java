//package com.yuan.simple.expand.ui.multi;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.yuan.simple.R;
//import com.yuan.simple.expand.module.MultiItemBean;
//
///**
// * @author yuanye
// * @date 2018/12/12
// */
//public class MultiDemoAdapter extends MultiAdapter<SectionBean, MultiItemBean> {
//
//    public MultiDemoAdapter(Context context) {
//        super(context);
//    }
//
//    @Override
//    public void onBindSectionView(View sectionView, SectionBean section, int position) {
//        TextView textView = sectionView.findViewById(R.id.tv_title);
//        textView.setText("标题" + section.name);
//    }
//
//    @Override
//    public int getItemLayout(ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case TYPE_ITEM:
//                return R.layout.multi_item;
//            case TYPE_SECTION:
//                return R.layout.multi_section;
//        }
//        return 0;
//    }
//
//    @Override
//    public void onBindHolder(ViewHolder holder, int position) {
//        switch (getItemViewType(position)) {
//            case TYPE_ITEM:
//                holder.setText(R.id.tv_content, "内容" +
//                        getMultiData().get(position).getItem().name);
//                break;
//            case TYPE_SECTION:
//                holder.setText(R.id.tv_title, "标题" +
//                        getMultiData().get(position).getSection().name);
//                break;
//        }
//    }
//}
