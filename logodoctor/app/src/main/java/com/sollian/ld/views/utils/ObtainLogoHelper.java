package com.sollian.ld.views.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.sollian.ld.utils.FileUtil;
import com.sollian.ld.utils.LDUtil;

import java.io.File;

/**
 * Created by sollian on 2015/11/16.
 */
public class ObtainLogoHelper {
    public interface OnCutFinishListener {
        void onCutFinish(File cutFile);
    }

    private static final int REQUEST_TAKE_PHOTO = 0X1110;
    private static final int REQUEST_PICK_PHOTO = 0X1111;
    private static final int REQUEST_ZOOM_PHOTO = 0X1112;

    private Activity activity;
    private File cameraFile;
    private File cutFile;
    private OnCutFinishListener onCutFinishListener;

    public ObtainLogoHelper(Activity activity, OnCutFinishListener onCutFinishListener) {
        this.activity = activity;
        this.onCutFinishListener = onCutFinishListener;
    }

    public void goPickPhoto() {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        activity.startActivityForResult(getAlbum, REQUEST_PICK_PHOTO);
    }

    public void goTakePhoto() {
        cameraFile = genPhotoFile(FileUtil.DIR_CAMERA);

        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
        activity.startActivityForResult(cameraintent, REQUEST_TAKE_PHOTO);
    }

    public File genPhotoFile(String dir) {
        return new File(FileUtil.getDirectory(dir), FileUtil.genPhotoName());
    }

    public void startPhotoZoom(Uri uri) {
        cutFile = genPhotoFile(FileUtil.DIR_CUT);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);

        intent.putExtra("output", Uri.fromFile(cutFile));
        intent.putExtra("outputFormat", "JPEG");
        activity.startActivityForResult(intent, REQUEST_ZOOM_PHOTO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri;
            switch (requestCode & 0xffff) {
                case REQUEST_PICK_PHOTO:
                    //照片的原始资源地址
                    uri = data.getData();
                    if (uri == null) {
                        LDUtil.toast("图片获取失败");
                        return;
                    }
                    startPhotoZoom(uri);
                    break;
                case REQUEST_TAKE_PHOTO:
                    if (!cameraFile.exists()) {
                        LDUtil.toast("图片保存失败");
                        return;
                    }
                    startPhotoZoom(Uri.fromFile(cameraFile));
                    break;
                case REQUEST_ZOOM_PHOTO:
                    if (!cutFile.exists()) {
                        LDUtil.toast("图片保存失败");
                        return;
                    }
                    if (onCutFinishListener != null) {
                        onCutFinishListener.onCutFinish(cutFile);
                    }
//                    Bitmap bmp = BitmapFactory.decodeFile(cutFile.getAbsolutePath());
//                    if (bmp != null) {
//                        vImg.setImageBitmap(bmp);
//                    }
//                    uploadImg();
                    break;
            }
        } else {
            activity.finish();
        }
    }
}
