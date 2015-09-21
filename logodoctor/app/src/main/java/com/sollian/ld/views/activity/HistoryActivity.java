package com.sollian.ld.views.activity;

import android.os.Bundle;
import android.view.View;

import com.sollian.ld.R;
import com.sollian.ld.models.History;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.views.adapter.HistoryAdpater;
import com.sollian.ld.views.titlebar.TitlebarHelper;

import java.util.ArrayList;
import java.util.List;

import waterdroplistview.WaterDropListView;

/**
 * Created by sollian on 2015/9/21.
 */
public class HistoryActivity extends BaseActivity {
    private WaterDropListView waterDropListView;

    private HistoryAdpater adapter;

    private List<History> dataSource = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        init();

        showProgressDialog(LDUtil.MSG_LOADING);
        getData();
    }

    private void init() {
        initTitle();

        waterDropListView = (WaterDropListView) findViewById(R.id.list);
        View empty = findViewById(R.id.empty);
        waterDropListView.setEmptyView(empty);
        waterDropListView.setWaterDropListViewListener(new WaterDropListener());
        waterDropListView.setPullLoadEnable(false);

        adapter = new HistoryAdpater(this, dataSource);
        waterDropListView.setAdapter(adapter);
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
        titlebarHelper.setTitle(R.string.history);
    }

    private void getData() {
        waterDropListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                waterDropListView.stopRefresh();
            }
        }, 2000);
    }

    private void getMoreData() {
        waterDropListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                waterDropListView.stopLoadMore();
            }
        }, 2000);
    }

    private class WaterDropListener implements WaterDropListView.IWaterDropListViewListener {

        @Override
        public void onRefresh() {
            getData();
        }

        @Override
        public void onLoadMore() {
            getMoreData();
        }
    }

}
