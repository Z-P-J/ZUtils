package com.zpj.utils;

import android.app.Application;
import androidx.core.content.FileProvider;

public final class ZUtilsFileProvider extends FileProvider {

    @Override
    public boolean onCreate() {
        ContextUtils.init((Application) getContext());
        return super.onCreate();
    }

}
