package com.sollian.ld.views.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.sollian.ld.BaseActivity;
import com.sollian.ld.R;
import com.sollian.ld.views.fragment.BaseLoginFragment;
import com.sollian.ld.views.fragment.LoginFragment;
import com.sollian.ld.views.fragment.SignUpFragment;
import com.sollian.ld.views.titlebar.TitlebarHelper;

public class LoginActivity extends BaseActivity implements BaseLoginFragment.ChangeModeListener {

    private BaseLoginFragment loginFragment;
    private BaseLoginFragment signUpFragment;

    private TitlebarHelper titlebarHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        changeMode(Mode.LOGIN);
    }

    private void init() {
        titlebarHelper = new TitlebarHelper(this);
        titlebarHelper.showLeft(false);
        titlebarHelper.showRight(false);

        setDoubleClickToExit(true);
    }

    @Override
    public void changeMode(Mode mode) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        BaseLoginFragment fragment = null;
        switch (mode) {
            case LOGIN:
                if (loginFragment == null) {
                    loginFragment = new LoginFragment(titlebarHelper, this);
                }
                fragment = loginFragment;
                break;
            case SIGNUP:
                if (signUpFragment == null) {
                    signUpFragment = new SignUpFragment(titlebarHelper, this);
                }
                fragment = signUpFragment;
                break;
        }
        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        transaction.commit();

        changeTitle(mode);
    }

    private void changeTitle(Mode mode) {
        switch (mode) {
            case LOGIN:
                titlebarHelper.setTitle(R.string.login);
                break;
            case SIGNUP:
                titlebarHelper.setTitle(R.string.sign_up);
                break;
        }
    }
}

