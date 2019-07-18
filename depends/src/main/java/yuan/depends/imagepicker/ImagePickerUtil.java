package yuan.depends.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.isseiaoki.simplecropview.FreeCropImageView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import yuan.core.tool.RouteUtil;
import yuan.depends.R;


/**
 * 描述： 图片选择器
 * <p>
 * github <a herf= 'https://github.com/CysionLiu/ImagePicker'></a>
 *
 * @author yuanye
 * @date 2019/7/18 15:20
 */
public class ImagePickerUtil {

    /**
     * 启动图片选择
     *
     * @param context
     * @param listener
     */
    public static void startAlbum(final Context context, final OnSelectListener listener) {
        startAlbum(context, false, true, listener);
    }

    /**
     * 启动图片选择
     *
     * @param context      上下文
     * @param isCrop       是否剪裁
     * @param isShowCamera 是否显示相机
     * @param listener     完成选择监听
     */
    public static void startAlbum(final Context context, boolean isCrop, boolean isShowCamera, final OnSelectListener listener) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(isShowCamera);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setFreeCrop(isCrop, FreeCropImageView.CropMode.FREE);//新版添加,自由裁剪，优先于setCrop
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        RouteUtil.openResult(context, ImageGridActivity.class,
                new RouteUtil.OnActivityResultListener() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {

                        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                            if (data != null && requestCode == RouteUtil.REQUESTCODE) {
                                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                                listener.onSelected(images);
                            } else {
                                Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });
    }

    /**
     * 监听完成选择
     */
    public interface OnSelectListener {
        void onSelected(ArrayList<ImageItem> images);
    }

    /**
     * 采用Glide加载
     */
    private static class GlideImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            RequestOptions requestOptions = new RequestOptions()
                    .error(ContextCompat.getDrawable(activity, R.drawable.ic_image_default_error));
            //Glide 4.8之后更新
            Glide.with(activity).load(path)
                    .apply(requestOptions)
                    .into(imageView);
        }

        @Override
        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
            RequestOptions requestOptions = new RequestOptions()
                    .error(ContextCompat.getDrawable(activity, R.drawable.ic_image_default_error));
            //Glide 4.8之后更新
            Glide.with(activity).load(path)
                    .apply(requestOptions)
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }
    }
}
