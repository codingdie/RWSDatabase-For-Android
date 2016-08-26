package com.codingdie.rwsdatabase.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import com.codingdie.rwsdatabase.R;
import com.codingdie.rwsdatabase.RWSDatabaseCreator;
import com.codingdie.rwsdatabase.RWSDatabaseManager;
import com.codingdie.rwsdatabase.connection.WritableConnection;

import java.io.File;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String dbPath = getSDPath() + File.separator + "test.db";

        RWSDatabaseManager rwsDatabaseManager = new RWSDatabaseCreator().databasePath(dbPath).versionManager(VersionManager.class).version(2).connectionPoolSize(5).create();
        multipleWriteTest(rwsDatabaseManager);
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
    public void  multipleWriteTest(final RWSDatabaseManager rwsDatabaseManager){
       new Thread(new Runnable() {
           @Override
           public void run() {
               WritableConnection writableSQLiteConnection=rwsDatabaseManager.getWritableConnection();
               writableSQLiteConnection.execWriteSQL("delete from Class where classId<=3",new Object[]{});

               writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,?)",new Object[]{"一班",1});
               writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,? )",new Object[]{"二班",2});
               writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,?)",new Object[]{"三班",3});
               try {
                   Thread.sleep(10000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               rwsDatabaseManager.releaseWritableConnection();
           }
       }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                WritableConnection writableSQLiteConnection=rwsDatabaseManager.getWritableConnection();
                writableSQLiteConnection.execWriteSQL("delete from Class where classId>3 and classId<=6",new Object[]{});
                writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,?)",new Object[]{"四班",4});
                writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,? )",new Object[]{"五班",5});
                writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,?)",new Object[]{"六班",6});
                rwsDatabaseManager.releaseWritableConnection();

            }
        }).start();
    }


}
