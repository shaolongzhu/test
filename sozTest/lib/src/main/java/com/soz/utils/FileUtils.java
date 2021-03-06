package com.soz.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件辅助类
 * Created by zhushaolong on 9/5/16.
 */
public class FileUtils {
    private FileUtils() {
    }

    /**
     * 把 assets 目录下的文件复制到 data/data/files 目录下
     * @param context
     * @param sourceName
     */
    public static void extractAssets(Context context, String sourceName) {
        AssetManager assetManager = context.getAssets();
        InputStream io = null;
        FileOutputStream fos = null;
        try {
            io = assetManager.open(sourceName);
            File file = context.getFileStreamPath(sourceName);
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = io.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSilently(io);
            closeSilently(fos);
        }
    }

    private static void closeSilently(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 基本目录 /data/data/<package>/files/plugin/
     * @param context
     * @return
     */
    private static File getPluginBaseDir(Context context) {
        File file = context.getFileStreamPath("plugin");
        return enforceDirExists(file);
    }

    private static synchronized File enforceDirExists(File baseDir) {
        if (!baseDir.exists()) {
            boolean ret = baseDir.mkdir();
            if (!ret) {
                throw new RuntimeException("create dir " + baseDir + "failed");
            }
        }
        return baseDir;
    }

    public static File getPluginOptDexDir(Context context) {
        return enforceDirExists(new File(getPluginBaseDir(context), "odex"));
    }

    public static File getPluginLibDir(Context context) {
        return enforceDirExists(new File(getPluginBaseDir(context), "lib"));
    }
}
