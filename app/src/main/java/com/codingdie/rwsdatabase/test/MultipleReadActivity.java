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
import com.codingdie.rwsdatabase.log.RWSLogUtil;
import com.codingdie.rwsdatabase.test.db.SqliteHelper;
import com.codingdie.rwsdatabase.test.db.VersionManager;
import com.codingdie.rwsdatabase.version.imp.UpgradeDatabaseListener;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class MultipleReadActivity extends Activity {
    private TextView textView1;
    private TextView textView2;
    private TextView time;
    private EditText poolSize;
    private EditText dbSize;
    private EditText progressEdit;

    private Button button;
    private int count1 = 0;
    private int count2 = 0;
    private long beginTime = 0;
    private SqliteHelper sqliteHelper;
    private RWSDatabaseManager rwsDatabaseManager ;
    private Timer timeTimer = new Timer();

    private   Timer newTestTimer = new Timer();
    private   Timer oldTestTimer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mutiple_read);



        textView1 = (TextView) findViewById(R.id.read1);
        textView2 = (TextView) findViewById(R.id.read2);
        poolSize=(EditText)findViewById(R.id.poolSize);
        dbSize=(EditText)findViewById(R.id.dbSize);
        button=(Button)findViewById(R.id.control);
        time = (TextView) findViewById(R.id.time);
        progressEdit =(EditText)findViewById(R.id.proress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(button.getText().equals("begin test")){
                     MultipleReadActivity.this.deleteDatabase("test1");
                     MultipleReadActivity.this.deleteDatabase("test2");
                     beginTime=System.currentTimeMillis();
                     beginTimeTimer();
                     sqliteHelper = new SqliteHelper( MultipleReadActivity.this, "test2");
                     rwsDatabaseManager = new RWSDatabaseCreator( MultipleReadActivity.this) //context
                                                    .databaseName("test1")                      //dbname
                                                    .versionManager(VersionManager.class)       //versionmanager 版本管理器
                                                    .version(4).addUpgradeDatabaseListener(new UpgradeDatabaseListener() {
                                 @Override
                                 public void beginUpgrade() {

                                 }

                                 @Override
                                 public void endUpgrade() {

                                 }

                                 @Override
                                 public void progress(double progress) {
                                     RWSLogUtil.log("progress:"+progress);

                                     progressEdit.setText(String.valueOf(progress));
                                 }
                             })                                 //version 版本
                                                    .connectionPoolSize(20)                      //connectionPoolSize 连接池大小
                                                    .create();
                     SQLiteDatabase sqLiteDatabase=  sqliteHelper.getWritableDatabase();
                     WritableConnection writableConnection=rwsDatabaseManager.getWritableConnection();
                     sqLiteDatabase.beginTransaction();
                     writableConnection.beginTransaction();

                     for(int i=0;i<Integer.valueOf( dbSize.getText().toString());i++){
                         sqLiteDatabase.execSQL("insertObject into Student(`studentName`,`studentId`) values (?,?)", new Object[]{i, i});
                         writableConnection.execWriteSQL("insertObject into Student(`studentName`,`studentId`) values (?,?)", new Object[]{i, i});
                     }
                     sqLiteDatabase.setTransactionSuccessful();
                     writableConnection.setTransactionSuccessful();
                     sqLiteDatabase.endTransaction();
                     writableConnection.endTransaction();
                     rwsDatabaseManager.releaseWritableConnection();
                     sqLiteDatabase.close();
                     newTestRead(rwsDatabaseManager);
                     oldTestRead(sqliteHelper);
                     button.setText("end test");
                 }else{
                     oldTestTimer.cancel();
                     newTestTimer.cancel();
                     timeTimer.cancel();
                     rwsDatabaseManager.destroy();
                     sqliteHelper.close();
                     count1=0;
                     count2=3;
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
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }



    public void oldTestRead(final SqliteHelper sqliteHelper) {
        oldTestTimer=new Timer();
        oldTestTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase sqLiteDatabase = sqliteHelper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("select sum(studentId)  from Student ", new String[]{});
                        cursor.moveToNext();
                        final long sum=cursor.getInt(0);
//                        RWSLogUtil.log("2:"+sum);

                        cursor.close();
                        count2++;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                 textView2.setText("第"+count2+"次查询总和是:"+sum);
                            }
                        });
                    }
                }).start();
            }
        },0,1);

    }
    public void newTestRead(final RWSDatabaseManager rwsDatabaseManager) {
        newTestTimer=new Timer();
        newTestTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ReadableConnection writableSQLiteConnection = rwsDatabaseManager.getReadableDatabase();
                        Cursor cursor = writableSQLiteConnection.execReadSQL("select sum(studentId)  from Student ", new String[]{});
                        cursor.moveToNext();
                        final long sum=cursor.getInt(0);
//                        RWSLogUtil.log("1:"+sum);
                        cursor.close();
                        rwsDatabaseManager.releaseReadableDatabase(writableSQLiteConnection);
                        count1++;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                textView1.setText("第"+count1+"次查询总和是:"+sum);
                            }
                        });

                    }
                }).start();
            }
        },0,1);
    }

}
