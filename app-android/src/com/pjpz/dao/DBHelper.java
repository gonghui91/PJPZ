package com.pjpz.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by storm on 14-4-8.
 */
public class DBHelper extends SQLiteOpenHelper {
    // 数据库名
    private static final String DB_NAME = "pjpz.db";

    // 数据库版本
    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	DownloadsDataHelper.DownloadsDBInfo.TABLE.create(db);
        PeriodicalsDataHelper.PeriodicalsDBInfo.TABLE.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
