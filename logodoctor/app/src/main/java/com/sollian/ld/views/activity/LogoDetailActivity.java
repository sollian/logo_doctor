package com.sollian.ld.views.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.sollian.ld.R;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.views.fragment.LogoDetailFragment;
import com.sollian.ld.views.titlebar.TitlebarHelper;

/**
 * Created by sollian on 2015/9/19.
 */
public class LogoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_detail);

        initTitle();
        init();
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
        LogoDetailFragment fragment = new LogoDetailFragment();
        fragment.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
