package com.codingdie.rwsdatabase.orm.cache;

import android.util.LruCache;
import com.codingdie.rwsdatabase.orm.cache.model.ClassInfo;
import com.codingdie.rwsdatabase.orm.util.ReflectUtil;

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
    public ClassInfo getClassInfo(Class aClass){
        ClassInfo classInfo = get(aClass);
        if(classInfo ==null){
            classInfo= ClassInfo.newInstance(aClass);
            put(aClass,classInfo);
        }
        return  classInfo;
    }

}
