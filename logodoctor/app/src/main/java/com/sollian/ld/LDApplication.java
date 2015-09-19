package com.sollian.ld;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.sollian.ld.utils.LDUtil;

/**
 * Created by john on 2015/9/6.
 */
public class LDApplication extends Application {
    private static LDApplication instance;

    public static LDApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        LDUtil.setContext(this);
        /**
         * leancloud
         */
        AVOSCloud.initialize(this, "l9I3JyCSfvbrQBy4X5Sdff6f", "0N6seNcJCdjpfgxlFNs9tXYP");
    }

}
