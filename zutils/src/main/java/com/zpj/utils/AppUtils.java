package com.zpj.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Create by h4de5ing 2016/5/18 018
 */
public class AppUtils {

    public static String getAppName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        String appName = null;
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            appName = String.valueOf(pm.getApplicationLabel(applicationInfo));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    public static String getAppName(Context context) {
        return getAppName(context, context.getPackageName());
    }

    public static String getAppName(String packageName) {
        return getAppName(ContextUtils.getApplicationContext(), packageName);
    }

    public static String getAppName() {
        return getAppName(ContextUtils.getApplicationContext());
    }


    public static Drawable getAppIcon(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Drawable appIcon = null;
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            appIcon = applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appIcon;
    }

    public static Drawable getAppIcon(String packageName) {
        return getAppIcon(ContextUtils.getApplicationContext(), packageName);
    }

    public static Drawable getAppIcon(Context context) {
        return getAppIcon(context, context.getPackageName());
    }

    public static Drawable getAppIcon() {
        return getAppIcon(ContextUtils.getApplicationContext());
    }

    public static Drawable getApkIcon(Context context, String apkFilePath) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        if (packageInfo == null) {
            return null;
        }

        packageInfo.applicationInfo.sourceDir = apkFilePath;
        packageInfo.applicationInfo.publicSourceDir = apkFilePath;

