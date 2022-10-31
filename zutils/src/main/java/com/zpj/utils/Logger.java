package com.zpj.utils;

import java.util.Locale;

public class Logger {

    private static final String TAG_PREFIX = "ZUtils_%s";
    private static final String TAG_PREFIX_OBJECT = "ZUtils_%s@%s";

    public static void v(Object tag, String messageTemplate, Object... args) {
        Throwable tr = getThrowableToLog(args);
        String message = formatLog(messageTemplate, tr, args);
        if (tr != null) {
            android.util.Log.v(normalizeTag(tag), message, tr);
        } else {
            android.util.Log.v(normalizeTag(tag), message);
        }
    }

    public static void d(Object tag, String messageTemplate, Object... args) {
        Throwable tr = getThrowableToLog(args);
        String message = formatLog(messageTemplate, tr, args);
        if (tr != null) {
            android.util.Log.d(normalizeTag(tag), message, tr);
        } else {
            android.util.Log.d(normalizeTag(tag), message);
        }
    }

    public static void i(Object tag, String messageTemplate, Object... args) {
        Throwable tr = getThrowableToLog(args);
        String message = formatLog(messageTemplate, tr, args);
        if (tr != null) {
            android.util.Log.i(normalizeTag(tag), message, tr);
        } else {
            android.util.Log.i(normalizeTag(tag), message);
        }
    }

    public static void w(Object tag, String messageTemplate, Object... args) {
        Throwable tr = getThrowableToLog(args);
        String message = formatLog(messageTemplate, tr, args);
        if (tr != null) {
            android.util.Log.w(normalizeTag(tag), message, tr);
        } else {
            android.util.Log.w(normalizeTag(tag), message);
        }
    }

    public static void e(Object tag, String messageTemplate, Object... args) {
        Throwable tr = getThrowableToLog(args);
        String message = formatLog(messageTemplate, tr, args);
        if (tr != null) {
            android.util.Log.e(normalizeTag(tag), message, tr);
        } else {
            android.util.Log.e(normalizeTag(tag), message);
        }
    }

    public static String normalizeTag(Object tag) {
        if (tag instanceof Class) {
            return String.format(TAG_PREFIX, ((Class<?>) tag).getSimpleName());
        }

        if (tag instanceof CharSequence) {
            return String.format(TAG_PREFIX, tag);
        }
        return String.format(TAG_PREFIX_OBJECT, tag.getClass().getSimpleName(),
                Integer.toHexString(tag.hashCode()));
    }

    private static String getTag(Object tag) {
        if (tag instanceof Class) {
            return ((Class<?>) tag).getSimpleName();
        }
        return String.valueOf(tag);
    }

    private static Throwable getThrowableToLog(Object[] args) {
        if (args == null || args.length == 0) return null;

        Object lastArg = args[args.length - 1];

        if (!(lastArg instanceof Throwable)) return null;
        return (Throwable) lastArg;
    }

    private static String formatLog(String messageTemplate, Throwable tr, Object... params) {
        if ((params != null) && ((tr == null && params.length > 0) || params.length > 1)) {
            messageTemplate = String.format(Locale.US, messageTemplate, params);
        }

        return messageTemplate;
    }

}
