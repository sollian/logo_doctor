package com.sollian.ld;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.sollian.ld.utils.LogUtil;

public class BaseFragment extends Fragment {

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

}
