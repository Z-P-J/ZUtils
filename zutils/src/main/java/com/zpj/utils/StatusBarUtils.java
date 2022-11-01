package com.zpj.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtils {

    public static boolean supportTransparentStatusBar() {
        return OSUtils.isMiui()
                || OSUtils.isFlyme()
                || (OSUtils.isOppo() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 设置状态栏透明
     *
     * @param window
     */
    public static void transparentStatusBar(Window window) {
        if (OSUtils.isMiui() || OSUtils.isFlyme()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                transparentStatusBarAbove21(window);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else if ((OSUtils.isOppo() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)) {
            transparentStatusBarAbove21(window);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            transparentStatusBarAbove21(window);
        }
    }

    public static void transparentStatusBar(Activity activity) {
        transparentStatusBar(activity.getWindow());
    }

    @TargetApi(21)
    private static void transparentStatusBarAbove21(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * 设置状态栏图标白色主题
     *
     * @param window
     */
    public static void setLightMode(Window window) {
        if (OSUtils.isMiui()) {
            setMIUIStatusBarDarkMode(window, false);
        } else if (OSUtils.isFlyme()) {
            setFlymeStatusBarDarkMode(window, false);
        } else if (OSUtils.isOppo()) {
            setOppoStatusBarDarkMode(window, false);
        } else {
            setStatusBarDarkMode(window, false);
        }
    }

    /**
     * 设置状态栏图片黑色主题
     *
     * @param window
     */
    public static void setDarkMode(Window window) {
        if (OSUtils.isMiui()) {
            setMIUIStatusBarDarkMode(window, true);
        } else if (OSUtils.isFlyme()) {
            setFlymeStatusBarDarkMode(window, true);
        } else if (OSUtils.isOppo()) {
            setOppoStatusBarDarkMode(window, true);
        } else {
            setStatusBarDarkMode(window, true);
        }
    }

    /**
     * 设置状态栏图标白色主题
     */
    public static void setLightMode(Activity activity) {
        setLightMode(activity.getWindow());
    }

    /**
     * 设置状态栏图片黑色主题
     */
    public static void setDarkMode(Activity activity) {
        setDarkMode(activity.getWindow());
    }

    private static void setStatusBarDarkMode(Window window, boolean darkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (darkMode) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }

    private static void setMIUIStatusBarDarkMode(Window window, boolean darkMode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Class<? extends Window> clazz = window.getClass();
            try {
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(window, darkMode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
            }
        }
        setStatusBarDarkMode(window, darkMode);
    }

    private static void setFlymeStatusBarDarkMode(Window window, boolean darkMode) {
        FlymeStatusBarUtils.setStatusBarDarkIcon(window, darkMode);
    }

    private static final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010;

    private static void setOppoStatusBarDarkMode(Window window, boolean darkMode) {
        int vis = window.getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (darkMode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (darkMode) {
                vis |= SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            } else {
                vis &= ~SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            }
        }
        window.getDecorView().setSystemUiVisibility(vis);
    }

    /**
     * 设置状态栏颜色和透明度
     *
     * @param window
     * @param color
     * @param alpha
     */
    public static void setStatusBarColor(Window window, @ColorInt int color, int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(calculateStatusColor(color, alpha));
        }
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static int getNavBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 检测是否有虚拟导航栏
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * Return whether the navigation bar visible.
     * <p>Call it in onWindowFocusChanged will get right result.</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isNavBarVisible(Context context) {
        boolean isVisible = false;
        ViewGroup decorView = (ViewGroup) ContextUtils.getActivity(context).getWindow().getDecorView();
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = context
                        .getResources()
                        .getResourceEntryName(id);
                if ("navigationBarBackground".equals(resourceEntryName)
                        && child.getVisibility() == View.VISIBLE) {
                    isVisible = true;
                    break;
                }
            }
        }
        if (isVisible) {
            int visibility = decorView.getSystemUiVisibility();
            isVisible = (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
        }
        return isVisible;
    }

}
