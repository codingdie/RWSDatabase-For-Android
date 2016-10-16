package com.codingdie.rwsdatabase.connection;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xupen on 2016/8/22.
 */
class SQLiteConnection   {
    protected boolean inUsing = false;
    protected boolean isWritable = false;
    protected int index;
    protected long beginUsingTime;
    protected SQLiteDatabase sqLiteDatabase;

    protected boolean isInUsing() {
        return inUsing;
    }

    protected boolean isWritable() {
        return isWritable;
    }

    protected int getIndex() {
        return index;
    }

    protected long getBeginUsingTime() {
        return beginUsingTime;
    }

    protected void setInUsing(boolean inUsing) {
        this.inUsing = inUsing;
        if (inUsing) {
            this.beginUsingTime = System.currentTimeMillis();
        } else {
            this.beginUsingTime = 0;
        }
    }

    protected void setWritable(boolean writable) {
        isWritable = writable;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    protected void destroy() {
        this.sqLiteDatabase.close();
    }

    protected SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    protected void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public int getVersion() {
        return this.sqLiteDatabase.getVersion();
    }


}
