package com.codingdie.rwsdatabase.connection.Imp;

import com.codingdie.rwsdatabase.connection.model.InitSQLiteConnectionPoolConfig;
import com.codingdie.rwsdatabase.connection.model.SQLiteConnection;

/**
 * Created by xupen on 2016/8/22.
 */
public  interface SQLConnectionPoolManagerImp {
    public SQLiteConnection getReadConnection();
    public SQLiteConnection getWriteConnection();
    public  void releaseWriteConnection();
    public  void sheduleCheck();
    public  void releaseReadConnection(SQLiteConnection SQLiteConnection);
    public  void initConnnectionPool(InitSQLiteConnectionPoolConfig connectionPoolConfig, InitSQLiteDatabaseImp initSQLiteDatabaseImp);
    public  void destroy();
    public  void log(String  String);
    public  void openLog(boolean openFlag);


}
