package com.yuan.ui_extra;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：图片查看Fragment
 * <p>
 * 用法：使用时，新建类继承ImageBrose（推荐）或 直接启动该Fragment
 * <p>
 * 依赖： Glide /GestureViews
 *
 * @author yuanye
 * @date 2019/4/24 11:10
 */
public abstract class ImageBrowse extends Fragment {

    private static final String EXTRA_SELECT_POS = "select_pos";
    private static final String EXTRA_SELECT_DATA = "select_data";
    /**
     * 当前选中的位置
     */
    private int currentPosition = 0;

    /**
     * 数据源
     * 支持Glide支持数据源
     */
    private ArrayList<String> mData;

    /**
     * viewPager
     */
    private ViewPager ultraViewPager;

    /**
     * 获取手势ImageView
     * 缩放
     *
     * @return
     */
    abstract ImageView getGestureView();

    /**
     * 显示当前页面
     *
     * @param position
     */
    abstract <T extends ImageView> void onCurrentPage(T imageView, int position);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ultraViewPager = new ViewPager(getActivity());
        return ultraViewPager;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentPosition = getActivity().getIntent().getIntExtra(EXTRA_SELECT_POS, 0);
        mData = getActivity().getIntent().getStringArrayListExtra(EXTRA_SELECT_DATA);
        //Initializing ViewPager
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(mData);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.addOnPageChangeListener(adapter);
        ultraViewPager.setCurrentItem(currentPosition <= 0 ? 0 : currentPosition);
        ultraViewPager.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        ultraViewPager.setPageMargin((int) (12 * getResources().getDisplayMetrics().density));
    }

    /**
     * Created by YuanYe on 2017/11/15.
     */
    class PhotoPagerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private List<T> mAllPhotos;
        /**
         * Item布局集合
         */
        private ArrayList<ImageView> pageViews;

        /**
         * 同时可见的Item数量
         */
        private int visibleNum = 4;


        public PhotoPagerAdapter(List<T> allPhotos) {
            this.mAllPhotos = allPhotos;
            pageViews = new ArrayList<>();
            for (int i = 0; i < visibleNum; i++) {
                //动态生成PageViews
                ImageView imageView = getGestureView();
                if (imageView == null) imageView = new ImageView(getActivity());
                pageViews.add(imageView);
            }
        }

        @Override
        public int getCount() {
            return mAllPhotos.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            int i = position % visibleNum;
            Glide.with(container.getContext()).load(mAllPhotos.get(position)).into(pageViews.get(i));
            ((ViewPager) container).addView(pageViews.get(i));
            return pageViews.get(i);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imageView = (ImageView) object;
            clearImageView(imageView);
            int i = position % visibleNum;
            container.removeView(pageViews.get(i));
            System.gc();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 释放ImageView资源
         *
         * @param imageView
         */
        private void clearImageView(ImageView imageView) {
            if (imageView == null) {
                return;
            }
            imageView.setDrawingCacheEnabled(true);
            Bitmap bm = imageView.getDrawingCache();
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
            Glide.clear(imageView);
            imageView.setDrawingCacheEnabled(false);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            onCurrentPage(pageViews.get(position), position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
