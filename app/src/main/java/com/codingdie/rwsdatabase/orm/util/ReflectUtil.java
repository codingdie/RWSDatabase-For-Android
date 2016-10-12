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
            ClassInfo aClassInfo=   ClassCache.getInstance().get(a.getClass());
            List<PropertyInfo> propertyInfos=aClassInfo.getNotArrayProperties();
            for(int i=0;i<propertyInfos.size();i++){
                PropertyInfo propertyInfo=propertyInfos.get(i);
                Field field= propertyInfo.getField();
                field.setAccessible(true);
                 if(propertyInfo.getType()>PropertyInfo.PROPERTYTYPE_SHORT&&propertyInfo.getType()<=PropertyInfo.PROPERTYTYPE_STRING){
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

    public static void main(String[] args) throws  Exception{
       TestClass testClass2=new  TestClass();
        testClass2.setString("a");
        testClass2.setIntValue(1);
        testClass2.setIntegerValue(1);
        TestClass testClass1=new  TestClass();
        testClass1.setString("a");
        testClass1.setIntValue(1);
        testClass1.setIntegerValue(1);
        System.out.println(compareObjectWithoutArrayProp(testClass1,testClass2));
    }

    public static   class TestClass{
        private String string;
        private int intValue;
        private Integer integerValue;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public Integer getIntegerValue() {
            return integerValue;
        }

        public void setIntegerValue(Integer integerValue) {
            this.integerValue = integerValue;
        }
    }
}

