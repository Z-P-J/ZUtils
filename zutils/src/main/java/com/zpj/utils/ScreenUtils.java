package com.zpj.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

public class ScreenUtils {

    public static float dp2px(float dp) {
        return dp2px(ContextUtils.getApplicationContext(), dp);
    }

    public static float px2dp(float px) {
        return px2dp(ContextUtils.getApplicationContext(), px);
    }

    public static float density() {
        return density(ContextUtils.getApplicationContext());
    }


    public static int dp2pxInt(float dp) {
        return dp2pxInt(ContextUtils.getApplicationContext(), dp);
    }

    public static float px2dpInt(float px) {
        return px2dpInt(ContextUtils.getApplicationContext(), px);
    }


    public static int sp2px(float spValue) {
        return sp2px(ContextUtils.getApplicationContext(), spValue);
    }

    public static int px2sp(float pxValue) {
        return px2sp(ContextUtils.getApplicationContext(), pxValue);
    }






    public static float dp2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * density(context);
    }

    public static float px2dp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / density(context);
    }

    public static float density(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }


    public static int dp2pxInt(Context context, float dp) {
        return (int) (dp2px(context, dp) + 0.5f);
    }

    public static float px2dpInt(Context context, float px) {
        return (int) (px2dp(context, px) + 0.5f);
    }


    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

//    public static DisplayMetrics getDisplayMetrics(Context context) {
////        Activity activity;
////        if (!(context instanceof Activity) && context instanceof ContextWrapper) {
////            activity = (Activity) ((ContextWrapper) context).getBaseContext();
////        } else {
////            activity = (Activity) context;
////        }
//        Activity activity = ContextUtils.getActivity(context);
//        DisplayMetrics metrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        return metrics;
//    }

    /**
     * 获取屏幕大小
     *
     * @param context
     * @return
     */
//    public static int[] getScreenPixelSize(Context context) {
//        DisplayMetrics metrics = getDisplayMetrics(context);
//        return new int[]{metrics.widthPixels, metrics.heightPixels};
//    }

    public static int[] getScreenSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int[] getScreenSize() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

//    /**
//     * 获取屏幕的宽度（单位：px）
//     *
//     * @return 屏幕宽px
//     */
//    public static int getScreenWidth(Context context) {
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
//        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
//        return dm.widthPixels;
//    }
//
//    /**
//     * 获取屏幕的高度（单位：px）
//     *
//     * @return 屏幕高px
//     */
//    public static int getScreenHeight(Context context) {
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
//        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
//        return dm.heightPixels;
//    }

//    public static int getStatusBarHeight(Context context) {
//        int statusBarHeight = 0;
//        try {
//            Class<?> c = Class.forName("com.android.internal.R$dimen");
//            Object obj = c.newInstance();
//            Field field = c.getField("status_bar_height");
//            int x = Integer.parseInt(field.getString(obj).toString());
//            statusBarHeight = context.getResources().getDimensionPixelSize(x);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return statusBarHeight;
//    }

    public static int getStatusBarHeight(Context context) {
        return StatusBarUtils.getStatusBarHeight(context);
    }

    public static int getStatusBarHeight() {
        return StatusBarUtils.getStatusBarHeight();
    }

    /**
     * 获取底部导航栏 (Navigation Bar) 高度
     */
    public static int getNavBarHeight(Context context) {
        return StatusBarUtils.getNavBarHeight(context);
    }

    public static int getNavBarHeight() {
        return StatusBarUtils.getNavBarHeight();
    }

    public static int getAppInScreenHeight(Context context) {
        return getScreenHeight(context) - getStatusBarHeight(context);
    }

    public static int getAppInScreenHeight() {
        return getScreenHeight() - getStatusBarHeight();
    }

//    /**
//     * 获得屏幕宽高pix
//     *
//     * @param context
//     * @return
//     */
//    public static int[] getScreen(Context context) {
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(displayMetrics);
//        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
//    }

    /***
     * 获得标题栏的高度pix
     *
     * @param activity
     * @return
     */
    public static int getTitleHeight(Activity activity) {
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleheight = contentTop - getStatusBarHeight(activity);
        return titleheight;
    }


    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int[] screen = getScreenSize(activity);
        int width = screen[0];
        int height = screen[1];
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int[] screen = getScreenSize(activity);
        int width = screen[0];
        int height = screen[1];
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }


    /**
     * 设置屏幕为全屏
     * <p>需在 {@code setContentView} 之前调用</p>
     *
     * @param activity activity
     */
    public static void setFullScreen(final Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 判断是否横屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isLandscape() {
        return Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 设置屏幕为横屏
     * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
     * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
     *
     * @param activity activity
     */
    public static void setLandscape(final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void setLandscape(final Context context) {
        Activity activity = ContextUtils.getActivity(context);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 判断是否竖屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isPortrait() {
        return Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    public static void setPortrait(final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void setPortrait(final Context context) {
        Activity activity = ContextUtils.getActivity(context);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 获取屏幕旋转角度
     *
     * @param activity activity
     * @return 屏幕旋转角度
     */
    public static int getScreenRotation(final Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            default:
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
    }

    /**
     * 判断是否锁屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isScreenLock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    public static boolean isScreenLock() {
        return isScreenLock(ContextUtils.getApplicationContext());
    }

    /**
     * 获取进入休眠时长
     *
     * @return 进入休眠时长，报错返回-1
     */
    public static int getSleepDuration(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getSleepDuration() {
        return getSleepDuration(ContextUtils.getApplicationContext());
    }

    /**
     * 设置进入休眠时长
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
     *
     * @param duration 时长
     */
    public static void setSleepDuration(Context context, final int duration) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, duration);
    }

    public static void setSleepDuration(final int duration) {
        setSleepDuration(ContextUtils.getApplicationContext(), duration);
    }


}