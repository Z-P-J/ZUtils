package com.zpj.utils;

import android.app.Application;
import android.support.v4.content.FileProvider;

public final class ZUtilsFileProvider extends FileProvider {

    @Override
    public boolean onCreate() {
        ContextUtils.init((Application) getContext());
        return super.onCreate();
    }

}
