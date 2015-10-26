package com.sollian.ld.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.sollian.ld.R;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.business.local.LocalManager;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.models.History;
import com.sollian.ld.models.User;
import com.sollian.ld.utils.IntentUtil;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.views.adapter.HistoryAdpater;
import com.sollian.ld.views.fragment.LogoDetailFragment;
import com.sollian.ld.views.titlebar.TitlebarHelper;

import java.util.ArrayList;
import java.util.List;

import waterdroplistview.WaterDropListView;

/**
 * Created by sollian on 2015/9/21.
 */
public class HistoryActivity extends BaseActivity {
    private static final String MSG_DELETE = "正在删除";

    private WaterDropListView waterDropListView;

    private HistoryAdpater adapter;

    private List<History> dataSource = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        init();

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

        waterDropListView.setOnItemClickListener(new MyItemClickListener());
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

    @Override
    protected void onResume() {
        super.onResume();
        registerForContextMenu(waterDropListView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterForContextMenu(waterDropListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
//        menu.setHeaderTitle("人物简介");
        //添加菜单项
        menu.add(0, 0, 0, "删除");
        menu.add(0, 1, 0, "删除全部历史记录");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        User user = LocalManager.syncGetCurUser();
        int id = item.getItemId();
        switch (id) {
            case 0:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                History history = adapter.getItem(info.position - 1);
                deleteHistory(user.getName(), history.getId());
                break;
            case 1:
                deleteHistory(user.getName(), null);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteHistory(String username, String ids) {
        showProgressDialog(MSG_DELETE);
        NetManager.asyncDeleteHistory(this, username, ids, new DeleteHistoryCallback());
    }

    private void getData() {
        showProgressDialog(LDUtil.MSG_LOADING);
        NetManager.asyncQueryHistory(this, 0 + "", new QueryHistoryCallback(false));
    }

    private void getMoreData() {
        String minId = "0";
        if (!dataSource.isEmpty()) {
            minId = dataSource.get(dataSource.size() - 1).getId();
        }
        showProgressDialog(LDUtil.MSG_LOADING);
        NetManager.asyncQueryHistory(this, minId, new QueryHistoryCallback(true));
    }

    private class QueryHistoryCallback implements LDCallback {
        private boolean append;

        public QueryHistoryCallback(boolean append) {
            this.append = append;
        }

        @Override
        public void callback(@NonNull LDResponse response) {
            hideProgressDialog();
            if (!append) {
                dataSource.clear();
            }
            if (response.success()) {
                List<History> data = (List<History>) response.getObj();
                if (data == null || data.size() < NetManager.LIMIT) {
                    waterDropListView.setPullLoadEnable(false);
                } else {
                    waterDropListView.setPullLoadEnable(true);
                }
                if (!append) {
                    waterDropListView.stopRefresh();
                } else {
                    waterDropListView.stopLoadMore();
                }
                if (data != null && !data.isEmpty()) {
                    dataSource.addAll(data);
                }
            } else {
                LDUtil.toast(response.getErrorMsg());
            }
            adapter.notifyDataSetChanged();
        }
    }

    private class DeleteHistoryCallback implements LDCallback {
        @Override
        public void callback(@NonNull LDResponse response) {
            hideProgressDialog();
            if (response.success()) {
                getData();
            } else {
                LDUtil.toast(response.getErrorMsg());
            }
        }
    }

    private class SetReadCallback implements LDCallback {
        private History history;

        public SetReadCallback(History history) {
            this.history = history;
        }

        @Override
        public void callback(@NonNull LDResponse response) {
            if (response.success()) {
                if (history != null && adapter != null) {
                    history.setRead((byte) 1);
                    adapter.notifyDataSetChanged();
                }
            }
        }
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

    private class MyItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int pos = position - 1;
            History item = adapter.getItem(pos);
            if (item.isProcessing()) {
                LDUtil.toast("处理中，请稍后查看");
            } else if (!item.isSuccess()) {
                LDUtil.toast("识别失败，非常抱歉");
            } else {
                if (!item.isRead()) {
                    NetManager.asyncSetHistoryRead(HistoryActivity.this, item.getId(), new SetReadCallback(item));
                }
                Intent intent = new Intent(HistoryActivity.this, HistoryDetailActivity.class);
                intent.putExtra(HistoryDetailActivity.KEY_IMG, item.getWrappedImg());
                intent.putExtra(LogoDetailFragment.KEY_ID, item.getLogoId());
                IntentUtil.startActivity(HistoryActivity.this, intent);
            }
        }
    }

}
