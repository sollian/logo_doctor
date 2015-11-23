package com.sollian.ld.views.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sollian.ld.R;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.utils.SharePrefUtil;
import com.sollian.ld.utils.http.FileUploadAsyncTask;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.views.fragment.ObtainLogoNewFragment;
import com.sollian.ld.views.fragment.ObtainLogoSelectFragment;
import com.sollian.ld.views.titlebar.TitlebarHelper;
import com.sollian.ld.views.utils.ObtainLogoHelper;

import java.io.File;

import smartimageview.SmartImageView;

/**
 * Created by sollian on 2015/11/16.
 */
public class NewLogoActivity extends BaseActivity implements View.OnClickListener, ObtainLogoSelectFragment.SelectDataChangeListener, ObtainLogoNewFragment.NewDataChangeListener {
    private static final int STATE_NEW = 0;
    private static final int STATE_SELECT = 1;

    private int state = -1;

    private SmartImageView vImg;
    private Button vSubmit;
    private Button vNew, vSelect;

    private ObtainLogoHelper obtainLogoHelper;
    private File cutFile;

    private String name, id, description, extra, category = "汽车";

    private ObtainLogoSelectFragment obtainLogoSelectFragment;
    private ObtainLogoNewFragment obtainLogoNewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_logo);

        initTitle();
        init();

        changeState(STATE_NEW);
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
        titlebarHelper.setTitle(R.string.new_logo);
    }

    private void init() {
        vImg = (SmartImageView) findViewById(R.id.img);
        vImg.setOnClickListener(this);
        vSubmit = (Button) findViewById(R.id.submit);
        vSubmit.setOnClickListener(this);
        vNew = (Button) findViewById(R.id.bt_new);
        vNew.setOnClickListener(this);
        vSelect = (Button) findViewById(R.id.bt_select);
        vSelect.setOnClickListener(this);
        obtainLogoHelper = new ObtainLogoHelper(this, new ObtainLogoHelper.OnCutFinishListener() {
            @Override
            public void onCutFinish(File file) {
                cutFile = file;
                Bitmap bmp = BitmapFactory.decodeFile(cutFile.getAbsolutePath());
                if (bmp != null) {
                    vImg.setImageBitmap(bmp);
                }
            }
        });
        showDialog();
    }

    private void changeState(int state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (state) {
            case STATE_NEW:
                vNew.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                vSelect.setBackgroundColor(0xffeeeeee);
                if (obtainLogoNewFragment == null) {
                    obtainLogoNewFragment = new ObtainLogoNewFragment();
                    obtainLogoNewFragment.setNewDataChangeListener(this);
                }
                transaction.replace(R.id.container, obtainLogoNewFragment);
                break;
            case STATE_SELECT:
                vNew.setBackgroundColor(0xffeeeeee);
                vSelect.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                if (obtainLogoSelectFragment == null) {
                    obtainLogoSelectFragment = new ObtainLogoSelectFragment();
                    obtainLogoSelectFragment.setSelectDataChangeListener(this);
                }
                transaction.replace(R.id.container, obtainLogoSelectFragment);
                break;
        }
        transaction.commit();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{getString(R.string.take_pic), getString(R.string.view_pic)}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        obtainLogoHelper.goTakePhoto();
                        break;
                    case 1:
                        obtainLogoHelper.goPickPhoto();
                        break;
                }
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            obtainLogoHelper.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v == vImg) {
            showDialog();
        } else if (v == vSubmit) {
            submit();
        } else if (v == vNew) {
            changeState(STATE_NEW);
        } else if (v == vSelect) {
            changeState(STATE_SELECT);
        }
    }

    private void submit() {
        if (cutFile == null) {
            LDUtil.toast("请选择logo");
            return;
        }
        if (!isDataValid()) {
            return;
        }
        showProgressDialog("正在上传...");
        String url = NetManager.LOGO_UPLOAD;
        switch (state) {
            case STATE_NEW:
                url += "name=" + name + "&&description=" + description + "&&category=" + category;
                if(!TextUtils.isEmpty(extra)) {
                    url += "&&extra=" + extra;
                }
                break;
            case STATE_SELECT:
                url += "id=" + id;
                break;
        }
        FileUploadAsyncTask task = new FileUploadAsyncTask(this, url, new MyFileUploadListener());
        task.execute(cutFile);
    }

    private boolean isDataValid() {
        switch (state) {
            case STATE_NEW:
                obtainLogoNewFragment.triggerDataChangeListener();
                if (TextUtils.isEmpty(name)) {
                    LDUtil.toast("请填写名称");
                    return false;
                }
                if (TextUtils.isEmpty(description)) {
                    LDUtil.toast("请填写描述");
                    return false;
                }
                return true;
            case STATE_SELECT:
                if (TextUtils.isEmpty(id)) {
                    LDUtil.toast("请选择logo");
                    return false;
                }
                return true;
        }
        return false;
    }

    @Override
    public void selectDataChange(String id) {
        this.id = id;
    }

    @Override
    public void newDataChange(String name, String extra, String desc) {
        this.name = name;
        this.extra = extra;
        this.description = desc;
    }

    private class MyFileUploadListener implements FileUploadAsyncTask.FileUploadListener {

        @Override
        public void onStart() {
        }

        @Override
        public void onProgress(int progress) {
        }

        @Override
        public void onFinish(String msg) {
            hideProgressDialog();
            if (!TextUtils.isEmpty(msg) && msg.matches("\\d+")) {
                LDUtil.toast("上传成功");
                finish();
            } else {
                LDUtil.toast(msg);
            }
        }
    }
}
