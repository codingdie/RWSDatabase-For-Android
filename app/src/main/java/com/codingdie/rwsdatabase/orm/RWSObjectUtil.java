package com.codingdie.rwsdatabase.orm;

import com.codingdie.rwsdatabase.orm.cache.RWSClassInfoCache;
import com.codingdie.rwsdatabase.orm.cache.model.RWSClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.RWSPropertyInfo;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by xupeng on 2016/10/16.
 */
public class RWSObjectUtil {

    public static <T> boolean checkKeyPropertyIsNull(T obj) {
        try {
            RWSClassInfo RWSClassInfo = RWSClassInfoCache.getInstance().getRWSClassInfo(obj.getClass());
            List<Integer> integers = RWSClassInfo.getKeyPropertyIndexes();
            List<RWSPropertyInfo> RWSPropertyInfoList = RWSClassInfo.getProperties();

            if (integers != null && integers.size() != 0) {
                for (int i : integers) {
                    RWSPropertyInfo RWSPropertyInfo = RWSPropertyInfoList.get(i);
                    Field field = RWSPropertyInfo.getField();
                    field.setAccessible(true);
                    Object o = field.get(obj);
                    if(o==null){
                        return  true;
                    }
                    if(RWSPropertyInfo.getType()== RWSPropertyInfo.PROPERTYTYPE_STRING){
                        if(((String) o).length()==0){
                            return  true;
                        }
                    }
                    if(RWSPropertyInfo.getType()== RWSPropertyInfo.PROPERTYTYPE_SHORT|| RWSPropertyInfo.getType()== RWSPropertyInfo.PROPERTYTYPE_INT){
                        if(((Comparable)o).compareTo(0)==0){
                            return  true;
                        }
                    }
                    if(RWSPropertyInfo.getType()== RWSPropertyInfo.PROPERTYTYPE_LONG){
                        if(((Comparable)o).compareTo(0L)==0){
                            return  true;
                        }
                    }
                    if(RWSPropertyInfo.getType()== RWSPropertyInfo.PROPERTYTYPE_FLOAT){
                        if(((Comparable)o).compareTo(0.00f)==0){
                            return  true;
                        }
                    }
                    if(RWSPropertyInfo.getType()== RWSPropertyInfo.PROPERTYTYPE_DOUBLE){
                        if(((Comparable)o).compareTo(0.00d)==0){
                            return  true;
                        }
                    }
                }
            }
            return  false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }

    }
}
