package com.sollian.ld.views.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sollian.ld.BaseActivity;
import com.sollian.ld.R;
import com.sollian.ld.models.Logo;
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
    }

    private void init() {
        initTitle();

        ClearEditText clearEditText = (ClearEditText) findViewById(R.id.search);
        clearEditText.setFocusable(false);
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
        titlebarHelper.setTitle(R.string.look_around);
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
