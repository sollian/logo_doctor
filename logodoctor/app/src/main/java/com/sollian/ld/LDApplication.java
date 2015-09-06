package com.sollian.ld;

import android.app.Application;

import com.sollian.ld.utils.LDUtils;

/**
 * Created by john on 2015/9/6.
 */
public class LDApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LDUtils.setContext(this);
    }

}
