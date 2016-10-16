package com.codingdie.rwsdatabase.connection;

import android.util.LruCache;

/**
 * Created by xupen on 2016/9/28.
 */
public class RWSTableCache extends  LruCache<String,RWSTableInfo>{
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


    public RWSTableInfo getRWSClassInfo(String tableName, ReadableConnection readableConnection){
        RWSTableInfo rwsTableInfo = get(tableName);
        if(rwsTableInfo ==null){
            rwsTableInfo = readableConnection.getTableInfo(tableName);
            put(tableName, rwsTableInfo);
        }
        return rwsTableInfo;
    }

}
