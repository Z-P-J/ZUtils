package com.zpj.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

public class ContextUtils {

    private static volatile Application INSTANCE;

//    static {
//        if (Looper.getMainLooper() != Looper.myLooper()) {
//            final Object obj = new Object();
//            synchronized (obj) {
//                try {
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            getApplication();
//                            obj.notify();
//                        }
//                    });
//                    obj.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            getApplication();
//        }
//    }
//
//    public static Application getApplication() {
//        if (INSTANCE != null) {
//            return INSTANCE;
//        }
//        Application app = null;
//        try {
//            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
//            if (app == null)
//                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
//        } catch (final Exception e) {
//            try {
//                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
//            } catch (final Exception ex) {
//
//            }
//        } finally {
//            INSTANCE = app;
//        }
//        return app;
//    }

    synchronized static void init(Application application) {
        INSTANCE = application;
    }

    public static Application getApplication() {
        return INSTANCE;
    }

    public static Context getApplicationContext() {
        return INSTANCE.getApplicationContext();
    }

    public static Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        } else {
            return ((Activity) ((ContextWrapper) context).getBaseContext());
        }
    }

}
