package com.yuan.base.tools.okhttp;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by YuanYe on 2017/9/13.
 * 文件下载
 */
public abstract class FileBack implements BaseMainBack {

    /**
     * 文件保存的地址
     */
    private String saveDir = ""; //下载文件保存目录
    /**
     * 是否保存文件 true:保存， false:不保存
     */
    private boolean isSave;
    /**
     * 主线程
     */
    private Handler mainHandler;

    /**
     * @param fileDir 需要保存的完整地址
     */
    public FileBack(String fileDir) {
        this.saveDir = fileDir;
        init(saveDir);
    }

    /**
     * 默认不保存
     */
    public FileBack() {
        init(saveDir);
    }

    /**
     * 初始化
     */
    private void init(String fileDir) {
        //校验地址是否可用|
        if (TextUtils.isEmpty(saveDir)) {
            isSave = false;
        } else {
            File cacheFile = new File(fileDir);
            if (cacheFile.getParentFile().exists()) {
                cacheFile.mkdirs();
            }
            isSave = true;
        }
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 下载成功
     * 执行在主线程
     */
    @MainThread
    public abstract void onSuccess(String fileDir, byte[] bytes);

    /**
     * 下载中
     *
     * @param percent 下载百分比
     * @param total   总的大小
     * @param current 已下载的大小
     */
    @MainThread
    public void onDowning(int percent, long total, long current) {
    }


    @Override
    public void onResponse(Response response) throws Exception {
        if (!isSave) {
            byte[] buffer = new byte[2048];
            int len;
            long sum = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            long total = response.contentLength;
            InputStream is = response.body.byteStream();
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                sum += len;
                final int progress = (int) (sum * 1.0f / total * 100);
                final long temp = sum;
                //切换到主线程
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 下载中
                        onDowning(progress, total, temp);
                    }
                });
            }
            bos.close();
            runMainSuccess(saveDir, bos.toByteArray());
        } else {
            InputStream is = null;
            byte[] buf = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;
            // 储存下载文件的目录
            final String savePath = isExistDir(saveDir);
            try {
                is = response.body.byteStream();
                long total = response.contentLength;
                final String fileName = getNameFromUrl(response.requestUrl); //文件名
                File file = new File(savePath, fileName);
                fos = new FileOutputStream(file);
                long sum = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    final long temp = sum;
                    final int progress = (int) (sum * 1.0f / total * 100);
                    //切换到主线程
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 下载中
                            onDowning(progress, total, temp);
                        }
                    });
                }
                fos.flush();
                runMainSuccess(savePath + File.separator + fileName, null);
            } catch (final Exception ioE) {
                runMainException(ioE);
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    runMainException(e);
                }
                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    runMainException(e);
                }
            }
        }
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    private void runMainException(Exception exception) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                onFail(exception);
            }
        });
    }

    private void runMainSuccess(String saveDir, final byte[] bytes) {
        //切换到主线程
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // 下载完成
                onSuccess(saveDir, bytes);
            }
        });
    }

}
