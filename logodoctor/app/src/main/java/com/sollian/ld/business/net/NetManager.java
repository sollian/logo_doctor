package com.sollian.ld.business.net;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.models.History;
import com.sollian.ld.models.Logo;
import com.sollian.ld.models.User;
import com.sollian.ld.utils.HttpManager;
import com.sollian.ld.utils.ThreadUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sollian on 2015/9/16.
 */
public class NetManager {
    private static final String ERROR_DEFAULT = "未知错误";
    private static final String ERROR_NO_DATA = "无数据";

    public static final int LIMIT = 20;
    /**
     * 服务器地址
     */
    public static final String BASE_URL = "http://121.42.150.146/logodoctor";
    private static final String BASE_PAGE_URL = BASE_URL + "/php/";
    private static final String QUERY_LOGO_ALL = BASE_PAGE_URL + "getLogo.php";
    private static final String QUERY_LOGO_ID = BASE_PAGE_URL + "getLogo.php?id=";
    private static final String QUERY_HISTORY = BASE_PAGE_URL + "getHistory.php?minId=";
    private static final String SET_HISTORY_READ = BASE_PAGE_URL + "setHistoryRead.php?id=";


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
                        netResponse.setErrorMsg(ERROR_DEFAULT);
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

    public static void asyncQueryAllLogo(final Activity activity, final LDCallback callback) {
        netQuery(activity, QUERY_LOGO_ALL, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback != null) {
                    LDResponse<List<Logo>> netResponse = new LDResponse<>();
                    if (TextUtils.isEmpty(data)) {
                        netResponse.setErrorMsg(ERROR_NO_DATA);
                    } else {
                        Type listType = new TypeToken<ArrayList<Logo>>() {
                        }.getType();
                        Gson gson = new Gson();
                        ArrayList<Logo> logos = gson.fromJson(data, listType);
                        netResponse.setObj(logos);
                    }
                    callback.callback(netResponse);
                }
            }
        });

    }

    public static void asyncQueryLogoById(final Activity activity, final String id, final LDCallback callback) {
        netQuery(activity, QUERY_LOGO_ID + id, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback != null) {
                    LDResponse<Logo> netResponse = new LDResponse<>();
                    if (TextUtils.isEmpty(data)) {
                        netResponse.setErrorMsg(ERROR_NO_DATA);
                    } else {
                        Gson gson = new Gson();
                        Logo logo = gson.fromJson(data, Logo.class);
                        netResponse.setObj(logo);
                    }
                    callback.callback(netResponse);
                }
            }
        });
    }

    public static void asyncQueryHistory(final Activity activity, final String minId, final LDCallback callback) {
        netQuery(activity, QUERY_HISTORY + minId, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback != null) {
                    LDResponse<List<History>> netResponse = new LDResponse<>();
                    if (TextUtils.isEmpty(data)) {
                        netResponse.setErrorMsg(ERROR_NO_DATA);
                    } else {
                        Type listType = new TypeToken<ArrayList<History>>() {
                        }.getType();
                        Gson gson = new Gson();
                        ArrayList<History> histories = gson.fromJson(data, listType);
                        netResponse.setObj(histories);
                    }
                    callback.callback(netResponse);
                }
            }
        });

    }

    /**
     * 将History设置为已读，成功则返回受影响的个数
     */
    public static void asyncSetHistoryRead(final Activity activity, final String id, final LDCallback callback) {
        netQuery(activity, SET_HISTORY_READ + id, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback != null) {
                    LDResponse<String> netResponse = new LDResponse<>();
                    if (TextUtils.isEmpty(data)) {
                        netResponse.setErrorMsg(ERROR_NO_DATA);
                    }
                    if (!data.matches("\\d+")) {
                        netResponse.setErrorMsg(data);
                    }
                    netResponse.setObj(data);
                    callback.callback(netResponse);
                }
            }
        });

    }


    private static void netQuery(final Activity activity, final String url, final OnNetQueryDoneListener l) {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                final String data = HttpManager.getInstance().getHttp(activity, url);
                if (l == null) {
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l.onNetQueryDone(data);
                    }
                });
            }
        });
    }

    private interface OnNetQueryDoneListener {
        void onNetQueryDone(String data);
    }

}
