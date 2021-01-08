package yuan.core.tool;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SimpleArrayMap;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;

/**
 * 2018/8/16.
 *
 * @author yuanye
 * 常用工具集合,在wanglei 初始版本上修改而来
 * <p>
 * package: 获取包名、获取版本号、安装、卸载、是否是系统App、
 * ---------Activity是否运行在前台、App是否运行在后台、获取meta-data中的值
 * <p>
 * Dimens: dp转px、px转dp
 * <p>
 * Random:0-9随机数、0-9a-zA-Z随机数、A-B随机数、a-z随机数
 * <p>
 * Files:文件写入、文件移动、文件复制、文件读取、文件判断、获取文件名、获取文件夹、获取文件大小
 * <p>
 * Date: long转String、String转long、是否是今天、当月多少天、本周周几、是否是本月第一天
 */
public class Kits {

    public static class Package {
        /**
         * 获取版本号
         *
         * @param context 上下文
         * @return 版本号
         */
        public static int getVersionCode(@NonNull Context context) {
            PackageManager pManager = context.getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo = pManager.getPackageInfo(context.getPackageName(), 0);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return packageInfo.versionCode;
        }

        /**
         * 获取当前版本
         *
         * @param context 上下文
         * @return 当前版本名
         */
        public static String getVersionName(@NonNull Context context) {
            PackageManager pManager = context.getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo = pManager.getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return packageInfo.versionName;
        }

