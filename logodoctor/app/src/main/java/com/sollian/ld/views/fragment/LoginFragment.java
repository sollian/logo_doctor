package com.sollian.ld.views.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.R;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.views.titlebar.TitlebarHelper;

public class LoginFragment extends BaseLoginFragment implements View.OnClickListener {

    private EditText vName;
    private EditText vPwd;

    public LoginFragment(TitlebarHelper titlebarHelper, ChangeModeListener changeModeListener) {
        super(titlebarHelper, changeModeListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.bt_login).setOnClickListener(this);
        view.findViewById(R.id.tv_gosignup).setOnClickListener(this);

        vName = (EditText) view.findViewById(R.id.name);
        vPwd = (EditText) view.findViewById(R.id.pwd);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_gosignup:
                changeModeListener.changeMode(ChangeModeListener.Mode.SIGNUP);
                break;
        }
    }

    private void login() {
        String name = vName.getText().toString().trim();
        String pwd = vPwd.getText().toString().trim();
        String errorMessage = checkNameValidity(name);
        if (errorMessage != null) {
            vName.setError(errorMessage);
            return;
        }
        errorMessage = checkPwdValidity(pwd);
        if (errorMessage != null) {
            vPwd.setError(errorMessage);
            return;
        }
        ((BaseActivity) getActivity()).showProgressDialog("正在登陆");
        NetManager.asyncLogin(name, pwd, new LoginCallback());
    }

    private class LoginCallback implements LDCallback {

        @Override
        public void callback(@NonNull LDResponse response) {
            ((BaseActivity) getActivity()).hideProgressDialog();
            if (response.success()) {
                goMain();
            } else {
                LDUtil.toast(response.getErrorMsg());
            }
        }
    }
}
