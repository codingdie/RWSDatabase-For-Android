package com.codingdie.rwsdatabase.orm.util;

import com.codingdie.rwsdatabase.orm.annotation.Colum;
import com.codingdie.rwsdatabase.orm.cache.ClassCache;
import com.codingdie.rwsdatabase.orm.cache.model.ClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.PropertyInfo;

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
                    Colum annotation=field.getAnnotation(Colum.class);
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
                    Colum annotation=field.getAnnotation(Colum.class);
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
                    Colum annotation=field.getAnnotation(Colum.class);
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
            ClassInfo aClassInfo=   ClassCache.getInstance().getClassInfo(a.getClass());
            List<PropertyInfo> propertyInfos=aClassInfo.getNotArrayProperties();
            for(int index=0;index<propertyInfos.size();index++){
                PropertyInfo propertyInfo=propertyInfos.get(index);
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
        @Colum(isKey = true)
        private String string;
        @Colum(isKey = true)
        private int intValue;
        @Colum(isKey = true)
        private Integer integerValue;
        @Colum(isKey = true)
        private float floatValue;
        @Colum(isKey = true)
        private Float FloatValue;
        private double doubleValue;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(Float floatValue) {
            FloatValue = floatValue;
        }

        public void setFloatValue(float floatValue) {
            this.floatValue = floatValue;
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

