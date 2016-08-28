package com.codingdie.rwsdatabase;

import android.content.Context;

/**
 * Created by xupen on 2016/8/25.
 */
public class RWSDatabaseCreator {
    private  String dbPath;
    private  int version=1;
    private  Class versionManager;
    private  int connectionPoolSize =5;
    private  Context context;

    public RWSDatabaseCreator(Context context) {
        this.context = context;
    }

    public    RWSDatabaseManager  create( ){
        RWSDatabaseManager rwsDatabaseManager=new RWSDatabaseManager();
        rwsDatabaseManager.init(dbPath,version,versionManager, connectionPoolSize);
        return  rwsDatabaseManager;
     }


    public RWSDatabaseCreator databasePath(String dbPath) {
        this.dbPath = dbPath;
        return this;
    }
    public RWSDatabaseCreator databaseName(String name) {
        this.dbPath = context.getDatabasePath(name).getAbsolutePath();
        return this;
    }


    public RWSDatabaseCreator version(int version) {
        this.version = version;
        return this;
    }

    public RWSDatabaseCreator versionManager(Class versionManager) {
        this.versionManager = versionManager;
        return this;
    }

    public RWSDatabaseCreator connectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
        return this;
    }
}
