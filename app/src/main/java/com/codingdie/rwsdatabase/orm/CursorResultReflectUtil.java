package com.codingdie.rwsdatabase.orm;

import android.database.Cursor;
import android.util.Log;

import com.codingdie.rwsdatabase.orm.cache.ClassCache;
import com.codingdie.rwsdatabase.orm.cache.model.ClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.PropertyInfo;
import com.codingdie.rwsdatabase.orm.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupen on 2016/10/12.
 */
public class CursorResultReflectUtil {

    public static <T> T toObject(Cursor cursor, Class<T> tClass,String[]... ignoreProps) {
        try {
            if (cursor.getCount() > 0) {
                T objFinal=null;
                while(cursor.moveToNext()){
                	 T tmp=fillOneObject(cursor,tClass);
                    if(objFinal==null){
                        objFinal=tmp;
                    }else{
                        if(!ReflectUtil.compareObjectWithoutArrayProp(tmp,objFinal)){
                             break;
                        }
                        addArrayPropertyFromBToA(objFinal,tmp);

                    }
                }
                cursor.close();
                return  objFinal;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static <E> List<E>  toList(Cursor cursor, Class<E> tClass,String[]... ignoreProps){
        List<E> list=new ArrayList<E>();
        try {
            E obj=null;
            while(cursor.moveToNext()){
                E tmp=fillOneObject(cursor,tClass);
                if(tmp==null){
                	break;
                }
                if(obj==null){
                    obj=tmp;
                    list.add(obj);
                }else{
                    if(!ReflectUtil.compareObjectWithoutArrayProp(tmp,obj)){
                        obj=tmp;
                        list.add(obj);
                    }
                    addArrayPropertyFromBToA(obj,tmp);
                }
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return list;
        }
    }

    private static void addArrayPropertyFromBToA(Object a, Object b) {
        try {
             List<PropertyInfo> propertyInfos=ClassCache.getInstance().getClassInfo(a.getClass()).getArrayProperties();
             if(propertyInfos!=null&&propertyInfos.size()>0){
                 for(PropertyInfo propertyInfo :propertyInfos){
                    Field field= propertyInfo.getField();
                     field.setAccessible(true);
                     List lista=(List) field.get(a);
                     List listb=(List) field.get(b);
                     if(listb!=null&&listb.size()>0){
                         for(Object newItem :listb){
                             boolean flag=false;
                             for(Object item :lista){
                                 if(ReflectUtil.compareObjectWithoutArrayProp(item,newItem)){
                                     flag=true;
                                     break;
                                 }
                             }
                             if(!flag){
                                 lista.add(newItem);
                                 field.set(a,lista);
                             }
                         }

                     }
                 }
             }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static <T> T fillOneObject(Cursor cursor, Class<T> tClass) {
        try {
            Object o = tClass.newInstance();
            ClassInfo classInfo = ClassCache.getInstance().getClassInfo(tClass);
            if (classInfo.getProperties() != null && classInfo.getProperties().size() > 0) {
                for (PropertyInfo propertyInfo : classInfo.getProperties()) {
                    List<String> alias = propertyInfo.getAlias();
                    int index = getIndexFromCousorByAlias(alias, cursor);
                    Field field = propertyInfo.getField();
                    field.setAccessible(true);
                    if (index != -1) {

                        if (propertyInfo.getType() == PropertyInfo.PROPERTYTYPE_SHORT) {
                            field.set(o, cursor.getShort(index));
                        } else if (propertyInfo.getType() == PropertyInfo.PROPERTYTYPE_SHORT) {
                            field.set(o, cursor.getShort(index));
                        } else if (propertyInfo.getType() == PropertyInfo.PROPERTYTYPE_INT) {
                            field.set(o, cursor.getInt(index));
                        } else if (propertyInfo.getType() == PropertyInfo.PROPERTYTYPE_LONG) {
                            field.set(o, cursor.getLong(index));
                        } else if (propertyInfo.getType() == PropertyInfo.PROPERTYTYPE_FLOAT) {
                            field.set(o, cursor.getFloat(index));
                        } else if (propertyInfo.getType() == PropertyInfo.PROPERTYTYPE_DOUBLE) {
                            field.set(o, cursor.getDouble(index));
                        } else if (propertyInfo.getType() == PropertyInfo.PROPERTYTYPE_STRING) {
                            field.set(o, cursor.getString(index));
                        }

                    } else {
                        if (propertyInfo.getType() == PropertyInfo.PROPERTYTYPE_COLLECTION) {
                            List list = new ArrayList();
                            Object object = fillOneObject(cursor, propertyInfo.getCollectionItemClass());
                            if (object != null) {
                                list.add(object);
                            }
                            field.set(o, list);
                        }else{
                            if (propertyInfo.isKey()) {
                                return null;
                            }
                        }

                    }

                }
            }
           if(checkKeyPropertyIsNull(o)==true){
               return  null;
           }
            return (T) o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private static boolean checkKeyPropertyIsNull(Object obj) {
        try {
            ClassInfo classInfo =ClassCache.getInstance().getClassInfo(obj.getClass());
            List<Integer> integers = classInfo.getKeyPropertyIndexes();
            List<PropertyInfo> propertyInfoList = classInfo.getProperties();

            if (integers != null && integers.size() != 0) {
                for (int i : integers) {
                    PropertyInfo propertyInfo = propertyInfoList.get(i);
                    Field field = propertyInfo.getField();
                    field.setAccessible(true);
                    Object o = field.get(obj);
                    if(o==null){
                        return  true;
                    }
                    if(propertyInfo.getType()==PropertyInfo.PROPERTYTYPE_STRING){
                        if(((String) o).length()==0){
                            return  true;
                        }
                    }
                    if(propertyInfo.getType()==PropertyInfo.PROPERTYTYPE_SHORT||propertyInfo.getType()==PropertyInfo.PROPERTYTYPE_INT){
                        if(((Comparable)o).compareTo(0)==0){
                            return  true;
                        }
                    }
                    if(propertyInfo.getType()==PropertyInfo.PROPERTYTYPE_LONG){
                    	Long S=00l;
                    	S.compareTo(123L);
                        if(((Comparable)o).compareTo(0L)==0){
                            return  true;
                        }
                    }
                    if(propertyInfo.getType()==PropertyInfo.PROPERTYTYPE_FLOAT){
                        if(((Comparable)o).compareTo(0.00f)==0){
                            return  true;
                        }
                    }
                    if(propertyInfo.getType()==PropertyInfo.PROPERTYTYPE_DOUBLE){
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

    private static int getIndexFromCousorByAlias(List<String> alias, Cursor cursor) {
        int index=-1;
        for (String alia : alias) {
            index = getColumnIndexWhenComlunmMaybeRepeat(cursor, alia);
            if (index != -1) {
                 break;
             }
        }
        return index;
    }


    private static int getColumnIndexWhenComlunmMaybeRepeat(Cursor cursor, String name) {
        int index=-1;
        
        for(int i=0;i<cursor.getColumnCount();i++){
            if(cursor.getColumnName(i).equals(name)){
                String string = cursor.getString(i);
                if( string!=null&&string.trim().length()>0){
                    index=i;
                 break;
                }
            }
        }
        return index;
    }

}
