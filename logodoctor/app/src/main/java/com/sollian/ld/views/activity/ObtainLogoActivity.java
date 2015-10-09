package com.sollian.ld.views.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sollian.ld.R;
import com.sollian.ld.business.local.LocalManager;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.models.User;
import com.sollian.ld.utils.FileUtil;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.utils.SharePrefUtil;
import com.sollian.ld.utils.http.FileUploadAsyncTask;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.views.titlebar.TitlebarHelper;

import java.io.File;

import customprogressview.ProgressDownloadView;
import smartimageview.SmartImageView;

/**
 * Created by sollian on 2015/9/22.
 */
public class ObtainLogoActivity extends BaseActivity {
    public static final String KEY_MODE = "mode";

    public static final int PICK_PHOTO = 0;
    public static final int TAKE_PHOTO = 1;

    private static final int REQUEST_TAKE_PHOTO = 0X1110;
    private static final int REQUEST_PICK_PHOTO = 0X1111;
    private static final int REQUEST_ZOOM_PHOTO = 0X1112;

    private SmartImageView vImg;

    private TextView tvRetry;
    private ProgressDownloadView progressDownloadView;

    private File cameraFile;
    private File cutFile;

    private boolean isUploading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtain_logo);

        checkData();

        init();
    }

    @Override
    public void onBackPressed() {
        if (isUploading) {
            LDUtil.toast("请等待图片上传完毕");
        } else {
            super.onBackPressed();
        }
    }

    private void checkData() {
        int mode = getIntent().getIntExtra(KEY_MODE, PICK_PHOTO);
        switch (mode) {
            case PICK_PHOTO:
                goPickPhoto();
                break;
            case TAKE_PHOTO:
                goTakePhoto();
                break;
            default:
                LDUtil.toast(LDUtil.MSG_DATA_ERROR);
                finish();
                break;
        }
    }

    private void init() {
        initTitle();

        vImg = (SmartImageView) findViewById(R.id.img);

        progressDownloadView = (ProgressDownloadView) findViewById(R.id.progress);

        tvRetry = (TextView) findViewById(R.id.tv_retry);
        tvRetry.setVisibility(View.INVISIBLE);
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg();
            }
        });
    }

    private void initTitle() {
        TitlebarHelper titlebarHelper = new TitlebarHelper(this);
        titlebarHelper.showRight(false);
        titlebarHelper.showLeft(true);
        titlebarHelper.getLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titlebarHelper.setTitle(R.string.obtain_photo);
    }

    private void goPickPhoto() {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum, REQUEST_PICK_PHOTO);
    }

    private void goTakePhoto() {
        cameraFile = genPhotoFile(FileUtil.DIR_CAMERA);

        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
        startActivityForResult(cameraintent, REQUEST_TAKE_PHOTO);
    }

    private File genPhotoFile(String dir) {
        return new File(FileUtil.getDirectory(dir), FileUtil.genPhotoName());
    }

    private void startPhotoZoom(Uri uri) {
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
        startActivityForResult(intent, REQUEST_ZOOM_PHOTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
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
                    Bitmap bmp = BitmapFactory.decodeFile(cutFile.getAbsolutePath());
                    if (bmp != null) {
                        vImg.setImageBitmap(bmp);
                    }
                    uploadImg();
                    break;
            }
        } else {
            finish();
        }
    }

    private void uploadImg() {
        tvRetry.setVisibility(View.INVISIBLE);
        User user = LocalManager.syncGetCurUser();
        if (user == null) {
            LDUtil.toast("用户信息有误");
            return;
        }
        String url = NetManager.FILE_UPLOAD + user.getName();
        FileUploadAsyncTask task = new FileUploadAsyncTask(this, url, new MyFileUploadListener());
        task.execute(cutFile);
    }

    private class MyFileUploadListener implements FileUploadAsyncTask.FileUploadListener {

        @Override
        public void onStart() {
            isUploading = true;
        }

        @Override
        public void onProgress(int progress) {
            progressDownloadView.setPercentage(progress);
        }

        @Override
        public void onFinish(String msg) {
            if (!TextUtils.isEmpty(msg) && msg.matches("\\d+")) {
                progressDownloadView.drawSuccess();
                LDUtil.toast("识别完毕您将收到通知");
                new SharePrefUtil.RemindPref().addToRemindSet(msg);
                progressDownloadView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isUploading = false;
                        onBackPressed();
                    }
                }, 2500);
            } else {
                isUploading = false;
                tvRetry.setVisibility(View.VISIBLE);
                progressDownloadView.drawFail();
                LDUtil.toast(msg);
            }
        }
    }
}
