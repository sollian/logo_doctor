package com.sollian.ld.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.R;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.models.Logo;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.views.titlebar.TitlebarHelper;

import smartimageview.SmartImageView;

/**
 * Created by sollian on 2015/9/19.
 */
public class LogoDetailActivity extends BaseActivity {
    public static final String KEY_ID = "id";

    private SmartImageView vImg;
    private TextView vName;
    private TextView vExtra;
    private TextView vDesc;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_detail);

        if (!checkData()) {
            LDUtil.toast(LDUtil.MSG_DATA_ERROR);
            finish();
            return;
        }

        initTitle();

        init();

        getData();
    }

    private boolean checkData() {
        Intent intent = getIntent();
        id = intent.getStringExtra(KEY_ID);
        return !TextUtils.isEmpty(id);
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
        titlebarHelper.setTitle(R.string.logo_detail);
    }

    private void init() {
        vImg = (SmartImageView) findViewById(R.id.img);
        vName = (TextView) findViewById(R.id.name);
        vExtra = (TextView) findViewById(R.id.extra);
        vDesc = (TextView) findViewById(R.id.desc);
    }

    private void getData() {
        showProgressDialog("正在加载");
        NetManager.asyncQueryLogoById(this, id, new QueryLogoByIdCallback());
    }

    private void fillData(Logo logo) {
        if (!TextUtils.isEmpty(logo.getWrappedImg())) {
            vImg.setImageUrl(logo.getWrappedImg(), R.drawable.ic_launcher, R.drawable.ic_launcher);
        } else {
            vImg.setImageResource(R.drawable.ic_launcher);
        }

        vName.setText(logo.getName());

        if (!TextUtils.isEmpty(logo.getExtra())) {
            vExtra.setText(logo.getExtra());
        } else {
            vExtra.setText("");
        }

        if (!TextUtils.isEmpty(logo.getDesc())) {
            vDesc.setText(logo.getDesc());
        } else {
            vDesc.setText("");
        }
    }

    private class QueryLogoByIdCallback implements LDCallback {

        @Override
        public void callback(@NonNull LDResponse response) {
            hideProgressDialog();
            if (response.success()) {
                Logo logo = (Logo) response.getObj();
                fillData(logo);
            } else {
                LDUtil.toast(response.getErrorMsg());
            }
        }
    }
}
