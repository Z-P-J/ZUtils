package com.zpj.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

public class ZUtils {
//    private static ZUtils zUtils;
//    private final WeakReference<Context> weakReference;

    private static Application INSTANCE;

    static {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            final Object obj = new Object();
            synchronized (obj) {
                try {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            getApplication();
                            obj.notify();
                        }
                    });
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            getApplication();
        }
    }

    public static Application getApplication() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {

            }
        } finally {
            INSTANCE = app;
        }
        return app;
    }

//    private ZUtils(Context context) {
////        this.context = context;
//        weakReference = new WeakReference<>(context);
//    }

//    public static void init(Context context) {
//        if (zUtils == null) {
//            synchronized (ZUtils.class) {
//                if (zUtils == null) {
//                    zUtils = new ZUtils(context);
//                }
//            }
//        }
//    }

//    public static ZUtils getInstance() {
//        if (zUtils == null) {
//            throw new RuntimeException("You must init ZUtils in your application!");
//        }
//        return zUtils;
//    }
//
//    public Context getContext() {
//        if (weakReference.get() == null) {
//            return INSTANCE.getApplicationContext();
//        } else {
//            return weakReference.get();
//        }
//    }

    public static Context getApplicationContext() {
        return INSTANCE.getApplicationContext();
    }


//    private ZUtils(Application context) {
//        this.application = context;
//    }
//
//    public static void init(Application context) {
//        if (zUtils == null) {
//            synchronized (ZUtils.class) {
//                if (zUtils == null) {
//                    zUtils = new ZUtils(context);
//                }
//            }
//        }
//    }
//
//    public static void init(Activity context) {
//        if (zUtils == null) {
//            synchronized (ZUtils.class) {
//                if (zUtils == null) {
//                    zUtils = new ZUtils(context.getApplication());
//                }
//            }
//        }
//    }
//
//    public static ZUtils getInstance() {
//        if (zUtils == null) {
//            throw new RuntimeException("You must init ZUtils in your application!");
//        }
//        return zUtils;
//    }
//
//    public Context getContext() {
//        return application.getApplicationContext();
//    }

}
