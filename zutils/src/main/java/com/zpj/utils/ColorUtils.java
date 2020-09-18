package com.zpj.utils;

import android.graphics.Color;
import android.util.Log;

public class ColorUtils {

    private static final float CONTRAST_LIGHT_ITEM_THRESHOLD = 1.5f;

    private ColorUtils() {

    }

    @Deprecated
    public static boolean isDarkColor(int color) {
        float a = getContrastForColor(color);
        return a >= CONTRAST_LIGHT_ITEM_THRESHOLD;
    }

    public static boolean isDarkenColor(int color) {
        return calculateLuminance(color) <= 0.5;
    }

    public static double calculateLuminance(int color) {
        return android.support.v4.graphics.ColorUtils.calculateLuminance(color);
    }

    private static float getContrastForColor(int color) {
        float bgR = Color.red(color) / 255f;
        float bgG = Color.green(color) / 255f;
        float bgB = Color.blue(color) / 255f;
        bgR = (bgR < 0.03928f) ? bgR / 12.92f : (float) Math.pow((bgR + 0.055f) / 1.055f, 2.4f);
        bgG = (bgG < 0.03928f) ? bgG / 12.92f : (float) Math.pow((bgG + 0.055f) / 1.055f, 2.4f);
        bgB = (bgB < 0.03928f) ? bgB / 12.92f : (float) Math.pow((bgB + 0.055f) / 1.055f, 2.4f);
        float bgL = 0.2126f * bgR + 0.7152f * bgG + 0.0722f * bgB;
        return Math.abs((1.05f) / (bgL + 0.05f));
    }

    public static int getDarkenedColor(int color, float darkenFraction) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= darkenFraction;
        return Color.HSVToColor(hsv);
    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    public static int darkenColor(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.2));
        green = (int) Math.floor(green * (1 - 0.2));
        blue = (int) Math.floor(blue * (1 - 0.2));
        Log.e("testcolor", red + "" + green + "" + blue);
        return Color.rgb(red, green, blue);
    }

    public static int lightColor(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 + 0.2));
        green = (int) Math.floor(green * (1 + 0.2));
        blue = (int) Math.floor(blue * (1 + 0.2));
        Log.e("testcolor", red + "" + green + "" + blue);
        return Color.rgb(red, green, blue);
    }

    /**
     * 合成指定颜色、指定不透明度的颜色，
     * 0:完全透明，1：不透明
     *
     * @param color
     * @param alpha 0:完全透明，1：不透明
     * @return
     */
    public static int alphaColor(int color, float alpha) {
        Log.d("alphaColor", "alpha=" + alpha);
//        if (alpha == 1.0f) {
//            alpha = 0.8f;
//        }
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & color;
        return a + rgb;
    }

    /**
     * 颜色渐变
     *
     * @param color1 起始颜色
     * @param color2 终止颜色
     * @param ratio 颜色变化频率 从0-1
     * @return 颜色值
     */
    public static int blendColors(int color1, int color2, float ratio) {
        float inverseRatio = 1.0F - ratio;
        float a = (float) Color.alpha(color1) * inverseRatio + (float) Color.alpha(color2) * ratio;
        float r = (float) Color.red(color1) * inverseRatio + (float) Color.red(color2) * ratio;
        float g = (float) Color.green(color1) * inverseRatio + (float) Color.green(color2) * ratio;
        float b = (float) Color.blue(color1) * inverseRatio + (float) Color.blue(color2) * ratio;
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

}
