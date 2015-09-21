package com.sollian.ld.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sollian.ld.R;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.models.Logo;
import com.sollian.ld.utils.IntentUtil;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.views.adapter.LogoSortAdapter;
import com.sollian.ld.views.otherview.ClearEditText;
import com.sollian.ld.views.otherview.SideIndexBar;
import com.sollian.ld.views.titlebar.TitlebarHelper;

import java.util.ArrayList;
import java.util.List;

public class LookAroundActivity extends BaseActivity {

    private List<Logo> dataSource = new ArrayList<>();

    private ListView listView;

    private LogoSortAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_around);

        init();

        getData();
    }

    private void init() {
        initTitle();

        ClearEditText clearEditText = (ClearEditText) findViewById(R.id.search);
        clearEditText.setFocusable(false);
        clearEditText.setFocusableInTouchMode(true);
        clearEditText.addTextChangedListener(new MyTextWatcher());

        TextView vToast = (TextView) findViewById(R.id.toast);
        SideIndexBar sideIndexBar = (SideIndexBar) findViewById(R.id.sideindexbar);
        sideIndexBar.setTextView(vToast);
        sideIndexBar.setOnTouchingLetterChangedListener(new MyLetterChangeListener());

        listView = (ListView) findViewById(R.id.list);
        adapter = new LogoSortAdapter(this, dataSource);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new MyItemClickListener());
        listView.setTextFilterEnabled(true);
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
        titlebarHelper.setTitle(R.string.look_around);
    }

    private void getData() {
        showProgressDialog(LDUtil.MSG_LOADING);
        NetManager.asyncQueryAllLogo(this, new QueryAllLogoCallback());
    }

    private class QueryAllLogoCallback implements LDCallback {

        @Override
        public void callback(@NonNull LDResponse response) {
            hideProgressDialog();
            if (response.success()) {
                dataSource.clear();
                dataSource.addAll((List<Logo>) response.getObj());
                adapter.notifyDataSetChanged();
            } else {
                LDUtil.toast(response.getErrorMsg());
            }
        }
    }

    private class MyLetterChangeListener implements SideIndexBar.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(String s) {
            // 该字母首次出现的位置
            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                listView.setSelection(position);
            }
        }
    }

    private class MyItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(LookAroundActivity.this, LogoDetailActivity.class);
            intent.putExtra(LogoDetailActivity.KEY_ID, adapter.getItem(position).getId());
            IntentUtil.startActivity(LookAroundActivity.this, intent);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            listView.setFilterText(s.toString());
        }
    }
}
