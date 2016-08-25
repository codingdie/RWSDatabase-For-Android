package com.codingdie.rwsdatabase.connection;

import com.codingdie.rwsdatabase.connection.Imp.SQLConnectionPoolManagerImp;
import com.codingdie.rwsdatabase.connection.Imp.InitSQLiteDatabaseImp;
import com.codingdie.rwsdatabase.connection.model.InitSQLiteConnectionPoolConfig;
import com.codingdie.rwsdatabase.connection.model.SQLiteConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xupen on 2016/8/22.
 */
public class SQLConnectionPoolManager implements SQLConnectionPoolManagerImp {
    private List<SQLiteConnection> readConnectionsPool;
    private SQLiteConnection writeConnection;
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
    public SQLiteConnection getReadConnection() {
        return (SQLiteConnection) execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                return getReadConnectionAfterInit();
            }
        });
    }

    private SQLiteConnection getReadConnectionAfterInit() {
        SQLiteConnection restConnection = null;
        try {
            readConnectionLock.lock();
            restConnection = getRestConnnectionForRead(readConnectionsPool);
            if (restConnection != null) {
                log("getReadConnection:" + restConnection.getIndex());
                restConnection.setInUsing(true);
            }
            while (restConnection == null) {
                log("wait for read connection");
                readConnectionCondition.await();
                restConnection = getRestConnnectionForRead(readConnectionsPool);
                if (restConnection != null) {
                    log("getReadConnection:" + restConnection.getIndex());
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

    private SQLiteConnection getRestConnnectionForRead(List<SQLiteConnection> SQLiteConnections) {
        for (int i = 0; i < SQLiteConnections.size(); i++) {
            if (SQLiteConnections.get(i).isInUsing() == false) {
                return SQLiteConnections.get(i);
            }
        }
        return null;
    }

    @Override
    public SQLiteConnection getWriteConnection() {
        return (SQLiteConnection) execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                return getWriteConnectionAfterInit();
            }
        });
    }

    private SQLiteConnection getWriteConnectionAfterInit() {
        try {
            writeConnectionLock.lock();
            if (writeConnection.isInUsing() == false) {
                log("getWriteConnection:" + writeConnection.getIndex());
                writeConnection.setInUsing(true);
                writeConnectionLock.unlock();
                return writeConnection;
            }
            while (writeConnection.isInUsing()) {
                log("wait for writeconnection");
                writeConnectionCondition.await();
                if (!writeConnection.isInUsing()) {
                    writeConnection.setInUsing(true);
                    log("getWriteConnection:" + writeConnection.getIndex());
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
    public void releaseWriteConnection() {
        execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                writeConnectionLock.lock();
                writeConnection.setInUsing(false);
                log("releaseWriteConnection:" + writeConnection.getIndex() + "/" + (System.currentTimeMillis() - writeConnection.getBeginUsingTime()));
                writeConnectionCondition.signalAll();
                writeConnectionLock.unlock();
                return null;
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
    public void releaseReadConnection(final SQLiteConnection SQLiteConnection) {

        execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                readConnectionLock.lock();
                SQLiteConnection.setInUsing(false);
                log("releaseReadConnection:" + SQLiteConnection.getIndex() + "/" + (System.currentTimeMillis() - SQLiteConnection.getBeginUsingTime()));
                readConnectionCondition.signalAll();

                readConnectionLock.unlock();
                return null;
            }
        });
    }


    @Override
    public void initConnnectionPool(InitSQLiteConnectionPoolConfig connectionPoolConfig, InitSQLiteDatabaseImp initSQLiteDatabaseImp) {
        versionControlLock.lock();
        writeConnection = SQLiteConnection.createWritableConnection(connectionPoolConfig.getDbPath(), 0);
        readConnectionsPool = new ArrayList<SQLiteConnection>();
        for (int i = 1; i < connectionPoolConfig.getMaxCount(); i++) {
            readConnectionsPool.add(SQLiteConnection.createReadableConnection(connectionPoolConfig.getDbPath(), i));
        }
        if (initSQLiteDatabaseImp != null) {
            writeConnection.setInUsing(true);
            log("begin init database");
            initSQLiteDatabaseImp.initDatabase(writeConnection);
            log("end init database");

            writeConnection.setInUsing(false);
        }
        initEndFlag = true;
        versionControlCondition.signalAll();
        versionControlLock.unlock();

    }

    @Override
    public void destroy() {
        for (int i = 0; i < readConnectionsPool.size(); i++) {
            log("销毁链接" + readConnectionsPool.get(i).getIndex());
            readConnectionsPool.get(i).destroy();
            readConnectionsPool.get(i).destroy();
        }
        writeConnection.destroy();
    }


    @Override
    public void log(String log) {
        if (openLog) {
            System.out.println(log);
        }
    }

    @Override
    public void openLog(boolean openFlag) {
        openLog = openFlag;
    }

    private interface AfterInitOperator {
        public Object exec();
    }
}
