package com.zpj.utils;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {

    public static void setViewPositiveSize(View target, int width, int height) {
        if (width <= 0 && height <= 0) return;
        ViewGroup.LayoutParams params = target.getLayoutParams();
        if (width > 0) params.width = width;
        if (height > 0) params.height = height;
        target.setLayoutParams(params);
    }

    public static void setViewSize(View target, int width, int height) {
        if (width < 0) {
            if (width != ViewGroup.LayoutParams.MATCH_PARENT && width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
        if (height < 0) {
            if (height != ViewGroup.LayoutParams.MATCH_PARENT && height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
        ViewGroup.LayoutParams params = target.getLayoutParams();
        params.width = width;
        params.height = height;
        target.setLayoutParams(params);
    }

    public static void setViewMargin(View target, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
        params.leftMargin = left;
        params.topMargin = top;
        params.rightMargin = right;
        params.bottomMargin = bottom;
        target.setLayoutParams(params);
    }

    public static void setViewMargins(View target, int margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
        params.leftMargin = margin;
        params.topMargin = margin;
        params.rightMargin = margin;
        params.bottomMargin = margin;
        target.setLayoutParams(params);
    }

    public static void setViewVerticalMargin(View target, int margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
        params.topMargin = margin;
        params.bottomMargin = margin;
        target.setLayoutParams(params);
    }

    public static void setViewHorizontalMargin(View target, int margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
        params.leftMargin = margin;
        params.rightMargin = margin;
        target.setLayoutParams(params);
    }

    public static boolean isInRect(float x, float y, Rect rect) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }

    public static boolean isInRectF(float x, float y, RectF rect) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }

}
