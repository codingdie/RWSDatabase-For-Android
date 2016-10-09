package com.codingdie.rwsdatabase.connection;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by xupen on 2016/8/22.
 */
public class SQLiteConnection   {
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

    public Cursor execReadSQL(String sql , String[] param){
        return   this.sqLiteDatabase.rawQuery(sql ,param);
    }
    @Deprecated
    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {
        return   this.sqLiteDatabase.query(table ,columns,selection,selectionArgs,groupBy,having,orderBy,limit);
    }

    //TODO
    @Deprecated
    public <T> T queryObject(String sql , String[] param,Class<T> tClass,List<String>... ignoreProps){
       return  null;
    }

    //TODO
    @Deprecated
    public List queryObjectList(String sql , String[] param, Class tClass,List<String>... ignoreProps){
        return  null;
    }


}
