package com.codingdie.rwsdatabase.connection.Imp;

import com.codingdie.rwsdatabase.connection.model.SQLiteConnectionPoolConfig;
import com.codingdie.rwsdatabase.connection.model.SQLiteConnection;

/**
 * Created by xupen on 2016/8/22.
 */
public  interface ConnectionPoolManagerImp {
    public SQLiteConnection getReadConnection();
    public SQLiteConnection getWriteConnection();
    public  void releaseWriteConnection();
    public  void sheduleCheck();
    public  void releaseReadConnection(SQLiteConnection SQLiteConnection);
    public  void initConnnectionPool(SQLiteConnectionPoolConfig connectionPoolConfig, InitSQLiteDatabaseImp initSQLiteDatabaseImp);
    public  void destroy();
    public  void log(String  String);
    public  void openLog(boolean openFlag);


}
