package com.codingdie.rwsdatabase.orm;

import android.database.Cursor;

import com.codingdie.rwsdatabase.orm.cache.RWSClassInfoCache;
import com.codingdie.rwsdatabase.orm.cache.model.RWSClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.RWSPropertyInfo;
import com.codingdie.rwsdatabase.orm.util.RWSReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2016/10/12.
 */
public class RWSCursorResultReflectUtil {

    public static <T> T toObject(Cursor cursor, Class<T> tClass,String[]... ignoreProps) {
        try {
            if (cursor.getCount() > 0) {
                T objFinal=null;
                while(cursor.moveToNext()){
                	 T tmp=fillOneObject(cursor,tClass);
                    if(objFinal==null){
                        objFinal=tmp;
                    }else{
                        if(!RWSReflectUtil.compareObjectWithoutArrayProp(tmp,objFinal)){
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
                    if(!RWSReflectUtil.compareObjectWithoutArrayProp(tmp,obj)){
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
             List<RWSPropertyInfo> RWSPropertyInfos = RWSClassInfoCache.getInstance().getRWSClassInfo(a.getClass()).getArrayProperties();
             if(RWSPropertyInfos !=null&& RWSPropertyInfos.size()>0){
                 for(RWSPropertyInfo RWSPropertyInfo : RWSPropertyInfos){
                    Field field= RWSPropertyInfo.getField();
                     field.setAccessible(true);
                     List lista=(List) field.get(a);
                     List listb=(List) field.get(b);
                     if(listb!=null&&listb.size()>0){
                         for(Object newItem :listb){
                             boolean flag=false;
                             for(Object item :lista){
                                 if(RWSReflectUtil.compareObjectWithoutArrayProp(item,newItem)){
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
            RWSClassInfo RWSClassInfo = RWSClassInfoCache.getInstance().getRWSClassInfo(tClass);
            if (RWSClassInfo.getProperties() != null && RWSClassInfo.getProperties().size() > 0) {
                for (RWSPropertyInfo RWSPropertyInfo : RWSClassInfo.getProperties()) {
                    List<String> alias = RWSPropertyInfo.getAlias();
                    int index = getIndexFromCousorByAlias(alias, cursor);
                    Field field = RWSPropertyInfo.getField();
                    field.setAccessible(true);
                    if (index != -1) {

                        if (RWSPropertyInfo.getType() == RWSPropertyInfo.PROPERTYTYPE_SHORT) {
                            field.set(o, cursor.getShort(index));
                        } else if (RWSPropertyInfo.getType() == RWSPropertyInfo.PROPERTYTYPE_SHORT) {
                            field.set(o, cursor.getShort(index));
                        } else if (RWSPropertyInfo.getType() == RWSPropertyInfo.PROPERTYTYPE_INT) {
                            field.set(o, cursor.getInt(index));
                        } else if (RWSPropertyInfo.getType() == RWSPropertyInfo.PROPERTYTYPE_LONG) {
                            field.set(o, cursor.getLong(index));
                        } else if (RWSPropertyInfo.getType() == RWSPropertyInfo.PROPERTYTYPE_FLOAT) {
                            field.set(o, cursor.getFloat(index));
                        } else if (RWSPropertyInfo.getType() == RWSPropertyInfo.PROPERTYTYPE_DOUBLE) {
                            field.set(o, cursor.getDouble(index));
                        } else if (RWSPropertyInfo.getType() == RWSPropertyInfo.PROPERTYTYPE_STRING) {
                            field.set(o, cursor.getString(index));
                        }

                    } else {
                        if (RWSPropertyInfo.getType() == RWSPropertyInfo.PROPERTYTYPE_COLLECTION) {
                            List list = new ArrayList();
                            Object object = fillOneObject(cursor, RWSPropertyInfo.getCollectionItemClass());
                            if (object != null) {
                                list.add(object);
                            }
                            field.set(o, list);
                        }else{
                            if (RWSPropertyInfo.isKey()) {
                                return null;
                            }
                        }

                    }

                }
            }
           if(RWSObjectUtil.checkKeyPropertyIsNull(o)==true){
               return  null;
           }
            return (T) o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
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
