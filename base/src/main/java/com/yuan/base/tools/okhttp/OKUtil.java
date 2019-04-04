package com.yuan.base.tools.okhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 描述：基于OKHttp的网络请求封装。
 * <p>
 * 使用方式：
 * 1.通过new OKUtil()发起一次网络请求，无法设置公共参数
 * 2.通过getInstance()发起一次网络请求，可以设置公共参数
 * <p>
 * 特性：
 * 1.支持get,post请求，支持设置单次head,使用简单
 * 2.支持json,file,byte上传
 * 3.文件下载，自动Gson解析，支持扩展解析
 * 4.支持全局设置head/配置okHttp信息
 *
 * @author yuanye
 * @date 2019/4/4 13:10
 */
public class OKUtil {

    private OkHttpClient client;
    private Request.Builder requestBuilder;
    private Context mContext;
    /**
     * 单例
     */
    private static OKUtil okHttp;

    public static OKUtil getInstance() {
        if (okHttp == null) {
            throw new NullPointerException("第一次调用请先进行全局OkHttp参数配置,请确认是否需要全局配置");
        }
        return okHttp;
    }

    public static OKUtil getInstance(@NonNull Context context, @NonNull OKConfig config) {
        if (okHttp == null) {
            okHttp = new OKUtil(context, config);
        }
        return okHttp;
    }

    /**
     * 通过创建对象方式启动，可以动态配置每次请求参数
     *
     * @param context
     */
    public OKUtil(@NonNull Context context) {
        //获取Client
        client = new RxHttpClient(context).getClient();
        requestBuilder = new Request.Builder();
        this.mContext = context;
    }

    /**
     * 对OKHttpUtil进行基本设置
     *
     * @param config 配置
     */
    public OKUtil(@NonNull Context context, @NonNull OKConfig config) {
        //获取Client
        client = new RxHttpClient(context, config).getClient();
        requestBuilder = new Request.Builder();
        this.mContext = context;
        if (!TextUtils.isEmpty(config.getCommonHead()) && !TextUtils.isEmpty(config.getCommonHeadKey())) {
            //TODO 公共head,可以统一添加
            requestBuilder.addHeader(config.getCommonHeadKey(), config.getCommonHead());
        }
    }

    public ParamBuild post(@NonNull String httpUrl) {
        if (TextUtils.isEmpty(httpUrl)) {
            throw new NullPointerException("地址：url == null");
        }
        requestBuilder.url(httpUrl);
        return new ParamBuild(mContext, requestBuilder, client, ParamBuild.POST, httpUrl);
    }


    public ParamBuild get(@NonNull String httpUrl) {
        if (TextUtils.isEmpty(httpUrl)) {
            throw new NullPointerException("地址：url == null");
        }
        requestBuilder.url(httpUrl);
        return new ParamBuild(mContext, requestBuilder, client, ParamBuild.GET, httpUrl);
    }


    /**
     * 参数配置
     *
     * @author yuanye
     * @date 2018/11/28 13:40
     */
    public class ParamBuild {

        private final static String TAG = "ParamBuild";

        public final static int GET = 1001;

        public final static int POST = 1002;

        public final static int PUT = 1003;

        public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        /**
         * 请求参数
         */
        private Request.Builder requestBuilder;

        /**
         * 执行体
         */
        private OkHttpClient client;

        /**
         * 上传参数集合:<key,value></key,value>
         */
        private Map<String, String> params;
        /**
         * 上传文件集合 : <key,path></key,path>
         */
        private Map<String, String> files;
        /**
         * 上传字节集合
         */
        private List<byte[]> bytes;
        /**
         * context
         */
        private Context mContext;

        /**
         * 请求类型： get,post等
         */
        private int requestType;
        /**
         * 上传文件时统一构造参数
         */
        private MultipartBody.Builder multipartBody = null;

        /**
         * 统一只有参数式构造参数
         */
        private FormBody.Builder paramsBody = null;

