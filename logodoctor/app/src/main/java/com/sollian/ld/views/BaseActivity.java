package com.sollian.ld.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.sollian.ld.R;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.utils.LogUtil;

/**
 * Created by sollian on 2015/9/2.
 */
public class BaseActivity extends FragmentActivity {
    private static final int BACK_COUNT = 3000;

    private int backCounter = 0;
    private boolean backFlag = false;
    private Handler backHandler;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onCreate));
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onStart));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onPostCreate));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onResume));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onPostResume));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onPause));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onStop));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onDestroy));
    }

    public void showProgressDialog(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void setDoubleClickToExit(boolean flag) {
        backFlag = flag;
        if (flag) {
            if (backHandler == null) {
                backHandler = new Handler();
            }
            backHandler.removeCallbacks(null);
        } else {
            if (backHandler != null) {
                backHandler.removeCallbacks(null);
            }
            backHandler = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (backFlag) {
            if (backCounter == 0) {
                backCounter++;
                LDUtil.toast("再次点击退出程序");
                startDoubleClickCount();
            } else {
                clearDoubleClickCount();
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void startDoubleClickCount() {
        if (backHandler == null) {
            return;
        }
        backHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                backCounter = 0;
            }
        }, BACK_COUNT);
    }

    private void clearDoubleClickCount() {
        if (backHandler != null) {
            backHandler.removeCallbacks(null);
        }
        backHandler = null;
        backCounter = 0;
    }
}
