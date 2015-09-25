package com.sollian.ld.utils.cache;

import android.support.annotation.NonNull;

import com.sollian.ld.utils.poll.PollUtil;
import com.sollian.ld.views.LDApplication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sollian on 2015/9/24.
 */
public class RemindCache implements ICache {
    private static final String TAG = RemindCache.class.getSimpleName();

    public static final int KEY_ADD_REMIND = 0X1000;
    public static final int KEY_REMOVE_REMIND = 0X1001;
    public static final int KEY_CLEAR_REMIND = 0X1002;

    private static RemindCache instance;

    public synchronized static RemindCache getInstance() {
        if (instance == null) {
            instance = new RemindCache();
        }
        return instance;
    }

    private RemindCache() {
    }

    @Override
    public Set<Integer> getKeys() {
        Integer[] keys = new Integer[]{
            KEY_ADD_REMIND,
            KEY_REMOVE_REMIND,
            KEY_CLEAR_REMIND,
        };
        return new HashSet<>(Arrays.asList(keys));
    }

    @Override
    public void onCacheEvent(int key, @NonNull Object data) {
        switch (key) {
            case KEY_ADD_REMIND:
                PollUtil.startPoll(LDApplication.getInstance());
                break;
            case KEY_REMOVE_REMIND:
                PollUtil.stopPoll(LDApplication.getInstance());
                break;
            case KEY_CLEAR_REMIND:
                PollUtil.stopPoll(LDApplication.getInstance());
                break;
        }
    }

}
