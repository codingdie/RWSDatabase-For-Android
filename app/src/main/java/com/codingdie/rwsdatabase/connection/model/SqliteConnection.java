package com.codingdie.rwsdatabase.connection.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xupen on 2016/8/22.
 */
public class SQLiteConnection {
    private boolean inUsing = false;
    private boolean isWritable = false;
    private int index;
    private long beginUsingTime;
    private SQLiteDatabase sqLiteDatabase;

    public boolean isInUsing() {
        return inUsing;
    }

    public void setInUsing(boolean inUsing) {
        this.inUsing = inUsing;
        if (inUsing) {
            this.beginUsingTime = System.currentTimeMillis();
        } else {
            this.beginUsingTime = 0;
        }
    }

    public boolean isWritable() {
        return isWritable;
    }

    public void setWritable(boolean writable) {
        isWritable = writable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getBeginUsingTime() {
        return beginUsingTime;
    }

    public void destroy() {

    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public static SQLiteConnection createReadableConnection(String dbPath, int index) {
        SQLiteConnection readableConnection = new SQLiteConnection();
        readableConnection.setInUsing(false);
        readableConnection.setWritable(false);
        readableConnection.setIndex(index);
        readableConnection.setSqLiteDatabase(SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY & SQLiteDatabase.CREATE_IF_NECESSARY));
        return readableConnection;
    }

    ;

    public static SQLiteConnection createWritableConnection(String dbPath, int index) {
        SQLiteConnection writableConnection = new SQLiteConnection();
        writableConnection.setInUsing(false);
        writableConnection.setWritable(true);
        writableConnection.setIndex(index);
        writableConnection.setSqLiteDatabase(SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE & SQLiteDatabase.CREATE_IF_NECESSARY));
        return writableConnection;
    }

}
