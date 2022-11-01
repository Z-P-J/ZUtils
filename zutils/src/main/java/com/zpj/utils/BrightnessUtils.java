package com.zpj.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import androidx.annotation.FloatRange;
import android.view.WindowManager;

import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;

public final class BrightnessUtils {

    private static final String TAG = "brightness_manager";
    private final static String SYSTEM_BRIGHTNESS = "_z_utils_system_brightness";
    private static final String APP_BRIGHTNESS = "_z_utils_app_brightness";

    private BrightnessUtils() {

    }

    @FloatRange(from = 0, to = 100)
    public static float getSystemBrightness(Context context) {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (float) screenBrightness / 255 * 100;
    }

    @FloatRange(from = 0, to = 100)
    public static float getAppBrightness(Context context) {
        return PrefsHelper.with(TAG).getFloat(APP_BRIGHTNESS, getSystemBrightness(context));
    }

    public static void setBrightness(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (PrefsHelper.with(TAG).getBoolean(SYSTEM_BRIGHTNESS, true)) {
            lp.screenBrightness = BRIGHTNESS_OVERRIDE_NONE;
        } else {
            float brightness = PrefsHelper.with(TAG).getFloat(APP_BRIGHTNESS, getSystemBrightness(activity));
            lp.screenBrightness = brightness / 100;
        }
        activity.getWindow().setAttributes(lp);
    }

    public static void setBrightness(Activity activity, @FloatRange(from = 0, to = 100) float brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = brightness / 100;
        PrefsHelper.with(TAG).putFloat(APP_BRIGHTNESS, brightness);
        activity.getWindow().setAttributes(lp);
    }

    public static void setAutoBrightness(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = BRIGHTNESS_OVERRIDE_NONE;
        activity.getWindow().setAttributes(lp);
        PrefsHelper.with(TAG).putBoolean(SYSTEM_BRIGHTNESS, true);
    }

    public static boolean isSystemBrightness() {
        return PrefsHelper.with(TAG).getBoolean(SYSTEM_BRIGHTNESS, false);
    }

    public static void setSystemBrightness(boolean isSystemBrightness) {
        PrefsHelper.with(TAG).putBoolean(SYSTEM_BRIGHTNESS, isSystemBrightness);
    }

    /**
     * 获得当前屏幕亮度的模式
     *
     * @return 1 为自动调节屏幕亮度,0 为手动调节屏幕亮度,-1 获取失败
     */
    public static int getBrightnessMode(Context context) {
        int mode = -1;
        try {
            mode = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return mode;
    }

}