        /**
         * 安装App
         *
         * @param context  上下文
         * @param filePath 需要安装app的文件路径
         * @return
         */
        public static boolean installApp(@NonNull Context context, @NonNull String filePath) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            File file = new File(filePath);
            if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
                return false;
            }

            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }

        /**
         * 卸载App
         *
         * @param context     上下文
         * @param packageName 需要卸载app的完整包名
         * @return
         */
        public static boolean uninstallApp(@NonNull Context context, @NonNull String packageName) {
            if (packageName == null || packageName.length() == 0) {
                return false;
            }

            Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder().append("package:")
                    .append(packageName).toString()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }

        /**
         * 判断是否是系统App
         *
         * @param context     上下文
         * @param packageName 包名
         * @return
         */
        public static boolean isSystemApp(@NonNull Context context, @NonNull String packageName) {
            if (context == null) {
                return false;
            }
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null || packageName == null || packageName.length() == 0) {
                return false;
            }

            try {
                ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
                return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * 判断某个包名是否运行在顶层
         *
         * @param context             上下文
         * @param activityPackageName Activity完整包名
         * @return 是否运行在最上层
         */
        public static Boolean isTopActivity(@NonNull Context context, @NonNull String activityPackageName) {
            if (context == null || TextUtils.isEmpty(activityPackageName)) {
                return false;
            }
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
            if (tasksInfo == null || tasksInfo.isEmpty()) {
                return false;
            }
            try {
                return activityPackageName.equals(tasksInfo.get(0).topActivity.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 获取Manifest下<meta-data></meta-data>标签的值
         *
         * @param context 上下文
         * @param key     meta-data标签中对应的key值
         * @return meta-data中对应的value值
         */
        public static String getAppMetaData(@NonNull Context context, @NonNull String key) {
            if (context == null || TextUtils.isEmpty(key)) {
                return null;
            }
            String resultData = null;
            try {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                    if (applicationInfo != null) {
                        if (applicationInfo.metaData != null) {
                            resultData = applicationInfo.metaData.getString(key);
                        }
                    }

                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return resultData;
        }

        /**
         * 判断当前应用是否运行在后台
         *
         * @param context 上下文
         * @return App是否运行在后台
         */
        public static boolean isAppRunInBackground(@NonNull Context context) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
            if (taskList != null && !taskList.isEmpty()) {
                ComponentName topActivity = taskList.get(0).topActivity;
                if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Device {
        /**
         * Return whether device is rooted.
         * 判断当前设备是否Root
         *
         * @return {@code true}: yes<br>{@code false}: no
         */
        public static boolean isDeviceRooted() {
            String su = "su";
            String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                    "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
            for (String location : locations) {
                if (new File(location + su).exists()) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Return the version name of device's system.
         * 获取SDK版本名称
         *
         * @return the version name of device's system
         */
        public static String getSDKVersionName() {
            return Build.VERSION.RELEASE;
        }

        /**
         * Return version code of device's system.
         * 获取SDK版本号
         *
         * @return version code of device's system
         */
        public static int getSDKVersionCode() {
            return Build.VERSION.SDK_INT;
        }

        /**
         * Return the android id of device.
         *
         * @return the android id of device
         */
        @SuppressLint("HardwareIds")
        public static String getAndroidID(@NonNull Context context) {
            String id = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );
            return id == null ? "" : id;
        }

        /**
         * Return the MAC address.
         * <p>Must hold
         * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
         * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
         *
         * @return the MAC address
         */
        @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
        public static String getMacAddress(Context context) {
            return getMacAddress(context, (String[]) null);
        }

        /**
         * 获取设备Mac地址
         * Return the MAC address.
         * <p>Must hold
         * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
         * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
         *
         * @return the MAC address
         */
        @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
        public static String getMacAddress(Context context, final String... excepts) {
            String macAddress = getMacAddressByWifiInfo(context);
            if (isAddressNotInExcepts(macAddress, excepts)) {
                return macAddress;
            }
            macAddress = getMacAddressByNetworkInterface();
            if (isAddressNotInExcepts(macAddress, excepts)) {
                return macAddress;
            }
            macAddress = getMacAddressByInetAddress();
            if (isAddressNotInExcepts(macAddress, excepts)) {
                return macAddress;
            }
            macAddress = getMacAddressByFile();
            if (isAddressNotInExcepts(macAddress, excepts)) {
                return macAddress;
            }
            return "";
        }


        private static boolean isAddressNotInExcepts(final String address, final String... excepts) {
            if (excepts == null || excepts.length == 0) {
                return !"02:00:00:00:00:00".equals(address);
            }
            for (String filter : excepts) {
                if (address.equals(filter)) {
                    return false;
                }
            }
            return true;
        }

        @SuppressLint({"HardwareIds", "MissingPermission"})
        private static String getMacAddressByWifiInfo(@NonNull Context context) {
            try {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifi != null) {
                    WifiInfo info = wifi.getConnectionInfo();
                    if (info != null) return info.getMacAddress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "02:00:00:00:00:00";
        }

        private static String getMacAddressByNetworkInterface() {
            try {
                Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
                while (nis.hasMoreElements()) {
                    NetworkInterface ni = nis.nextElement();
                    if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "02:00:00:00:00:00";
        }

        private static String getMacAddressByInetAddress() {
            try {
                InetAddress inetAddress = getInetAddress();
                if (inetAddress != null) {
                    NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                    if (ni != null) {
                        byte[] macBytes = ni.getHardwareAddress();
                        if (macBytes != null && macBytes.length > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (byte b : macBytes) {
                                sb.append(String.format("%02x:", b));
                            }
                            return sb.substring(0, sb.length() - 1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "02:00:00:00:00:00";
        }

        private static InetAddress getInetAddress() {
            try {
                Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
                while (nis.hasMoreElements()) {
                    NetworkInterface ni = nis.nextElement();
                    // To prevent phone of xiaomi return "10.0.2.15"
                    if (!ni.isUp()) continue;
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress inetAddress = addresses.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String hostAddress = inetAddress.getHostAddress();
                            if (hostAddress.indexOf(':') < 0) return inetAddress;
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static String getMacAddressByFile() {
            ShellUtil.CommandResult result = ShellUtil.execCmd("getprop wifi.interface", false);
            if (result.result == 0) {
                String name = result.successMsg;
                if (name != null) {
                    result = ShellUtil.execCmd("cat /sys/class/net/" + name + "/address", false);
                    if (result.result == 0) {
                        String address = result.successMsg;
                        if (address != null && address.length() > 0) {
                            return address;
                        }
                    }
                }
            }
            return "02:00:00:00:00:00";
        }

        /**
         * 获取设备支持的SO平台类型
         * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
         * element in the list.
         *
         * @return an ordered list of ABIs supported by this device
         */
        public static String[] getABIs() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return Build.SUPPORTED_ABIS;
            } else {
                if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                    return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
                }
                return new String[]{Build.CPU_ABI};
            }
        }

        /**
         * 设备关机
         * Shutdown the device
         * 需要添加一下权限
         * <p>Requires root permission
         * or hold {@code android:sharedUserId="android.uid.system"},
         * {@code <uses-permission android:name="android.permission.SHUTDOWN/>}
         * in manifest.</p>
         */
        public static void shutdown(Context context) {
            ShellUtil.execCmd("reboot -p", true);
            Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
            intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        /**
         * 申请Root权限，需要现在添加系统权限
         * Reboot the device.
         * <p>Requires root permission
         * or hold {@code android:sharedUserId="android.uid.system"} in manifest.</p>
         */
        public static void reboot(@NonNull Context context) {
            ShellUtil.execCmd("reboot", true);
            Intent intent = new Intent(Intent.ACTION_REBOOT);
            intent.putExtra("nowait", 1);
            intent.putExtra("interval", 1);
            intent.putExtra("window", 0);
            context.sendBroadcast(intent);
        }

        /**
         * 重启设备，需要系统权限
         * Reboot the device.
         * <p>Requires root permission
         * or hold {@code android:sharedUserId="android.uid.system"},
         * {@code <uses-permission android:name="android.permission.REBOOT" />}</p>
         *
         * @param reason code to pass to the kernel (e.g., "recovery") to
         *               request special boot modes, or null.
         */
        public static void reboot(Context context, final String reason) {
            PowerManager mPowerManager =
                    (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            try {
                if (mPowerManager == null) return;
                mPowerManager.reboot(reason);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static class Dimens {

        public static float dpToPx(@NonNull Context context, float dp) {
            return dp * context.getResources().getDisplayMetrics().density;
        }

        public static float pxToDp(@NonNull Context context, float px) {
            return px / context.getResources().getDisplayMetrics().density;
        }

        public static int dpToPxInt(@NonNull Context context, float dp) {
            return (int) (dpToPx(context, dp) + 0.5f);
        }

        public static int pxToDpInt(@NonNull Context context, float px) {
            return (int) (pxToDp(context, px) + 0.5f);
        }
    }


    public static class Random {
        public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String NUMBERS = "0123456789";
        public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";


        /**
         * @param length 需要获取随机数的长度
         * @return 带有数字、大小写字母的随机数
         */
        public static String getRandomNumbersAndLetters(int length) {
            return getRandom(NUMBERS_AND_LETTERS, length);
        }

        /**
         * @param length 需要获取随机数的长度
         * @return 数字的指定长度随机数
         */
        public static String getRandomNumbers(int length) {
            return getRandom(NUMBERS, length);
        }

        /**
         * @param length 需要获取随机数的长度
         * @return 大小写字母的指定长度随机数
         */
        public static String getRandomLetters(int length) {
            return getRandom(LETTERS, length);
        }

        /**
         * @param length 需要获取随机数的长度
         * @return 大写字母的指定长度随机数
         */
        public static String getRandomCapitalLetters(int length) {
            return getRandom(CAPITAL_LETTERS, length);
        }

        /**
         * @param length 需要获取随机数的长度
         * @return 小写字母的指定长度随机数
         */
        public static String getRandomLowerCaseLetters(int length) {
            return getRandom(LOWER_CASE_LETTERS, length);
        }


        private static String getRandom(String source, int length) {
            return TextUtils.isEmpty(source) ? null : getRandom(source.toCharArray(), length);
        }

        private static String getRandom(char[] sourceChar, int length) {
            if (sourceChar == null || sourceChar.length == 0 || length < 0) {
                return null;
            }
            StringBuilder str = new StringBuilder(length);
            java.util.Random random = new java.util.Random();
            for (int i = 0; i < length; i++) {
                str.append(sourceChar[random.nextInt(sourceChar.length)]);
            }
            return str.toString();
        }

        public static int getRandom(int max) {
            return getRandom(0, max);
        }

        /**
         * @param min 最小值
         * @param max 最大值
         * @return 范围随机数
         */
        public static int getRandom(int min, int max) {
            if (min > max) {
                return 0;
            }
            if (min == max) {
                return min;
            }
            return min + new java.util.Random().nextInt(max - min);
        }
    }

    public static class Files {

        public final static String FILE_EXTENSION_SEPARATOR = ".";

        /**
         * read file.读取文件并将文件按照指定的字符类型解析
         *
         * @param filePath    文件路径
         * @param charsetName 字符编码 The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
         * @return if file not exist, return null, else return content of file
         * @throws RuntimeException if an error occurs while operator BufferedReader
         */
        public static StringBuilder readTextFile(@NonNull String filePath, @NonNull String charsetName) {
            File file = new File(filePath);
            StringBuilder fileContent = new StringBuilder("");
            if (file == null || !file.isFile()) {
                return null;
            }

            BufferedReader reader = null;
            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
                reader = new BufferedReader(is);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (!fileContent.toString().equals("")) {
                        fileContent.append("\r\n");
                    }
                    fileContent.append(line);
                }
                return fileContent;
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            } finally {
                IO.close(reader);
            }
        }

        /**
         * write file
         *
         * @param filePath 写入文件的地址
         * @param content  写入文件的内容
         * @param append   是覆盖原有文件还是续写原有文件is append, if true, write to the end of file, else clear content of file and write into it
         * @return return false if content is empty, true otherwise
         * @throws RuntimeException if an error occurs while operator FileWriter
         */
        public static boolean writeFile(String filePath, String content, boolean append) {
            if (TextUtils.isEmpty(content)) {
                return false;
            }

            FileWriter fileWriter = null;
            try {
                mkDirs(filePath);
                fileWriter = new FileWriter(filePath, append);
                fileWriter.write(content);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            } finally {
                IO.close(fileWriter);
            }
        }

        /**
         * write file
         *
         * @param filePath    文件保存地址
         * @param contentList 文本集合
         * @param append      is append, if true, write to the end of file, else clear content of file and write into it
         * @return return false if contentList is empty, true otherwise
         * @throws RuntimeException if an error occurs while operator FileWriter
         */
        public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
            if (contentList == null || contentList.isEmpty()) {
                return false;
            }

            FileWriter fileWriter = null;
            try {
                mkDirs(filePath);
                fileWriter = new FileWriter(filePath, append);
                int i = 0;
                for (String line : contentList) {
                    if (i++ > 0) {
                        fileWriter.write("\r\n");
                    }
                    fileWriter.write(line);
                }
                return true;
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            } finally {
                IO.close(fileWriter);
            }
        }

        /**
         * write file, the string will be written to the begin of the file
         *
         * @param filePath 文件地址
         * @param content  内容文本
         * @return 是否成功
         */
        public static boolean writeFile(String filePath, String content) {
            return writeFile(filePath, content, false);
        }

        /**
         * write file, the string list will be written to the begin of the file
         *
         * @param filePath    文件地址
         * @param contentList 内容集合
         * @return 是否成功
         */
        public static boolean writeFile(String filePath, List<String> contentList) {
            return writeFile(filePath, contentList, false);
        }

        /**
         * @param filePath 文件路径、默认图片格式PNG
         * @param bitmap   需要写入的图片文件
         * @return 是否成功
         */
        public static boolean writeFile(String filePath, Bitmap bitmap) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            return writeFile(filePath, isBm);
        }

        /**
         * write file, the bytes will be written to the begin of the file
         *
         * @param filePath 文件路径
         * @param stream   输出字节流
         * @return 是否成功
         * @see {@link #writeFile(String, InputStream, boolean)}
         */
        public static boolean writeFile(String filePath, InputStream stream) {
            return writeFile(filePath, stream, false);
        }

        /**
         * write file
         *
         * @param stream the input stream
         * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
         * @return return true
         * @throws RuntimeException if an error occurs while operator FileOutputStream
         */
        public static boolean writeFile(String filePath, InputStream stream, boolean append) {
            return writeFile(filePath != null ? new File(filePath) : null, stream, append);
        }

        /**
         * write file, the bytes will be written to the begin of the file
         *
         * @param file   保存的文件
         * @param stream 输出的字节流
         * @return 是否成功
         * @see {@link #writeFile(File, InputStream, boolean)}
         */
        public static boolean writeFile(File file, InputStream stream) {
            return writeFile(file, stream, false);
        }

        /**
         * write file
         *
         * @param file   the file to be opened for writing.
         * @param stream the input stream
         * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
         * @return return true
         * @throws RuntimeException if an error occurs while operator FileOutputStream
         */
        public static boolean writeFile(File file, InputStream stream, boolean append) {
            OutputStream o = null;
            try {
                mkDirs(file.getAbsolutePath());
                o = new FileOutputStream(file, append);
                byte data[] = new byte[1024];
                int length = -1;
                while ((length = stream.read(data)) != -1) {
                    o.write(data, 0, length);
                }
                o.flush();
                return true;
            } catch (FileNotFoundException e) {
                throw new RuntimeException("FileNotFoundException occurred. ", e);
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            } finally {
                IO.close(o);
                IO.close(stream);
            }
        }

        /**
         * move file 移动文件
         *
         * @param sourceFilePath 原文件路径
         * @param destFilePath   目标文件路径
         */
        public static void moveFile(String sourceFilePath, String destFilePath) {
            if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
                throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
            }
            moveFile(new File(sourceFilePath), new File(destFilePath));
        }

        /**
         * move file 移动文件
         *
         * @param srcFile  原文件
         * @param destFile 目标文件
         */
        public static void moveFile(File srcFile, File destFile) {
            boolean rename = srcFile.renameTo(destFile);
            if (!rename) {
                copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
                deleteFile(srcFile.getAbsolutePath());
            }
        }

        /**
         * copy file 复制文件
         *
         * @param sourceFilePath 源文件路径
         * @param destFilePath   目标文件路径
         * @return
         * @throws RuntimeException if an error occurs while operator FileOutputStream
         */
        public static boolean copyFile(String sourceFilePath, String destFilePath) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(sourceFilePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("FileNotFoundException occurred. ", e);
            }
            return writeFile(destFilePath, inputStream);
        }

        /**
         * read file to string list, a element of list is a line
         *
         * @param filePath    读取文本文件
         * @param charsetName 编码格式 The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
         * @return if file not exist, return null, else return content of file
         * @throws RuntimeException if an error occurs while operator BufferedReader
         */
        public static List<String> readFileToList(String filePath, String charsetName) {
            File file = new File(filePath);
            List<String> fileContent = new ArrayList<String>();
            if (file == null || !file.isFile()) {
                return null;
            }

            BufferedReader reader = null;
            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
                reader = new BufferedReader(is);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    fileContent.add(line);
                }
                return fileContent;
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            } finally {
                IO.close(reader);
            }
        }

        /**
         * get file name from path, not include suffix
         * <p/>
         * <pre>
         *      getFileNameWithoutExtension(null)               =   null
         *      getFileNameWithoutExtension("")                 =   ""
         *      getFileNameWithoutExtension("   ")              =   "   "
         *      getFileNameWithoutExtension("abc")              =   "abc"
         *      getFileNameWithoutExtension("a.mp3")            =   "a"
         *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
         *      getFileNameWithoutExtension("c:\\")              =   ""
         *      getFileNameWithoutExtension("c:\\a")             =   "a"
         *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
         *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
         *      getFileNameWithoutExtension("/home/admin")      =   "admin"
         *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
         * </pre>
         *
         * @param filePath 文件路径
         * @return 文件名 file name from path, not include suffix
         * @see
         */
        public static String getFileName(@NonNull String filePath) {
            if (TextUtils.isEmpty(filePath)) {
                return filePath;
            }

            int extenIndex = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
            int fileIndex = filePath.lastIndexOf(File.separator);
            if (fileIndex == -1) {
                return (extenIndex == -1 ? filePath : filePath.substring(0, extenIndex));
            }
            if (extenIndex == -1) {
                return filePath.substring(fileIndex + 1);
            }
            return (fileIndex < extenIndex ? filePath.substring(fileIndex + 1, extenIndex) : filePath.substring(fileIndex + 1));
        }

        /**
         * get suffix of file from path
         * <p/>
         * <pre>
         *      getFileExtension(null)               =   ""
         *      getFileExtension("")                 =   ""
         *      getFileExtension("   ")              =   "   "
         *      getFileExtension("a.mp3")            =   "mp3"
         *      getFileExtension("a.b.rmvb")         =   "rmvb"
         *      getFileExtension("abc")              =   ""
         *      getFileExtension("c:\\")              =   ""
         *      getFileExtension("c:\\a")             =   ""
         *      getFileExtension("c:\\a.b")           =   "b"
         *      getFileExtension("c:a.txt\\a")        =   ""
         *      getFileExtension("/home/admin")      =   ""
         *      getFileExtension("/home/admin/a.txt/b")  =   ""
         *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
         * </pre>
         *
         * @param filePath 文件路径
         * @return 文件夹名
         */
        public static String getFolderName(@NonNull String filePath) {
            if (TextUtils.isEmpty(filePath)) {
                return filePath;
            }

            int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
            int filePosi = filePath.lastIndexOf(File.separator);
            if (extenPosi == -1) {
                return "";
            }
            return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
        }

        /**
         * Creates the directory named by the trailing filename of this file, including the complete directory path required
         * to create this directory. <br/>
         * <br/>
         * <ul>
         * <strong>Attentions:</strong>
         * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
         * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
         * </ul>
         *
         * @param filePath
         * @return true if the necessary directories have been created or the target directory already exists, false one of
         * the directories can not be created.
         * <ul>
         * <li>if return null, return false</li>
         * <li>if target directory already exists, return true</li>
         * </ul>
         */
        public static boolean mkDirs(@NonNull String filePath) {
            String folderName = getFolderName(filePath);
            if (TextUtils.isEmpty(folderName)) {
                return false;
            }
            File folder = new File(folderName);
            return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
        }

        /**
         * Indicates if this file represents a file on the underlying file system.
         *
         * @param filePath 文件路径
         * @return 是否存在
         */
        public static boolean isFileExist(@NonNull String filePath) {
            if (TextUtils.isEmpty(filePath)) {
                return false;
            }

            File file = new File(filePath);
            return (file.exists() && file.isFile());
        }

        /**
         * Indicates if this file represents a directory on the underlying file system.
         *
         * @param dirPath 文件夹路径
         * @return 文件夹是否存在
         */
        public static boolean isFolderExist(@NonNull String dirPath) {
            if (TextUtils.isEmpty(dirPath)) {
                return false;
            }

            File dire = new File(dirPath);
            return (dire.exists() && dire.isDirectory());
        }

        /**
         * delete file or directory
         * <ul>
         * <li>if path is null or empty, return true</li>
         * <li>if path not exist, return true</li>
         * <li>if path exist, delete recursion. return true</li>
         * <ul>
         *
         * @param path 文件路径
         * @return 是否删除成功
         */
        public static boolean deleteFile(@NonNull String path) {
            if (TextUtils.isEmpty(path)) {
                return true;
            }

            File file = new File(path);
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                return file.delete();
            }
            if (!file.isDirectory()) {
                return false;
            }
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }
            return file.delete();
        }

        /**
         * get file size
         * <ul>
         * <li>if path is null or empty, return -1</li>
         * <li>if path exist and it is a file, return file size, else return -1</li>
         * <ul>
         *
         * @param path 文件路径
         * @return returns the length of this file in bytes. returns -1 if the file does not exist.
         */
        public static long getFileSize(String path) {
            if (TextUtils.isEmpty(path)) {
                return -1;
            }

            File file = new File(path);
            return (file.exists() && file.isFile() ? file.length() : -1);
        }

    }

    private static class IO {
        /**
         * 关闭流
         *
         * @param closeable
         */
        public static void close(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    public static class Date {

        public static SimpleDateFormat m = new SimpleDateFormat("MM", Locale.getDefault());
        public static SimpleDateFormat d = new SimpleDateFormat("dd", Locale.getDefault());
        public static SimpleDateFormat md = new SimpleDateFormat("MM-dd", Locale.getDefault());
        public static SimpleDateFormat hm = new SimpleDateFormat("HH:mm", Locale.getDefault());
        public static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        public static SimpleDateFormat mdhm = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        public static SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        public static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        public static SimpleDateFormat ymdhmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

        /**
         * 根据时间戳转化为String
         *
         * @param formatType string的格式
         * @param time       时间撮
         * @return 时间字符串
         */
        public static String getForLong(@NonNull String formatType, long time) {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType, Locale.getDefault());
            return formatter.format(new java.util.Date(time));
        }

        /**
         * 字符串时间转long
         *
         * @param time
         * @param formatType yyyy-MM-dd HH:mm:ss:SS
         * @return 时间戳
         */
        public static long getForString(@NonNull String formatType, @NonNull String time) {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType, Locale.getDefault());
            java.util.Date date = null;
            try {
                date = formatter.parse(time);
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }

        /**
         * 是否是当天
         *
         * @param time 指定时间
         * @return 结果
         */
        public static boolean isToday(long time) {
            return isSameDay(time, Calendar.getInstance().getTimeInMillis());
        }

        /**
         * 是否是同一天
         *
         * @param time1
         * @param time2
         * @return
         */
        public static boolean isSameDay(long time1, long time2) {
            String aDay = getForLong("yyyy-MM-dd", time1);
            String bDay = getForLong("yyyy-MM-dd", time2);
            return aDay.equals(bDay);
        }

        /**
         * 获取月份的天数
         *
         * @param mills 时间戳
         * @return 当前月份的天数
         */
        public static int getMonthDay(long mills) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mills);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);

            switch (month) {
                case Calendar.JANUARY:
                case Calendar.MARCH:
                case Calendar.MAY:
                case Calendar.JULY:
                case Calendar.AUGUST:
                case Calendar.OCTOBER:
                case Calendar.DECEMBER:
                    return 31;
                case Calendar.APRIL:
                case Calendar.JUNE:
                case Calendar.SEPTEMBER:
                case Calendar.NOVEMBER:
                    return 30;
                case Calendar.FEBRUARY:
                    return (year % 4 == 0) ? 29 : 28;
                default:
                    throw new IllegalArgumentException("Invalid Month");
            }
        }

        /**
         * 获取当月第一天的时间（毫秒值）
         *
         * @param mills 时间戳
         * @return 第一天的时间戳
         */
        public static long getMonthFristDay(long mills) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mills);
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            return calendar.getTimeInMillis();
        }

        /**
         * 获取星期,0-周日,1-周一，2-周二，3-周三，4-周四，5-周五，6-周六
         *
         * @param mills 时间戳
         * @return 周几
         */
        public static int getWeek(long mills) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mills);

            return calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
    }


    public static class NetWork {
        public static final String NETWORK_TYPE_WIFI = "wifi";
        public static final String NETWORK_TYPE_3G = "eg";
        public static final String NETWORK_TYPE_2G = "2g";
        public static final String NETWORK_TYPE_WAP = "wap";
        public static final String NETWORK_TYPE_UNKNOWN = "unknown";
        public static final String NETWORK_TYPE_DISCONNECT = "disconnect";

        /**
         * 获取当前网络类型
         *
         * @param context 上下文
         * @return 网络类型
         */
        public static int getNetworkType(@NonNull Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
            return networkInfo == null ? -1 : networkInfo.getType();
        }

        /**
         * @param context 上下文
         * @return 网络名称
         */
        public static String getNetworkTypeName(@NonNull Context context) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo;
            String type = NETWORK_TYPE_DISCONNECT;
            if (manager == null || (networkInfo = manager.getActiveNetworkInfo()) == null) {
                return type;
            }

            if (networkInfo.isConnected()) {
                String typeName = networkInfo.getTypeName();
                if ("WIFI".equalsIgnoreCase(typeName)) {
                    type = NETWORK_TYPE_WIFI;
                } else if ("MOBILE".equalsIgnoreCase(typeName)) {
                    String proxyHost = android.net.Proxy.getDefaultHost();
                    type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORK_TYPE_3G : NETWORK_TYPE_2G)
                            : NETWORK_TYPE_WAP;
                } else {
                    type = NETWORK_TYPE_UNKNOWN;
                }
            }
            return type;
        }

        /**
         * 是否有网络连接
         */
        public static boolean isDisConnect(@NonNull Context context) {
            return NetWork.NETWORK_TYPE_DISCONNECT.equals(NetWork.getNetworkTypeName(context));
        }

        /**
         * 当前网络是否快
         */
        private static boolean isFastMobileNetwork(@NonNull Context context) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return false;
            }
            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true;
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true;
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true;
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return true;
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return true;
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return true;
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return false;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return true;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;
            }
        }
    }

    public static class Empty {

        /**
         * Return whether object is empty.
         *
         * @param obj The object.
         * @return {@code true}: yes<br>{@code false}: no
         */
        public static boolean isEmpty(final Object obj) {
            if (obj == null) {
                return true;
            }
            if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
                return true;
            }
            if (obj instanceof CharSequence && obj.toString().length() == 0) {
                return true;
            }
            if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
                return true;
            }
            if (obj instanceof Map && ((Map) obj).isEmpty()) {
                return true;
            }
            if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
                return true;
            }
            if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
                return true;
            }
            if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
                return true;
            }
            if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
                return true;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                    return true;
                }
            }
            if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
                return true;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (obj instanceof android.util.LongSparseArray
                        && ((android.util.LongSparseArray) obj).size() == 0) {
                    return true;
                }
            }
            return false;
        }

        public static boolean isEmpty(final CharSequence obj) {
            return obj == null || obj.toString().length() == 0;
        }

        public static boolean isEmpty(final Collection obj) {
            return obj == null || obj.isEmpty();
        }

        public static boolean isEmpty(final Map obj) {
            return obj == null || obj.isEmpty();
        }

        public static boolean isEmpty(final SimpleArrayMap obj) {
            return obj == null || obj.isEmpty();
        }

        public static boolean isEmpty(final SparseArray obj) {
            return obj == null || obj.size() == 0;
        }

        public static boolean isEmpty(final SparseBooleanArray obj) {
            return obj == null || obj.size() == 0;
        }

        public static boolean isEmpty(final SparseIntArray obj) {
            return obj == null || obj.size() == 0;
        }

        public static boolean isEmpty(final LongSparseArray obj) {
            return obj == null || obj.size() == 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public static boolean isEmpty(final SparseLongArray obj) {
            return obj == null || obj.size() == 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public static boolean isEmpty(final android.util.LongSparseArray obj) {
            return obj == null || obj.size() == 0;
        }

        /**
         * Return whether object is not empty.
         *
         * @param obj The object.
         * @return {@code true}: yes<br>{@code false}: no
         */
        public static boolean isNotEmpty(final Object obj) {
            return !isEmpty(obj);
        }


        public static boolean isNotEmpty(final CharSequence obj) {
            return !isEmpty(obj);
        }

        public static boolean isNotEmpty(final Collection obj) {
            return !isEmpty(obj);
        }

        public static boolean isNotEmpty(final Map obj) {
            return !isEmpty(obj);
        }

        public static boolean isNotEmpty(final SimpleArrayMap obj) {
            return !isEmpty(obj);
        }

        public static boolean isNotEmpty(final SparseArray obj) {
            return !isEmpty(obj);
        }

        public static boolean isNotEmpty(final SparseBooleanArray obj) {
            return !isEmpty(obj);
        }

        public static boolean isNotEmpty(final SparseIntArray obj) {
            return !isEmpty(obj);
        }

        public static boolean isNotEmpty(final LongSparseArray obj) {
            return !isEmpty(obj);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public static boolean isNotEmpty(final SparseLongArray obj) {
            return !isEmpty(obj);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public static boolean isNotEmpty(final android.util.LongSparseArray obj) {
            return !isEmpty(obj);
        }

        /**
         * Return whether object1 is equals to object2.
         *
         * @param o1 The first object.
         * @param o2 The second object.
         * @return {@code true}: yes<br>{@code false}: no
         */
        public static boolean equals(final Object o1, final Object o2) {
            return o1 == o2 || (o1 != null && o1.equals(o2));
        }

        /**
         * Require the objects are not null.
         *
         * @param objects The object.
         * @throws NullPointerException if any object is null in objects
         */
        public static void requireNonNull(final Object... objects) {
            if (objects == null) throw new NullPointerException();
            for (Object object : objects) {
                if (object == null) throw new NullPointerException();
            }
        }

        /**
         * Return the nonnull object or default object.
         *
         * @param object        The object.
         * @param defaultObject The default object to use with the object is null.
         * @param <T>           The value type.
         * @return the nonnull object or default object
         */
        public static <T> T getOrDefault(final T object, final T defaultObject) {
            if (object == null) {
                return defaultObject;
            }
            return object;
        }

        /**
         * Return the hash code of object.
         *
         * @param o The object.
         * @return the hash code of object
         */
        public static int hashCode(final Object o) {
            return o != null ? o.hashCode() : 0;
        }
    }

    public static class Real {
        /**
         * 匹配手机号的规则：[3578]是手机号第二位可能出现的数字
         */
        private static final String REGEX_MOBILE = "^[1][35678][0-9]{9}$";

        /**
         * 校验手机号
         *
         * @param mobile
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isMobile(@NonNull String mobile) {
            return Pattern.matches(REGEX_MOBILE, mobile);
        }


        /**
         * 176，177，178,
         * 180，181，182,183,184,185，186，187,188。，189。
         * 145，147
         * 130，131，132，133，134,135,136,137, 138,139
         * 150,151, 152,153，155，156，157,158,159,
         * 是否是中国手机号
         */
        public static boolean isChinaPhone(@NonNull String str) {
            String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(str);
            return m.matches();
        }

        /**
         * 手机号用****号隐藏中间数字
         *
         * @param phone
         * @return
         */
        public static String formatePhone(String phone) {
            String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            return phone_s;
        }

        /*
         * *********************************判断是否身份证号码*******************************************
         */

        /**
         * 功能：身份证的有效验证
         *
         * @param IDStr 身份证号
         * @return true 有效：false 无效
         * @throws ParseException
         */
        public static boolean isIDNum(@NonNull String IDStr) {
            String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
            String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
            String Ai = "";
            // ================ 号码的长度18位 ================
            if (IDStr.length() != 18) {
                return false;
            }
            // ================ 数字 除最后以为都为数字 ================
            Ai = IDStr.substring(0, 17);
            if (isNumeric(Ai) == false) {
                //errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
                return false;
            }
            // ================ 出生年月是否有效 ================
            String strYear = Ai.substring(6, 10);// 年份
            String strMonth = Ai.substring(10, 12);// 月份
            String strDay = Ai.substring(12, 14);// 日
            if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
//          errorInfo = "身份证生日无效。";
                return false;
            }
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                    //errorInfo = "身份证生日不在有效范围。";
                    return false;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
                //errorInfo = "身份证月份无效";
                return false;
            }
            if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
                //errorInfo = "身份证日期无效";
                return false;
            }
            // ================ 地区码时候有效 ================
            Hashtable h = GetAreaCode();
            if (h.get(Ai.substring(0, 2)) == null) {
                //errorInfo = "身份证地区编码错误。";
                return false;
            }
            // ================ 判断最后一位的值 ================
            int TotalmulAiWi = 0;
            for (int i = 0; i < 17; i++) {
                TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
            }
            int modValue = TotalmulAiWi % 11;
            String strVerifyCode = ValCodeArr[modValue];
            Ai = Ai + strVerifyCode;

            if (Ai.equals(IDStr) == false) {
                //errorInfo = "身份证无效，不是合法的身份证号码";
                return false;
            }
            return true;
        }

        /**
         * 功能：设置地区编码
         *
         * @return Hashtable 对象
         */
        @SuppressWarnings("unchecked")
        private static Hashtable GetAreaCode() {
            Hashtable hashtable = new Hashtable();
            hashtable.put("11", "北京");
            hashtable.put("12", "天津");
            hashtable.put("13", "河北");
            hashtable.put("14", "山西");
            hashtable.put("15", "内蒙古");
            hashtable.put("21", "辽宁");
            hashtable.put("22", "吉林");
            hashtable.put("23", "黑龙江");
            hashtable.put("31", "上海");
            hashtable.put("32", "江苏");
            hashtable.put("33", "浙江");
            hashtable.put("34", "安徽");
            hashtable.put("35", "福建");
            hashtable.put("36", "江西");
            hashtable.put("37", "山东");
            hashtable.put("41", "河南");
            hashtable.put("42", "湖北");
            hashtable.put("43", "湖南");
            hashtable.put("44", "广东");
            hashtable.put("45", "广西");
            hashtable.put("46", "海南");
            hashtable.put("50", "重庆");
            hashtable.put("51", "四川");
            hashtable.put("52", "贵州");
            hashtable.put("53", "云南");
            hashtable.put("54", "西藏");
            hashtable.put("61", "陕西");
            hashtable.put("62", "甘肃");
            hashtable.put("63", "青海");
            hashtable.put("64", "宁夏");
            hashtable.put("65", "新疆");
            hashtable.put("71", "台湾");
            hashtable.put("81", "香港");
            hashtable.put("82", "澳门");
            hashtable.put("91", "国外");
            return hashtable;
        }

        /**
         * 功能：判断字符串是否为数字
         *
         * @param str
         * @return
         */
        private static boolean isNumeric(String str) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (isNum.matches()) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 功能：判断字符串是否为日期格式
         *
         * @param strDate
         * @return
         */
        private static boolean isDate(String strDate) {
            Pattern pattern = Pattern
                    .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
            Matcher m = pattern.matcher(strDate);
            if (m.matches()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static class Blur {
        /**
         * Return the blur bitmap fast.
         * <p>zoom out, blur, zoom in</p>
         *
         * @param src    The source of bitmap.
         * @param scale  The scale(0...1).
         * @param radius The radius(0...25).
         * @return the blur bitmap
         */
        public static Bitmap fastBlur(@NonNull Context context, final Bitmap src,
                                      @FloatRange(
                                              from = 0, to = 1, fromInclusive = false
                                      ) final float scale,
                                      @FloatRange(
                                              from = 0, to = 25, fromInclusive = false
                                      ) final float radius) {
            return fastBlur(context, src, scale, radius, false);
        }

        /**
         * Return the blur bitmap fast.
         * <p>zoom out, blur, zoom in</p>
         *
         * @param src     The source of bitmap.
         * @param scale   The scale(0...1).
         * @param radius  The radius(0...25).
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the blur bitmap
         */
        public static Bitmap fastBlur(@NonNull Context context, final Bitmap src,
                                      @FloatRange(
                                              from = 0, to = 1, fromInclusive = false
                                      ) final float scale,
                                      @FloatRange(
                                              from = 0, to = 25, fromInclusive = false
                                      ) final float radius,
                                      final boolean recycle) {
            if (src == null || src.getWidth() == 0 || src.getHeight() == 0) return null;
            int width = src.getWidth();
            int height = src.getHeight();
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            Bitmap scaleBitmap =
                    Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas();
            PorterDuffColorFilter filter = new PorterDuffColorFilter(
                    Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
            paint.setColorFilter(filter);
            canvas.scale(scale, scale);
            canvas.drawBitmap(scaleBitmap, 0, 0, paint);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                scaleBitmap = renderScriptBlur(context, scaleBitmap, radius, recycle);
            } else {
                scaleBitmap = stackBlur(scaleBitmap, (int) radius, recycle);
            }
            if (scale == 1) {
                if (recycle && !src.isRecycled()) src.recycle();
                return scaleBitmap;
            }
            Bitmap ret = Bitmap.createScaledBitmap(scaleBitmap, width, height, true);
            if (!scaleBitmap.isRecycled()) scaleBitmap.recycle();
            if (recycle && !src.isRecycled()) src.recycle();
            return ret;
        }

        /**
         * Return the blur bitmap using render script.
         *
         * @param src    The source of bitmap.
         * @param radius The radius(0...25).
         * @return the blur bitmap
         */
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public static Bitmap renderScriptBlur(@NonNull Context context, final Bitmap src,
                                              @FloatRange(
                                                      from = 0, to = 25, fromInclusive = false
                                              ) final float radius) {
            return renderScriptBlur(context, src, radius, false);
        }

        /**
         * Return the blur bitmap using render script.
         *
         * @param src     The source of bitmap.
         * @param radius  The radius(0...25).
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the blur bitmap
         */
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public static Bitmap renderScriptBlur(@NonNull Context context, final Bitmap src,
                                              @FloatRange(
                                                      from = 0, to = 25, fromInclusive = false
                                              ) final float radius,
                                              final boolean recycle) {
            RenderScript rs = null;
            Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
            try {
                rs = RenderScript.create(context);
                rs.setMessageHandler(new RenderScript.RSMessageHandler());
                Allocation input = Allocation.createFromBitmap(rs,
                        ret,
                        Allocation.MipmapControl.MIPMAP_NONE,
                        Allocation.USAGE_SCRIPT);
                Allocation output = Allocation.createTyped(rs, input.getType());
                ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                blurScript.setInput(input);
                blurScript.setRadius(radius);
                blurScript.forEach(output);
                output.copyTo(ret);
            } finally {
                if (rs != null) {
                    rs.destroy();
                }
            }
            return ret;
        }

        /**
         * Return the blur bitmap using stack.
         *
         * @param src    The source of bitmap.
         * @param radius The radius(0...25).
         * @return the blur bitmap
         */
        public static Bitmap stackBlur(final Bitmap src, final int radius) {
            return stackBlur(src, radius, false);
        }

        /**
         * Return the blur bitmap using stack.
         *
         * @param src     The source of bitmap.
         * @param radius  The radius(0...25).
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the blur bitmap
         */
        public static Bitmap stackBlur(final Bitmap src, int radius, final boolean recycle) {
            Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
            if (radius < 1) {
                radius = 1;
            }
            int w = ret.getWidth();
            int h = ret.getHeight();

            int[] pix = new int[w * h];
            ret.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int dv[] = new int[256 * divsum];
            for (i = 0; i < 256 * divsum; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }
            ret.setPixels(pix, 0, w, 0, 0, w, h);
            return ret;
        }

    }

    public static class Image {
        /**
         * 图片的八个位置
         **/
        public static final int TOP = 0;      //上
        public static final int BOTTOM = 1;      //下
        public static final int LEFT = 2;      //左
        public static final int RIGHT = 3;      //右
        public static final int LEFT_TOP = 4;    //左上
        public static final int LEFT_BOTTOM = 5;  //左下
        public static final int RIGHT_TOP = 6;    //右上
        public static final int RIGHT_BOTTOM = 7;  //右下

        /**
         * 图像原始宽高的比例缩放图片
         *
         * @param src    源位图对象
         * @param scaleX 宽度比例系数
         * @param scaleY 高度比例系数
         * @return 返回位图对象
         */
        public static Bitmap zoomBitmap(@NonNull Bitmap src, float scaleX, float scaleY) {
            Matrix matrix = new Matrix();
            matrix.setScale(scaleX, scaleY);
            Bitmap t_bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            return t_bitmap;
        }


        /**
         * 图像放大缩小--根据宽度和高度
         *
         * @param src    bitmap数据源
         * @param width  指定缩放的宽度
         * @param height 指定缩放的高度
         * @return 缩放后的图片
         */
        public static Bitmap zoomBimtap(@NonNull Bitmap src, int width, int height) {
            return Bitmap.createScaledBitmap(src, width, height, true);
        }

        /**
         * 将Drawable转为Bitmap对象
         *
         * @param drawable
         * @return
         */
        public static Bitmap drawableToBitmap(@NonNull Drawable drawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        /**
         * 将Bitmap转换为Drawable对象
         *
         * @param bitmap
         * @return
         */
        public static Drawable bitmapToDrawable(@NonNull Bitmap bitmap) {
            Drawable drawable = new BitmapDrawable(bitmap);
            return drawable;
        }

        /**
         * Bitmap转byte[]
         *
         * @param bitmap 图片数据源（默认以PNG加载bitmap）
         * @return 图片字节
         */
        public static byte[] bitmapToByte(@NonNull Bitmap bitmap) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return out.toByteArray();
        }

        /**
         * byte[]转Bitmap
         *
         * @param data
         * @return
         */
        public static Bitmap byteToBitmap(@NonNull byte[] data) {
            if (data.length != 0) {
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            }
            return null;
        }

        /**
         * 绘制带圆角的图像
         *
         * @param src    图片源
         * @param radius 圆角度数
         * @return 生成的圆角矩形Bitmap
         */
        public static Bitmap createRounderBitmap(@NonNull Bitmap src, int radius) {
            final int w = src.getWidth();
            final int h = src.getHeight();
            // 高清量32位图
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            Canvas canvas = new Canvas(bitmap);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(0xff424242);
            // 防止边缘的锯齿
            paint.setFilterBitmap(true);
            Rect rect = new Rect(0, 0, w, h);
            RectF rectf = new RectF(rect);
            // 绘制带圆角的矩形
            canvas.drawRoundRect(rectf, radius, radius, paint);
            // 取两层绘制交集，显示上层
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            // 绘制图像
            canvas.drawBitmap(src, rect, rect, paint);
            return bitmap;
        }

        /**
         * 带倒影的图像
         *
         * @param src
         * @return
         */
        public static Bitmap createMirrorBitmap(@NonNull Bitmap src) {
            // 两个图像间的空隙
            final int spacing = 4;
            final int w = src.getWidth();
            final int h = src.getHeight();
            // 绘制高质量32位图
            Bitmap bitmap = Bitmap.createBitmap(w, h + h / 2 + spacing, Bitmap.Config.ARGB_8888);
            // 创建燕X轴的倒影图像
            Matrix m = new Matrix();
            m.setScale(1, -1);
            Bitmap t_bitmap = Bitmap.createBitmap(src, 0, h / 2, w, h / 2, m, true);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            //  绘制原图像
            canvas.drawBitmap(src, 0, 0, paint);
            // 绘制倒影图像
            canvas.drawBitmap(t_bitmap, 0, h + spacing, paint);
            // 线性渲染-沿Y轴高到低渲染
            Shader shader = new LinearGradient(0, h + spacing, 0, h + spacing + h / 2, 0x70ffffff, 0x00ffffff, Shader.TileMode.MIRROR);
            paint.setShader(shader);
            // 取两层绘制交集，显示下层。
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            // 绘制渲染倒影的矩形
            canvas.drawRect(0, h + spacing, w, h + h / 2 + spacing, paint);
            return bitmap;
        }

        /**
         * 独立的倒影图像
         *
         * @param src 图片源
         * @return
         */
        public static Bitmap createReflectionBitmapForSingle(@NonNull Bitmap src) {
            final int w = src.getWidth();
            final int h = src.getHeight();
            // 绘制高质量32位图
            Bitmap bitmap = Bitmap.createBitmap(w, h / 2, Bitmap.Config.ARGB_8888);
            // 创建沿X轴的倒影图像
            Matrix m = new Matrix();
            m.setScale(1, -1);
            Bitmap t_bitmap = Bitmap.createBitmap(src, 0, h / 2, w, h / 2, m, true);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            // 绘制倒影图像
            canvas.drawBitmap(t_bitmap, 0, 0, paint);
            // 线性渲染-沿Y轴高到低渲染
            Shader shader = new LinearGradient(0, 0, 0, h / 2, 0x70ffffff,
                    0x00ffffff, Shader.TileMode.MIRROR);
            paint.setShader(shader);
            // 取两层绘制交集。显示下层。
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            // 绘制渲染倒影的矩形
            canvas.drawRect(0, 0, w, h / 2, paint);
            return bitmap;
        }

        /**
         * 图片灰度转化
         *
         * @param src 原始图片
         * @return
         */
        public static Bitmap createGreyBitmap(@NonNull Bitmap src) {
            final int w = src.getWidth();
            final int h = src.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            // 颜色变换的矩阵
            ColorMatrix matrix = new ColorMatrix();
            // saturation 饱和度值，最小可设为0，此时对应的是灰度图；为1表示饱和度不变，设置大于1，就显示过饱和
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            paint.setColorFilter(filter);
            canvas.drawBitmap(src, 0, 0, paint);
            return bitmap;
        }

        /**
         * 添加水印效果
         *
         * @param src       源位图
         * @param watermark 水印
         * @param direction 方向
         * @param spacing   间距
         * @return
         */
        public static Bitmap createWaterMark(@NonNull Bitmap src, @NonNull Bitmap watermark, int direction, int spacing) {
            final int w = src.getWidth();
            final int h = src.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            if (direction == LEFT_TOP) {
                canvas.drawBitmap(watermark, spacing, spacing, null);
            } else if (direction == LEFT_BOTTOM) {
                canvas.drawBitmap(watermark, spacing, h - watermark.getHeight() - spacing, null);
            } else if (direction == RIGHT_TOP) {
                canvas.drawBitmap(watermark, w - watermark.getWidth() - spacing, spacing, null);
            } else if (direction == RIGHT_BOTTOM) {
                canvas.drawBitmap(watermark, w - watermark.getWidth() - spacing, h - watermark.getHeight() - spacing, null);
            }
            return bitmap;
        }

        /**
         * 合成图像
         *
         * @param direction
         * @param bitmaps
         * @return
         */
        public static Bitmap mergeBitmaps(int direction, @NonNull Bitmap... bitmaps) {
            if (bitmaps.length < 2) {
                return null;
            }
            Bitmap firstBitmap = bitmaps[0];
            for (int i = 0; i < bitmaps.length; i++) {
                firstBitmap = mergeBitmap(firstBitmap, bitmaps[i], direction);
            }
            return firstBitmap;
        }

        /**
         * 合成两张图像
         *
         * @param firstBitmap  第一张图片
         * @param secondBitmap 第二张图片
         * @param direction    两张图片的相对位置
         * @return
         */
        private static Bitmap mergeBitmap(@NonNull Bitmap firstBitmap, @NonNull Bitmap secondBitmap,
                                          int direction) {
            if (firstBitmap == null) {
                return null;
            }
            if (secondBitmap == null) {
                return firstBitmap;
            }
            final int fw = firstBitmap.getWidth();
            final int fh = firstBitmap.getHeight();
            final int sw = secondBitmap.getWidth();
            final int sh = secondBitmap.getHeight();
            Bitmap bitmap = null;
            Canvas canvas = null;
            if (direction == TOP) {
                bitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                canvas.drawBitmap(secondBitmap, 0, 0, null);
                canvas.drawBitmap(firstBitmap, 0, sh, null);
            } else if (direction == BOTTOM) {
                bitmap = Bitmap.createBitmap(fw > sw ? fw : sw, fh + sh, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                canvas.drawBitmap(firstBitmap, 0, 0, null);
                canvas.drawBitmap(secondBitmap, 0, fh, null);
            } else if (direction == LEFT) {
                bitmap = Bitmap.createBitmap(fw + sw, sh > fh ? sh : fh, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                canvas.drawBitmap(secondBitmap, 0, 0, null);
                canvas.drawBitmap(firstBitmap, sw, 0, null);
            } else if (direction == RIGHT) {
                bitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
                        Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                canvas.drawBitmap(firstBitmap, 0, 0, null);
                canvas.drawBitmap(secondBitmap, fw, 0, null);
            }
            return bitmap;
        }


        /**
         * 图片抖动
         * 对于只有黑白两种颜色的图片，通过抖动可以产生视觉上的灰度
         *
         * @param img 数据源
         * @return
         */
        public static Bitmap createShakeBitmap(@NonNull Bitmap img) {
            int width = img.getWidth();         //获取位图的宽
            int height = img.getHeight();       //获取位图的高
            int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组
            img.getPixels(pixels, 0, width, 0, 0, width, height);
            int[] gray = new int[height * width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int grey = pixels[width * i + j];
                    int red = ((grey & 0x00FF0000) >> 16);
                    gray[width * i + j] = red;
                }
            }
            int e = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int g = gray[width * i + j];
                    if (g >= 128) {
                        pixels[width * i + j] = 0xffffffff;
                        e = g - 255;
                    } else {
                        pixels[width * i + j] = 0xff000000;
                        e = g;
                    }
                    if (j < width - 1 && i < height - 1) {
                        //右边像素处理
                        gray[width * i + j + 1] += 3 * e / 8;
                        //下
                        gray[width * (i + 1) + j] += 3 * e / 8;
                        //右下
                        gray[width * (i + 1) + j + 1] += e / 4;
                    } else if (j == width - 1 && i < height - 1) {//靠右或靠下边的像素的情况
                        //下方像素处理
                        gray[width * (i + 1) + j] += 3 * e / 8;
                    } else if (j < width - 1 && i == height - 1) {
                        //右边像素处理
                        gray[width * (i) + j + 1] += e / 4;
                    }
                }
            }
            Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return mBitmap;
        }
    }
}
