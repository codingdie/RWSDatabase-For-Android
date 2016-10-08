package com.codingdie.rwsdatabase.test.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xupen on 2016/8/26.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public SqliteHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `Class`");
        db.execSQL("CREATE TABLE `Class` ( `classId`  INTEGER PRIMARY KEY  ,`className`  TEXT)");
        db.execSQL("DROP TABLE IF EXISTS `Student`");
        db.execSQL("CREATE TABLE `Student` ( `classId`  INTEGER PRIMARY KEY  ,`studentId`  INTEGER ,`studentName`  TEXT)");
        db.execSQL("DROP TABLE IF EXISTS `Teacher`");
        db.execSQL("CREATE TABLE `Teacher` ( `classId`  INTEGER PRIMARY KEY  ,`teacherId`  INTEGER ,`teacherName`  TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
