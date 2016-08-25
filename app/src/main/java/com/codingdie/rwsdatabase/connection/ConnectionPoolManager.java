package com.codingdie.rwsdatabase.connection;

import com.codingdie.rwsdatabase.connection.Imp.ConnectionPoolManagerImp;
import com.codingdie.rwsdatabase.connection.Imp.InitSQLiteDatabaseImp;
import com.codingdie.rwsdatabase.connection.model.SQLiteConnectionPoolConfig;
import com.codingdie.rwsdatabase.connection.model.SqliteConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xupen on 2016/8/22.
 */
public class ConnectionPoolManager implements ConnectionPoolManagerImp {
    private List<SqliteConnection> readConnectionsPool;
    private SqliteConnection writeConnection;
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
    public SqliteConnection getReadConnection() {
        return (SqliteConnection) execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                return getReadConnectionAfterInit();
            }
        });
    }

    private SqliteConnection getReadConnectionAfterInit() {
        SqliteConnection restConnection = null;
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

    private SqliteConnection getRestConnnectionForRead(List<SqliteConnection> sqliteConnections) {
        for (int i = 0; i < sqliteConnections.size(); i++) {
            if (sqliteConnections.get(i).isInUsing() == false) {
                return sqliteConnections.get(i);
            }
        }
        return null;
    }

    @Override
    public SqliteConnection getWriteConnection() {
        return (SqliteConnection) execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                return getWriteConnectionAfterInit();
            }
        });
    }

    private SqliteConnection getWriteConnectionAfterInit() {
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
    public void releaseReadConnection(final SqliteConnection sqliteConnection) {

        execAfterInit(new AfterInitOperator() {
            @Override
            public Object exec() {
                readConnectionLock.lock();
                sqliteConnection.setInUsing(false);
                log("releaseReadConnection:" + sqliteConnection.getIndex() + "/" + (System.currentTimeMillis() - sqliteConnection.getBeginUsingTime()));
                readConnectionCondition.signalAll();

                readConnectionLock.unlock();
                return null;
            }
        });
    }


    @Override
    public void initConnnectionPool(SQLiteConnectionPoolConfig connectionPoolConfig, InitSQLiteDatabaseImp initSQLiteDatabaseImp) {
        versionControlLock.lock();
        writeConnection =SqliteConnection.createWritableConnection(connectionPoolConfig.getDbPath(), 0);
        readConnectionsPool = new ArrayList<SqliteConnection>();
        for (int i = 1; i < connectionPoolConfig.getMaxCount(); i++) {
            readConnectionsPool.add(SqliteConnection.createReadableConnection(connectionPoolConfig.getDbPath(), i));
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
