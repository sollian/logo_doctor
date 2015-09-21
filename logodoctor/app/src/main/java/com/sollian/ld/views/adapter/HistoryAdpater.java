package com.sollian.ld.views.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sollian.ld.R;
import com.sollian.ld.models.History;

import java.util.List;

import smartimageview.SmartImageView;

/**
 * Created by sollian on 2015/9/21.
 */
public class HistoryAdpater extends AbsBaseAdapter<History> {
    public HistoryAdpater(Context context, List<History> dataSource) {
        super(context, dataSource);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_history;
    }

    @Override
    protected IHolder getHolder() {
        return new Holder();
    }

    private class Holder implements IHolder {
        private SmartImageView sivImg;
        private TextView tvDate;
        private TextView tvMark;

        @Override
        public void onBindView(View convertView) {
            sivImg = (SmartImageView) convertView.findViewById(R.id.img);
            tvDate = (TextView) convertView.findViewById(R.id.date);
            tvMark = (TextView) convertView.findViewById(R.id.mark);
        }

        @Override
        public void onProcessView(int position, View convertView) {
            History item = getItem(position);
            if (!TextUtils.isEmpty(item.getWrappedImg())) {
                sivImg.setImageUrl(item.getWrappedImg(), R.drawable.ic_launcher, R.drawable.ic_launcher);
            } else {
                sivImg.setImageResource(R.drawable.ic_launcher);
            }

            tvDate.setText(item.getCreateTime());

            if (item.isProcessing()) {
                tvMark.setVisibility(View.VISIBLE);
                tvMark.setBackgroundResource(R.drawable.tv_bg_red);
                tvMark.setText(R.string.processing);
            } else if (!item.isRead()) {
                tvMark.setVisibility(View.VISIBLE);
                tvMark.setBackgroundResource(R.drawable.tv_bg_green);
                tvMark.setText(R.string.unread);
            } else {
                tvMark.setVisibility(View.GONE);
            }
        }
    }
}
