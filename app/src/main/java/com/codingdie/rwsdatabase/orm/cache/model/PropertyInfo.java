package com.codingdie.rwsdatabase.orm.cache.model;

import com.codingdie.rwsdatabase.orm.cache.ClassCache;

import java.lang.reflect.Field;

/**
 * Created by xupen on 2016/9/28.
 */
public class PropertyInfo {


    private String name;
    private int type=-1;
    private String collectionItemClass;
    private Field field;

    private boolean isKey;
    private String[] alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }

    public String[] getAlias() {
        return alias;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public int getType() {
        return type;
    }

    public void setType(String typeStr) {
        if(typeStr.equals(propertyTypeStrs[0])){
            this.type=PROPERTYTYPE_SHORT;
        }else    if(typeStr.equals(propertyTypeStrs[1])){
            this.type=PROPERTYTYPE_INT;
        }else    if(typeStr.equals(propertyTypeStrs[2])){
            this.type=PROPERTYTYPE_LONG;
        }else    if(typeStr.equals(propertyTypeStrs[3])){
            this.type=PROPERTYTYPE_FLOAT;
        }else    if(typeStr.equals(propertyTypeStrs[4])){
            this.type=PROPERTYTYPE_DOUBLE;
        }else    if(typeStr.equals(propertyTypeStrs[5])){
            this.type=PROPERTYTYPE_STRING;
        }else    if(typeStr.equals(propertyTypeStrs[6])){
            this.type=PROPERTYTYPE_SHORT;
        }else    if(typeStr.equals(propertyTypeStrs[7])){
            this.type=PROPERTYTYPE_INT;
        }else    if(typeStr.equals(propertyTypeStrs[8])){
            this.type=PROPERTYTYPE_LONG;
        }else    if(typeStr.equals(propertyTypeStrs[9])){
            this.type=PROPERTYTYPE_FLOAT;
        }else    if(typeStr.equals(propertyTypeStrs[10])){
            this.type=PROPERTYTYPE_DOUBLE;
        } else if(typeStr.startsWith("java.util.List<")){
            try {
                collectionItemClass=Class.forName(typeStr.substring(15,typeStr.length()-1)).toString();
                this.type= PROPERTYTYPE_COLLECTION;
            }catch (Exception ex){
                collectionItemClass=null;
            }
        }
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public static boolean  checkFieldCanOrm(String typeStr) {
        if(typeStr.equals(propertyTypeStrs[0])){
             return  true;
        }else    if(typeStr.equals(propertyTypeStrs[1])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[2])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[3])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[4])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[5])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[6])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[7])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[8])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[9])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[10])){
            return  true;
        }else if(typeStr.startsWith("java.util.List<")){
            try {
                Class.forName(typeStr.substring(15,typeStr.length()-1));
                return  true;
            }catch (Exception ex){
                return  false;
            }
        }
        return  false;
    }
    public static boolean  checkFieldIsNum(String typeStr) {
        if(typeStr.equals(propertyTypeStrs[0])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[1])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[2])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[3])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[4])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[6])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[7])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[8])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[9])){
            return  true;
        }else    if(typeStr.equals(propertyTypeStrs[10])){
            return  true;
        }
        return  false;
    }

    private final static String[] propertyTypeStrs = {
            "class java.lang.Short",
            "class java.lang.Integer",
            "class java.lang.Long",

            "class java.lang.Float",
            "class java.lang.Double",

            "class java.lang.String",

            "short",
            "int",
            "long",

            "float",
            "double",
    };
    public static  final int PROPERTYTYPE_SHORT=0;
    public static  final int PROPERTYTYPE_INT=1;
    public static  final int PROPERTYTYPE_LONG=2;

    public static  final int PROPERTYTYPE_FLOAT=3;
    public static  final int PROPERTYTYPE_DOUBLE=4;

    public static  final int PROPERTYTYPE_STRING=5;

    public static  final int PROPERTYTYPE_OBJECT=6;
    public static  final int PROPERTYTYPE_COLLECTION =7;

}
