package com.yuan.ui_extra;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片查看Activity
 * 依赖： Glide 用于加载图片
 * GestureViews 用于查看图片.
 */
public abstract class PhotoActivity extends Activity {

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
    private List<Object> mData;

    /**
     * 获取ViewPager
     *
     * @return
     */
    abstract ViewPager getViewPager();

    /**
     * 获取页面布局
     *
     * @return
     */
    abstract View getContentView();

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        currentPosition = getIntent().getIntExtra(EXTRA_SELECT_POS, 0);
        Object = getIntent().get(EXTRA_SELECT_DATA);
        //Initializing ViewPager
        ViewPager ultraViewPager = getViewPager();
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(mData);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.addOnPageChangeListener(adapter);
        ultraViewPager.setCurrentItem(currentPosition <= 0 ? 0 : currentPosition);
        ultraViewPager.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
        ultraViewPager.setPageMargin((int) (12 * getResources().getDisplayMetrics().density));
    }

    /**
     * Created by YuanYe on 2017/11/15.
     */
    class PhotoPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private List<String> mAllPhotos;
        /**
         * Item布局集合
         */
        private ArrayList<ImageView> pageViews;

        /**
         * 同时可见的Item数量
         */
        private int visibleNum = 4;


        public PhotoPagerAdapter(List<String> allPhotos) {
            this.mAllPhotos = allPhotos;
            pageViews = new ArrayList<>();
            for (int i = 0; i < visibleNum; i++) {
                //动态生成PageViews
                pageViews.add(getGestureView());
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
