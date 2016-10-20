package com.codingdie.rwsdatabase;

import android.content.Context;
import android.os.Handler;
import com.codingdie.rwsdatabase.connection.Impl.InitSQLiteDatabaseImp;
import com.codingdie.rwsdatabase.connection.ReadableConnection;
import com.codingdie.rwsdatabase.connection.SQLConnectionPoolManager;
import com.codingdie.rwsdatabase.connection.WritableConnection;
import com.codingdie.rwsdatabase.connection.model.InitSQLiteConnectionPoolConfig;
import com.codingdie.rwsdatabase.version.RWSVersionController;
import com.codingdie.rwsdatabase.version.proxy.UpgradeDatabaseListener;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xupeng on 2016/8/25.
 */
public class RWSDatabaseManager {
    private SQLConnectionPoolManager connectionPoolManager;
    private ReentrantLock initLock = new ReentrantLock();
    private Condition initLockCondition = initLock.newCondition();
    private boolean initFlag = false;
    private Handler mainHandler;

    public void init(final String dbPath, final int version, final Class versionManager, final int maxConnectionCount, final UpgradeDatabaseListener upgradeDatabaseListener, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initLock.lock();
                mainHandler = new Handler(context.getMainLooper());

                InitSQLiteConnectionPoolConfig initSQLiteConnectionPoolConfig = new InitSQLiteConnectionPoolConfig();
                initSQLiteConnectionPoolConfig.setMaxCount(maxConnectionCount);
                initSQLiteConnectionPoolConfig.setDbPath(dbPath);
                connectionPoolManager = new SQLConnectionPoolManager();
                connectionPoolManager.openLog(false);
                connectionPoolManager.initConnnectionPool(initSQLiteConnectionPoolConfig, new InitSQLiteDatabaseImp() {
                    @Override
                    public void initDatabase(WritableConnection sqLiteConnection) {
                        RWSVersionController RWSVersionController = new RWSVersionController();
                        RWSVersionController.createOrUpgradeDatabase(version, versionManager, sqLiteConnection, upgradeDatabaseListener, mainHandler);
                        initFlag = true;
                        initLockCondition.signalAll();
                        initLock.unlock();
                    }
                });
            }
        }).start();

    }

    public <T> T execReadOperator(final ReadOperator<T> readOperator) {
        return execAfterInit(new AfterInitOperator<T>() {
            @Override
            public T exec() {
                T t = null;
                ReadableConnection readableConnection = null;
                try {
                    readableConnection = connectionPoolManager.getReadableConnection();
                    t = readOperator.exec(readableConnection);

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (readableConnection != null) {
                        connectionPoolManager.releaseReadConnection(readableConnection);
                    }
                    return t;
                }
            }
        });
    }

    public <T> T execWriteOperator(final WriteOperator<T> writeOperator) {
      return   execAfterInit(new AfterInitOperator<T>() {
            @Override
            public T exec() {
                WritableConnection writableConnection = null;
                T t=null;
                try {
                    writableConnection = connectionPoolManager.getWritableConnection();
                    writableConnection.beginTransaction();
                    t=writeOperator.exec(writableConnection);
                    writableConnection.setTransactionSuccessful();
                    writableConnection.endTransaction();

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (writableConnection != null) {
                        connectionPoolManager.releaseWritableConnection();
                    }
                    return t;
                }
            }
        });
    }

    public int getRestWritableConnectionCount() {
        return execAfterInit(new AfterInitOperator<Integer>() {
            @Override
            public Integer exec() {
                return connectionPoolManager.getRestWritableConnectionCount();
            }
        });
    }

    public int getRestReadableConnectionCount() {
        return execAfterInit(new AfterInitOperator<Integer>() {
            @Override
            public Integer exec() {
                return connectionPoolManager.getRestReadableConnectionCount();
            }
        });
    }

    @Deprecated
    public WritableConnection getWritableConnection() {
        return execAfterInit(new AfterInitOperator<WritableConnection>() {
            @Override
            public WritableConnection exec() {
                return connectionPoolManager.getWritableConnection();
            }
        });
    }

    @Deprecated
    public void releaseWritableConnection() {
        execAfterInit(new AfterInitOperator<Void>() {
            @Override
            public Void exec() {
                connectionPoolManager.releaseWritableConnection();
                return null;
            }
        });
    }

    @Deprecated
    public ReadableConnection getReadableDatabase() {
        return execAfterInit(new AfterInitOperator<ReadableConnection>() {
            @Override
            public ReadableConnection exec() {
                return connectionPoolManager.getReadableConnection();
            }
        });
    }

    @Deprecated
    public void releaseReadableDatabase(final ReadableConnection readableConnection) {
        execAfterInit(new AfterInitOperator<Void>() {
            @Override
            public Void exec() {
                connectionPoolManager.releaseReadConnection(readableConnection);
                return null;
            }
        });
    }

    public void destroy() {
        execAfterInit(new AfterInitOperator<Void>() {
            @Override
            public Void exec() {
                connectionPoolManager.destroy();
                return null;
            }
        });
    }

    private <T> T execAfterInit(AfterInitOperator<T> afterInitOperator) {
        T object = null;
        try {
            initLock.lock();
            if (initFlag) {
                initLock.unlock();
                object = afterInitOperator.exec();
            }
            while (!initFlag) {
                initLockCondition.await();
                if (initFlag) {
                    initLock.unlock();
                    object = afterInitOperator.exec();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            initLock.unlock();
        }
        return object;

    }

    private interface AfterInitOperator<T> {
        public T exec();
    }

}

