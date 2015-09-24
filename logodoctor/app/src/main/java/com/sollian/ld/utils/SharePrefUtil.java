package com.sollian.ld.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sollian.ld.utils.cache.CacheDispatcher;
import com.sollian.ld.utils.cache.RemindCache;
import com.sollian.ld.views.LDApplication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sollian on 2015/9/24.
 */
public class SharePrefUtil {
    private static final String LD_PREF = "ld_pref";

    private static SharePrefUtil instance;
    private SharedPreferences sp;

    public static SharePrefUtil getInstance() {
        return instance;
    }

    static {
        instance = new SharePrefUtil(LDApplication.getInstance());
    }

    private SharePrefUtil(Context context) {
        sp = context.getSharedPreferences(LD_PREF, Activity.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return sp.getString(key, null);
    }

    public void removeKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public static class RemindPref {
        private static final String KEY_REMIND_LIST = "remind_list";

        private SharePrefUtil sharePrefUtil;

        public RemindPref() {
            sharePrefUtil = SharePrefUtil.getInstance();
        }

        public Set<String> getRemindSet() {
            String remind = sharePrefUtil.getString(KEY_REMIND_LIST);
            return str2Set(remind);
        }

        /**
         * 多个id用“,”分割
         */
        public void addToRemindSet(String historyIds) {
            if (TextUtils.isEmpty(historyIds)) {
                return;
            }
            String remind = sharePrefUtil.getString(KEY_REMIND_LIST);
            if (TextUtils.isEmpty(remind)) {
                sharePrefUtil.putString(KEY_REMIND_LIST, historyIds);
            } else {
                sharePrefUtil.putString(KEY_REMIND_LIST, remind + "," + historyIds);
            }

            CacheDispatcher.getInstance().dispatch(RemindCache.KEY_ADD_REMIND, historyIds);
        }

        /**
         * 多个id用“,”分割
         */
        public void deleteFromRemindSet(String historyIds) {
            Set<String> remindSet = getRemindSet();
            if (remindSet == null || remindSet.isEmpty() || !remindSet.contains(historyIds)) {
                return;
            }
            remindSet.removeAll(str2Set(historyIds));
            sharePrefUtil.putString(KEY_REMIND_LIST, set2Str(remindSet));

            CacheDispatcher.getInstance().dispatch(RemindCache.KEY_REMOVE_REMIND, historyIds);
        }

        public void clearRemindSet() {
            sharePrefUtil.removeKey(KEY_REMIND_LIST);
            CacheDispatcher.getInstance().dispatch(RemindCache.KEY_CLEAR_REMIND, "");
        }

        private String set2Str(Set<String> set) {
            if (set == null || set.isEmpty()) {
                return null;
            }
            String string = "";
            for (String historyId : set) {
                string += historyId + ",";
            }
            return string.substring(0, string.length() - 1);
        }

        @NonNull
        private Set<String> str2Set(String string) {
            Set<String> set = new HashSet<>();
            if (!TextUtils.isEmpty(string)) {
                String arr[] = string.split(",");
                set.addAll(Arrays.asList(arr));
            }
            return set;
        }
    }
}
