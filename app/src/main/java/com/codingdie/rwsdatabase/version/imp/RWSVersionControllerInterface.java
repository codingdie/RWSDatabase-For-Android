package com.codingdie.rwsdatabase.version.imp;

import android.os.Handler;
import com.codingdie.rwsdatabase.connection.WritableConnection;

/**
 * Created by xupeng on 2016/8/25.
 */
public interface RWSVersionControllerInterface {

    public  void createOrUpgradeDatabase(int version, Class versionManaer, WritableConnection db,UpgradeDatabaseListener upgradeDatabaseListener,Handler mainHandler);

}
