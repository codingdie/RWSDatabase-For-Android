package com.codingdie.rwsdatabase.connection;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xupen on 2016/8/26.
 */
public class WritableConnection extends SQLiteConnection {
    public void execWriteSQL(String sql, Object[] param) {
        this.sqLiteDatabase.execSQL(sql, param);
    }
    public void execWriteSQL(String sql) {
        this.sqLiteDatabase.execSQL(sql);
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
     static WritableConnection createWritableConnection(String dbPath, int index) {
         WritableConnection writableConnection = new WritableConnection();
        writableConnection.setInUsing(false);
        writableConnection.setWritable(true);
        writableConnection.setIndex(index);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
        sqLiteDatabase.enableWriteAheadLogging();
        writableConnection.setSqLiteDatabase(sqLiteDatabase);
        return writableConnection;
    }


    public void setVersion(int version) {
        this.sqLiteDatabase.setVersion(version);
    }
}
