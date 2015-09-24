package com.sollian.ld.utils.cache;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by john on 2015/9/24.
 */
public class CacheDispatcher {

    private static CacheDispatcher instance;

    private Set<ICache> receivers;

    public static CacheDispatcher getInstance() {
        return instance;
    }

    static {
        instance = new CacheDispatcher();
    }

    private CacheDispatcher() {
        receivers = new HashSet<>();
    }

    public void register(ICache cache) {
        receivers.add(cache);
    }

    public void unregister(ICache cache) {
        receivers.remove(cache);
    }

    public void clear() {
        receivers.clear();
    }

    public void dispatch(int key, @NonNull Object data) {
        if (receivers.isEmpty()) {
            return;
        }
        for (ICache cache : receivers) {
            if (cache != null && cache.getKeys().contains(key)) {
                cache.onCacheEvent(key, data);
            }
        }
    }
}
