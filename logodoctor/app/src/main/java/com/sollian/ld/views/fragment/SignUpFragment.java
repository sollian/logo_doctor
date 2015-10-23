package com.sollian.ld.views.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sollian.ld.R;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.models.User;
import com.sollian.ld.utils.IntentUtil;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.utils.SharePrefUtil;
import com.sollian.ld.views.titlebar.TitlebarHelper;

public class SignUpFragment extends BaseLoginFragment implements View.OnClickListener {

    private EditText vPwd;
    private EditText vPwd2;
    private EditText vName;

    private String name;
    private String pwd;

    public SignUpFragment(TitlebarHelper titlebarHelper, ChangeModeListener changeModeListener) {
        super(titlebarHelper, changeModeListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.bt_signup).setOnClickListener(this);
        view.findViewById(R.id.tv_gologin).setOnClickListener(this);

        vPwd = (EditText) view.findViewById(R.id.pwd);
        vPwd2 = (EditText) view.findViewById(R.id.pwd_confirm);
        vName = (EditText) view.findViewById(R.id.name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_signup:
                signUp();
                break;
            case R.id.tv_gologin:
                changeModeListener.changeMode(ChangeModeListener.Mode.LOGIN);
                break;
        }
    }

    private void signUp() {
        pwd = vPwd.getText().toString().trim();
        String pwd2 = vPwd2.getText().toString().trim();
        name = vName.getText().toString().trim();
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
        errorMessage = checkPwdConfirm(pwd, pwd2);
        if (errorMessage != null) {
            vPwd2.setError(errorMessage);
            return;
        }
        showProgressDialog("正在登陆");
        NetManager.asyncSignUp(getActivity(), name, pwd, new SignUpCallback());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentUtil.REQUEST_CODE_SIGN_UP && resultCode == Activity.RESULT_OK) {
            goMain();
        }
    }

    private class SignUpCallback implements LDCallback {

        @Override
        public void callback(@NonNull LDResponse response) {
            hideProgressDialog();
            if (response.success()) {
                User user = new User(name, pwd);
                SharePrefUtil.UserPref userPref = new SharePrefUtil.UserPref();
                userPref.setUser(user);
                goMain();
            } else {
                LDUtil.toast(response.getErrorMsg());
            }
        }
    }

}
