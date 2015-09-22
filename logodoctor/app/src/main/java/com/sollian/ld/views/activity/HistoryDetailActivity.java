package com.sollian.ld.views.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.sollian.ld.R;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.views.fragment.LogoDetailFragment;
import com.sollian.ld.views.titlebar.TitlebarHelper;

import smartimageview.SmartImageView;

/**
 * Created by sollian on 2015/9/22.
 */
public class HistoryDetailActivity extends BaseActivity {
    public static final String KEY_IMG = "img";

    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        if (!checkData()) {
            LDUtil.toast(LDUtil.MSG_DATA_ERROR);
            finish();
            return;
        }

        initTitle();
        init();
    }

    private boolean checkData() {
        img = getIntent().getStringExtra(KEY_IMG);
        return !TextUtils.isEmpty(img);
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
        titlebarHelper.setTitle(R.string.detail);
    }

    private void init() {
        SmartImageView sivImg = (SmartImageView) findViewById(R.id.img);
        sivImg.setImageUrl(img, R.drawable.ic_launcher, R.drawable.ic_launcher);

        LogoDetailFragment fragment = new LogoDetailFragment();
        fragment.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
