package com.sollian.ld.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sollian.ld.models.User;
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

    public void putInt(String key, int value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        if (TextUtils.isEmpty(key)) {
            return 0;
        }
        return sp.getInt(key, 0);
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

    public static abstract class AbsPref {
        SharePrefUtil sharePrefUtil;

        public AbsPref() {
            sharePrefUtil = SharePrefUtil.getInstance();
        }
    }

    public static class UserPref extends AbsPref {
        private static final String KEY_USER_NAME = "user_name";
        private static final String KEY_USER_PWD = "user_password";

        public void setUser(User user) {
            String name, password;
            if (user == null) {
                name = null;
                password = null;
            } else {
                name = user.getName();
                password = user.getPassword();
            }
            sharePrefUtil.putString(KEY_USER_NAME, name);
            sharePrefUtil.putString(KEY_USER_PWD, password);
        }

        public User getUser() {
            String name = sharePrefUtil.getString(KEY_USER_NAME);
            String pwd = sharePrefUtil.getString(KEY_USER_PWD);
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
                return new User(name, pwd);
            } else {
                return null;
            }
        }
    }

    public static class RemindPref extends AbsPref {
        private static final String KEY_REMIND_LIST = "remind_list";
        private static final String KEY_REMIND_QUERY_COUNT = "remind_query_count";

        private static final int MAX_QUERY_COUNT = 5;

        public boolean isRemindQueryValid() {
            int queryCount = sharePrefUtil.getInt(KEY_REMIND_QUERY_COUNT);
            sharePrefUtil.putInt(KEY_REMIND_QUERY_COUNT, queryCount + 1);
            return queryCount < MAX_QUERY_COUNT;
        }

        public void setRemindQueryValid() {
            sharePrefUtil.putInt(KEY_REMIND_QUERY_COUNT, 0);
        }

        public synchronized String getRemindIds() {
            return sharePrefUtil.getString(KEY_REMIND_LIST);
        }

        public synchronized Set<String> getRemindSet() {
            return str2Set(getRemindIds());
        }

        /**
         * 多个id用“,”分割
         */
        public synchronized void addToRemindSet(String historyIds) {
            if (TextUtils.isEmpty(historyIds)) {
                return;
            }
            String remind = sharePrefUtil.getString(KEY_REMIND_LIST);
            if (TextUtils.isEmpty(remind)) {
                sharePrefUtil.putString(KEY_REMIND_LIST, historyIds);
            } else {
                sharePrefUtil.putString(KEY_REMIND_LIST, remind + "," + historyIds);
            }

            setRemindQueryValid();

            CacheDispatcher.getInstance().dispatch(RemindCache.KEY_ADD_REMIND, historyIds);
        }

        /**
         * 多个id用“,”分割
         */
        public synchronized void deleteFromRemindSet(String historyIds) {
            Set<String> remindSet = getRemindSet();
            if (remindSet == null || remindSet.isEmpty()) {
                return;
            }
            remindSet.removeAll(str2Set(historyIds));
            sharePrefUtil.putString(KEY_REMIND_LIST, set2Str(remindSet));

            setRemindQueryValid();

            CacheDispatcher.getInstance().dispatch(RemindCache.KEY_REMOVE_REMIND, historyIds);
        }

        public synchronized void clearRemindSet() {
            sharePrefUtil.removeKey(KEY_REMIND_LIST);

            setRemindQueryValid();

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
