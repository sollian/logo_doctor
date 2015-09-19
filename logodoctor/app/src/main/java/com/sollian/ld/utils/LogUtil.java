
package com.sollian.ld.utils;

import android.util.Log;

/**
 * @author sollian
 */
public class LogUtil {
    public static final String TAG_LIFE_CYCLE = "life_cycle";

    private static boolean isLoggable(String tag, int level) {
        return Log.isLoggable(tag, level);
    }

    public static void v(String tag, String info) {
        if (isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, info);
        }
    }

    public static void i(String tag, String info) {
        if (isLoggable(tag, Log.INFO)) {
            Log.i(tag, info);
        }
    }

    public static void d(String tag, String info) {
        if (isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, info);
        }
    }

    public static void w(String tag, String info) {
        if (isLoggable(tag, Log.WARN)) {
            Log.w(tag, info);
        }
    }

    public static void e(String tag, String info) {
        if (isLoggable(tag, Log.ERROR)) {
            Log.e(tag, info);
        }
    }

    public static void lifeCycle(String info) {
        d(TAG_LIFE_CYCLE, info);
    }

}
