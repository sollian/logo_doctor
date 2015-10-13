package com.sollian.ld.business.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.sollian.ld.business.db.dbhelper.DbHelper;
import com.sollian.ld.models.Logo;
import com.sollian.ld.utils.LogUtil;
import com.sollian.ld.utils.ThreadUtil;
import com.sollian.ld.views.LDApplication;

import java.util.List;

/**
 * Created by sollian on 2015/10/12.
 */
public class LogoDB {
    private static final String TAG = LogoDB.class.getSimpleName();

    private SQLiteDatabase db;

    public LogoDB() {
        db = new DbHelper(LDApplication.getInstance()).getWritableDatabase();
    }

    public List<Logo> queryAll() {
        String sql = "select * from " + Logo.LogoDAOProxy.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        List<Logo> result = Logo.LogoDAOProxy.getDatasFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

    public Logo queryById(String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }
        String sql = "select * from " + Logo.LogoDAOProxy.TABLE_NAME
            + " where " + Logo.LogoDAOProxy.COL_SERVER_ID + "=" + id;
        Cursor cursor = db.rawQuery(sql, null);
        Logo logo = Logo.LogoDAOProxy.getDataFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        return logo;
    }

    public boolean update(Logo logo) {
        if (logo == null || queryById(logo.getId()) == null) {
            return false;
        }
        String where = Logo.LogoDAOProxy.COL_SERVER_ID + "=?";
        String[] whereArgs = {logo.getId()};
        int count = db.update(Logo.LogoDAOProxy.TABLE_NAME,
            Logo.LogoDAOProxy.getContentValues(logo),
            where,
            whereArgs
        );
        LogUtil.d(TAG, "更新行数：" + count);
        return true;
    }

    public void insertOrUpdate(Logo logo) {
        if (logo == null) {
            return;
        }
        if (!update(logo)) {
            long rawId = db.insert(Logo.LogoDAOProxy.TABLE_NAME, null, Logo.LogoDAOProxy.getContentValues(logo));
            LogUtil.d(TAG, "插入成功：" + rawId);
        }
    }

    public void asyncInsertOrUpdate(final List<Logo> logos) {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                if (logos == null || logos.isEmpty()) {
                    return;
                }
                db.beginTransaction();
                try {
                    for (Logo logo : logos) {
                        insertOrUpdate(logo);
                    }
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                db.endTransaction();
            }
        });

    }

}
