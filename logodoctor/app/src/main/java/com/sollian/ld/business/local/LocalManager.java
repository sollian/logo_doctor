package com.sollian.ld.business.local;

import com.avos.avoscloud.AVUser;
import com.sollian.ld.models.User;

/**
 * Created by sollian on 2015/9/16.
 */
public class LocalManager {

    public static User syncGetCurUser() {
        AVUser currentUser = AVUser.getCurrentUser();
        User user = null;
        if (currentUser != null) {
            user = new User();
            user.setName(currentUser.getUsername());
        }
        return user;
    }
}
