package com.codingdie.rwsdatabase.connection;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xupen on 2016/8/26.
 */
public class WritableConnection extends ReadableConnection {

    public void execWriteSQL(String sql, Object[] param) {
        this.sqLiteDatabase.execSQL(sql, param);
    }

    public void execWriteSQL(String sql) {
        this.sqLiteDatabase.execSQL(sql);
    }

    @Deprecated
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return this.sqLiteDatabase.update(table, values, whereClause, whereArgs);
    }

    @Deprecated
    public long insert(String table, String nullColumnHack, ContentValues values)
            throws SQLException {
        return this.sqLiteDatabase.insertOrThrow(table, nullColumnHack, values);
    }

    @Deprecated
    public int delete(String table, String whereClause, String[] whereArgs) {
        return this.sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    //TODO
    @Deprecated
    public <T> void insertObjectIntoTable(T object ,String tableName) {
        object.getClass();
    }

    //TODO
    @Deprecated
    public <T> void insertObject(T object ) {
        object.getClass();
    }

    //TODO
    @Deprecated
    public <T> void updateObject(T object ) {
        object.getClass();
    }

    public void beginTransaction() {
        this.sqLiteDatabase.beginTransaction();
    }

    public void endTransaction() {
        this.sqLiteDatabase.endTransaction();
    }

    public void setTransactionSuccessful() {
        this.sqLiteDatabase.setTransactionSuccessful();
    }

    public void setVersion(int version) {
        this.sqLiteDatabase.setVersion(version);
    }

    protected  static WritableConnection createWritableConnection(String dbPath, int index) {
        WritableConnection writableConnection = new WritableConnection();
        writableConnection.setInUsing(false);
        writableConnection.setWritable(true);
        writableConnection.setIndex(index);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
        sqLiteDatabase.enableWriteAheadLogging();
        writableConnection.setSqLiteDatabase(sqLiteDatabase);
        return writableConnection;
    }


}
