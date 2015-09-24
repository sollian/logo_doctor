package com.sollian.ld.utils.cache;

import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Created by sollian on 2015/9/24.
 */
public interface ICache {

    Set<Integer> getKeys();

    void onCacheEvent(int key, @NonNull Object data);
}
