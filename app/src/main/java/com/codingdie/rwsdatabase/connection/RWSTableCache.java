package com.codingdie.rwsdatabase.connection;

import android.util.LruCache;

/**
 * Created by xupen on 2016/9/28.
 */
public class RWSTableCache extends  LruCache<String,RWSTable>{
    private  static RWSTableCache instance;
    public static int maxCacheCount=100;

    public RWSTableCache(int maxSize) {
        super(maxSize);
    }

    public  static synchronized RWSTableCache getInstance(){
        if(instance ==null){
          instance =new RWSTableCache(maxCacheCount);
        }
       return instance;
    }


    public RWSTable getRWSClassInfo(String tableName,ReadableConnection readableConnection){
        RWSTable rwsTable = get(tableName);
        if(rwsTable ==null){
            rwsTable = readableConnection.getTableInfo(tableName);
            put(tableName, rwsTable);
        }
        return rwsTable;
    }

}
