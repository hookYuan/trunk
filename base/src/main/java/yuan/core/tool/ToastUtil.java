package yuan.core.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/29
 *     desc  : utils about sToast
 *
 *     2019-07-12 修复Context引起内存泄漏
 * </pre>
 */
public final class ToastUtil {

    private static final int COLOR_DEFAULT = 0xFEFFFFFF;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static final String NULL = "null";

    private static Toast sToast;
    private static int sGravity = -1;
    private static int sXOffset = -1;
    private static int sYOffset = -1;
    private static int sBgColor = COLOR_DEFAULT;
    private static int sBgResource = -1;
    private static int sMsgColor = COLOR_DEFAULT;
    private static int sMsgTextSize = -1;

    private ToastUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Set the gravity.
     *
     * @param gravity The gravity.
     * @param xOffset X-axis offset, in pixel.
     * @param yOffset Y-axis offset, in pixel.
     */
    public static void setGravity(final int gravity, final int xOffset, final int yOffset) {
        sGravity = gravity;
        sXOffset = xOffset;
        sYOffset = yOffset;
    }

    /**
     * Set the color of background.
     *
     * @param backgroundColor The color of background.
     */
    public static void setBgColor(@ColorInt final int backgroundColor) {
        sBgColor = backgroundColor;
    }

    /**
     * Set the resource of background.
     *
     * @param bgResource The resource of background.
     */
    public static void setBgResource(@DrawableRes final int bgResource) {
        sBgResource = bgResource;
    }

    /**
     * Set the color of message.
     *
     * @param msgColor The color of message.
     */
    public static void setMsgColor(@ColorInt final int msgColor) {
        sMsgColor = msgColor;
    }

