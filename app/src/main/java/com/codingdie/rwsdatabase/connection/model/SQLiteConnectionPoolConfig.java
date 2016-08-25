package com.codingdie.rwsdatabase.connection.model;

import java.io.File;

/**
 * Created by xupen on 2016/8/22.
 */
public class SQLiteConnectionPoolConfig {
    private int  maxCount;
    private String dbPath;

    //TODO
    private int  initCount;

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getInitCount() {
        return initCount;
    }

    public void setInitCount(int initCount) {
        this.initCount = initCount;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }
}
