package com.codingdie.rwsdatabase.connection;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.codingdie.rwsdatabase.exception.RWSOrmException;
import com.codingdie.rwsdatabase.orm.RWSObjectUtil;
import com.codingdie.rwsdatabase.orm.cache.RWSClassInfoCache;
import com.codingdie.rwsdatabase.orm.cache.model.RWSClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.RWSPropertyInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupen on 2016/8/26.
 */
public class WritableConnection extends ReadableConnection {

    public void execWriteSQL(String sql, Object[] param) {
        this.sqLiteDatabase.execSQL(sql, param);
    }

    public void execWriteSQL(String sql) {
        this.sqLiteDatabase.execSQL(sql);
    }

    @Deprecated
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return this.sqLiteDatabase.update(table, values, whereClause, whereArgs);
    }

    @Deprecated
    public long insertObject(String table, String nullColumnHack, ContentValues values)
            throws SQLException {
        return this.sqLiteDatabase.insertOrThrow(table, nullColumnHack, values);
    }

    @Deprecated
    public int delete(String table, String whereClause, String[] whereArgs) {
        return this.sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    public <T> void insertObjectIntoTable(T object, String tableName) {
        if (RWSObjectUtil.checkKeyPropertyIsNull(object)) {
            throw new RWSOrmException(RWSOrmException.KEY_PROPERTY_IS_NULL);
        }
        try {
            RWSTableInfo rwsTableInfo = RWSTableCache.getInstance().getRWSClassInfo(tableName, this);
            RWSClassInfo rwsClassInfo = RWSClassInfoCache.getInstance().getRWSClassInfo(object.getClass());
            String comlumStr = "";
            String placeholderStr = "";
            List<Object> values = new ArrayList();
            for (RWSColumInfo colum : rwsTableInfo.getColums()) {
                comlumStr += colum.getName() + ",";
                placeholderStr += "?,";
                boolean valueFlag = false;
                for (RWSPropertyInfo rwsPropertyInfo : rwsClassInfo.getProperties()) {
                    if (rwsPropertyInfo.getAlias().contains(colum.getName())) {
                        Field field = rwsPropertyInfo.getField();
                        field.setAccessible(true);
                        values.add(field.get(object));
                        valueFlag = true;
                        break;
                    }
                }
                if (valueFlag == false) {
                    values.add(null);
                }
            }
            if (comlumStr.length() > 0) {
                comlumStr = comlumStr.substring(0, comlumStr.length() - 1);
                placeholderStr = placeholderStr.substring(0, placeholderStr.length() - 1);
            }
            this.sqLiteDatabase.execSQL("insert into " + tableName + " (" + comlumStr + ") values (" + placeholderStr + ")", values.toArray());
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public <T> void insertObject(T object) {
        RWSClassInfo rwsClassInfo = RWSClassInfoCache.getInstance().getRWSClassInfo(object.getClass());
        this.insertObjectIntoTable(object, rwsClassInfo.getTableName());
    }


    public <T> void updateObject(T object) {
        if (RWSObjectUtil.checkKeyPropertyIsNull(object)) {
            throw new RWSOrmException(RWSOrmException.KEY_PROPERTY_IS_NULL);
        }

        RWSClassInfo rwsClassInfo = RWSClassInfoCache.getInstance().getRWSClassInfo(object.getClass());
        String tableName = rwsClassInfo.getTableName();
        if (TextUtils.isEmpty(tableName)) {
            throw new RWSOrmException(RWSOrmException.NO_RWSTABLE_ANNOTATION);
        }
        try {
            RWSTableInfo rwsTableInfo = RWSTableCache.getInstance().getRWSClassInfo(tableName, this);
            ContentValues contentValues=new ContentValues();

            for (RWSColumInfo colum : rwsTableInfo.getColums()) {

                Object value=null;
                RWSPropertyInfo relatedRwsPropertyInfo=null;
                for (RWSPropertyInfo rwsPropertyInfo : rwsClassInfo.getProperties()) {
                    if (rwsPropertyInfo.getAlias().contains(colum.getName())) {
                        Field field = rwsPropertyInfo.getField();
                        field.setAccessible(true);
                        value=field.get(object);
                        relatedRwsPropertyInfo=rwsPropertyInfo;
                        break;
                    }
                }
                if(value!=null){
                    if (relatedRwsPropertyInfo == null) {
                          if(relatedRwsPropertyInfo.isKey()){
                              continue;
                          }
                    }
                     if(value instanceof  Integer){
                         contentValues.put(colum.getName(),(Integer)value);
                     }else  if(value instanceof  Long){
                         contentValues.put(colum.getName(),(Long)value);
                     }else  if(value instanceof  Float){
                         contentValues.put(colum.getName(),(Float)value);
                     }else  if(value instanceof  String){
                         contentValues.put(colum.getName(),(String)value);
                     }else  if(value instanceof  Short){
                         contentValues.put(colum.getName(),(Short)value);
                     } else  if(value instanceof  Double){
                         contentValues.put(colum.getName(),(Double)value);
                     } else  if(value instanceof  byte[]){
                         contentValues.put(colum.getName(),(byte[])value);
                     }else  if(value instanceof  Byte){
                         contentValues.put(colum.getName(),(Byte)value);
                     }
                }else{
                    if (relatedRwsPropertyInfo == null) {
                        if(relatedRwsPropertyInfo.isKey()){
                            continue;
                        }
                    }
                    contentValues.putNull(colum.getName());
                }

            }
//            this.sqLiteDatabase.update(tableName,contentValues,)
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    @Deprecated
    public void beginTransaction() {
        this.sqLiteDatabase.beginTransaction();
    }

    @Deprecated
    public void endTransaction() {
        this.sqLiteDatabase.endTransaction();
    }

    @Deprecated
    public void setTransactionSuccessful() {
        this.sqLiteDatabase.setTransactionSuccessful();
    }

    @Deprecated
    public void setVersion(int version) {
        this.sqLiteDatabase.setVersion(version);
    }

    protected static WritableConnection createWritableConnection(String dbPath, int index) {
        WritableConnection writableConnection = new WritableConnection();
        writableConnection.setInUsing(false);
        writableConnection.setWritable(true);
        writableConnection.setIndex(index);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
        sqLiteDatabase.enableWriteAheadLogging();
        writableConnection.setSqLiteDatabase(sqLiteDatabase);
        return writableConnection;
    }


}
