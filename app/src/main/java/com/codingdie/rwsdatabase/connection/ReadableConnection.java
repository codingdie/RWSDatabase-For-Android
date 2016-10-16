package com.codingdie.rwsdatabase.connection;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.codingdie.rwsdatabase.orm.RWSCursorResultReflectUtil;

import java.util.List;

/**
 * Created by xupen on 2016/8/26.
 */
public class ReadableConnection extends  SQLiteConnection {

    protected static ReadableConnection createReadableConnection(String dbPath, int index) {
        ReadableConnection readableConnection = new ReadableConnection();
        readableConnection.setInUsing(false);
        readableConnection.setWritable(false);
        readableConnection.setIndex(index);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.CREATE_IF_NECESSARY);
        sqLiteDatabase.enableWriteAheadLogging();
        readableConnection.setSqLiteDatabase(sqLiteDatabase);
        return readableConnection;
    }
    public Cursor execReadSQL(String sql , String[] param){
        return   this.sqLiteDatabase.rawQuery(sql ,param);
    }

    @Deprecated
    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {
        return   this.sqLiteDatabase.query(table ,columns,selection,selectionArgs,groupBy,having,orderBy,limit);
    }

    public <T> T queryObject(String sql , String[] param,Class<T> tClass,String[]... ignoreProps){
        return RWSCursorResultReflectUtil.toObject(this.execReadSQL(sql,param),tClass,ignoreProps);
    }

    public <E> List<E> queryObjectList(String sql , String[] param, Class<E> tClass, String[]... ignoreProps){
        return RWSCursorResultReflectUtil.toList(this.execReadSQL(sql,param),tClass,ignoreProps);
    }

    protected RWSTable getTableInfo(String tableName) {
        RWSTable rwsTable=new RWSTable();
        rwsTable.setName(tableName);
        List<RWSColum> columList=   this.queryObjectList("pragma table_info([?])",new String[]{tableName},RWSColum.class);
        rwsTable.setColums(columList);
        return  rwsTable;
    }

}
