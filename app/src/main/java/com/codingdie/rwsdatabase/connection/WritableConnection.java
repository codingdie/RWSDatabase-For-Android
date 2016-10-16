package com.codingdie.rwsdatabase.connection;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.codingdie.rwsdatabase.exception.RWSOrmException;
import com.codingdie.rwsdatabase.orm.RWSObjectUtil;
import com.codingdie.rwsdatabase.orm.cache.RWSClassInfoCache;
import com.codingdie.rwsdatabase.orm.cache.model.RWSClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.RWSPropertyInfo;

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
    public long insert(String table, String nullColumnHack, ContentValues values)
            throws SQLException {
        return this.sqLiteDatabase.insertOrThrow(table, nullColumnHack, values);
    }

    @Deprecated
    public int delete(String table, String whereClause, String[] whereArgs) {
        return this.sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    public <T> void insertObjectIntoTable(T object ,String tableName)   {
        if(RWSObjectUtil.checkKeyPropertyIsNull(object)){
            throw  new RWSOrmException(RWSOrmException.KEY_PROPERTY_IS_NULL);
        }
        try {
            RWSTable rwsTable=  RWSTableCache.getInstance().getRWSClassInfo(tableName,this);
            RWSClassInfo rwsClassInfo= RWSClassInfoCache.getInstance().getRWSClassInfo(object.getClass());
            String comlumStr="";
            String placeholderStr="";
            List<Object> values=new ArrayList();
            for(RWSColum colum :rwsTable.getColums()){
                for(RWSPropertyInfo rwsPropertyInfo: rwsClassInfo.getProperties()){
                    if(rwsPropertyInfo.getAlias().contains(colum.getName())){
                        values.add(rwsPropertyInfo.getField().get(object));
                        break;
                    }
                }
                comlumStr+=colum.getName()+",";
                placeholderStr+="?,";
            }
            if(comlumStr.length()>0){
                comlumStr.substring(0,comlumStr.length()-1);
                placeholderStr.substring(0,placeholderStr.length()-1);
            }
            this.sqLiteDatabase.execSQL("insert into "+tableName+" ("+comlumStr+") values ("+placeholderStr+")",values.toArray());
        }catch (IllegalAccessException ex){
            ex.printStackTrace();
        }
    }

    public <T> void insertObject(T object ) {
        RWSClassInfo rwsClassInfo= RWSClassInfoCache.getInstance().getRWSClassInfo(object.getClass());
       this.insertObjectIntoTable(object,rwsClassInfo.getTableName());
    }

    //TODO
    @Deprecated
    public <T> void updateObject(T object ) {
        object.getClass();
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

    protected  static WritableConnection createWritableConnection(String dbPath, int index) {
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
