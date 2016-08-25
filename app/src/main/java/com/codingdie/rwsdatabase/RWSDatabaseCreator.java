package com.codingdie.rwsdatabase;

/**
 * Created by xupen on 2016/8/25.
 */
public class RWSDatabaseCreator {
    private  String dbPath;
    private  int version=1;
    private  Class versionManager;
    private  int connectionPoolSize =5;


     public    RWSDatabaseManager  create(){
        RWSDatabaseManager rwsDatabaseManager=new RWSDatabaseManager();
        rwsDatabaseManager.init(dbPath,version,versionManager, connectionPoolSize);
        return  rwsDatabaseManager;
    }


    public RWSDatabaseCreator setDbPath(String dbPath) {
        this.dbPath = dbPath;
        return this;
    }


    public RWSDatabaseCreator setVersion(int version) {
        this.version = version;
        return this;
    }

    public RWSDatabaseCreator setVersionManager(Class versionManager) {
        this.versionManager = versionManager;
        return this;
    }

    public RWSDatabaseCreator setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
        return this;
    }
}
