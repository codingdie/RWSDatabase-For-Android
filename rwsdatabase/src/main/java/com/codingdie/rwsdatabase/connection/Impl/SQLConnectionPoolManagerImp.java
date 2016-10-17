package com.codingdie.rwsdatabase.connection.Impl;

import com.codingdie.rwsdatabase.connection.ReadableConnection;
import com.codingdie.rwsdatabase.connection.WritableConnection;
import com.codingdie.rwsdatabase.connection.model.InitSQLiteConnectionPoolConfig;

/**
 * Created by xupeng on 2016/8/22.
 */
public  interface SQLConnectionPoolManagerImp {

    public ReadableConnection getReadableConnection();

    public WritableConnection getWritableConnection();

    public  void releaseWritableConnection();

    public  int getRestReadableConnectionCount();

    public  int getRestWritableConnectionCount();

    public  void sheduleCheck();

    public  void releaseReadConnection(ReadableConnection SQLiteConnection);

    public  void initConnnectionPool(InitSQLiteConnectionPoolConfig connectionPoolConfig, InitSQLiteDatabaseImp initSQLiteDatabaseImp);

    public  void destroy();

    public  void openLog(boolean openFlag);


}
