package com.sollian.ld.views;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sollian.ld.R;
import com.sollian.ld.utils.LogUtil;

public class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onAttach));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onCreate));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onActivityCreate));
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onStart));
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onResume));
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onPause));
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onStop));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onDestroyView));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onDestroy));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.lifeCycle(getClass().getSimpleName() + getString(R.string.lifecycle_onDetach));
    }

    public void showProgressDialog(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
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

}
