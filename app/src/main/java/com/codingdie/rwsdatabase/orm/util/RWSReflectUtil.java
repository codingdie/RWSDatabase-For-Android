package com.codingdie.rwsdatabase.orm.util;

import com.codingdie.rwsdatabase.orm.annotation.RWSColum;
import com.codingdie.rwsdatabase.orm.cache.RWSClassInfoCache;
import com.codingdie.rwsdatabase.orm.cache.model.RWSClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.RWSPropertyInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupen on 2016/9/28.
 */
public class RWSReflectUtil {
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
        @RWSColum(isKey = true)
        private String string;
        @RWSColum(isKey = true)
        private int intValue;
        @RWSColum(isKey = true)
        private Integer integerValue;
        @RWSColum(isKey = true)
        private float floatValue;
        @RWSColum(isKey = true)
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

