package com.sollian.ld.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sollian.ld.BaseActivity;
import com.sollian.ld.R;
import com.sollian.ld.utils.IntentUtil;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.utils.ThreadUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTakePic;
    private TextView tvViewPic;
    private TextView tvLookAround;
    private TextView tvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        setDoubleClickToExit(true);

        View vTakePic = findViewById(R.id.take_photo);
        View vViewPic = findViewById(R.id.view_picture);
        View vLookAround = findViewById(R.id.look_around);
        View vHistory = findViewById(R.id.history);

        tvTakePic = (TextView) vTakePic.findViewById(R.id.block_title);
        tvTakePic.setText(R.string.take_pic);
        tvTakePic.setOnClickListener(this);
        tvViewPic = (TextView) vViewPic.findViewById(R.id.block_title);
        tvViewPic.setText(R.string.view_pic);
        tvViewPic.setOnClickListener(this);
        tvLookAround = (TextView) vLookAround.findViewById(R.id.block_title);
        tvLookAround.setText(R.string.look_around);
        tvLookAround.setOnClickListener(this);
        tvHistory = (TextView) vHistory.findViewById(R.id.block_title);
        tvHistory.setText(R.string.history);
        tvHistory.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadUtil.shutDown();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view == tvTakePic) {
            LDUtil.toast(getString(R.string.take_pic));
        } else if (view == tvViewPic) {
            LDUtil.toast(getString(R.string.view_pic));
        } else if (view == tvLookAround) {
            intent = new Intent(this, LookAroundActivity.class);
        } else if (view == tvHistory) {
            LDUtil.toast(getString(R.string.history));
        }
        if (intent != null) {
            IntentUtil.startActivity(this, intent);
        }
    }
}