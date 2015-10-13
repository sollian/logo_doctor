package com.sollian.ld.business.db.dbhelper;

import android.text.TextUtils;

/**
 * Created by sollian on 2015/10/12.
 */
public abstract class AbsDAOProxy<T> {
    public static final String COL_ID = "_id";

    public static StringBuilder getSQLCreateTablePrefix(String tableName) {
        if (TextUtils.isEmpty(tableName)) {
            throw new NullPointerException("tableName cannot be null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ")
            .append(tableName)
            .append("(")
            .append(COL_ID)
            .append(" INTEGER PRIMARY KEY AUTOINCREMENT")
            .append(",");
        return sb;
    }

    public static String getSQLCreateTableSuffix() {
        return ");";
    }
}
