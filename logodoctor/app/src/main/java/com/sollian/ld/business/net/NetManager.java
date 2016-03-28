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
import com.sollian.ld.utils.LogUtil;
import com.sollian.ld.utils.ThreadUtil;
import com.sollian.ld.utils.http.HttpManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sollian on 2015/9/16.
 */
public class NetManager {
    private static final String ERROR_DEFAULT = "未知错误";
    private static final String ERROR_NO_DATA = "无数据";

    public static final int LIMIT = 10;
    /**
     * 服务器地址
     */
//    public static final String BASE_URL = "http://10.210.113.48/logodoctor";
    public static final String BASE_URL = "http://121.42.205.235/logodoctor";
    private static final String BASE_PAGE_URL = BASE_URL + "/php/";
    private static final String SIGN_UP = BASE_PAGE_URL + "register.php";
    private static final String LOGIN = BASE_PAGE_URL + "login.php";
    private static final String QUERY_LOGO_ALL = BASE_PAGE_URL + "getLogo.php";
    private static final String QUERY_LOGO_ID = BASE_PAGE_URL + "getLogo.php?id=";
    private static final String QUERY_HISTORY = BASE_PAGE_URL + "getHistory.php?maxId=";
    private static final String SET_HISTORY_READ = BASE_PAGE_URL + "setHistoryRead.php?id=";
    private static final String QUERY_HISTORY_STATE = BASE_PAGE_URL + "queryHistoryState.php?ids=";
    private static final String DELETE_HISTORY = BASE_PAGE_URL + "deleteHistory.php?user=";
    public static final String FILE_UPLOAD = BASE_PAGE_URL + "uploadFile.php?user=";
    public static final String LOGO_UPLOAD = BASE_PAGE_URL + "uploadLogo.php?";
    private static final String TAG = "NetManager";

    public static void asyncLogin(final Activity activity, @NonNull String name, @NonNull String pwd, final LDCallback callback) {
        ArrayList<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("password", pwd));
        // 封装请求参数的实体对象
        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(params, HttpManager.CHARSET);
        } catch (UnsupportedEncodingException e) {
            LogUtil.e(TAG, "sendArticle UnsupportedEncodingException");
            return;
        }

        netQuery(activity, LOGIN, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback != null) {
                    LDResponse<String> netResponse = new LDResponse<>();
                    netResponse.setErrorMsg(data);
                    callback.callback(netResponse);
                }
            }
        }, entity);
    }

    public static void asyncSignUp(final Activity activity, @NonNull final String name, @NonNull String pwd, final LDCallback callback) {
        ArrayList<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("password", pwd));
        // 封装请求参数的实体对象
        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(params, HttpManager.CHARSET);
        } catch (UnsupportedEncodingException e) {
            LogUtil.e(TAG, "sendArticle UnsupportedEncodingException");
            return;
        }

        netQuery(activity, SIGN_UP, new OnNetQueryDoneListener() {
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
        }, entity);
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

    public static void asyncQueryHistory(final Activity activity, final String maxId, final LDCallback callback) {
        if (TextUtils.isEmpty(maxId)) {
            return;
        }
        netQuery(activity, QUERY_HISTORY + maxId + "&user=" + LocalManager.syncGetCurUser().getName(), new OnNetQueryDoneListener() {
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
    public static void asyncQueryHistoryState(final Activity activity, final String historyIds, final LDCallback callback) {
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
     * 删除历史记录
     *
     * @param activity
     * @param userName
     * @param historyIds 多个id用","分割；为null时，删除全部历史记录
     * @param callback
     */
    public static void asyncDeleteHistory(final Activity activity, final String userName, final String historyIds, final LDCallback callback) {
        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(historyIds)) {
            return;
        }
        String url = DELETE_HISTORY + userName;
        if (!TextUtils.isEmpty(historyIds)) {
            url += "&&ids=" + historyIds;
        }
        netQuery(activity, url, new OnNetQueryDoneListener() {
            @Override
            public void onNetQueryDone(String data) {
                if (callback == null) {
                    return;
                }
                LDResponse<String> netResponse = new LDResponse<>();
                if (TextUtils.isEmpty(data)) {
                    netResponse.setObj("");
                } else {
                    if (data.matches("\\d+")) {
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
        netQuery(activity, url, l, null);
    }

    private static void netQuery(final Activity activity, final String url, final OnNetQueryDoneListener l, final HttpEntity entity) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                String data;
                if (entity == null) {
                    data = HttpManager.getInstance().getHttp(activity, url);
                } else {
                    data = HttpManager.getInstance().postHttp(activity, url, entity);
                }
                final String response = data;
                if (l == null) {
                    return;
                }
                if (activity == null) {
                    l.onNetQueryDone(data);
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            l.onNetQueryDone(response);
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