    /**
     * Set the text size of message.
     *
     * @param textSize The text size of message.
     */
    public static void setMsgTextSize(final int textSize) {
        sMsgTextSize = textSize;
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param text The text.
     */
    public static void showShort(@NonNull Context context, final CharSequence text) {
        show(context, text == null ? NULL : text, Toast.LENGTH_SHORT);
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param resId The resource id for text.
     */
    public static void showShort(@NonNull Context context, @StringRes final int resId) {
        show(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    public static void showShort(@NonNull Context context, @StringRes final int resId, final Object... args) {
        show(context, resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param format The format.
     * @param args   The args.
     */
    public static void showShort(final String format, final Object... args) {
        show(format, Toast.LENGTH_SHORT, args);
    }

    /**
     * Show the sToast for a long period of time.
     *
     * @param text The text.
     */
    public static void showLong(@NonNull Context context, final CharSequence text) {
        show(context, text == null ? NULL : text, Toast.LENGTH_LONG);
    }

    /**
     * Show the sToast for a long period of time.
     *
     * @param resId The resource id for text.
     */
    public static void showLong(@NonNull Context context, @StringRes final int resId) {
        show(context, resId, Toast.LENGTH_LONG);
    }

    /**
     * Show the sToast for a long period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    public static void showLong(@NonNull Context context, @StringRes final int resId, final Object... args) {
        show(context, resId, Toast.LENGTH_LONG, args);
    }

    /**
     * Show the sToast for a long period of time.
     *
     * @param format The format.
     * @param args   The args.
     */
    public static void showLong(final String format, final Object... args) {
        show(format, Toast.LENGTH_LONG, args);
    }

    /**
     * Show custom sToast for a short period of time.
     *
     * @param layoutId ID for an XML layout resource to load.
     */
    public static View showCustomShort(@NonNull Context context, @LayoutRes final int layoutId) {
        final View view = getView(context, layoutId);
        show(context, view, Toast.LENGTH_SHORT);
        return view;
    }

    /**
     * Show custom sToast for a long period of time.
     *
     * @param layoutId ID for an XML layout resource to load.
     */
    public static View showCustomLong(@NonNull Context context, @LayoutRes final int layoutId) {
        final View view = getView(context, layoutId);
        show(context, view, Toast.LENGTH_LONG);
        return view;
    }

    /**
     * Cancel the sToast.
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
        }
    }

    private static void show(@NonNull Context context, @StringRes final int resId, final int duration) {
        show(context.getResources().getText(resId).toString(), duration);
    }

    private static void show(@NonNull Context context, @StringRes final int resId, final int duration, final Object... args) {
        show(String.format(context.getResources().getString(resId), args), duration);
    }

    private static void show(final String format, final int duration, final Object... args) {
        String text;
        if (format == null) {
            text = NULL;
        } else {
            text = String.format(format, args);
            if (text == null) {
                text = NULL;
            }
        }
        show(text, duration);
    }

    private static void show(@NonNull Context context, final CharSequence text, final int duration) {
        HANDLER.post(new Runnable() {
            @SuppressLint("ShowToast")
            @Override
            public void run() {
                if (context == null) return;
                WeakReference<Context> weakContext = new WeakReference(context);
                cancel();
                sToast = Toast.makeText(weakContext.get(), text, duration);
                final TextView tvMessage = sToast.getView().findViewById(android.R.id.message);
                if (sMsgColor != COLOR_DEFAULT) {
                    tvMessage.setTextColor(sMsgColor);
                }
                if (sMsgTextSize != -1) {
                    tvMessage.setTextSize(sMsgTextSize);
                }
                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                    sToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setBg(tvMessage);
                showToast(weakContext.get());
            }
        });
    }

    private static void show(@NonNull Context context, final View view, final int duration) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (context == null) return;
                WeakReference<Context> weakContext = new WeakReference(context);
                cancel();
                sToast = new Toast(weakContext.get());
                sToast.setView(view);
                sToast.setDuration(duration);
                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                    sToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setBg();
                showToast(weakContext.get());
            }
        });
    }

    private static void showToast(@NonNull Context context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            try {
                //noinspection JavaReflectionMemberAccess
                Field field = View.class.getDeclaredField("mContext");
                field.setAccessible(true);
                field.set(sToast.getView(), new ApplicationContextWrapperForApi25(context));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        sToast.show();
    }

    private static void setBg() {
        if (sBgResource != -1) {
            final View toastView = sToast.getView();
            toastView.setBackgroundResource(sBgResource);
        } else if (sBgColor != COLOR_DEFAULT) {
            final View toastView = sToast.getView();
            Drawable background = toastView.getBackground();
            if (background != null) {
                background.setColorFilter(
                        new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
                );
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    toastView.setBackground(new ColorDrawable(sBgColor));
                } else {
                    toastView.setBackgroundDrawable(new ColorDrawable(sBgColor));
                }
            }
        }
    }

    private static void setBg(final TextView tvMsg) {
        if (sBgResource != -1) {
            final View toastView = sToast.getView();
            toastView.setBackgroundResource(sBgResource);
            tvMsg.setBackgroundColor(Color.TRANSPARENT);
        } else if (sBgColor != COLOR_DEFAULT) {
            final View toastView = sToast.getView();
            Drawable tvBg = toastView.getBackground();
            Drawable msgBg = tvMsg.getBackground();
            if (tvBg != null && msgBg != null) {
                tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
                tvMsg.setBackgroundColor(Color.TRANSPARENT);
            } else if (tvBg != null) {
                tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else if (msgBg != null) {
                msgBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else {
                toastView.setBackgroundColor(sBgColor);
            }
        }
    }

    private static View getView(@NonNull Context context, @LayoutRes final int layoutId) {
        LayoutInflater inflate =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflate != null ? inflate.inflate(layoutId, null) : null;
    }

    private static final class ApplicationContextWrapperForApi25 extends ContextWrapper {

        ApplicationContextWrapperForApi25(@NonNull Context context) {
            super(context);
        }

        @Override
        public Context getApplicationContext() {
            return this;
        }

        @Override
        public Object getSystemService(@NonNull String name) {
            if (Context.WINDOW_SERVICE.equals(name)) {
                // noinspection ConstantConditions
                return new WindowManagerWrapper(
                        (WindowManager) getBaseContext().getSystemService(name)
                );
            }
            return super.getSystemService(name);
        }

        private static final class WindowManagerWrapper implements WindowManager {

            private final WindowManager base;

            private WindowManagerWrapper(@NonNull WindowManager base) {
                this.base = base;
            }

            @Override
            public Display getDefaultDisplay() {
                return base.getDefaultDisplay();
            }

            @Override
            public void removeViewImmediate(View view) {
                base.removeViewImmediate(view);
            }

            @Override
            public void addView(View view, ViewGroup.LayoutParams params) {
                try {
                    base.addView(view, params);
                } catch (BadTokenException e) {
                    Log.e("WindowManagerWrapper", e.getMessage());
                } catch (Throwable throwable) {
                    Log.e("WindowManagerWrapper", "[addView]", throwable);
                }
            }

            @Override
            public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
                base.updateViewLayout(view, params);
            }

            @Override
            public void removeView(View view) {
                base.removeView(view);
            }
        }
    }
}