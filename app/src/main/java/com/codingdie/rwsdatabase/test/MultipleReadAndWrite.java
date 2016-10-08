package com.codingdie.rwsdatabase.test;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.codingdie.rwsdatabase.R;
import com.codingdie.rwsdatabase.RWSDatabaseCreator;
import com.codingdie.rwsdatabase.RWSDatabaseManager;
import com.codingdie.rwsdatabase.connection.ReadableConnection;
import com.codingdie.rwsdatabase.connection.WritableConnection;
import com.codingdie.rwsdatabase.test.db.SqliteHelper;
import com.codingdie.rwsdatabase.test.db.VersionManager;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MultipleReadAndWrite extends Activity {
    private TextView read1;
    private TextView read2;
    private TextView read3;
    private TextView read4;
    private TextView write1;
    private TextView write2;
    private TextView time;
    private EditText poolSize;
    private EditText numSize;
    private  Button button;
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;
    private long beginTime = 0;
    private SqliteHelper sqliteHelper;
    private  RWSDatabaseManager rwsDatabaseManager ;
    private Handler handler = new Handler();

    private Timer timeTimer = new Timer();
    private   Timer newTestTimer1 = new Timer();
    private   Timer oldTestTimer1 = new Timer();
    private   Timer newTestTimer2 = new Timer();
    private   Timer oldTestTimer2 = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mutiple_read_and_write);
        String dbPath = getSDPath() + File.separator + "test.db";
        read1 = (TextView) findViewById(R.id.read1);
        read2 = (TextView) findViewById(R.id.read2);
        read3 = (TextView) findViewById(R.id.write1);
        read4 = (TextView) findViewById(R.id.read3);
        write1 = (TextView) findViewById(R.id.read4);
        write2 = (TextView) findViewById(R.id.write2);
        time = (TextView) findViewById(R.id.time);
        button=(Button)findViewById(R.id.control);
        numSize=(EditText)findViewById(R.id.numSize);
        poolSize=(EditText)findViewById(R.id.poolSize);
        rwsDatabaseManager = new RWSDatabaseCreator( MultipleReadAndWrite.this).databaseName("test1").versionManager(VersionManager.class).version(2).connectionPoolSize(Integer.valueOf(numSize.getText().toString())).create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.getText().equals("begin test")){
                    MultipleReadAndWrite.this.deleteDatabase("test1");
                    MultipleReadAndWrite.this.deleteDatabase("test2");
                    beginTime=System.currentTimeMillis();
                    beginTimeTimer();
                    sqliteHelper = new SqliteHelper( MultipleReadAndWrite.this, "test2");
                    rwsDatabaseManager = new RWSDatabaseCreator( MultipleReadAndWrite.this).databaseName("test1").versionManager(VersionManager.class).version(2).connectionPoolSize(Integer.valueOf(numSize.getText().toString())).create();

                    newTestReadAndWrite();
                    oldTestReadAndWrite();
                    button.setText("end test");
                }else{
                    timeTimer.cancel();
                    newTestTimer1.cancel();
                    newTestTimer2.cancel();
                    oldTestTimer1.cancel();
                    oldTestTimer2.cancel();

                    rwsDatabaseManager.destroy();
                    sqliteHelper.close();
                    count1=0;
                    count2=0;
                    count3=0;
                    count4=0;
                    button.setText("begin test");

                }
            }
        });
    }
    private void beginTimeTimer() {
        timeTimer=new Timer();
        timeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        time.setText("time:"+(System.currentTimeMillis()-beginTime));
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

    public void oldTestReadAndWrite() {
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from Student");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; ; j+=Integer.valueOf(numSize.getText().toString())) {
                    SQLiteDatabase writableSQLiteConnection = sqliteHelper.getWritableDatabase();
                    writableSQLiteConnection.beginTransaction();
                    for (int i=0;i<Integer.valueOf(numSize.getText().toString());i++){
                        final int now=i+j;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                write2.setText("正在插入:"+now);
                            }
                        });
                        writableSQLiteConnection.execSQL("insert into Student(`studentName`,`studentId`) values (?,?)", new Object[]{now, now});

                    }
                    writableSQLiteConnection.setTransactionSuccessful();
                    writableSQLiteConnection.endTransaction();

                }

            }
        }).start();
        oldTestTimer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SQLiteDatabase readableConnection = sqliteHelper.getReadableDatabase();
                Cursor cursor = readableConnection.rawQuery("select sum(studentId)  from Student ", new String[]{});
                cursor.moveToNext();
                final   long sum = cursor.getLong(0);
                cursor.close();
                count3++;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        read3.setText("第"+count3+"次求和:"+sum);
                    }
                });
            }
        },0,1000);
        oldTestTimer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SQLiteDatabase readableConnection = sqliteHelper.getReadableDatabase();
                Cursor cursor = readableConnection.rawQuery("select max(studentId)  from Student ", new String[]{});
                cursor.moveToNext();
                final   long max = cursor.getLong(0);
                cursor.close();
                count4++;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        read3.setText("第"+count4+"次求和:"+max);
                    }
                });
            }
        },0,100);
    }

    public void newTestReadAndWrite() {
        WritableConnection writableSQLiteConnection = rwsDatabaseManager.getWritableConnection();
        writableSQLiteConnection.execWriteSQL("delete from Student");
        rwsDatabaseManager.releaseWritableConnection();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0;  ; j+=Integer.valueOf(numSize.getText().toString())) {
                    WritableConnection writableSQLiteConnection = rwsDatabaseManager.getWritableConnection();
                    writableSQLiteConnection.beginTransaction();

                    for (int i=0;i<Integer.valueOf(numSize.getText().toString());i++){
                        final int now=i+j;
                         handler.post(new Runnable() {
                             @Override
                             public void run() {
                               write1.setText("正在插入:"+now);
                             }
                         });
                        writableSQLiteConnection.execWriteSQL("insert into Student(`studentName`,`studentId`) values (?,?)", new Object[]{now, now});

                    }

                    writableSQLiteConnection.setTransactionSuccessful();
                    writableSQLiteConnection.endTransaction();

                    rwsDatabaseManager.releaseWritableConnection();
                }

            }
        }).start();

        newTestTimer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ReadableConnection readableConnection = rwsDatabaseManager.getReadableDatabase();
                Cursor cursor = readableConnection.execReadSQL("select sum(studentId)  from Student ", new String[]{});
                cursor.moveToNext();
                final   long sum = cursor.getLong(0);
                cursor.close();
                count1++;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        read1.setText("第"+count1+"次求和:"+sum);
                    }
                });
            }
        },0,1000);
        newTestTimer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ReadableConnection readableConnection = rwsDatabaseManager.getReadableDatabase();
                Cursor cursor = readableConnection.execReadSQL("select max(studentId)  from Student ", new String[]{});
                cursor.moveToNext();
                final   long max = cursor.getLong(0);
                cursor.close();
                count2++;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        read2.setText("第"+count4+"次求和:"+max);
                    }
                });
            }
        },0,100);
    }


}
