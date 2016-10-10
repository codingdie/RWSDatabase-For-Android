package com.codingdie.rwsdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.codingdie.rwsdatabase.exception.RWSDatabaseException;
import com.codingdie.rwsdatabase.version.imp.UpgradeDatabaseListener;

import java.io.File;

/**
 * Created by xupen on 2016/8/25.
 */
public class RWSDatabaseCreator {
    private  String dbPath;
    private  String dbName;

    private  int version=1;
    private  Class versionManager;
    private  int connectionPoolSize =5;
    private  Context context;
    private UpgradeDatabaseListener upgradeDatabaseListener;

    public RWSDatabaseCreator(Context context) {
        this.context = context;
    }

    public  RWSDatabaseManager  create( ){
        RWSDatabaseManager rwsDatabaseManager=new RWSDatabaseManager();
        rwsDatabaseManager.init(getDataBasePath(),version,versionManager, connectionPoolSize,upgradeDatabaseListener ,context);
        return  rwsDatabaseManager;
     }


    public static   boolean checkNeedUpgrdadeDatabase(String dbName,int curVersion,Context context){
        return  checkNeedUpgrdadeDatabaseInPath(context.getDatabasePath(dbName).getAbsolutePath(),curVersion,context);
    }

    public static   boolean checkNeedUpgrdadeDatabaseInPath(String path,int curVersion,Context context){
        File file=new File(path);
        if(!file.exists()){
            return  false;
        }
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.CREATE_IF_NECESSARY);
        if(!sqLiteDatabase.isOpen()){
            return false;
        }

        boolean flag=false;
        flag= sqLiteDatabase.needUpgrade(curVersion);
        if(sqLiteDatabase.getVersion()==0&&curVersion==1){
            flag=false;
        }
        sqLiteDatabase.close();
        return  flag;
    }

    public RWSDatabaseCreator databasePath(String dbPath) {
        this.dbPath = dbPath;
        return this;
    }

    public RWSDatabaseCreator databaseName(String name) {
        this.dbName=name;
        return this;
    }

    public RWSDatabaseCreator version(int version) {
        this.version = version;
        return this;
    }

    public RWSDatabaseCreator versionManager(Class versionManager) {
        this.versionManager = versionManager;
        return this;
    }

    public RWSDatabaseCreator connectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
        return this;
    }

    public RWSDatabaseCreator addUpgradeDatabaseListener(UpgradeDatabaseListener upgradeDatabaseListener) {
        this.upgradeDatabaseListener = upgradeDatabaseListener;
        return this;
    }

    private String getDataBasePath() {
        String finalDBPath="";
        if(dbPath!=null&&!dbPath.equals("")){

            File file=new File(dbPath);
            finalDBPath=file.getAbsolutePath()+File.separator+dbName+".sqlite";
            File dbFile = new File(finalDBPath);
            dbFile.mkdirs();
            if(!dbFile.getParentFile().exists()){
                throw new RWSDatabaseException(RWSDatabaseException.PATH_ERROR);
            }
        }else{
            context.getDatabasePath(dbName).mkdirs();
            finalDBPath= context.getDatabasePath(dbName).getAbsolutePath();
        }
        return finalDBPath;
    }

}
