package com.codingdie.rwsdatabase.orm.util;

import com.codingdie.rwsdatabase.orm.annotation.RWSColum;
import com.codingdie.rwsdatabase.orm.cache.RWSClassInfoCache;
import com.codingdie.rwsdatabase.orm.cache.model.RWSClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.RWSPropertyInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    public static List<RWSPropertyInfo> getAllProperty(Class aClass){
        List<RWSPropertyInfo> RWSPropertyInfos =new ArrayList<RWSPropertyInfo>();
        Field[] fields= aClass.getDeclaredFields();
        if(fields!=null&&fields.length>0){
            for(int i=0;i<fields.length;i++){
                Field field=fields[i];
                if(RWSPropertyInfo.checkFieldCanOrm(field.getGenericType().toString())){
                    RWSPropertyInfo RWSPropertyInfo =new RWSPropertyInfo();
                    RWSPropertyInfo.setName(field.getName());
                    RWSPropertyInfo.setType(field.getGenericType().toString());
                    RWSPropertyInfo.setField(field);
                    RWSColum annotation=field.getAnnotation(RWSColum.class);
                    if(annotation!=null&&!annotation.ignore()){
                        RWSPropertyInfo.setAlias(annotation.alias());
                        RWSPropertyInfo.setKey(annotation.isKey());
                    }
                    if(annotation==null||annotation!=null&&annotation.ignore()==false){
                        RWSPropertyInfos.add(RWSPropertyInfo);
                    }
                }


            }
        }
        return RWSPropertyInfos;
    }

    public static List<RWSPropertyInfo> getNotArrayProperty(Class aClass){
        List<RWSPropertyInfo> RWSPropertyInfos =new ArrayList<RWSPropertyInfo>();
        Field[] fields= aClass.getDeclaredFields();
        if(fields!=null&&fields.length>0){
            for(int i=0;i<fields.length;i++){
                Field field=fields[i];
                if(field.getGenericType().toString().startsWith("java.util.List<")){
                    continue;
                }

                if(RWSPropertyInfo.checkFieldCanOrm(field.getGenericType().toString())){
                    RWSPropertyInfo RWSPropertyInfo =new RWSPropertyInfo();
                    RWSPropertyInfo.setName(field.getName());
                    RWSPropertyInfo.setType(field.getGenericType().toString());
                    RWSPropertyInfo.setField(field);
                    RWSColum annotation=field.getAnnotation(RWSColum.class);
                    if(annotation!=null&&!annotation.ignore()){
                        RWSPropertyInfo.setAlias(annotation.alias());
                        RWSPropertyInfo.setKey(annotation.isKey());
                    }
                    if(annotation==null||annotation!=null&&annotation.ignore()==false){
                        RWSPropertyInfos.add(RWSPropertyInfo);
                    }
                }


            }
        }
        return RWSPropertyInfos;
    }

    public static List<RWSPropertyInfo> getArrayProperty(Class aClass){
        List<RWSPropertyInfo> RWSPropertyInfos =new ArrayList<RWSPropertyInfo>();
        Field[] fields= aClass.getDeclaredFields();
        if(fields!=null&&fields.length>0){
            for(int i=0;i<fields.length;i++){
                Field field=fields[i];
                if(!field.getGenericType().toString().startsWith("java.util.List<")){
                    continue;
                }

                if(RWSPropertyInfo.checkFieldCanOrm(field.getGenericType().toString())){
                    RWSPropertyInfo RWSPropertyInfo =new RWSPropertyInfo();
                    RWSPropertyInfo.setName(field.getName());
                    RWSPropertyInfo.setType(field.getGenericType().toString());
                    RWSPropertyInfo.setField(field);
                    RWSColum annotation=field.getAnnotation(RWSColum.class);
                    if(annotation!=null&&!annotation.ignore()){
                        RWSPropertyInfo.setAlias(annotation.alias());
                        RWSPropertyInfo.setKey(annotation.isKey());
                    }
                    if(annotation==null||annotation!=null&&annotation.ignore()==false){
                        RWSPropertyInfos.add(RWSPropertyInfo);
                    }
                }


            }
        }
        return RWSPropertyInfos;
    }

    public static  boolean   compareObjectWithoutArrayProp(Object a,Object b) {
        try {
            if(a==null||b==null){
                return  false;
            }
            if(!a.getClass().equals(b.getClass())){
                return false;
            }
            boolean flag=true;
            RWSClassInfo aRWSClassInfo =   RWSClassInfoCache.getInstance().getRWSClassInfo(a.getClass());
            List<RWSPropertyInfo> RWSPropertyInfos = aRWSClassInfo.getNotArrayProperties();
            for(int index = 0; index< RWSPropertyInfos.size(); index++){
                RWSPropertyInfo RWSPropertyInfo = RWSPropertyInfos.get(index);
                Field field= RWSPropertyInfo.getField();
                field.setAccessible(true);
                if(RWSPropertyInfo.getType()> RWSPropertyInfo.PROPERTYTYPE_SHORT&& RWSPropertyInfo.getType()<= RWSPropertyInfo.PROPERTYTYPE_STRING){
                    Object avalue=  field.get(a);
                    Object bvalue= field.get(b);
                    if((avalue==null&&bvalue!=null)||(avalue!=null&&bvalue==null)||(avalue!=null&&bvalue!=null&&!avalue.equals(bvalue))){
                        flag=false;
                    }
                }
            }
            return  flag;
        }catch (Exception ex){
            ex.printStackTrace();
            return  false;
        }

    }

}
