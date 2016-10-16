package com.codingdie.rwsdatabase.orm.util;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2016/9/28.
 */
public class RWSCursorUtil {
   public   <T> T toObjct(Class<T> aClass, Cursor cursor) throws  Exception {
       if(cursor.getCount()>0){
           T t= aClass.newInstance();
           if(cursor.moveToNext()){

           }
           return  t;
       }else {
           return  null;
       }
   }
   public List<String> getAllConumn(Cursor cursor){
       List<String> strings=new ArrayList<String>();
       for(int i=0;i<cursor.getColumnCount();i++){
           strings.add(cursor.getColumnName(i));
       }
       return strings;
   }
}
