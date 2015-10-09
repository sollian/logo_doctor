package com.sollian.ld.business.local;

import com.sollian.ld.models.User;
import com.sollian.ld.utils.SharePrefUtil;

/**
 * Created by sollian on 2015/9/16.
 */
public class LocalManager {

    public static User syncGetCurUser() {
        return new SharePrefUtil.UserPref().getUser();
    }
}
