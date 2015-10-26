package com.sollian.ld.utils.poll;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.sollian.ld.R;
import com.sollian.ld.business.LDCallback;
import com.sollian.ld.business.LDResponse;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.utils.LogUtil;
import com.sollian.ld.utils.NotifyUtil;
import com.sollian.ld.utils.SharePrefUtil;
import com.sollian.ld.views.LDApplication;
import com.sollian.ld.views.activity.HistoryActivity;

/**
 * Created by sollian on 2015/9/24.
 */
public class PollRemindService extends Service {
    private static final String TAG = PollRemindService.class.getSimpleName();

    private final static int KEY_FAIL = 0;
    private final static int KEY_SUCCESS = 1;
    private final static String KEY_DATA = "data";

    private SharePrefUtil.RemindPref remindPref;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            LogUtil.e(TAG, "2----" + msg.getData().getString(KEY_DATA));
            switch (msg.what) {
                case KEY_FAIL:
                    String errorMsg = msg.getData().getString(KEY_DATA);
                    LogUtil.e(TAG, errorMsg);
                    break;
                case KEY_SUCCESS:
                    String historyIds = msg.getData().getString(KEY_DATA);
                    if (!TextUtils.isEmpty(historyIds)) {
                        //有识别完毕的图片了
                        remindPref.deleteFromRemindSet(historyIds);
                        String[] arr = historyIds.split(",");
                        Intent intent = new Intent(PollRemindService.this, HistoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        NotifyUtil.showNotify(PollRemindService.this, intent,
                            getString(R.string.new_message), getString(R.string.msg_detail, arr.length));
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        remindPref = new SharePrefUtil.RemindPref();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        queryRemind();
        return super.onStartCommand(intent, flags, startId);
    }

    private void queryRemind() {
        String historyIds = remindPref.getRemindIds();
        LogUtil.e(TAG, "1----" + historyIds);
        if (TextUtils.isEmpty(historyIds) || !remindPref.isRemindQueryValid()) {
            PollUtil.stopPoll(LDApplication.getInstance());
        } else {
            NetManager.asyncQueryHistoryState(null, historyIds, new QueryRemindCallback());
        }
    }

    private class QueryRemindCallback implements LDCallback {

        @Override
        public void callback(@NonNull LDResponse response) {
            if (response.success()) {
                String data = (String) response.getObj();
                Message msg = handler.obtainMessage(KEY_SUCCESS);
                Bundle bundle = msg.getData();
                bundle.putString(KEY_DATA, data);
                handler.sendMessage(msg);
            } else {
                Message msg = handler.obtainMessage(KEY_FAIL);
                Bundle bundle = msg.getData();
                bundle.putString(KEY_DATA, response.getErrorMsg());
                handler.sendMessage(msg);
            }
        }
    }
}
