package com.sollian.ld.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by sollian on 2015/9/6.
 */
public class FileUtil {

    /**
     * SD卡保存目录
     */
    private static final String ROOTDIR = "/LogoDoc/";
    // 拍照
    public static final String DIR_CAMERA = ROOTDIR + "CameraImg";
    //裁剪后的图片
    public static final String DIR_CUT = ROOTDIR + "CutImg";
    // SmartImageView、CircleImageView缓存图像存储地址
    public static final String DIR_IMG = ROOTDIR + "CacheImg";

    // 图片格式
    public static final Bitmap.CompressFormat BMP_FORMAT = Bitmap.CompressFormat.JPEG;
    public static final String BMP_SUFFIX = ".jpg";
    public static final int BMP_QUALITY = 80;

    @NonNull
    public static String getDirectory(String dirName) {
        if (dirName == null) {
            dirName = "";
        }
        String dir = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + dirName;// 获取根目录
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    @NonNull
    public static String genPhotoName() {
        String imgName = UUID.randomUUID().toString();
        imgName = imgName.replaceAll("-", "");
        imgName += BMP_SUFFIX;
        return imgName;
    }

    public static String getBmpNameFromUrl(String url) {
        return TextUtils.isEmpty(url) ? null : Base64.encodeToString(url.getBytes(), Base64.NO_PADDING | Base64.NO_WRAP)
            + BMP_SUFFIX;
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
