package com.codingdie.rwsdatabase.orm.util;

import com.codingdie.rwsdatabase.orm.annotation.ColumAnnotation;
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

    public static void main(String[] args) {
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(getAllProperty(TestClass.class)));
    }
}
