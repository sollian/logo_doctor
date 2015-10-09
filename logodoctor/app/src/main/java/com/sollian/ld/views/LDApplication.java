package com.sollian.ld.views;

import android.app.Application;

import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.utils.poll.PollUtil;

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
//        AVOSCloud.initialize(this, "l9I3JyCSfvbrQBy4X5Sdff6f", "0N6seNcJCdjpfgxlFNs9tXYP");

        PollUtil.startPoll(this);
    }

}
