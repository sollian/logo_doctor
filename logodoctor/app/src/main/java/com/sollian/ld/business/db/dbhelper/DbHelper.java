package com.sollian.ld.business.db.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sollian.ld.models.Logo;

/**
 * Created by sollian on 2015/10/12.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Logo.LogoDAOProxy.getSQLCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
