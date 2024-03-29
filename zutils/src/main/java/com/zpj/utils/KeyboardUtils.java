package com.zpj.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    private static final String TAG = "KeyboardUtil";

    private static final int KEYBOARD_RETRY_ATTEMPTS = 10;
    private static final long KEYBOARD_RETRY_DELAY_MS = 100;

    /**
     * Shows the software keyboard if necessary.
     * @param view The currently focused {@link View}, which would receive soft keyboard input.
     */
    public static void showKeyboard(final View view) {
//        final Handler handler = new Handler();
//        final AtomicInteger attempt = new AtomicInteger();
//        Runnable openRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // Not passing InputMethodManager.SHOW_IMPLICIT as it does not trigger the
//                // keyboard in landscape mode.
//                InputMethodManager imm =
//                        (InputMethodManager) view.getContext().getSystemService(
//                                Context.INPUT_METHOD_SERVICE);
//                // Third-party touches disk on showSoftInput call. http://crbug.com/619824,
//                // http://crbug.com/635118
//                StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
//                try {
//                    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
//                } catch (IllegalArgumentException e) {
//                    if (attempt.incrementAndGet() <= KEYBOARD_RETRY_ATTEMPTS) {
//                        handler.postDelayed(this, KEYBOARD_RETRY_DELAY_MS);
//                    } else {
//                        Log.e(TAG, "Unable to open keyboard.  Giving up.", e);
//                    }
//                } finally {
//                    StrictMode.setThreadPolicy(oldPolicy);
//                }
//            }
//        };
//        openRunnable.run();
        if (view == null || view.getContext() == null) return;
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 200L);
    }

    /**
     * Hides the keyboard.
     * @param focusView The {@link View} that is currently accepting input.
     * @return Whether the keyboard was visible before.
     */
    public static void hideSoftInputKeyboard(View focusView) {
//        InputMethodManager imm =
//                (InputMethodManager) view.getContext().getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (focusView == null || focusView.getContext() == null) return;
        InputMethodManager imm = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);

//        if (focusView != null) {
//            IBinder binder = focusView.getWindowToken();
//            if (binder != null) {
//                InputMethodManager imd = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imd.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_IMPLICIT_ONLY);
//            }
//        }
    }

    public static void showSoftInputKeyboard(Context context, View focusView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示软键盘
     */
    public static void showKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus()
                    .getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
    }

    public static void showKeyBoard(Context context, View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.findFocus();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
