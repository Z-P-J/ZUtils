package com.zpj.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;

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
        return androidx.core.graphics.ColorUtils.calculateLuminance(color);
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
     * @param color RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    public static int darkenColor(int color) {
        return darkenColor(color, 0.2f);
    }

    public static int darkenColor(int color, float darkRatio) {
        int alpha = color >> 24;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        red = (int) Math.floor(red * (1 - darkRatio));
        green = (int) Math.floor(green * (1 - darkRatio));
        blue = (int) Math.floor(blue * (1 - darkRatio));
        Log.e("darkenColor", alpha + " " + red + " " + green + " " + blue);
        return Color.argb(alpha, red, green, blue);
    }

    public static int lightColor(int color) {
        return lightColor(color, 0.2f);
    }

    public static int lightColor(int color, float lightRatio) {
        int alpha = color >> 24;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        red = (int) Math.floor(red * (1 + lightRatio));
        green = (int) Math.floor(green * (1 + lightRatio));
        blue = (int) Math.floor(blue * (1 + lightRatio));
        Log.e("lightColor", alpha + " " + red + " " + green + " " + blue);
//        return Color.rgb(red, green, blue);
        return Color.argb(alpha, red, green, blue);
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

    /**
     * @author 小米Xylitol
     * @email xiaomi987@hotmail.com
     * @desc  低版本ArgbEvaluator计算有问题，直接使用新版
     * @date 2018-05-15 17:10
     */
    /**
     *  渐变色工具
     * @param fraction  滑动数值
     * @param startValue  开始颜色
     * @param endValue   结束颜色
     * @return
     */
    public static int evaluateArgb(float fraction, int startValue, int endValue) {
        float startA = ((startValue >> 24) & 0xff) / 255.0f;
        float startR = ((startValue >> 16) & 0xff) / 255.0f;
        float startG = ((startValue >>  8) & 0xff) / 255.0f;
        float startB = ( startValue        & 0xff) / 255.0f;

        float endA = ((endValue >> 24) & 0xff) / 255.0f;
        float endR = ((endValue >> 16) & 0xff) / 255.0f;
        float endG = ((endValue >>  8) & 0xff) / 255.0f;
        float endB = ( endValue        & 0xff) / 255.0f;

        // convert from sRGB to linear
        startR = (float) Math.pow(startR, 2.2);
        startG = (float) Math.pow(startG, 2.2);
        startB = (float) Math.pow(startB, 2.2);

        endR = (float) Math.pow(endR, 2.2);
        endG = (float) Math.pow(endG, 2.2);
        endB = (float) Math.pow(endB, 2.2);

        // compute the interpolated color in linear space
        float a = startA + fraction * (endA - startA);
        float r = startR + fraction * (endR - startR);
        float g = startG + fraction * (endG - startG);
        float b = startB + fraction * (endB - startB);

        // convert back to sRGB in the [0..255] range
        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
    }


    public static int getColorPrimary(Context context){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public static int getColorPrimaryDark(Context context){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    public static int getColorAccent(Context context){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

}
