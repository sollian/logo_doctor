
package com.sollian.ld.utils;

import android.util.Log;

/**
 * @author sollian
 */
public class LogUtil {
    public static final String TAG_LIFE_CYCLE = "life_cycle";
    public static final String TAG_TEST = "test";

    private static boolean isLoggable(String tag, int level) {
        return Log.isLoggable(tag, level);
    }

    public static void v(String tag, String info) {
        Log.v(tag, info);
    }

    public static void i(String tag, String info) {
        Log.i(tag, info);
    }

    public static void d(String tag, String info) {
        Log.d(tag, info);
    }

    public static void w(String tag, String info) {
        Log.w(tag, info);
    }

    public static void e(String tag, String info) {
        Log.e(tag, info);
    }

    public static void lifeCycle(String info) {
        if (isLoggable(TAG_LIFE_CYCLE, Log.DEBUG)) {
            Log.d(TAG_LIFE_CYCLE, info);
        }
    }

}
