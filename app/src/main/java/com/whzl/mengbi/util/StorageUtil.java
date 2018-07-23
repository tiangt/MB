package com.whzl.mengbi.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;

/**
 * 存储 相关 utils
 * @author shaw
 */
public class StorageUtil {

    public static File getCacheDir(Context ctx) {
        File file = ctx.getExternalCacheDir();
        if (file == null) {
            file = ctx.getCacheDir();
        }
        return file;
    }

    public static File getImgFileDir(Context ctx) {
        File file = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (file == null) {
            file = ctx.getFilesDir();
        }
        return file;
    }

    public static File getTempDir() {
        File file = Environment.getExternalStorageDirectory();
        File tempDir = new File(file.getAbsoluteFile() + "/mengbi/temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        return tempDir;
    }

    public static File getDownloadDir() {
        String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/mengbi/downloads/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getLogDir() {
        String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/mengbi/log/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static long getAvailaleSize() {
        File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
        long availableBlocks;

        // 由于API18（Android4.3）以后getBlockSize过时并且改为了getBlockSizeLong
        // 因此这里需要根据版本号来使用那一套API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return blockSize * availableBlocks;
    }

    public static String getAvailaleSizeStr() {
        long size = getAvailaleSize();
        return formatSize(size);
    }

    public static String formatSize(long size) {
        float sizeK = size / 1024;
        if (sizeK < 1000) {
            return String.format("%.2f", sizeK) + "K";
        }
        float sizeM = sizeK / 1024;
        if (sizeM < 1000) {
            return String.format("%.2f", sizeM) + "M";
        }
        float sizeG = sizeM / 1024;
        return String.format("%.2f", sizeG) + "G";

    }

    public static long getAllSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long totalBlocks;
        long blockSize;
        // 由于API18（Android4.3）以后getBlockSize过时并且改为了getBlockSizeLong
        // 因此这里需要根据版本号来使用那一套API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
        } else {
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
        }
        return totalBlocks * blockSize;
    }

    public static String getAllSizeStr() {
        return formatSize(getAllSize());
    }

    public static long getFolderSize(File cacheFile) {
        long size = 0;
        try {
            File[] fileList = cacheFile.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static long getTotalCacheSize(Context context) {
        long cacheSize = 0;
            cacheSize = getFolderSize(context.getCacheDir());
            cacheSize += getFolderSize(context.getFilesDir());
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File externalCacheDir = context.getExternalCacheDir();
                if (externalCacheDir != null) {
                    cacheSize += getFolderSize(externalCacheDir);
                }
                File folderFile = context.getExternalFilesDir(null);
                if (folderFile != null) {
                    cacheSize += getFolderSize(folderFile);
                }
            }
            return cacheSize;
    }

    public static String getTotalCatchStr(Context context){
        return formatSize(getTotalCacheSize(context)/2);
    }

    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 如果下面还有文件
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteAllCache(Context context) {
        deleteFolderFile(context.getCacheDir().getPath(), true);
        deleteFolderFile(context.getFilesDir().getPath(), true);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                deleteFolderFile(externalCacheDir.getPath(), true);
            }
            File folderFile = context.getExternalFilesDir(null);
            if (folderFile != null) {
                deleteFolderFile(folderFile.getPath(), true);
            }

        }
    }
}
