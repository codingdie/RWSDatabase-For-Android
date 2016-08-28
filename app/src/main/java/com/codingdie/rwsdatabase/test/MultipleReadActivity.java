package com.codingdie.rwsdatabase.test;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.codingdie.rwsdatabase.R;
import com.codingdie.rwsdatabase.RWSDatabaseCreator;
import com.codingdie.rwsdatabase.RWSDatabaseManager;
import com.codingdie.rwsdatabase.connection.ReadableConnection;
import com.codingdie.rwsdatabase.connection.WritableConnection;
import com.codingdie.rwsdatabase.log.LogUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class MultipleReadActivity extends Activity {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;

    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;
    private long beginTime = 0;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                textView1.setText("执行第" + count1 + "次查询结果");
            } else if (msg.what == 4) {
                textView2.setText("执行第" + count2 + "次查询结果");
            }
            return false;
        }
    });


    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String dbPath = getSDPath() + File.separator + "test.db";
        textView1 = (TextView) findViewById(R.id.read1);
        textView2 = (TextView) findViewById(R.id.read2);
        textView3 = (TextView) findViewById(R.id.write1);
        textView4 = (TextView) findViewById(R.id.read3);
        textView5 = (TextView) findViewById(R.id.read4);
        textView6 = (TextView) findViewById(R.id.write2);
        textView7 = (TextView) findViewById(R.id.time);
        beginTime=System.currentTimeMillis();
        SqliteHelper sqliteHelper = new SqliteHelper(this, "test");
        RWSDatabaseManager rwsDatabaseManager = new RWSDatabaseCreator().databasePath(dbPath).versionManager(VersionManager.class).version(2).connectionPoolSize(100).create();
        newTestRead(rwsDatabaseManager);
//        oldTestRead(sqliteHelper);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                     handler.post(new Runnable() {
                         @Override
                         public void run() {
                             textView7.setText("time:"+(System.currentTimeMillis()-beginTime));
                         }
                     });
            }
        },0,10);
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



    public void oldTestRead(final SqliteHelper sqliteHelper) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase writableSQLiteConnection = sqliteHelper.getReadableDatabase();
                        Cursor cursor = writableSQLiteConnection.rawQuery("select max(studentId)  from Student ", new String[]{});
                        cursor.moveToNext();
                        LogUtil.log("2:"+cursor.getInt(0));

                        Message message = new Message();
                        message.what = 4;
                        handler.sendMessage(message);
                        cursor.close();
                        count2++;
                        writableSQLiteConnection.close();
                    }
                }).start();
            }
        },0,1);

    }
    public void newTestRead(final RWSDatabaseManager rwsDatabaseManager) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ReadableConnection writableSQLiteConnection = rwsDatabaseManager.getReadableDatabase();
                        Cursor cursor = writableSQLiteConnection.execReadSQL("select max(studentId)  from Student ", new String[]{});
                        cursor.moveToNext();
                        LogUtil.log("1:"+cursor.getInt(0));

                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        cursor.close();
                        count1++;
                        rwsDatabaseManager.releaseReadableDatabase(writableSQLiteConnection);
                    }
                }).start();
            }
        },0,1);
    }

}
