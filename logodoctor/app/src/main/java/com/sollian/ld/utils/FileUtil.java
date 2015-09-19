package com.sollian.ld.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by sollian on 2015/9/6.
 */
public class FileUtil {

    /**
     * SD卡保存目录
     */
    private static final String ROOTDIR = "/LogoDoc/";
    // 图片存储地址
    public static final String DIR_LARGEIMG = ROOTDIR + "LargeImg";
    // 拍照
    public static final String DIR_CAMERA = ROOTDIR + "CameraImg";
    // SmartImageView、CircleImageView缓存图像存储地址
    public static final String DIR_IMG = ROOTDIR + "CacheImg";

    // 图片格式
    public static final Bitmap.CompressFormat BMP_FORMAT = Bitmap.CompressFormat.JPEG;
    public static final String BMP_SUFFIX = ".jpg";
    public static final int BMP_QUALITY = 80;

    public static String getDirectory(String dirName) {
        String dir = null;
        if (dirName != null) {
            dir = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + dirName;// 获取根目录
        }
        return dir;
    }

    public static String getBmpNameFromUrl(String url) {
        String bmpName = null;
        if (url != null) {
            bmpName = Base64.encodeToString(url.getBytes(), Base64.NO_PADDING | Base64.NO_WRAP)
                + BMP_SUFFIX;
        }
        return bmpName;
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
