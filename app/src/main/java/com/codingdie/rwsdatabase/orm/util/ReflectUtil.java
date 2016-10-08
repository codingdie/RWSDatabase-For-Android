package com.codingdie.rwsdatabase.orm.util;

import com.codingdie.rwsdatabase.orm.annotation.ColumAnnotation;
import com.codingdie.rwsdatabase.orm.cache.ClassCache;
import com.codingdie.rwsdatabase.orm.cache.model.ClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.PropertyInfo;
import com.codingdie.rwsdatabase.orm.cache.model.TestClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupen on 2016/9/28.
 */
public class ReflectUtil {
    public static List<PropertyInfo> getAllProperty(Class aClass){
        List<PropertyInfo> propertyInfos=new ArrayList<PropertyInfo>();
        Field[] fields= aClass.getDeclaredFields();
        if(fields!=null&&fields.length>0){
            for(int i=0;i<fields.length;i++){
                Field field=fields[i];
                if(PropertyInfo.checkFieldCanOrm(field.getGenericType().toString())){
                    PropertyInfo propertyInfo=new PropertyInfo();
                    propertyInfo.setName(field.getName());
                    propertyInfo.setType(field.getGenericType().toString());
                    propertyInfo.setField(field);
                    ColumAnnotation annotation=field.getAnnotation(ColumAnnotation.class);
                    if(annotation!=null&&!annotation.ignore()){
                        propertyInfo.setAlias(annotation.alias());
                        propertyInfo.setKey(annotation.isKey());
                    }
                    if(annotation==null||annotation!=null&&annotation.ignore()==false){
                        propertyInfos.add(propertyInfo);
                    }
                }


            }
        }
        return  propertyInfos;
    }

    public static List<PropertyInfo> getNotArrayProperty(Class aClass){
        List<PropertyInfo> propertyInfos=new ArrayList<PropertyInfo>();
        Field[] fields= aClass.getDeclaredFields();
        if(fields!=null&&fields.length>0){
            for(int i=0;i<fields.length;i++){
                Field field=fields[i];
                if(field.getGenericType().toString().startsWith("java.util.List<")){
                  continue;
                }

                if(PropertyInfo.checkFieldCanOrm(field.getGenericType().toString())){
                    PropertyInfo propertyInfo=new PropertyInfo();
                    propertyInfo.setName(field.getName());
                    propertyInfo.setType(field.getGenericType().toString());
                    propertyInfo.setField(field);
                    ColumAnnotation annotation=field.getAnnotation(ColumAnnotation.class);
                    if(annotation!=null&&!annotation.ignore()){
                        propertyInfo.setAlias(annotation.alias());
                        propertyInfo.setKey(annotation.isKey());
                    }
                    if(annotation==null||annotation!=null&&annotation.ignore()==false){
                        propertyInfos.add(propertyInfo);
                    }
                }


            }
        }
        return  propertyInfos;
    }

    public static List<PropertyInfo> getArrayProperty(Class aClass){
        List<PropertyInfo> propertyInfos=new ArrayList<PropertyInfo>();
        Field[] fields= aClass.getDeclaredFields();
        if(fields!=null&&fields.length>0){
            for(int i=0;i<fields.length;i++){
                Field field=fields[i];
                if(!field.getGenericType().toString().startsWith("java.util.List<")){
                    continue;
                }

                if(PropertyInfo.checkFieldCanOrm(field.getGenericType().toString())){
                    PropertyInfo propertyInfo=new PropertyInfo();
                    propertyInfo.setName(field.getName());
                    propertyInfo.setType(field.getGenericType().toString());
                    propertyInfo.setField(field);
                    ColumAnnotation annotation=field.getAnnotation(ColumAnnotation.class);
                    if(annotation!=null&&!annotation.ignore()){
                        propertyInfo.setAlias(annotation.alias());
                        propertyInfo.setKey(annotation.isKey());
                    }
                    if(annotation==null||annotation!=null&&annotation.ignore()==false){
                        propertyInfos.add(propertyInfo);
                    }
                }


            }
        }
        return  propertyInfos;
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
            ClassInfo aClassInfo=  ClassCache.getInstance().get(a.getClass());
            List<PropertyInfo> propertyInfos=aClassInfo.getNotArrayProperties();
            for(int i=0;i<propertyInfos.size();i++){
                PropertyInfo propertyInfo=propertyInfos.get(i);
                Field field= propertyInfo.getField();
                 if(propertyInfo.getType()==PropertyInfo.PROPERTYTYPE_STRING){
                    String avalue= (String) field.get(a);
                    String bvalue= (String) field.get(b);
                    if((avalue==null&&bvalue!=null)||(avalue!=null&&bvalue==null)||(avalue!=null&&bvalue!=null&&!avalue.equals(bvalue))){
                        flag=false;
                    }
                }else{
                     Number avalue= (Number) field.get(a);
                     Number bvalue= (Number) field.get(b);
                     if((avalue==null&&bvalue!=null)||(avalue!=null&&bvalue==null)||(avalue!=null&&bvalue!=null&&!avalue.equals(bvalue))){
                         flag=false;
                     }
                 }
            }
            return  flag;
        }catch (Exception ex){
            return  false;
        }

    }
}
