package com.codingdie.rwsdatabase;

import android.database.sqlite.SQLiteDatabase;
import com.codingdie.rwsdatabase.connection.Imp.InitSQLiteDatabaseImp;
import com.codingdie.rwsdatabase.connection.SQLConnectionPoolManager;
import com.codingdie.rwsdatabase.connection.model.InitSQLiteConnectionPoolConfig;
import com.codingdie.rwsdatabase.connection.model.SQLiteConnection;
import com.codingdie.rwsdatabase.version.VersionController;

/**
 * Created by xupen on 2016/8/25.
 */
public class RWSDatabaseManager {
    private SQLConnectionPoolManager connectionPoolManager;
    public void  init(String dbPath,final int version,final Class versionManager, int maxConnectionCount){
        InitSQLiteConnectionPoolConfig initSQLiteConnectionPoolConfig=new InitSQLiteConnectionPoolConfig();
        initSQLiteConnectionPoolConfig.setMaxCount(maxConnectionCount);
        initSQLiteConnectionPoolConfig.setDbPath(dbPath);
        connectionPoolManager=new SQLConnectionPoolManager();
        connectionPoolManager.initConnnectionPool(initSQLiteConnectionPoolConfig, new InitSQLiteDatabaseImp() {
            @Override
            public void initDatabase(SQLiteConnection sqLiteConnection) {
                SQLiteDatabase writeableDb=sqLiteConnection.getSqLiteDatabase();
                VersionController versionController=new VersionController();
                versionController.initDatabase(version, versionManager,writeableDb);
            }
        });
    }
}

