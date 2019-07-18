package yuan.ui_extra;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * 描述：图片加载接口，所有使用到该接口都均需自己实现，图片加载方式
 *
 * @author yuanye
 * @date 2019/7/2 13:54
 */
public interface ImageLoader extends Serializable {
    /**
     * 显示图片
     *
     * @param activity
     * @param path
     * @param imageView
     * @param width
     * @param height
     */
    void displayImage(Context activity, String path, ImageView imageView, int width, int height);

    /**
     * 显示预览图
     *
     * @param activity
     * @param path
     * @param imageView
     * @param width
     * @param height
     */
    void displayImagePreview(Context activity, String path, ImageView imageView, int width, int height);

    /**
     * 清理内存缓存
     */
    void clearMemoryCache();
}