        /**
         * 请求地址
         */
        private String url;


        public ParamBuild(@NonNull Context context, @NonNull Request.Builder request,
                          @NonNull OkHttpClient _client, int requestType, String url) {
            this.requestBuilder = request;
            this.client = _client;
            this.mContext = context;
            this.requestType = requestType;
            this.url = url;
            params = new HashMap<>();
            files = new HashMap<>();
            bytes = new ArrayList<byte[]>();
        }

        /**
         * ****************************添加头部****************************************
         */

        public ParamBuild addHead(@NonNull String key, @NonNull String value) {
            if (TextUtils.isEmpty(key)) throw new NullPointerException("参数：head.key == null");
            requestBuilder.addHeader(key, value);
            return this;
        }

        public ParamBuild put(@NonNull String key, @NonNull String value) {
            if (TextUtils.isEmpty(key)) throw new NullPointerException("参数：params.key == null");
            params.put(key, value);
            return this;
        }

        public ParamBuild put(@NonNull Map<String, String> params) {
            if (params == null) throw new NullPointerException("参数：params == null");
            params.putAll(params);
            return this;
        }

        public ParamBuild file(@NonNull String key, @NonNull String value) {
            if (TextUtils.isEmpty(key)) throw new NullPointerException("参数：files.key == null");
            files.put(key, value);
            return this;
        }

        public ParamBuild file(@NonNull Map<String, String> params) {
            if (params == null) throw new NullPointerException("参数：files == null");
            files.putAll(params);
            return this;
        }

        public ParamBuild bytes(@NonNull byte[] bytes1) {
            if (bytes1 == null) throw new NullPointerException("上传文件字节为空");
            bytes.add(bytes1);
            return this;
        }


        /**
         * ****************************最后的执行部分****************************************
         */

        public Execute json(@NonNull String json) {
            RequestBody requestBody = RequestBody.create(JSON, json);
            switch (requestType) {
                case GET:
                    requestBuilder.url(addParams(url, params));
                    requestBuilder.get();
                    break;
                case PUT:
                    requestBuilder.url(url);
                    requestBuilder.put(requestBody);
                    break;
                case POST:
                    requestBuilder.url(url);
                    requestBuilder.post(requestBody);
                    break;
            }

            return new Execute(mContext, requestBuilder, client);
        }

