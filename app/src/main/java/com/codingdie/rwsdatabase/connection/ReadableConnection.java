package com.codingdie.rwsdatabase.connection;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xupen on 2016/8/26.
 */
public class ReadableConnection extends  SQLiteConnection {
    protected static ReadableConnection createReadableConnection(String dbPath, int index) {
        ReadableConnection readableConnection = new ReadableConnection();
        readableConnection.setInUsing(false);
        readableConnection.setWritable(false);
        readableConnection.setIndex(index);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.CREATE_IF_NECESSARY);
        sqLiteDatabase.enableWriteAheadLogging();
        readableConnection.setSqLiteDatabase(sqLiteDatabase);
        return readableConnection;
    }

}
