package yuan.depends.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.security.MessageDigest;

import yuan.depends.R;

/**
 * 描述： 整合Glide基本使用
 * 圆角图片，圆形图片加载
 * <p>
 * 依赖库：
 * Glide
 *
 * @author yuanye
 * @date 2019/4/4 8:53
 */
public class GlideUtil {


    private static GlideUtil instance;
    RequestOptions options;

    private GlideUtil() {
        options = new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.priority(Priority.HIGH);

        options.error(R.drawable.ic_default_image);
        //设置占位符,默认
        options.placeholder(R.drawable.ic_default_image);
        //设置错误符,默认
        options.error(R.drawable.ic_default_image);
    }

    public static GlideUtil create() {
        if (instance == null) {
            synchronized (GlideUtil.class) {
                if (instance == null) {
                    instance = new GlideUtil();
                }
            }
        }
        return instance;
    }

    //设置占位符
    public GlideUtil setPlaceholder(int id) {
        options.placeholder(id);
        return instance;
    }

    public GlideUtil setPlaceholder(Drawable drawable) {
        options.placeholder(drawable);
        return instance;
    }

    //设置错误符
    public GlideUtil setError(int id) {
        options.error(id);
        return instance;
    }

    public GlideUtil setError(Drawable drawable) {
        options.error(drawable);
        return instance;
    }

    public void showImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);

    }

    //以图片宽度为基准
    public void showImageWidthRatio(String url, final ImageView imageView, final int width) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .apply(options)
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int imageWidth = resource.getWidth();
                        int imageHeight = resource.getHeight();
                        int height = width * imageHeight / imageWidth;
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        params.height = height;
                        params.width = width;
                        imageView.setImageBitmap(resource);
                    }
                });
    }

    //以图片高度为基准
    public void showImageHeightRatio(String url, final ImageView imageView, final int height) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int imageWidth = resource.getWidth();
                        int imageHeight = resource.getHeight();
                        int width = height * imageHeight / imageWidth;
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        params.height = height;
                        params.width = width;
                        imageView.setImageBitmap(resource);
                    }
                });
    }

    //设置图片固定的大小尺寸
    public void showImageWH(String url, final ImageView imageView, int height, int width) {
        options.override(width, height);
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }

    //设置图片圆角，以及弧度
    public void showImageRound(String url, final ImageView imageView, int radius) {
        options.transform(new RoundTransform(radius));
//        options.transform(new GlideCircleTransform());
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);

    }

    public void showImageRound(String url, final ImageView imageView, int radius, int height, int width) {
        //不一定有效，当原始图片为长方形时设置无效
        options.override(width, height);
        options.transform(new RoundTransform(radius));
//        options.centerCrop(); //不能与圆角共存
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);

    }


    public void showImageRound(String url, final ImageView imageView) {
        //自带圆角方法，显示圆形
        options.circleCrop();
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }


    /**
     * Created by YuanYe on 2017/6/5.
     * 圆角效果的Transform
     */
    static class RoundTransform extends BitmapTransformation {
        private static float radius = 4f;

        public RoundTransform() {
            this(4);
        }

        public RoundTransform(int dp) {
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }


        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }


    /**
     * Created by caoyingfu on 16/3/18.
     * 圆形的Transformation
     */
    static class CircleTransform extends BitmapTransformation {

        public CircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();

            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
}
