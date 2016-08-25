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

    public  void  destroy(){

    }

    public void setBeginUsingTime(long beginUsingTime) {
        this.beginUsingTime = beginUsingTime;
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }
    public static SQLiteConnection createReadableConnection(String path, int index) {
        SQLiteConnection readableConnection = new SQLiteConnection();
        readableConnection.setInUsing(false);
        readableConnection.setWritable(false);
        readableConnection.setIndex(index);
        return readableConnection;
    }

    ;

    public static SQLiteConnection createWritableConnection(String path, int index) {
        SQLiteConnection writableConnection = new SQLiteConnection();
        writableConnection.setInUsing(false);
        writableConnection.setWritable(true);
        writableConnection.setIndex(index);
        return writableConnection;
    }

}
