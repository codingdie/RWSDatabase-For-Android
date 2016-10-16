package com.codingdie.rwsdatabase.orm.cache.model;

import android.text.TextUtils;
import com.codingdie.rwsdatabase.orm.annotation.RWSTable;
import com.codingdie.rwsdatabase.orm.util.RWSReflectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2016/9/28.
 */
public class RWSClassInfo {

    private List<RWSPropertyInfo> properties=new ArrayList<RWSPropertyInfo>();
    private List<Integer> keyPropertyIndexes=new ArrayList<Integer>();
    private String tableName;

    public List<RWSPropertyInfo> getNotArrayProperties() {
      List<RWSPropertyInfo> RWSPropertyInfos =new ArrayList<RWSPropertyInfo>();
        for(int i=0;i<properties.size();i++){
            if(properties.get(i).getType()== RWSPropertyInfo.PROPERTYTYPE_COLLECTION){
                continue;
            }
            RWSPropertyInfos.add(properties.get(i));
        }
        return RWSPropertyInfos;
    }
    public List<RWSPropertyInfo> getArrayProperties() {
        List<RWSPropertyInfo> RWSPropertyInfos =new ArrayList<RWSPropertyInfo>();
        for(int i=0;i<properties.size();i++){
            if(properties.get(i).getType()== RWSPropertyInfo.PROPERTYTYPE_COLLECTION){
                RWSPropertyInfos.add(properties.get(i));
            }
        }
        return RWSPropertyInfos;
    }
    public List<RWSPropertyInfo> getProperties() {
        return properties;
    }

    public void setProperties(List<RWSPropertyInfo> properties) {
        this.properties = properties;
    }

    public List<Integer> getKeyPropertyIndexes() {
        return keyPropertyIndexes;
    }

    public void setKeyPropertyIndexes(List<Integer> keyPropertyIndexes) {
        this.keyPropertyIndexes = keyPropertyIndexes;
    }
    public static RWSClassInfo newInstance(Class aClass){
        RWSClassInfo RWSClassInfo =new RWSClassInfo();
        RWSClassInfo.setProperties(RWSReflectUtil.getAllProperty(aClass));
        for(int i = 0; i< RWSClassInfo.getProperties().size(); i++){
            if(RWSClassInfo.getProperties().get(i).isKey()){
                RWSClassInfo.getKeyPropertyIndexes().add(i);
            }
        }
        RWSTable RWSTable =  (RWSTable) aClass.getAnnotation(RWSTable.class);
        if(RWSTable !=null){
            String tableName= RWSTable.name();
            if(!TextUtils.isEmpty(tableName)){
                RWSClassInfo.setTableName(tableName);
            }
        }
        return RWSClassInfo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}