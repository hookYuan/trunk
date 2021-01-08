package com.yuan.simple.expand.ui.multi;


import com.yuan.simple.R;

import yuan.core.mvp.BaseActivity;

/**
 * @author yuanye
 * @date 2018-12-12 12:20:59
 */
public class MultiActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_multi;
    }

    @Override
    public void findViews() {
//        rlvList = findViewById(R.id.rlv_list);
//        titleBar = findViewById(R.id.title_bar);
//
//        rlvList.setLayoutManager(new GridLayoutManager(mContext, 2));
//        rlvList.setAdapter(getAdapter(MultiBean.TYPE_NORMAL, 1));
    }

    @Override
    public void initData() {

    }

    private boolean switchFlag = false;

    @Override
    public void setListener() {
//        titleBar.setRightOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (switchFlag) {
//                    switchFlag = false;
//                    //清除样式
//                    for (int i = 0; i < rlvList.getItemDecorationCount(); i++) {
//                        rlvList.removeItemDecorationAt(i);
//                    }
//
//                    rlvList.setLayoutManager(new GridLayoutManager(mContext, 2));
//                    rlvList.setAdapter(getAdapter(MultiAdapter.MultiBean.TYPE_NORMAL, 1));
//                    rlvList.addItemDecoration(new DecorationDivider(mContext));
//                    titleBar.setRightText("样式一");
//                } else {
//                    for (int i = 0; i < rlvList.getItemDecorationCount(); i++) {
//                        rlvList.removeItemDecorationAt(i);
//                    }
//                    rlvList.addItemDecoration(new DecorationDivider(mContext));
//
//                    rlvList.setLayoutManager(new GridLayoutManager(mContext, 1));
//                    rlvList.setAdapter(getAdapter(MultiAdapter.MultiBean.TYPE_FLOAT, 1));
//                    titleBar.setRightText("样式二");
//                    switchFlag = true;
//                }
//            }
//        });
    }


//    private MultiDemoAdapter getAdapter(int sectionType, int count) {
//        MultiDemoAdapter multiAdapter = new MultiDemoAdapter(mContext);
//        List<MultiItemBean> list = new ArrayList<>();
//        list.add(new MultiItemBean("1"));
//        list.add(new MultiItemBean("2"));
//        list.add(new MultiItemBean("3"));
//        list.add(new MultiItemBean("4"));
//        multiAdapter.addSection(new SectionBean("1"), sectionType);
//        multiAdapter.addData(list, count);
//        multiAdapter.addSection(new SectionBean("2"), sectionType);
//        multiAdapter.addData(list, count);
//        multiAdapter.addSection(new SectionBean("3"), sectionType);
//        multiAdapter.addData(list, count);
//        multiAdapter.addSection(new SectionBean("4"), sectionType);
//        multiAdapter.addData(list, count);
//        multiAdapter.addSection(new SectionBean("5"), sectionType);
//        multiAdapter.addData(list, count);
//        multiAdapter.addSection(new SectionBean("6"), sectionType);
//        multiAdapter.addData(list, count);
//        multiAdapter.addSection(new SectionBean("7"), sectionType);
//        multiAdapter.addData(list, count);
//        multiAdapter.addSection(new SectionBean("8"), sectionType);
//        multiAdapter.addData(list, count);
//        multiAdapter.addData(list, count);
//        return multiAdapter;
//    }
}