        public void execute(@NonNull BaseBack mainCall) {

            if (bytes.size() > 0 || files.size() > 0) {
                /**
                 * 当存在文件数据时，上传文件的方式
                 */
                multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

                //设置参数
                for (Map.Entry<String, String> param : params.entrySet()) {
                    multipartBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""),
                            RequestBody.create(null, param.getValue()));
                }
                //设置上传文件
                for (Map.Entry<String, String> file : files.entrySet()) {
                    File fileDir = new File(file.getValue());
                    RequestBody fileBody = RequestBody.create(MediaType
                            .parse(getMimeType(file.getValue())), fileDir);
                    multipartBody.addFormDataPart(file.getKey(), fileDir.getName(), fileBody);
                }
                //设置上传字节
                for (byte[] byte1 : bytes) {
                    multipartBody.addPart(Headers.of("Content-Disposition", "octet-stream;"),
                            RequestBody.create(null, byte1));
                }
                switch (requestType) {
                    case GET:
                        requestBuilder.url(addParams(url, params));
                        requestBuilder.get();
                        break;
                    case PUT:
                        requestBuilder.url(url);
                        requestBuilder.put(multipartBody.build());
                        break;
                    case POST:
                        requestBuilder.url(url);
                        requestBuilder.post(multipartBody.build());
                        break;
                }
            } else {
                paramsBody = new FormBody.Builder();
                //设置参数
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramsBody.add(entry.getKey(), entry.getValue());
                }
                switch (requestType) {
                    case GET:
                        requestBuilder.url(addParams(url, params));
                        requestBuilder.get();
                        break;
                    case PUT:
                        requestBuilder.url(url);
                        requestBuilder.put(paramsBody.build());
                        break;
                    case POST:
                        requestBuilder.url(url);
                        requestBuilder.post(paramsBody.build());
                        break;
                }
            }
            new Execute(mContext, requestBuilder, client).execute(mainCall);
        }

        /**
         * 获取文件MimeType
         *
         * @param filename 文件名
         * @return
         */
        private String getMimeType(@NonNull String filename) {
            FileNameMap filenameMap = URLConnection.getFileNameMap();
            String contentType = filenameMap.getContentTypeFor(filename);
            if (contentType == null) {
                contentType = "application/octet-stream"; //* exe,所有的可执行程序
            }
            return contentType;
        }

        /**
         * 地址上添加参数
         *
         * @param url
         * @param params
         * @return
         */
        private String addParams(String url, @Nullable Map<String, String> params) {
            Map.Entry entry;
            if (params != null && !params.isEmpty()) {
                for (Iterator var2 = params.entrySet().iterator(); var2.hasNext(); url = addParam(url, (String) entry.getKey(), (String) entry.getValue())) {
                    entry = (Map.Entry) var2.next();
                }
            }

            return url;
        }

        private String addParam(String url, String key, String value) {
            if (TextUtils.isEmpty(url)) {
                return url;
            } else {
                String[] subUrlHolder = new String[1];
                url = removeSubUrl(url, subUrlHolder);
                key = key + '=';
                int index = url.indexOf(63);
                if (index < 0) {
                    return addFirstParam(url, key, value) + subUrlHolder[0];
                } else {
                    int keyIndex = url.indexOf('&' + key, index);
                    if (keyIndex == -1) {
                        keyIndex = url.indexOf('?' + key, index);
                    }

                    return keyIndex != -1 ? replaceParam(url, value, keyIndex + 1 + key.length()) + subUrlHolder[0] : addFollowedParam(url, key, value) + subUrlHolder[0];
                }
            }
        }

        @NonNull
        private String addFirstParam(String url, String key, String value) {
            return url + '?' + key + value;
        }

        @NonNull
        private String removeSubUrl(@NonNull String url, @Nullable String[] subUrlHolder) {
            int indexHash = url.indexOf(35);
            if (indexHash < 0) {
                if (subUrlHolder != null) {
                    subUrlHolder[0] = "";
                }

                return url;
            } else {
                if (subUrlHolder != null) {
                    subUrlHolder[0] = url.substring(indexHash);
                }

                return url.substring(0, indexHash);
            }
        }

        private String replaceParam(@NonNull String url, String value, int valueStart) {
            int valueEnd = url.indexOf(38, valueStart);
            if (valueEnd == -1) {
                valueEnd = url.length();
            }

            StringBuilder sb = new StringBuilder(url);
            sb.replace(valueStart, valueEnd, value);
            return sb.toString();
        }

        private String addFollowedParam(@NonNull String url, String key, String value) {
            StringBuilder sb = new StringBuilder(url);
            if (!url.endsWith("&") && !url.endsWith("?")) {
                sb.append('&');
            }

            sb.append(key).append(value);
            return sb.toString();
        }

    }

    /**
     * 最后的执行
     * Created by YuanYe on 2017/9/26.
     */
    public class Execute {

        protected Request.Builder requestBuilder;
        protected OkHttpClient client;
        protected Context mContext;

        public Execute(Context context, Request.Builder request, OkHttpClient _client) {
            this.requestBuilder = request;
            this.client = _client;
            this.mContext = context;
        }

        /**
         * ****************************callBack请求封装****************************************
         */
        //统一对requestBuild处理，
        private Request getRequestBuild() {
            return requestBuilder.build();
        }

        //统一返回
        public void execute(BaseBack mainBack) {
            if (mainBack == null) throw new NullPointerException("回调：RxCall == null");
            MainCall baseFileBack = new MainCall(mainBack);
            client.newCall(getRequestBuild())
                    .enqueue(baseFileBack);
        }
    }

    /**
     * 接替OKHttp返回回调
     *
     * @author yuanye
     * @date 2018/11/28 11:56
     */
    class MainCall implements Callback {

        private static final String TAG = "MainCall";
        /**
         * 回调
         */
        private BaseBack mainBack;
        /**
         * 主线程
         */
        private Handler mainHandler;

        public MainCall(BaseBack mainBack) {
            this.mainBack = mainBack;
            mainHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void onFailure(Call call, IOException e) {
            runMainException(e);
        }

        @Override
        public void onResponse(Call call, okhttp3.Response response) throws IOException {
            Response baseResponse = new Response();
            baseResponse.code = response.code();
            baseResponse.mssage = response.message();
            baseResponse.requestUrl = response.request().url().url().getPath();

            ResponseBody body = response.body();
            baseResponse.contentLength = body.contentLength();

            baseResponse.body = body;

            try {
                mainBack.onResponse(baseResponse);
            } catch (Exception e) {
                runMainException(e);
            }
        }

        protected void runMainException(final Exception exception) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "error:" + exception.getMessage());
                    mainBack.onFail(exception);
                }
            });
        }
    }


    /**
     * Created by YuanYe on 2017/7/19.
     *
     * @author yuanye
     * @version 1.0
     * 本类设计说明：
     * 根据ClientBuilder构建Client初始化，
     * 根据CacheInterceptor实现OKHttp缓存,
     * 本类主要用于创建OKHttpClient对象,一次请求创建一个OKHttpClient对象
     */
    class RxHttpClient {

        private static final String TAG = "RxHttpClient";

        private OkHttpClient client; //主要创建的网络请求client
        private OKConfig config; //配置参数
        private Context mContext; //上下文

        public RxHttpClient(@NonNull Context context) {
            this.mContext = context;
            this.config = new OKConfig.Builder()
                    .isReConnect(true)
                    .build();
        }

        /**
         * 传入构建参数创建OkHttpClient
         *
         * @param context 上下文
         * @param _config 基本配置
         */
        public RxHttpClient(@NonNull Context context, @NonNull OKConfig _config) {
            this.mContext = context;
            this.config = _config;
        }

        /**
         * 创建OKHttpClient
         *
         * @return 返回创建的OKHttpClient实体
         */
        public OkHttpClient getClient() {
            if (client == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                //失败后是否重新连接
                builder.retryOnConnectionFailure(config.isReConnect());
                //连接超时
                builder.connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS);
                //读取超时
                builder.readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);
                //设置cookie，保存cookie,读取cookie
                CookieJar cookieJar = config.getCookie();
                if (cookieJar != null) {
                    builder.cookieJar(cookieJar);
                }
                //设置缓存
                if (!TextUtils.isEmpty(config.getCacheFolder())) {
                    File cacheFile = new File(config.getCacheFolder());
                    if (cacheFile != null && !cacheFile.exists()) {
                        cacheFile.mkdirs();
                    }
                    //设置缓存大小
                    Cache cache = new Cache(cacheFile, config.getMaxCacheSize());
                    builder.cache(cache);
                }
                //设置缓存拦截器，实现网络缓存(有网络的时候不缓存，没有网络的时候缓存)
                builder.addInterceptor(new CacheInterceptor(mContext))
                        .addNetworkInterceptor(new CacheInterceptor(mContext));
                //添加请求头
                client = builder.build();
            }
            return client;
        }


        /**
         * Created by YuanYe on 2017/7/20.
         * <p>
         * OKHttp拦截器，实现拦截后的缓存实现（有网络的时候不缓存，没有网络的时候缓存）
         */
        class CacheInterceptor implements Interceptor {
            private Context context;

            public CacheInterceptor(Context context) {
                this.context = context;
            }

            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();//获取请求
                //这里就是说判读我们的网络条件，要是有网络的话我么就直接获取网络上面的数据，要是没有网络的话我么就去缓存里面取数据
                if (!isConnect(context)) {
                    request = request.newBuilder()
                            //这个的话内容有点多啊，大家记住这么写就是只从缓存取，想要了解这个东西我等下在
                            // 给大家写连接吧。大家可以去看下，获取大家去找拦截器资料的时候就可以看到这个方面的东西反正也就是缓存策略。
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                okhttp3.Response originalResponse = chain.proceed(request);
                if (isConnect(context)) {
                    //这里大家看点开源码看看.header .removeHeader做了什么操作很简答，就是的加字段和减字段的。
                    String cacheControl = request.cacheControl().toString();
                    return originalResponse.newBuilder()
                            //这里设置的为0就是说不进行缓存，我们也可以设置缓存时间
                            .header("Cache-Control", "public, max-age=" + 0)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    int maxTime = 7 * 24 * 60 * 60;
                    return originalResponse.newBuilder()
                            //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                            .removeHeader("Pragma")
                            .build();
                }
            }


            /**
             * 判断网络情况
             *
             * @param context 上下文
             * @return false 表示没有网络 true 表示有网络
             */
            private boolean isConnect(Context context) {
                // 获得网络状态管理器
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                if (connectivityManager == null) {
                    return false;
                } else {
                    // 建立网络数组
                    NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

                    if (net_info != null) {
                        for (int i = 0; i < net_info.length; i++) {
                            // 判断获得的网络状态是否是处于连接状态
                            if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }
    }

    /**
     * 执行与主线程
     *
     * @author yuanye
     * @date 2018/11/28 11:44
     */
    public interface BaseBack {
        /**
         * 子线程调用方法
         */
        @AnyThread
        void onResponse(OKUtil.Response response) throws Exception;

        /**
         * 主线程处理异常方法
         */
        @MainThread
        void onFail(Exception e);
    }


    /**
     * Created by YuanYe on 2017/8/4.
     * RxCallBack---用于处理OKHttpUtil返回
     * Gson处理返回--使用RxJava切换处理方法到主线程
     * 支持的json说明：
     * 1、当setUseNetBean（）为空时，T代表完整Json的实体对象
     */
    public static abstract class JsonBack<T> implements BaseBack {

        private final static String TAG = "JsonBack";
        /**
         * 主线程
         */
        private Handler mainHandler;

        /**
         * 解析出的实体
         */
        private T entity = null;

        public JsonBack() {
            mainHandler = new Handler(Looper.getMainLooper());
        }

        /**
         * 主线程成功方法
         */
        @MainThread
        public abstract void onSuccess(T t, String json);

        /**
         * 子线程成功方法
         *
         * @param response
         * @throws IOException
         */
        @Override
        public void onResponse(Response response) {
            try {
                final String json = response.body.string();

                Class<T> clazz = getClazz();
                if (clazz != null) {
                    entity = new Gson().fromJson(json, clazz);
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(entity, json);
                    }
                });
            } catch (final Exception e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onFail(e);
                    }
                });
            }
        }

        @Override
        public void onFail(Exception e) {

        }

        /**
         * 反射泛型生成对象
         */
        private Class<T> getClazz() {
            try {
                return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()
                ).getActualTypeArguments()[0];
            } catch (ClassCastException e) {
                Log.i(TAG, e.getMessage());
            }
            return null;
        }

    }


    /**
     * Created by YuanYe on 2017/9/13.
     * 文件下载
     */
    public static abstract class FileBack implements BaseBack {

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
                final long total = response.contentLength;
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
                    final long total = response.contentLength;

                    //TODO 如果传入的路径是文件夹，取url做文件名，否则取传入文件名
                    String fileName = getNameFromUrl(response.requestUrl); //文件名

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
            File downloadFile = new File(saveDir);
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


        private void runMainException(final Exception exception) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFail(exception);
                }
            });
        }

        private void runMainSuccess(final String saveDir, final byte[] bytes) {
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

    /**
     * Created by YuanYe on 2017/9/8.
     * 用于配置OkHttpClient，采用build模式进行配置
     * 用于配置单次OkHttp请求参数，在OKHttp创建时初始化使用
     */
    public static class OKConfig {

        private final static long CONNECTTIMEOUT = 10 * 1000l; //链接超时，单位：毫秒
        private final static long READTIMEOUT = 10 * 1000l;//读取超时， 单位：毫秒
        private final static String CACHEFOLDER = "okCache"; //默认网络缓存文件夹

        private long connectTimeout; //连接超时时间
        private long readTimeout;//读取超时时间
        private CookieJar cookie;//设置Cookie
        private boolean isReConnect; //是否重新连接
        private String commonHead;  //公共头部
        private String commonHeadKey;//公共头部key

        private String cacheFolder;//OKHttp缓存文件存放的文件夹位置
        private long maxCacheSize;//最大缓存的大小

        private OKConfig(Builder builder) {
            connectTimeout = builder.connectTimeout;
            readTimeout = builder.readTimeout;
            cookie = builder.cookie;
            isReConnect = builder.isReConnect;
            commonHead = builder.commonHead;
            commonHeadKey = builder.commonHeadKey;
            cacheFolder = builder.cacheFolder;
            maxCacheSize = builder.maxCacheSize;
        }

        public long getConnectTimeout() {
            return connectTimeout;
        }

        public long getReadTimeout() {
            return readTimeout;
        }

        public CookieJar getCookie() {
            return cookie;
        }

        public boolean isReConnect() {
            return isReConnect;
        }

        public String getCommonHead() {
            return commonHead;
        }

        public String getCommonHeadKey() {
            return commonHeadKey;
        }

        public String getCacheFolder() {
            return cacheFolder;
        }

        public long getMaxCacheSize() {
            return maxCacheSize;
        }

        public final static class Builder {
            private long connectTimeout;
            private long readTimeout;
            private CookieJar cookie;
            private boolean isReConnect;
            private String commonHead;
            private String commonHeadKey;
            private String cacheFolder;
            private long maxCacheSize;

            public Builder() {
                connectTimeout = CONNECTTIMEOUT;
                readTimeout = READTIMEOUT;
                cacheFolder = CACHEFOLDER;
                //默认缓存大小为当先线程的八分之一
                maxCacheSize = Runtime.getRuntime().maxMemory() / 8;
            }

            public Builder connectTimeout(long val) {
                connectTimeout = val;
                return this;
            }

            public Builder readTimeout(long val) {
                readTimeout = val;
                return this;
            }

            public Builder cookie(CookieJar val) {
                cookie = val;
                return this;
            }

            public Builder isReConnect(boolean val) {
                isReConnect = val;
                return this;
            }

            public Builder commonHead(@NonNull String val) {
                commonHead = val;
                return this;
            }

            public Builder commonHeadKey(@NonNull String val) {
                commonHeadKey = val;
                return this;
            }

            public Builder cacheFolder(@NonNull String val) {
                cacheFolder = val;
                return this;
            }

            public Builder maxCacheSize(long val) {
                maxCacheSize = val;
                return this;
            }

            public OKConfig build() {
                return new OKConfig(this);
            }
        }
    }

    /**
     * 接替OKHttp返回
     *
     * @author yuanye
     * @date 2018/11/28 11:47
     */
    public static class Response {

        public ResponseBody body;
        /**
         * 内容长度
         */
        public long contentLength;
        /**
         * 请求地址
         */
        public String requestUrl;
        /**
         * 消息
         */
        public String mssage;

        /**
         * 请求状态码
         */
        public int code;

    }
}
