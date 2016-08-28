package com.codingdie.rwsdatabase;

import com.codingdie.rwsdatabase.connection.Imp.InitSQLiteDatabaseImp;
import com.codingdie.rwsdatabase.connection.ReadableConnection;
import com.codingdie.rwsdatabase.connection.SQLConnectionPoolManager;
import com.codingdie.rwsdatabase.connection.WritableConnection;
import com.codingdie.rwsdatabase.connection.model.InitSQLiteConnectionPoolConfig;
import com.codingdie.rwsdatabase.version.VersionController;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xupen on 2016/8/25.
 */
public class RWSDatabaseManager {
    private SQLConnectionPoolManager connectionPoolManager;
    private ReentrantLock initLock=new ReentrantLock();
    private Condition initLockCondition=initLock.newCondition();
    private  boolean initFlag=false;
    public void  init(final String dbPath,final int version,final Class versionManager,final int maxConnectionCount){
        new Thread(new Runnable() {
            @Override
            public void run() {
                initLock.lock();
                InitSQLiteConnectionPoolConfig initSQLiteConnectionPoolConfig=new InitSQLiteConnectionPoolConfig();
                initSQLiteConnectionPoolConfig.setMaxCount(maxConnectionCount);
                initSQLiteConnectionPoolConfig.setDbPath(dbPath);
                connectionPoolManager=new SQLConnectionPoolManager();
                connectionPoolManager.openLog(false);
                connectionPoolManager.initConnnectionPool(initSQLiteConnectionPoolConfig, new InitSQLiteDatabaseImp() {
                    @Override
                    public void initDatabase(WritableConnection sqLiteConnection) {
                        VersionController versionController=new VersionController();
                        versionController.createOrUpgradeDatabase(version, versionManager,sqLiteConnection);
                        initFlag=true;
                        initLockCondition.signalAll();
                        initLock.unlock();
                    }
                });
            }
        }).start();

    }
    public ReadableConnection getReadableDatabase(){
     return  (ReadableConnection)execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                return connectionPoolManager.getReadableConnection();
            }
        });
    }

    public void releaseReadableDatabase(final ReadableConnection readableConnection){
        execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                connectionPoolManager.releaseReadConnection(readableConnection);
                return null;
            }
        });
    }

    public WritableConnection getWritableConnection(){
        return  (WritableConnection)execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                return connectionPoolManager.getWritableConnection();
            }
        });
    }

    public void releaseWritableConnection(){
        execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                 connectionPoolManager.releaseWritableConnection();
                return null;
            }
        });
    }

    private Object execAfterInit(AfterInitOperator afterInitOperator) {
        Object object=null;
        try {
            initLock.lock();
            if (initFlag) {
                initLock.unlock();
                object= afterInitOperator.exec();
            }
            while (!initFlag) {
                initLockCondition.await();
                if (initFlag) {
                    initLock.unlock();
                    object= afterInitOperator.exec();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            initLock.unlock();
        }
        return object;

    }
    private interface AfterInitOperator {
        public Object exec();
    }
}

