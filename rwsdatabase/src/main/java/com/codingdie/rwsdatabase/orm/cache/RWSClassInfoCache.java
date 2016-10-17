package com.codingdie.rwsdatabase.orm.cache;

import android.util.LruCache;
import com.codingdie.rwsdatabase.orm.cache.model.RWSClassInfo;

/**
 * Created by xupeng on 2016/9/28.
 */
public class RWSClassInfoCache extends  LruCache<Class,RWSClassInfo>{
    private  static RWSClassInfoCache instance;
    public static int maxCacheCount=100;

    public RWSClassInfoCache(int maxSize) {
        super(maxSize);
    }

    public  static synchronized RWSClassInfoCache getInstance(){
        if(instance ==null){
          instance =new RWSClassInfoCache(maxCacheCount);
        }
       return instance;
    }
    public RWSClassInfo getRWSClassInfo(Class aClass){
        RWSClassInfo RWSClassInfo = get(aClass);
        if(RWSClassInfo ==null){
            RWSClassInfo = RWSClassInfo.newInstance(aClass);
            put(aClass, RWSClassInfo);
        }
        return RWSClassInfo;
    }

}
