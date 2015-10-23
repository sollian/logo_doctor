package com.sollian.ld.business.net;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.business.db.LogoDB;
import com.sollian.ld.business.local.LocalManager;
import com.sollian.ld.models.History;
import com.sollian.ld.models.Logo;
import com.sollian.ld.utils.ThreadUtil;
import com.sollian.ld.utils.http.HttpManager;

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
//    public static final String BASE_URL = "http://192.168.1.203/logodoctor";
        public static final String BASE_URL = "http://182.254.157.222:8080/logodoctor";
//    public static final String BASE_URL = "http://121.42.150.146/logodoctor";
    private static final String BASE_PAGE_URL = BASE_URL + "/php/";
    private static final String SIGN_UP = BASE_PAGE_URL + "register.php";
    private static final String LOGIN = BASE_PAGE_URL + "login.php";
    private static final String QUERY_LOGO_ALL = BASE_PAGE_URL + "getLogo.php";
    private static final String QUERY_LOGO_ID = BASE_PAGE_URL + "getLogo.php?id=";
    private static final String QUERY_HISTORY = BASE_PAGE_URL + "getHistory.php?minId=";
    private static final String SET_HISTORY_READ = BASE_PAGE_URL + "setHistoryRead.php?id=";
    public static final String FILE_UPLOAD = BASE_PAGE_URL + "uploadFile.php?user=";
    private static final String QUERY_HISTORY_STATE = BASE_PAGE_URL + "queryHistoryState.php?ids=";

    public static void asyncLogin(final Activity activity, @NonNull String name, @NonNull String pwd, final LDCallback callback) {
        netQuery(activity, LOGIN + "?name=" + name + "&password=" + pwd, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback != null) {
                    LDResponse<String> netResponse = new LDResponse<>();
                    netResponse.setErrorMsg(data);
                    callback.callback(netResponse);
                }
            }
        });
    }

    public static void asyncSignUp(final Activity activity, @NonNull final String name, @NonNull String pwd, final LDCallback callback) {
        netQuery(activity, SIGN_UP + "?name=" + name + "&password=" + pwd, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback != null) {
                    LDResponse<String> netResponse = new LDResponse<>();
                    netResponse.setErrorMsg(data);
                    if (!TextUtils.isEmpty(data) && data.matches("\\d+")) {
                        if (Integer.parseInt(data) >= 1) {
                            netResponse.setErrorMsg(null);
                        }
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
                        //本地存储
                        LogoDB logoDB = new LogoDB();
                        logoDB.asyncInsertOrUpdate(logos);
                    }
                    callback.callback(netResponse);
                }
            }
        });

    }

    public static void asyncQueryLogoById(final Activity activity, final String id, final LDCallback callback) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
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
        if (TextUtils.isEmpty(minId)) {
            return;
        }
        netQuery(activity, QUERY_HISTORY + minId + "&user=" + LocalManager.syncGetCurUser().getName(), new OnNetQueryDoneListener() {
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
        if (TextUtils.isEmpty(id)) {
            return;
        }
        netQuery(activity, SET_HISTORY_READ + id, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback != null) {
                    LDResponse<String> netResponse = new LDResponse<>();
                    if (TextUtils.isEmpty(data)) {
                        netResponse.setErrorMsg(ERROR_NO_DATA);
                    }
                    if (data.matches("\\d+")) {
                        netResponse.setObj(data);
                    } else {
                        netResponse.setErrorMsg(data);
                    }
                    callback.callback(netResponse);
                }
            }
        });
    }

    /**
     * 多个id用","分割
     * 调用成功，则返回处理好的history的id，否则返回错误信息
     */
    public static void asynQueryHistoryState(final Activity activity, final String historyIds, final LDCallback callback) {
        if (TextUtils.isEmpty(historyIds)) {
            return;
        }
        netQuery(activity, QUERY_HISTORY_STATE + historyIds, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback == null) {
                    return;
                }
                LDResponse<String> netResponse = new LDResponse<>();
                if (TextUtils.isEmpty(data)) {
                    netResponse.setObj("");
                } else {
                    String temp = data.replaceAll(",", "");
                    if (temp.matches("\\d+")) {
                        netResponse.setObj(data);
                    } else {
                        netResponse.setErrorMsg(data);
                    }
                }
                callback.callback(netResponse);
            }
        });
    }

    /**
     * activity为null时，回调运行在异步线程；否则运行在主线程
     */
    private static void netQuery(final Activity activity, final String url, final OnNetQueryDoneListener l) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                final String data = HttpManager.getInstance().getHttp(activity, url);
                if (l == null) {
                    return;
                }
                if (activity == null) {
                    l.onNetQueryDone(data);
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            l.onNetQueryDone(data);
                        }
                    });
                }
            }
        });
    }

    private interface OnNetQueryDoneListener {
        void onNetQueryDone(String data);
    }

}
