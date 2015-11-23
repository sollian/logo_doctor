package com.sollian.ld.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sollian.ld.R;
import com.sollian.ld.business.db.LogoDB;
import com.sollian.ld.models.Logo;
import com.sollian.ld.utils.IntentUtil;
import com.sollian.ld.utils.NotifyUtil;
import com.sollian.ld.utils.cache.CacheDispatcher;
import com.sollian.ld.utils.cache.RemindCache;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.views.fragment.LogoDetailFragment;
import com.sollian.ld.views.otherview.LogoFlyView;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTakePic;
    private TextView tvViewPic;
    private TextView tvLookAround;
    private TextView tvHistory;
    private TextView tvNew;
    private LogoFlyView flyView;

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
        tvNew = (TextView) findViewById(R.id.new_logo);
        tvNew.setOnClickListener(this);

        flyView = (LogoFlyView) findViewById(R.id.flyView);
        initFlyView();
    }

    private void initFlyView() {
        List<Logo> logos = new LogoDB().queryAll();
        if (logos != null && !logos.isEmpty()) {
            flyView.setLogoClickListener(new LogoClickListener());
            flyView.setLogos(logos);
            flyView.setVisibility(View.VISIBLE);
        } else {
            flyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flyView.getVisibility() == View.VISIBLE) {
            flyView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (flyView.getVisibility() == View.VISIBLE) {
            flyView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flyView.cancel();
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
            NotifyUtil.cancelAll(this);
        } else if(view == tvNew) {
            intent = new Intent(this, NewLogoActivity.class);
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            IntentUtil.startActivity(this, intent);
        }
    }

    private class LogoClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Logo logo = (Logo) v.getTag();
            if (logo != null) {
                Intent intent = new Intent(MainActivity.this, LogoDetailActivity.class);
                intent.putExtra(LogoDetailFragment.KEY_ID, logo.getId());
                IntentUtil.startActivity(MainActivity.this, intent);
            }
        }
    }
}
