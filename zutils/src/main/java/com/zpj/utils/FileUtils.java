package com.zpj.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileUtils {

    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteFile(String filename) {
        return new File(filename).delete();
    }

    public static void deleteFileByDirectory(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    public static boolean isFileExist(String filePath) {
        return new File(filePath).exists();
    }

    public static boolean writeFile(String filename, String content, boolean append) {
        boolean isSuccess = false;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(filename, append));
            bufferedWriter.write(content);
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(bufferedWriter);
        }
        return isSuccess;
    }

    public static String readFile(String filename) {
        File file = new File(filename);
        BufferedReader bufferedReader = null;
        String str = null;
        try {
            if (file.exists()) {
                bufferedReader = new BufferedReader(new FileReader(filename));
                str = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(bufferedReader);
        }
        return str;
    }

    public static StringBuilder readFile(File file, String charsetName) {
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            closeIO(reader);
        }
    }

    public static void copyFile(InputStream in, OutputStream out) {
        try {
            byte[] b = new byte[2 * 1024 * 1024]; //2M memory
            int len = -1;
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(in, out);
        }
    }

    public static void copyFileFast(File in, File out) {
        FileChannel filein = null;
        FileChannel fileout = null;
        try {
            filein = new FileInputStream(in).getChannel();
            fileout = new FileOutputStream(out).getChannel();
            filein.transferTo(0, filein.size(), fileout);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(filein, fileout);
        }
    }

    public static void shareFile(Context context, String title, String filePath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.parse("file://" + filePath);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void zip(InputStream is, OutputStream os) {
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(os);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                gzip.write(buf, 0, len);
                gzip.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(is, gzip);
        }
    }

    public static void unzip(InputStream is, OutputStream os) {
        GZIPInputStream gzip = null;
        try {
            gzip = new GZIPInputStream(is);
            byte[] buf = new byte[1024];
            int len;
            while ((len = gzip.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(gzip, os);
        }
    }

    public static String formatFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    public static String formatFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format(Locale.ENGLISH, "%.1fGB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0fMB" : "%.1fMB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0fKB" : "%.1fKB", f);
        } else
            return String.format(Locale.ENGLISH, "%dB", size);
    }

    public static void Stream2File(InputStream is, File file) {
        byte[] b = new byte[1024];
        int len;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(is, os);
        }
    }

    public static boolean createFolder(String filePath) {
        return createFolder(filePath, false);
    }

    public static boolean createFolder(String filePath, boolean recreate) {
        String folderName = getFolderName(filePath);
        if (folderName == null || folderName.length() == 0 || folderName.trim().length() == 0) {
            return false;
        }
        File folder = new File(folderName);
        if (folder.exists()) {
            if (recreate) {
                deleteFile(folderName);
                return folder.mkdirs();
            } else {
                return true;
            }
        } else {
            return folder.mkdirs();
        }
    }

    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    public static long getFileSize(String filepath) {
        if (TextUtils.isEmpty(filepath)) {
            return -1;
        }
        File file = new File(filepath);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    public static boolean rename(String filepath, String newName) {
        File file = new File(filepath);
        return file.exists() && file.renameTo(new File(newName));
    }

    public static String getFolderName(String filePath) {
        if (filePath == null || filePath.length() == 0 || filePath.trim().length() == 0) {
            return filePath;
        }
        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos == -1) ? "" : filePath.substring(0, filePos);
    }

    public static ArrayList<File> getFilesArray(String path) {
        File file = new File(path);
        File files[] = file.listFiles();
        ArrayList<File> listFile = new ArrayList<File>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    listFile.add(files[i]);
                }
                if (files[i].isDirectory()) {
                    listFile.addAll(getFilesArray(files[i].toString()));
                }
            }
        }
        return listFile;
    }

    public static boolean deleteFiles(String folder) {
        if (folder == null || folder.length() == 0 || folder.trim().length() == 0) {
            return true;
        }
        File file = new File(folder);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    public static void openImage(Context mContext, String imagePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(imagePath));
        intent.setDataAndType(uri, "image/*");
        mContext.startActivity(intent);
    }

    public static void openVideo(Context mContext, String videoPath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(videoPath));
        intent.setDataAndType(uri, "video/*");
        mContext.startActivity(intent);
    }

    public static void openURL(Context mContext, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

    public static void downloadFile(Context context, String fileurl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileurl));
        request.setDestinationInExternalPublicDir("/Download/", fileurl.substring(fileurl.lastIndexOf("/") + 1));
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }


    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String getAppExternalPath(Context context) {
/*        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append("Android/data/");
        sb.append(packageName);
        return sb.toString();*/
        return context.getObbDir().getAbsolutePath();
    }

    @Deprecated
    public static String getExtraPath(String folder) {
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder;
        File file = new File(storagePath);
        if (!file.exists()) {
            file.mkdir();
        }
        return storagePath;
    }

    public static String getCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String readAssetFile(Context context, String str) throws IOException {
        StringBuilder str2 = new StringBuilder();
        try (InputStream inputStream = context.getAssets().open(str)) {
            byte[] bArr = new byte[8092];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                str2.append(new String(bArr, 0, read));
            }
            return str2.toString();
        }
    }

    public static boolean saveBitmap(Bitmap bm, String picName) {
        try {
            File f = new File(picName);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }










    public enum FileType {
        VIDEO,
        MUSIC,
        IMAGE,
        EBOOK,
        ARCHIVE,
        HTML,
        TXT,
        APK,
        TORRENT,
        PDF,
        PPT,
        DOC,
        SWF,
        CHM,
        XLS,
        UNKNOWN
    }

    private static final String[] IMG = {".bmp", ".jpg", ".jpeg", ".png", ".tiff", ".gif", ".pcx", ".tga", ".exif", ".fpx", ".svg", ".psd",
            ".cdr", ".pcd", ".dxf", ".ufo", ".eps", ".ai", ".raw", ".wmf"};
    private static final String[] VIDEO = {".mp4", ".avi", ".mov", ".wmv", ".asf", ".navi", ".3gp", ".mkv", ".f4v", ".rmvb", ".webm", ".flv", ".rm", ".ts", ".vob", ".m2ts"};
    private static final String[] MUSIC = {".mp3", ".wma", ".wav", ".mod", ".ra", ".cd", ".md", ".asf", ".aac", ".vqf", ".ape", ".mid", ".ogg", ".m4a", ".vqf", ".flac", ".midi"};
    private static final String[] ARCHIVE = {".zip", ".rar", ".7z", ".iso", ".tar", ".gz", ".bz"};
    private static final String[] EBOOK = {".epub", ".umb", ".wmlc", ".pdb", ".mdx", ".xps"};


    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".z_utils_file_provider";
    }

    public static String getFileProviderName() {
        return getFileProviderName(ContextUtils.getApplicationContext());
    }

    public static FileType getFileType(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".torrent")) {
            return FileType.TORRENT;
        } else if (fileName.endsWith(".txt")) {
            return FileType.TXT;
        } else if (fileName.endsWith(".apk")) {
            return FileType.APK;
        } else if (fileName.endsWith(".pdf")) {
            return FileType.PDF;
        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return FileType.DOC;
        } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
            return FileType.PPT;
        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            return FileType.XLS;
        } else if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return FileType.HTML;
        } else if (fileName.endsWith(".swf")) {
            return FileType.SWF;
        } else if (fileName.endsWith(".chm")) {
            return FileType.CHM;
        }

        for (String s : IMG) {
            if (fileName.endsWith(s)) {
                return FileType.IMAGE;
            }
        }
        for (String s : VIDEO) {
            if (fileName.endsWith(s)) {
                return FileType.VIDEO;
            }
        }
        for (String s : ARCHIVE) {
            if (fileName.endsWith(s)) {
                return FileType.ARCHIVE;
            }
        }
        for (String s : MUSIC) {
            if (fileName.endsWith(s)) {
                return FileType.MUSIC;
            }
        }
        for (String s : EBOOK) {
            if (fileName.endsWith(s)) {
                return FileType.EBOOK;
            }
        }
        return FileType.UNKNOWN;
    }

    public static String getExtension(String path) {
        return path.substring(path.indexOf('.')+1).toLowerCase();
    }

    public static void openFile(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, getFileProviderName(context), file);

            context.grantUriPermission(context.getPackageName(), contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, getMIMEType(file));
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, getMIMEType(file));
        }
        context.startActivity(intent);
    }

    public static boolean openFile(Context context, String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            openFile(context, file);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file the file
     */
    public static String getMIMEType(File file) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        if (!TextUtils.isEmpty(extension)) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return type;
    }

    public static long getAvailableSize() {
        String sdcard = Environment.getExternalStorageState();
        String state = Environment.MEDIA_MOUNTED;
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        if(sdcard.equals(state)) {
            //获得Sdcard上每个block的size
            long blockSize = statFs.getBlockSizeLong();
            //获取可供程序使用的Block数量
            long blockavailable = statFs.getAvailableBlocksLong();
            //计算标准大小使用：1024，当然使用1000也可以
            return blockSize * blockavailable;
        } else {
            return -1;
        }
    }

    public static void writeToFile(String fileName, String content) {
        writeToFile(fileName, content.getBytes(StandardCharsets.UTF_8));
    }

    private static void writeToFile(String fileName, byte[] content) {
        File f = new File(fileName);

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream opt = new FileOutputStream(f, false);
            opt.write(content, 0, content.length);
            opt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(String file) {
        try {
            File f = new File(file);

            if (!f.exists() || !f.canRead()) {
                return null;
            }

            StringBuilder sb = new StringBuilder();
            FileInputStream fileInputStream = new FileInputStream(f);

            BufferedInputStream ipt = new BufferedInputStream(fileInputStream);

            byte[] buf = new byte[512];


            while (ipt.available() > 0) {
                int len = ipt.read(buf, 0, 512);
                sb.append(new String(buf, 0, len, StandardCharsets.UTF_8));
            }

            ipt.close();
            fileInputStream.close();

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }


    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }


    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