        Drawable drawable = null;
        try {
            drawable = packageManager.getApplicationIcon(packageInfo.applicationInfo);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static long getAppFirstInstallTime(Context context, String packageName) {
        long lastUpdateTime = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            lastUpdateTime = packageInfo.firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return lastUpdateTime;
    }

    public static long getAppFirstInstallTime(String packageName) {
        return getAppFirstInstallTime(ContextUtils.getApplicationContext(), packageName);
    }

    public static long getAppFirstInstallTime(Context context) {
        return getAppFirstInstallTime(context, context.getPackageName());
    }

    public static long getAppFirstInstallTime() {
        return getAppFirstInstallTime(ContextUtils.getApplicationContext());
    }

    public static long getAppLastUpdateTime(Context context, String packageName) {
        long lastUpdateTime = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            lastUpdateTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return lastUpdateTime;
    }


    public static long getAppSize(Context context, String packageName) {
        File file = getAppSourceFile(context, packageName);
        if (file != null) {
            return file.length();
        }
        return 0;
    }

    public static String getAppSourceDir(Context context, String packageName) {
        String sourceDir = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            sourceDir = applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return sourceDir;
    }

    public static File getAppSourceFile(String packageName) {
        return getAppSourceFile(ContextUtils.getApplicationContext(), packageName);
    }

    public static File getAppSourceFile(Context context, String packageName) {
        String sourceDir = getAppSourceDir(context, packageName);
        if (sourceDir != null) {
            return new File(sourceDir);
        }
        return null;
    }

    public static String getAppVersionName(Context context, String packageName) {
        String appVersion = null;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    public static String getApkVersionName(Context context, String filePath){
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getAppVersionCode(Context context, String packageName) {
        int appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            appVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionCode;
    }

    public static int getApkVersionCode(Context context, String filePath){
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getAppInstaller(Context context, String packageName) {
        return context.getPackageManager().getInstallerPackageName(packageName);
    }

    public static String getAppSignatureMD5(Context context, String pkgName) {
        return getAppSignature(context, pkgName, "MD5");
    }

    public static String getAppSignatureSHA1(Context context, String pkgName) {
        return getAppSignature(context, pkgName, "SHA1");
    }

    public static String getAppSignatureSHA256(Context context, String pkgName) {
        return getAppSignature(context, pkgName, "SHA256");
    }

    public static String getAppSignature(Context context, String pkgName, String algorithm) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(
                    pkgName, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = CipherUtils.hexDigest(sign.toByteArray(), algorithm);
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppSignature(Context context, String packageName) {
        return getAppSignatureMD5(context, packageName);
    }

    public static String getApkSignatureMD5(Context context, String apkPath) throws Exception {
        byte[] bytes = getSignaturesFromApk(context, apkPath);
        return CipherUtils.hexDigest(bytes, "MD5");
    }

    public static String getApkSignatureSHA1(Context context, String apkPath) throws Exception {
        byte[] bytes = getSignaturesFromApk(context, apkPath);
        return CipherUtils.hexDigest(bytes, "SHA1");
    }

    public static String getApkSignatureSHA256(Context context, String apkPath) throws Exception {
        byte[] bytes = getSignaturesFromApk(context, apkPath);
        return CipherUtils.hexDigest(bytes, "SHA256");
    }

    public static byte[] getSignaturesFromApk(Context context, String apkPath) throws IOException {
        Log.d("getSignaturesFromApk", "apkPath=" + apkPath);
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_SIGNATURES);
            Signature[] signatures = info.signatures;
            return signatures[0].toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static int getAppTargetSdkVersion(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            return applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getAppUid(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            return applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }

            });
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static boolean getRootPermission(Context context) {
        String packageCodePath = context.getPackageCodePath();
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + packageCodePath;
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static String[] getAppPermissions(Context context, String packname) {
        String[] requestedPermissions = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            requestedPermissions = info.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return requestedPermissions;
    }

    public static boolean hasPermission(Context context, String permission) {
        if (context != null && !TextUtils.isEmpty(permission)) {
            try {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    if (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(permission, context
                            .getPackageName())) {
                        return true;
                    }
                    Log.d("AppUtils", "Have you  declared permission " + permission + " in AndroidManifest.xml ?");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean isInstalled(Context context, String packageName) {
        boolean installed = false;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo in : installedApplications) {
            if (packageName.equals(in.packageName)) {
                installed = true;
                break;
            } else {
                installed = false;
            }
        }
        return installed;
    }

    public static boolean isApkInstalled(Context context, String packgeName){
        boolean isInstalled;
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packgeName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (Exception e) {
            e.printStackTrace();
            isInstalled = false;
        }
        return isInstalled;
    }

//    @Deprecated
//    public static boolean installApk(Context context, String filePath) {
//        File file = new File(filePath);
//        if (!file.exists() || !file.isFile() || file.length() <= 0) {
//            return false;
//        }
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);
//        return true;
//    }

    public static void installApk(Context context, String path) {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //设置intent的Action属性
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        //获取文件file的MIME类型
//        String type = "application/vnd.android.package-archive";
//        //设置intent的data和Type属性。
//        intent.setDataAndType(/*uri*/Uri.fromFile(new File(path)), type);
//        //跳转
//        context.startActivity(intent);
////        context.startActivityForResult(intent, INSTALL_REQUEST_CODE);
        FileUtils.openFile(context, new File(path));
    }

    @Deprecated
    public static boolean uninstallApk(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    public static void uninstallApp(Context context, String packageName) {
        Activity activity = ContextUtils.getActivity(context);
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        activity.startActivity(intent);
    }

    public static void shareApk(String path) {
        shareApk(ContextUtils.getApplicationContext(), path);
    }

    public static void shareApk(Context context, String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享应用"));
    }

    public static boolean isSystemApp(Context context, String packageName) {
        boolean isSys = false;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            if (applicationInfo != null && (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                isSys = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            isSys = false;
        }
        return isSys;
    }

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    public static boolean stopRunningService(Context context, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = context.stopService(intent_service);
        }
        return ret;
    }


    @SuppressLint("MissingPermission")
    public static void killProcesses(Context context, int pid, String processName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName;
        try {
            if (!processName.contains(":")) {
                packageName = processName;
            } else {
                packageName = processName.split(":")[0];
            }
            activityManager.killBackgroundProcesses(packageName);
            Method forceStopPackage = activityManager.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(activityManager, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String runScript(String script) {
        String sRet;
        try {
            final Process m_process = Runtime.getRuntime().exec(script);
            final StringBuilder sbread = new StringBuilder();
            Thread tout = new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(m_process.getInputStream()),
                            8192);
                    String ls_1;
                    try {
                        while ((ls_1 = bufferedReader.readLine()) != null) {
                            sbread.append(ls_1).append("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            tout.start();

            final StringBuilder sberr = new StringBuilder();
            Thread terr = new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(m_process.getErrorStream()),
                            8192);
                    String ls_1;
                    try {
                        while ((ls_1 = bufferedReader.readLine()) != null) {
                            sberr.append(ls_1).append("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            terr.start();

            m_process.waitFor();
            while (tout.isAlive()) {
                Thread.sleep(50);
            }
            if (terr.isAlive())
                terr.interrupt();
            String stdout = sbread.toString();
            String stderr = sberr.toString();
            sRet = stdout + stderr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sRet;
    }

    public static void runApp(Context context, String packagename) {
        context.startActivity(new Intent(context.getPackageManager().getLaunchIntentForPackage(packagename)));
    }

    public static void runApp(String packagename) {
        runApp(ContextUtils.getApplicationContext(), packagename);
    }

    @Deprecated
    public static void cleanCache(Context context) {
        FileUtils.deleteFileByDirectory(context.getCacheDir());
    }

    @Deprecated
    public static void cleanDatabases(Context context) {
        String filepath = String.format(String.format(context.getFilesDir().getParent() + File.separator + "%s", "databases"));
        FileUtils.deleteFileByDirectory(new File(filepath));
    }

    @Deprecated
    public static void cleanSharedPreference(Context context) {
        String filepath = String.format(String.format(context.getFilesDir().getParent() + File.separator + "%s", "shared_prefs"));
        FileUtils.deleteFileByDirectory(new File(filepath));
    }
}
