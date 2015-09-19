package com.sollian.ld.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * Created by sollian on 2015/9/6.
 */
public class LDUtil {
    private static Application context;

    public static void setContext(Application context) {
        LDUtil.context = context;
    }

    public static String getAppVersionName() {
        String versionName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getAppName() {
        String appName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0);
            appName = packageInfo.applicationInfo.loadLabel(
                context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    public static float dp2px(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dp, context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            sp, context.getResources().getDisplayMetrics());
    }

    public static void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
