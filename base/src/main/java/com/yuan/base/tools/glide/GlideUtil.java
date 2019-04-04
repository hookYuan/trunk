package com.yuan.base.tools.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

/**
 * 描述： 整合Glide基本使用
 *
 * @author yuanye
 * @date 2019/4/4 8:53
 */
public class GlideUtil {

    private static final ViewPropertyAnimation.Animator ANIMATOR =
            new ViewPropertyAnimation.Animator() {
                @Override
                public void animate(View view) {
                    view.setAlpha(0f);
                    view.animate().alpha(1f);
                }
            };

    private static
    @DrawableRes
    int placeholder = 0; //加载前的占位符

    private GlideUtil() {

    }

    /**
     * 加载本地 资源文件图片
     *
     * @param drawableId 资源ID
     * @param image      需要显示的ImageView
     */
    public static void load(@DrawableRes int drawableId, @NonNull ImageView image) {
        DisplayMetrics metrics = image.getResources().getDisplayMetrics();
        final int displayWidth = metrics.widthPixels;
        final int displayHeight = metrics.heightPixels;
        Glide.with(image.getContext())
                .load(drawableId)
                .asBitmap()
                .animate(ANIMATOR)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new BitmapImageViewTarget(image) {
                    @Override
                    public void getSize(final SizeReadyCallback cb) {
                        // We don't want to load very big images on devices with small screens.
                        // This will help Glide correctly choose images scale when reading them.
                        super.getSize(new SizeReadyCallback() {
                            @Override
                            public void onSizeReady(int width, int height) {
                                cb.onSizeReady(displayWidth / 2, displayHeight / 2);
                            }
                        });
                    }
                });
    }

    /**
     * 加载圆形图片
     *
     * @param path  图片的网络地址
     * @param image 需要显示的ImageView
     */
    public static void loadCircle(@Nullable String path, @NonNull final ImageView image) {
        final String imgPath = path;
        Glide.with(image.getContext())
                .load(imgPath == null ? null : imgPath)
                .crossFade(0)
                .placeholder(placeholder)
                .dontAnimate()
                .transform(new CircleTransform(image.getContext()))
                .thumbnail(Glide.with(image.getContext())  //缩略图
                        .load(imgPath == null ? null : imgPath)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                .into(image);
    }

    /**
     * 加载圆角矩形
     * 默认圆角弧度为4dp
     */
    public static void loadRound(@Nullable String path, @NonNull final ImageView image) {
        loadRound(path, image, (int) (image.getContext().getResources().getDisplayMetrics().density * 4.0f));
    }

    /**
     * 加载圆角矩形
     */
    public static void loadRound(@Nullable String path, @NonNull final ImageView image, int radius) {
        final String imgPath = path;
        Glide.with(image.getContext())
                .load(imgPath == null ? null : imgPath)
                .crossFade(0)
                .placeholder(placeholder)
                .dontAnimate()
                .transform(new RoundTransform(image.getContext(), radius))
                .thumbnail(Glide.with(image.getContext())  //缩略图
                        .load(imgPath == null ? null : imgPath)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                .into(image);
    }


    public static void load(@Nullable String path, @NonNull final ImageView image
            , @NonNull final @DrawableRes
                                    int placehol) {
        Glide.with(image.getContext())
                .load(path == null ? null : path)
                .crossFade(0)
                .placeholder(placehol)
                .dontAnimate()
                .thumbnail(Glide.with(image.getContext())
                        .load(path == null ? null : path)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                .into(image);
    }

    /**
     * 根据路径或者网络路径加载图片
     *
     * @param path  网络图片地址
     * @param image 需要显示的ImageView
     */
    public static void load(@Nullable String path, @NonNull final ImageView image) {
        load(path, image, placeholder);
    }


    /**
     * 加载原图
     *
     * @param path  加载图片的网络路径
     * @param image 需要显示的ImageView
     */
    public static void loadFull(@NonNull String path, @NonNull final ImageView image) {
        loadFull(path, image, new ImageLoadingListener() {
            @Override
            public void onLoaded() {

            }

            @Override
            public void onFailed() {

            }
        });
    }

    /**
     * 加载原图
     *
     * @param path     图片网络地址
     * @param image    需要显示的地址
     * @param listener 加载中的监听回调
     */
    public static void loadFull(@NonNull String path, @NonNull final ImageView image,
                                @Nullable final ImageLoadingListener listener) {
        final String urlImg = path;
        Glide.with(image.getContext())
                .load(urlImg)
                .asBitmap()
                .dontAnimate()
                .thumbnail(Glide.with(image.getContext())
                        .load(urlImg)
                        .asBitmap()
                        .animate(ANIMATOR)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                .listener(new GlideDrawableListener() {
                    @Override
                    public void onSuccess(String url) {
                        if (url.equals(urlImg)) {
                            if (listener != null) {
                                listener.onLoaded();
                            }
                        }
                    }

                    @Override
                    public void onFail(String url) {
                        if (listener != null) {
                            listener.onFailed();
                        }
                    }
                })
                .into(new GlideImageTarget(image));
    }


    public interface ImageLoadingListener {
        //加载完成
        void onLoaded();

        //加载失败
        void onFailed();
    }


    abstract static class GlideDrawableListener implements RequestListener<String, Bitmap> {

        @Override
        public boolean onException(Exception ex, String url,
                                   Target<Bitmap> target, boolean isFirstResource) {
            onFail(url);
            return false;
        }

        @Override
        public boolean onResourceReady(Bitmap resource, String url,
                                       Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
            onSuccess(url);
            return false;
        }

        public void onSuccess(String url) {
            // No-op
        }

        public void onFail(String url) {
            // No-op
        }

    }

    static class GlideImageTarget extends BitmapImageViewTarget {

        private static final long NO_ANIMATION_INTERVAL = 150L;

        private long startTime = 0L;

        GlideImageTarget(ImageView view) {
            super(view);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            super.onLoadStarted(placeholder);
            startTime = SystemClock.uptimeMillis();
        }

        @Override
        public void onResourceReady(Bitmap resource,
                                    GlideAnimation<? super Bitmap> glideAnimation) {

            if (startTime == 0 || SystemClock.uptimeMillis() - startTime < NO_ANIMATION_INTERVAL) {
                startTime = 0L;
                glideAnimation = null;
            }

            super.onResourceReady(resource, glideAnimation);
        }

        @Override
        public void onLoadCleared(Drawable placeholder) {
            super.onLoadCleared(placeholder);
            startTime = 0L;
        }

    }


    /**
     * Created by YuanYe on 2017/6/5.
     * 圆角效果的Transform
     */
    static class RoundTransform extends BitmapTransformation {
        private static float radius = 4f;

        public RoundTransform(Context context) {
            this(context, 4);
        }

        public RoundTransform(Context context, int dp) {
            super(context);
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
        public String getId() {
            return getClass().getName() + Math.round(radius);
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
        public String getId() {
            return getClass().getName();
        }
    }
}
