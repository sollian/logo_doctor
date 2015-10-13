package com.sollian.ld.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.sollian.ld.business.db.dbhelper.AbsDAOProxy;
import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.utils.pinyinutils.IPinYin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sollian on 2015/9/18.
 */
public class Logo implements IPinYin, IWrapImg {
    private String id;
    private String name;
    private String img;
    private String category;
    private String extra;
    private String desc;
    private String sortLetters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    @Override
    public String getWrappedImg() {
        return NetManager.BASE_URL + getImg();
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public String getSortedLetters() {
        return sortLetters;
    }

    public static final class LogoDAOProxy extends AbsDAOProxy<Logo> {
        public static final String TABLE_NAME = "logo";

        public static final String COL_SERVER_ID = "serverId";
        public static final String COL_NAME = "name";
        public static final String COL_IMG = "img";
        public static final String COL_EXTRA = "extra";
        public static final String COL_CATEGORY = "category";

        public static String getSQLCreateTable() {
            StringBuilder sb = getSQLCreateTablePrefix(TABLE_NAME);
            sb.append(COL_SERVER_ID).append(" INTEGER NOT NULL,")
                .append(COL_NAME).append(" TEXT NOT NULL,")
                .append(COL_IMG).append(" TEXT NOT NULL,")
                .append(COL_EXTRA).append(" TEXT,")
                .append(COL_CATEGORY).append(" TEXT NOT NULL")
                .append(getSQLCreateTableSuffix());
            return sb.toString();
        }

        public static ContentValues getContentValues(@NonNull Logo logo) {
            ContentValues values = new ContentValues();
            values.put(COL_SERVER_ID, logo.getId());
            values.put(COL_NAME, logo.getName());
            values.put(COL_IMG, logo.getImg());
            values.put(COL_EXTRA, logo.getExtra());
            values.put(COL_CATEGORY, logo.getCategory());
            return values;
        }

        public static Logo getDataFromCursor(Cursor cursor) {
            if (cursor != null && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }
            return null;
        }

        public static List<Logo> getDatasFromCursor(Cursor cursor) {
            List<Logo> list = null;
            if (cursor != null && cursor.moveToFirst()) {
                list = new ArrayList<>();
                do {
                    list.add(fromCursor(cursor));
                } while (cursor.moveToNext());
            }
            return list;
        }

        @NonNull
        private static Logo fromCursor(Cursor cursor) {
            Logo logo = new Logo();
            logo.setId(cursor.getString(cursor.getColumnIndex(COL_SERVER_ID)));
            logo.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
            logo.setImg(cursor.getString(cursor.getColumnIndex(COL_IMG)));
            logo.setExtra(cursor.getString(cursor.getColumnIndex(COL_EXTRA)));
            logo.setCategory(cursor.getString(cursor.getColumnIndex(COL_CATEGORY)));
            return logo;
        }
    }
}
