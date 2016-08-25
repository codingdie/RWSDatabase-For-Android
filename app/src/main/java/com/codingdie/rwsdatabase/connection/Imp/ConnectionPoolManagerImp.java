package com.codingdie.rwsdatabase.connection.Imp;

import com.codingdie.rwsdatabase.connection.model.SQLiteConnectionPoolConfig;
import com.codingdie.rwsdatabase.connection.model.SqliteConnection;

/**
 * Created by xupen on 2016/8/22.
 */
public  interface ConnectionPoolManagerImp {
    public SqliteConnection getReadConnection();
    public  SqliteConnection getWriteConnection();
    public  void releaseWriteConnection();
    public  void sheduleCheck();
    public  void releaseReadConnection(SqliteConnection sqliteConnection);
    public  void initConnnectionPool(SQLiteConnectionPoolConfig connectionPoolConfig, InitSQLiteDatabaseImp initSQLiteDatabaseImp);
    public  void destroy();
    public  void log(String  String);
    public  void openLog(boolean openFlag);


}
