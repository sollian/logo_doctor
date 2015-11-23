package com.sollian.ld.views.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sollian.ld.R;
import com.sollian.ld.business.local.LocalManager;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.models.User;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.utils.SharePrefUtil;
import com.sollian.ld.utils.http.FileUploadAsyncTask;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.views.titlebar.TitlebarHelper;
import com.sollian.ld.views.utils.ObtainLogoHelper;

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

    private SmartImageView vImg;

    private TextView tvRetry;
    private ProgressDownloadView progressDownloadView;

    private File cutFile;
    private ObtainLogoHelper obtainLogoHelper;

    private boolean isUploading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtain_logo);

        obtainLogoHelper = new ObtainLogoHelper(this, new ObtainLogoHelper.OnCutFinishListener() {
            @Override
            public void onCutFinish(File cutFile) {
                ObtainLogoActivity.this.cutFile = cutFile;
                Bitmap bmp = BitmapFactory.decodeFile(cutFile.getAbsolutePath());
                if (bmp != null) {
                    vImg.setImageBitmap(bmp);
                }
                uploadImg();
            }
        });

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
                obtainLogoHelper.goPickPhoto();
                break;
            case TAKE_PHOTO:
                obtainLogoHelper.goTakePhoto();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        obtainLogoHelper.onActivityResult(requestCode, resultCode, data);
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
