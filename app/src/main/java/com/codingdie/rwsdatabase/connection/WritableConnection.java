package com.codingdie.rwsdatabase.connection;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.codingdie.rwsdatabase.exception.RWSOrmException;
import com.codingdie.rwsdatabase.orm.cache.RWSClassInfoCache;
import com.codingdie.rwsdatabase.orm.cache.model.RWSClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.RWSPropertyInfo;
import com.codingdie.rwsdatabase.orm.util.RWSObjectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2016/8/26.
 */
public class WritableConnection extends ReadableConnection {

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
    public long insert(String table, String nullColumnHack, ContentValues values)
            throws SQLException {
        return this.sqLiteDatabase.insertOrThrow(table, nullColumnHack, values);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return this.sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    public <T> void insertIntoTable(T object, String tableName) {
        if (TextUtils.isEmpty(tableName)) {
            throw new RWSOrmException(RWSOrmException.NO_RWSTABLE_ANNOTATION);
        }
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

    public <T> void insert(T object) {
        RWSClassInfo rwsClassInfo = RWSClassInfoCache.getInstance().getRWSClassInfo(object.getClass());
        String tableName = rwsClassInfo.getTableName();
        if (TextUtils.isEmpty(tableName)) {
            throw new RWSOrmException(RWSOrmException.NO_RWSTABLE_ANNOTATION);
        }
        this.insertIntoTable(object, tableName);
    }

    public <T> int update(T object) {
        RWSClassInfo rwsClassInfo = RWSClassInfoCache.getInstance().getRWSClassInfo(object.getClass());
        String tableName = rwsClassInfo.getTableName();
        if (TextUtils.isEmpty(tableName)) {
            throw new RWSOrmException(RWSOrmException.NO_RWSTABLE_ANNOTATION);
        }
        if (!rwsClassInfo.hasKeyProperty()) {
            throw new RWSOrmException(RWSOrmException.NO_KEY_PROPERTY);
        }
        if (RWSObjectUtil.checkKeyPropertyIsNull(object)) {
            throw new RWSOrmException(RWSOrmException.KEY_PROPERTY_IS_NULL);
        }
        int affectCount=0;
        try {
            RWSTableInfo rwsTableInfo = RWSTableCache.getInstance().getRWSClassInfo(tableName, this);
            ContentValues contentValues = new ContentValues();

            for (RWSColumInfo colum : rwsTableInfo.getColums()) {
                RWSPropertyInfo relatedRwsPropertyInfo = rwsClassInfo.getProperty(colum.getName());
                if (relatedRwsPropertyInfo == null) {
                    contentValues.putNull(colum.getName());
                } else {
                    if (relatedRwsPropertyInfo.isKey()) {
                        continue;
                    }
                    Field field = relatedRwsPropertyInfo.getField();
                    field.setAccessible(true);
                    putValueIntoContentValues(contentValues, colum.getName(), field.get(object));
                }
            }
            List<RWSPropertyInfo> rwsKeyPropertyInfos = rwsClassInfo.getKeyPropertys();
            List<String> keyPropertyValues = new ArrayList<String>();
            String whereClause = "";
            for (RWSPropertyInfo rwsPropertyInfo : rwsKeyPropertyInfos) {
                whereClause += rwsPropertyInfo.getName() + "=? and";
                Field field = rwsPropertyInfo.getField();
                field.setAccessible(true);
                keyPropertyValues.add(String.valueOf(field.get(object)));
            }
            whereClause=whereClause.substring(0,whereClause.length()-3);
            affectCount=this.sqLiteDatabase.update(tableName, contentValues, whereClause, keyPropertyValues.toArray(new String[]{}));
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }finally {
            return affectCount;
        }
    }

    public <T>  int delete(T object) {
        RWSClassInfo rwsClassInfo = RWSClassInfoCache.getInstance().getRWSClassInfo(object.getClass());
        String tableName = rwsClassInfo.getTableName();
        if (TextUtils.isEmpty(tableName)) {
            throw new RWSOrmException(RWSOrmException.NO_RWSTABLE_ANNOTATION);
        }
        if (!rwsClassInfo.hasKeyProperty()) {
            throw new RWSOrmException(RWSOrmException.NO_KEY_PROPERTY);
        }
        if (RWSObjectUtil.checkKeyPropertyIsNull(object)) {
            throw new RWSOrmException(RWSOrmException.KEY_PROPERTY_IS_NULL);
        }
        int affectCount=0;
        try {
            List<RWSPropertyInfo> rwsKeyPropertyInfos = rwsClassInfo.getKeyPropertys();
            List<String> keyPropertyValues = new ArrayList<String>();
            String whereClause = "";
            for (RWSPropertyInfo rwsPropertyInfo : rwsKeyPropertyInfos) {
                whereClause += rwsPropertyInfo.getName() + "=? and";
                Field field = rwsPropertyInfo.getField();
                field.setAccessible(true);
                keyPropertyValues.add(String.valueOf(field.get(object)));
            }
            whereClause=whereClause.substring(0,whereClause.length()-3);
            affectCount=this.sqLiteDatabase.delete(tableName, whereClause, keyPropertyValues.toArray(new String[]{}));
        }catch (IllegalAccessException e){
           e.printStackTrace();
        }finally {
          return affectCount;
        }

    }

    private void putValueIntoContentValues(ContentValues contentValues, String key, Object value) {
        if (value == null) {
            contentValues.putNull(key);
        } else if (value instanceof Integer) {
            contentValues.put(key, (Integer) value);
        } else if (value instanceof Long) {
            contentValues.put(key, (Long) value);
        } else if (value instanceof Float) {
            contentValues.put(key, (Float) value);
        } else if (value instanceof String) {
            contentValues.put(key, (String) value);
        } else if (value instanceof Short) {
            contentValues.put(key, (Short) value);
        } else if (value instanceof Double) {
            contentValues.put(key, (Double) value);
        } else if (value instanceof byte[]) {
            contentValues.put(key, (byte[]) value);
        } else if (value instanceof Byte) {
            contentValues.put(key, (Byte) value);
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


}
