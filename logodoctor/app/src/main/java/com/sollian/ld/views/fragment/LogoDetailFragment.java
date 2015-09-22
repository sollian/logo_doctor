package com.sollian.ld.views.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sollian.ld.R;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.models.Logo;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.views.BaseFragment;
import com.sollian.ld.views.otherview.PlainTextView;

import smartimageview.SmartImageView;

/**
 * Created by sollian on 2015/9/22.
 */
public class LogoDetailFragment extends BaseFragment {
    public static final String KEY_ID = "id";
    private String id;

    private SmartImageView vImg;
    private TextView vName;
    private TextView vExtra;
    private PlainTextView vDesc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkData()) {
            LDUtil.toast(LDUtil.MSG_DATA_ERROR);
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logo_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        vImg = (SmartImageView) view.findViewById(R.id.img);
        vName = (TextView) view.findViewById(R.id.name);
        vExtra = (TextView) view.findViewById(R.id.extra);
        vDesc = (PlainTextView) view.findViewById(R.id.desc);

        getData();
    }

    private boolean checkData() {
        Bundle data = getArguments();
        if (data != null) {
            id = data.getString(KEY_ID);
        }
        return !TextUtils.isEmpty(id);
    }

    private void getData() {
        showProgressDialog("正在加载");
        NetManager.asyncQueryLogoById(getActivity(), id, new QueryLogoByIdCallback());
    }

    private void fillData(Logo logo) {
        if (!TextUtils.isEmpty(logo.getWrappedImg())) {
            vImg.setImageUrl(logo.getWrappedImg(), R.drawable.ic_launcher, R.drawable.ic_launcher);
        } else {
            vImg.setImageResource(R.drawable.ic_launcher);
        }

        vName.setText(logo.getName());

        if (!TextUtils.isEmpty(logo.getExtra())) {
            vExtra.setText(logo.getExtra());
        } else {
            vExtra.setText("");
        }

        if (!TextUtils.isEmpty(logo.getDesc())) {
            vDesc.setMText(logo.getDesc());
        } else {
            vDesc.setMText("");
        }
    }

    private class QueryLogoByIdCallback implements LDCallback {

        @Override
        public void callback(@NonNull LDResponse response) {
            hideProgressDialog();
            if (response.success()) {
                Logo logo = (Logo) response.getObj();
                fillData(logo);
            } else {
                LDUtil.toast(response.getErrorMsg());
            }
        }
    }
}
