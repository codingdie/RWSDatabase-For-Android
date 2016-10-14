package com.codingdie.rwsdatabase.orm.cache.model;

import android.text.TextUtils;
import com.codingdie.rwsdatabase.orm.annotation.Table;
import com.codingdie.rwsdatabase.orm.util.ReflectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupen on 2016/9/28.
 */
public class ClassInfo {

    private List<PropertyInfo> properties=new ArrayList<PropertyInfo>();
    private List<Integer> keyPropertyIndexes=new ArrayList<Integer>();
    private String tableName;

    public List<PropertyInfo> getNotArrayProperties() {
      List<PropertyInfo> propertyInfos=new ArrayList<PropertyInfo>();
        for(int i=0;i<properties.size();i++){
            if(properties.get(i).getType()==PropertyInfo.PROPERTYTYPE_COLLECTION){
                continue;
            }
            propertyInfos.add(properties.get(i));
        }
        return propertyInfos;
    }
    public List<PropertyInfo> getArrayProperties() {
        List<PropertyInfo> propertyInfos=new ArrayList<PropertyInfo>();
        for(int i=0;i<properties.size();i++){
            if(properties.get(i).getType()==PropertyInfo.PROPERTYTYPE_COLLECTION){
                propertyInfos.add(properties.get(i));
            }
        }
        return propertyInfos;
    }
    public List<PropertyInfo> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyInfo> properties) {
        this.properties = properties;
    }

    public List<Integer> getKeyPropertyIndexes() {
        return keyPropertyIndexes;
    }

    public void setKeyPropertyIndexes(List<Integer> keyPropertyIndexes) {
        this.keyPropertyIndexes = keyPropertyIndexes;
    }
    public static ClassInfo newInstance(Class aClass){
        ClassInfo classInfo=new ClassInfo();
        classInfo.setProperties(ReflectUtil.getAllProperty(aClass));
        for(int i=0;i<classInfo.getProperties().size();i++){
            if(classInfo.getProperties().get(i).isKey()){
                classInfo.getKeyPropertyIndexes().add(i);
            }
        }
        Table table=  (Table) aClass.getAnnotation(Table.class);
        if(table!=null){
            String tableName=table.name();
            if(!TextUtils.isEmpty(tableName)){
                classInfo.setTableName(tableName);
            }
        }
        return  classInfo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}