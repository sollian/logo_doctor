package com.sollian.ld.business.net;

import android.support.annotation.NonNull;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.models.User;

/**
 * Created by sollian on 2015/9/16.
 */
public class NetManager {
    private static final String DEFAULT_ERROR_MSG = "未知错误";

    public static void asyncLogin(@NonNull String name, @NonNull String pwd, final LDCallback callback) {
        AVUser.logInInBackground(name, pwd, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (callback != null) {
                    LDResponse<User> netResponse = new LDResponse<>();
                    if (avUser != null) {
                        User user = new User();
                        user.setName(avUser.getUsername());
                        netResponse.setObj(user);
                    } else if (e != null) {
                        netResponse.setErrorMsg(e.getMessage());
                    } else {
                        netResponse.setErrorMsg(DEFAULT_ERROR_MSG);
                    }
                    callback.callback(netResponse);
                }
            }
        });
    }

    public static void asyncSignUp(@NonNull final String name, @NonNull String pwd, final LDCallback callback) {
        AVUser user = new AVUser();
        user.setPassword(pwd);
        user.setUsername(name);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (callback != null) {
                    LDResponse<User> netResponse = new LDResponse<>();
                    if (e == null) {
                        User user = new User();
                        user.setName(name);
                        netResponse.setObj(user);
                    } else {
                        netResponse.setErrorMsg(e.getMessage());
                    }
                    callback.callback(netResponse);
                }
            }
        });
    }
}
