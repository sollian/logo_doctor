package com.sollian.ld.views.fragment;


import android.content.Intent;
import android.text.TextUtils;

import com.sollian.ld.BaseFragment;
import com.sollian.ld.utils.IntentUtil;
import com.sollian.ld.views.activity.MainActivity;
import com.sollian.ld.views.titlebar.TitlebarHelper;

public class BaseLoginFragment extends BaseFragment {

    public interface ChangeModeListener {
        enum Mode {
            LOGIN, SIGNUP,
        }

        void changeMode(Mode mode);
    }

    TitlebarHelper titlebarHelper;
    ChangeModeListener changeModeListener;

    public BaseLoginFragment(TitlebarHelper titlebarHelper, ChangeModeListener changeModeListener) {
        this.titlebarHelper = titlebarHelper;
        this.changeModeListener = changeModeListener;
    }

    String checkNameValidity(String name) {
        if (TextUtils.isEmpty(name)) {
            return "用户名号不能为空";
        }
        return null;
    }

    String checkPwdValidity(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return "密码不能为空";
        }
        if (pwd.length() < 6) {
            return "密码至少6位";
        }
        return null;
    }

    String checkPwdConfirm(String pwd, String pwdConfirm) {
        if (!TextUtils.equals(pwd, pwdConfirm)) {
            return "两次密码不一致";
        }
        return null;
    }

    void goMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        IntentUtil.startActivity(getActivity(), intent);
        IntentUtil.finish(getActivity());
    }

}
