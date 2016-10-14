package com.codingdie.rwsdatabase.orm.cache;

import android.util.LruCache;
import com.codingdie.rwsdatabase.orm.cache.model.RWSClassInfo;

/**
 * Created by xupen on 2016/9/28.
 */
public class RWSClassCache extends  LruCache<Class,RWSClassInfo>{
    private  static RWSClassCache instance;
    public static int maxCacheCount=100;

    public RWSClassCache(int maxSize) {
        super(maxSize);
    }

    public  static synchronized RWSClassCache getInstance(){
        if(instance ==null){
          instance =new RWSClassCache(maxCacheCount);
        }
       return instance;
    }
    public RWSClassInfo getClassInfo(Class aClass){
        RWSClassInfo RWSClassInfo = get(aClass);
        if(RWSClassInfo ==null){
            RWSClassInfo = RWSClassInfo.newInstance(aClass);
            put(aClass, RWSClassInfo);
        }
        return RWSClassInfo;
    }

}
