package com.codingdie.rwsdatabase.orm.cache;

import android.util.LruCache;
import com.codingdie.rwsdatabase.orm.cache.model.ClassInfo;

/**
 * Created by xupen on 2016/9/28.
 */
public class ClassCache  extends  LruCache<Class,ClassInfo>{
    private  static  ClassCache instance;
    public static int maxCacheCount=100;

    public ClassCache(int maxSize) {
        super(maxSize);
    }

    public  static synchronized ClassCache getInstance(){
        if(instance ==null){
          instance =new ClassCache(maxCacheCount);
        }
       return instance;
    }


}
