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

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MultipleReadAndWrite extends Activity {
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
                textView1.setText("第" + count1 + "次查询结果(1s一次):\n" + msg.obj);
            } else if (msg.what == 2) {
                textView2.setText("第" + count2 + "次查询结果(3s一次):\n" + msg.obj);
            } else if (msg.what == 3) {
                textView3.setText("0-" + msg.obj);
            } else if (msg.what == 4) {
                textView4.setText("第" + count3 + "次查询结果(1s一次):\n" + msg.obj);
            } else if (msg.what == 5) {
                textView5.setText("第" + count4 + "次查询结果(3s一次):\n" + msg.obj);
            } else if (msg.what == 6) {
                textView6.setText("0-" + msg.obj);
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
        RWSDatabaseManager rwsDatabaseManager = new RWSDatabaseCreator().databasePath(dbPath).versionManager(VersionManager.class).version(2).connectionPoolSize(5).create();
        newTestReadAndWrite(rwsDatabaseManager);
        oldTestReadAndWrite(sqliteHelper);
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
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    public void multipleWriteTest(final RWSDatabaseManager rwsDatabaseManager) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WritableConnection writableSQLiteConnection = rwsDatabaseManager.getWritableConnection();
                writableSQLiteConnection.execWriteSQL("delete from Class where classId<=3", new Object[]{});

                writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,?)", new Object[]{"一班", 1});
                writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,? )", new Object[]{"二班", 2});
                writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,?)", new Object[]{"三班", 3});
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
                WritableConnection writableSQLiteConnection = rwsDatabaseManager.getWritableConnection();
                writableSQLiteConnection.execWriteSQL("delete from Class where classId>3 and classId<=6", new Object[]{});
                writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,?)", new Object[]{"四班", 4});
                writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,? )", new Object[]{"五班", 5});
                writableSQLiteConnection.execWriteSQL("insert into Class(`className`,`classId`) values (?,?)", new Object[]{"六班", 6});
                rwsDatabaseManager.releaseWritableConnection();

            }
        }).start();
    }

    public void oldTestReadAndWrite(final SqliteHelper sqliteHelper) {
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from Student");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; ; j+=50) {
                    SQLiteDatabase writableSQLiteConnection = sqliteHelper.getWritableDatabase();
                    writableSQLiteConnection.beginTransaction();
                    for (int i=0;i<50;i++){
                        Message message = new Message();
                        message.what = 6;
                        message.obj = j+i;
                        handler.sendMessage(message);
                        try {
                            Thread.sleep(50 );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        writableSQLiteConnection.execSQL("insert into Student(`studentName`,`studentId`) values (?,?)", new Object[]{j+i, j+i});

                    }
                    writableSQLiteConnection.setTransactionSuccessful();
                    writableSQLiteConnection.endTransaction();

                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; ; i++) {
                    count3++;
                    SQLiteDatabase writableSQLiteConnection = sqliteHelper.getReadableDatabase();
                    Cursor cursor = writableSQLiteConnection.rawQuery("select max(studentId)  from Student ", new String[]{});
                    cursor.moveToNext();
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 4;
                    message.obj = cursor.getLong(0);
                    handler.sendMessage(message);
                    cursor.close();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; ; i++) {
                    count4++;
                    SQLiteDatabase readableConnection = sqliteHelper.getReadableDatabase();
                    Cursor cursor = readableConnection.rawQuery("select sum(studentId)  from Student ", new String[]{});
                    cursor.moveToNext();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 5;
                    message.obj = cursor.getLong(0);
                    handler.sendMessage(message);
                    cursor.close();
                }
        }
        }).start();
    }

    public void newTestReadAndWrite(final RWSDatabaseManager rwsDatabaseManager) {
        WritableConnection writableSQLiteConnection = rwsDatabaseManager.getWritableConnection();
        writableSQLiteConnection.execWriteSQL("delete from Student");
        rwsDatabaseManager.releaseWritableConnection();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; ; j+=50) {
                    WritableConnection writableSQLiteConnection = rwsDatabaseManager.getWritableConnection();
                    writableSQLiteConnection.beginTransaction();

                    for (int i=0;i<50;i++){
                        Message message = new Message();
                        message.what = 3;
                        message.obj = j+i;
                        handler.sendMessage(message);
                        try {
                            Thread.sleep(50 );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        writableSQLiteConnection.execWriteSQL("insert into Student(`studentName`,`studentId`) values (?,?)", new Object[]{j+i, j+i});

                    }

                    writableSQLiteConnection.setTransactionSuccessful();
                    writableSQLiteConnection.endTransaction();

                    rwsDatabaseManager.releaseWritableConnection();
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; ; i++) {

                    count1++;
                    ReadableConnection readableConnection = rwsDatabaseManager.getReadableDatabase();
                    Cursor cursor = readableConnection.execReadSQL("select max(studentId)  from Student ", new String[]{});
                    cursor.moveToNext();
                    Message message = new Message();
                    message.what = 1;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    message.obj = cursor.getLong(0);
                    handler.sendMessage(message);
                    cursor.close();
                    rwsDatabaseManager.releaseReadableDatabase(readableConnection);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; ; i++) {
                    count2++;
                    ReadableConnection readableConnection = rwsDatabaseManager.getReadableDatabase();
                    Cursor cursor = readableConnection.execReadSQL("select sum(studentId)  from Student ", new String[]{});
                    cursor.moveToNext();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 2;
                    message.obj = cursor.getLong(0);
                    handler.sendMessage(message);
                    cursor.close();
                    rwsDatabaseManager.releaseReadableDatabase(readableConnection);
                }

            }
        }).start();
    }


}
