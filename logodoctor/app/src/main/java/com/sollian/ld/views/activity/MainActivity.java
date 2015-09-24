package com.sollian.ld.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sollian.ld.R;
import com.sollian.ld.utils.IntentUtil;
import com.sollian.ld.utils.cache.CacheDispatcher;
import com.sollian.ld.utils.cache.RemindCache;
import com.sollian.ld.views.BaseActivity;

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

        CacheDispatcher.getInstance().register(RemindCache.getInstance());
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

        CacheDispatcher.getInstance().clear();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view == tvTakePic) {
            intent = new Intent(this, ObtainLogoActivity.class);
            intent.putExtra(ObtainLogoActivity.KEY_MODE, ObtainLogoActivity.TAKE_PHOTO);
        } else if (view == tvViewPic) {
            intent = new Intent(this, ObtainLogoActivity.class);
            intent.putExtra(ObtainLogoActivity.KEY_MODE, ObtainLogoActivity.PICK_PHOTO);
        } else if (view == tvLookAround) {
            intent = new Intent(this, LookAroundActivity.class);
        } else if (view == tvHistory) {
            intent = new Intent(this, HistoryActivity.class);
        }
        if (intent != null) {
            IntentUtil.startActivity(this, intent);
        }
    }
}
