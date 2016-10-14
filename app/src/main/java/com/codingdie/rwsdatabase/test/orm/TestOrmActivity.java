package com.codingdie.rwsdatabase.test.orm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.codingdie.rwsdatabase.operator.ReadOperator;
import com.codingdie.rwsdatabase.R;
import com.codingdie.rwsdatabase.RWSDatabaseCreator;
import com.codingdie.rwsdatabase.RWSDatabaseManager;
import com.codingdie.rwsdatabase.connection.ReadableConnection;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;

public class TestOrmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_orm);
        RWSDatabaseManager rwsDatabaseManager = new RWSDatabaseCreator( TestOrmActivity.this) //context
                .databaseName("ormtest")   //dbname
                .databasePath(getSDPath())   //dbpath optional default in the packet path
                .versionManager(ORMTestVersionManager.class)       //versionmanager 版本管理器
                .version(1)
                .create();

        testFillOneComplexObjectList(rwsDatabaseManager);
    }

    private void testFillOneSimpleObject(RWSDatabaseManager rwsDatabaseManager) {
         ClassInfo classInfo=  rwsDatabaseManager.execReadOperator(new ReadOperator<ClassInfo>() {
            @Override
            public ClassInfo exec(ReadableConnection readableConnection) {
               return  readableConnection.queryObject("select * from Class where classId=20",new String[]{}, ClassInfo.class);
            }
    });
    }

    private void testFillOneComplexObject(RWSDatabaseManager rwsDatabaseManager) {
        ReadableConnection readableConnection= rwsDatabaseManager.getReadableDatabase();
        ClassInfo classInfo= readableConnection.queryObject("select * from Class c left join student s on s.classId= c.classId  where c.classId=18",new String[]{}, ClassInfo.class);
        Log.i("test",new GsonBuilder().setPrettyPrinting().create().toJson(classInfo));
        rwsDatabaseManager.releaseReadableDatabase(readableConnection);
    }
    private void testFillOneComplexObjectList(RWSDatabaseManager rwsDatabaseManager) {
        ReadableConnection readableConnection= rwsDatabaseManager.getReadableDatabase();
        List<ClassInfo> classInfos= readableConnection.queryObjectList("select * from Class c left join student s on s.classId= c.classId  ",new String[]{}, ClassInfo.class);
        Log.i("test",new GsonBuilder().setPrettyPrinting().create().toJson(classInfos));
        rwsDatabaseManager.releaseReadableDatabase(readableConnection);
    }
    private void testFillOneSimpleObjectList(RWSDatabaseManager rwsDatabaseManager) {
        ReadableConnection readableConnection= rwsDatabaseManager.getReadableDatabase();
        List<ClassInfo> classInfos= readableConnection.queryObjectList("select * from Class ",new String[]{}, ClassInfo.class);
        Log.i("test",new GsonBuilder().setPrettyPrinting().create().toJson(classInfos));
        rwsDatabaseManager.releaseReadableDatabase(readableConnection);
    }
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


}
