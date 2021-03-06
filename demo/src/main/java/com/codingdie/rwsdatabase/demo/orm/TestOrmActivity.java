package com.codingdie.rwsdatabase.demo.orm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.codingdie.rwsdatabase.RWSDatabaseCreator;
import com.codingdie.rwsdatabase.RWSDatabaseManager;
import com.codingdie.rwsdatabase.ReadOperator;
import com.codingdie.rwsdatabase.WriteOperator;
import com.codingdie.rwsdatabase.connection.ReadableConnection;
import com.codingdie.rwsdatabase.connection.WritableConnection;
import com.codingdie.rwsdatabase.demo.R;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;

public class TestOrmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_orm);
        RWSDatabaseManager rwsDatabaseManager = new RWSDatabaseCreator(TestOrmActivity.this) //context
                .databaseName("ormtest")   //dbname
                .databasePath(getSDPath())
                .versionManager(ORMTestVersionManager.class)       //versionmanager 版本管理器
                .version(2)
                .create();
        final ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("名字100");
        classInfo.setClassId(100);
        rwsDatabaseManager.execWriteOperator(new WriteOperator() {
            @Override
            public void exec(WritableConnection writableConnection) {
                writableConnection.insert(classInfo);
                ClassInfo classInfo100 = writableConnection.queryObject("select * from Class where classId=100", new String[]{}, ClassInfo.class);
                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(classInfo100));
                writableConnection.delete(classInfo);
                classInfo100 = writableConnection.queryObject("select * from Class where classId=100", new String[]{}, ClassInfo.class);
                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(classInfo100));

            }
        });
    }

    private void testFillOneSimpleObject(RWSDatabaseManager rwsDatabaseManager) {
        ClassInfo classInfo = rwsDatabaseManager.execReadOperator(new ReadOperator<ClassInfo>() {
            @Override
            public ClassInfo exec(ReadableConnection readableConnection) {
                return readableConnection.queryObject("select * from Class where classId=20", new String[]{}, ClassInfo.class);
            }
        });
        Log.i("test", new GsonBuilder().setPrettyPrinting().create().toJson(classInfo));

    }

    private void testFillOneComplexObject(RWSDatabaseManager rwsDatabaseManager) {
        ClassInfo classInfo = rwsDatabaseManager.execReadOperator(new ReadOperator<ClassInfo>() {
            @Override
            public ClassInfo exec(ReadableConnection readableConnection) {
                return readableConnection.queryObject("select * from Class c left join student s on s.classId= c.classId  where c.classId=18", new String[]{}, ClassInfo.class);
            }
        });
        Log.i("test", new GsonBuilder().setPrettyPrinting().create().toJson(classInfo));
    }

    private void testFillComplexObjectList(RWSDatabaseManager rwsDatabaseManager) {
        List<ClassInfo> classInfos = rwsDatabaseManager.execReadOperator(new ReadOperator<List<ClassInfo>>() {
            @Override
            public List<ClassInfo> exec(ReadableConnection readableConnection) {
                return readableConnection.queryObjectList("select * from Class c left join student s on s.classId= c.classId  ", new String[]{}, ClassInfo.class);
            }
        });
        Log.i("test", new GsonBuilder().setPrettyPrinting().create().toJson(classInfos));

    }

    private void testFillOneSimpleObjectList(RWSDatabaseManager rwsDatabaseManager) {
        List<ClassInfo> classInfos = rwsDatabaseManager.execReadOperator(new ReadOperator<List<ClassInfo>>() {
            @Override
            public List<ClassInfo> exec(ReadableConnection readableConnection) {
                return readableConnection.queryObjectList("select * from Class ", new String[]{}, ClassInfo.class);
            }
        });
        Log.i("test", new GsonBuilder().setPrettyPrinting().create().toJson(classInfos));
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


}
