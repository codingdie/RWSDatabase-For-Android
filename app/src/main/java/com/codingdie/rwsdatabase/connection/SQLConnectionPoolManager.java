package com.codingdie.rwsdatabase.connection;

import com.codingdie.rwsdatabase.connection.Imp.SQLConnectionPoolManagerImp;
import com.codingdie.rwsdatabase.connection.Imp.InitSQLiteDatabaseImp;
import com.codingdie.rwsdatabase.connection.model.InitSQLiteConnectionPoolConfig;
import com.codingdie.rwsdatabase.log.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xupen on 2016/8/22.
 */
public class SQLConnectionPoolManager implements SQLConnectionPoolManagerImp {
    private List<ReadableConnection> readConnectionsPool;
    private WritableConnection writeConnection;
    private ReentrantLock versionControlLock = new ReentrantLock(true);
    private ReentrantLock readConnectionLock = new ReentrantLock(true);
    private ReentrantLock writeConnectionLock = new ReentrantLock(true);
    private Condition versionControlCondition = versionControlLock.newCondition();
    private Condition readConnectionCondition = readConnectionLock.newCondition();
    private Condition writeConnectionCondition = writeConnectionLock.newCondition();

    private boolean initEndFlag = false;
    private boolean openLog = true;
    private Timer timer = new Timer();

    @Override
    public ReadableConnection getReadableConnection() {
        return (ReadableConnection) execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                return getReadConnectionAfterInit();
            }
        });
    }

    private ReadableConnection getReadConnectionAfterInit() {
        ReadableConnection restConnection = null;
        try {
            readConnectionLock.lock();
            restConnection = getRestConnnectionForRead(readConnectionsPool);
            if (restConnection != null) {
                log("getReadableConnection:" + restConnection.getIndex());
                restConnection.setInUsing(true);
            }
            while (restConnection == null) {
                log("wait for read connection");
                readConnectionCondition.await();
                restConnection = getRestConnnectionForRead(readConnectionsPool);
                if (restConnection != null) {
                    log("getReadableConnection:" + restConnection.getIndex());
                    restConnection.setInUsing(true);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            readConnectionLock.unlock();
        }
        return restConnection;
    }

    private ReadableConnection getRestConnnectionForRead(List<ReadableConnection> SQLiteConnections) {
        for (int i = 0; i < SQLiteConnections.size(); i++) {
            if (SQLiteConnections.get(i).isInUsing() == false) {
                return SQLiteConnections.get(i);
            }
        }
        return null;
    }

    @Override
    public WritableConnection getWritableConnection() {
        return (WritableConnection) execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                return getWriteConnectionAfterInit();
            }
        });
    }

    private WritableConnection getWriteConnectionAfterInit() {
        try {
            writeConnectionLock.lock();
            if (writeConnection.isInUsing() == false) {
                log("getWritableConnection:" + writeConnection.getIndex());
                writeConnection.setInUsing(true);
                writeConnectionLock.unlock();
                return writeConnection;
            }
            while (writeConnection.isInUsing()) {
                log("wait for writeconnection");
                writeConnectionCondition.await();
                if (!writeConnection.isInUsing()) {
                    writeConnection.setInUsing(true);
                    log("getWritableConnection:" + writeConnection.getIndex());
                    writeConnectionLock.unlock();
                    return writeConnection;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeConnectionLock.unlock();

        } finally {
        }
        return null;
    }

    @Override
    public void releaseWritableConnection() {
        execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                writeConnectionLock.lock();
                writeConnection.setInUsing(false);
                log("releaseWritableConnection:" + writeConnection.getIndex() + "/" + (System.currentTimeMillis() - writeConnection.getBeginUsingTime()));
                writeConnectionCondition.signal();
                writeConnectionLock.unlock();
                return null;
            }
        });
    }

    @Override
    public int getRestReadableConnectionCount() {
        return (Integer) execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                Integer count=0;
                for (int i = 0; i < readConnectionsPool.size(); i++) {
                    if (readConnectionsPool.get(i).isInUsing() == false) {
                        count++;
                    }
                }
                return count;
            }
        });
    }

    @Override
    public int getRestWritableConnectionCount() {
        return (Integer) execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                if(writeConnection.isInUsing()==false){
                    return  1;
                }else{
                    return 0;
                }
            }
        });
    }

    private Object execAfterInit(AfterInitOperator afterInitOperator) {
        Object object=null;
        try {
            versionControlLock.lock();
            if (initEndFlag) {
                versionControlLock.unlock();
                object= afterInitOperator.exec();
            }
            while (!initEndFlag) {
                log("wait for initlock");
                versionControlCondition.await();
                log("end for initlock");
                if (initEndFlag) {
                    versionControlLock.unlock();
                    object= afterInitOperator.exec();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            versionControlLock.unlock();
        }
        return object;

    }

    @Override
    public void sheduleCheck() {

    }

    @Override
    public void releaseReadConnection(final ReadableConnection SQLiteConnection) {

        execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                readConnectionLock.lock();
                SQLiteConnection.setInUsing(false);
                log("releaseReadConnection:" + SQLiteConnection.getIndex() + "/" + (System.currentTimeMillis() - SQLiteConnection.getBeginUsingTime()));
                readConnectionCondition.signal();

                readConnectionLock.unlock();
                return null;
            }
        });
    }

    @Override
    public void initConnnectionPool(InitSQLiteConnectionPoolConfig connectionPoolConfig, InitSQLiteDatabaseImp initSQLiteDatabaseImp) {
        versionControlLock.lock();
        writeConnection = WritableConnection.createWritableConnection(connectionPoolConfig.getDbPath(), 0);
        readConnectionsPool = new ArrayList<ReadableConnection>();
        for (int i = 1; i < connectionPoolConfig.getMaxCount(); i++) {
            readConnectionsPool.add(ReadableConnection.createReadableConnection(connectionPoolConfig.getDbPath(), i));
        }
        if (initSQLiteDatabaseImp != null) {
            writeConnection.setInUsing(true);
            log("begin init database");
            initSQLiteDatabaseImp.initDatabase(writeConnection);
            log("end init database");

            writeConnection.setInUsing(false);
        }
        initEndFlag = true;
        versionControlCondition.signal();
        versionControlLock.unlock();

    }

    @Override
    public void destroy() {
        readConnectionLock.lock();

        for (int i = 0; i < readConnectionsPool.size(); i++) {
            log("销毁链接" + readConnectionsPool.get(i).getIndex());
            readConnectionsPool.get(i).destroy();
        }
        readConnectionsPool=new ArrayList<ReadableConnection>();
        readConnectionLock.unlock();
        writeConnectionLock.lock();

        writeConnection.destroy();
        writeConnection=null;
        writeConnectionLock.unlock();

    }

    private void log(String log) {
        if (openLog) {
            LogUtil.log(log);
        }
    }

    public void openLog(boolean openFlag) {
        this.openLog = openFlag;
    }

    private interface AfterInitOperator {
        public Object exec();
    }
}
